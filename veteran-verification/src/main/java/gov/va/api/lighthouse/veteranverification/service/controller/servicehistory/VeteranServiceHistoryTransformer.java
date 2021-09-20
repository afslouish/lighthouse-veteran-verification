package gov.va.api.lighthouse.veteranverification.service.controller.servicehistory;

import gov.va.api.lighthouse.veteranverification.api.v1.Deployment;
import gov.va.api.lighthouse.veteranverification.api.v1.ServiceHistoryResponse;
import gov.va.api.lighthouse.veteranverification.service.MpiLookupUtils;
import gov.va.api.lighthouse.veteranverification.service.controller.Transformers;
import gov.va.api.lighthouse.veteranverification.service.utils.UuidV5;
import gov.va.viers.cdi.emis.commonservice.v2.GuardReserveServicePeriods;
import gov.va.viers.cdi.emis.commonservice.v2.MilitaryServiceEpisode;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISdeploymentResponseType;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISguardReserveServicePeriodsResponseType;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISserviceEpisodeResponseType;
import java.time.LocalDate;
import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.hl7.v3.PRPAIN201306UV02;

@Builder
public class VeteranServiceHistoryTransformer {
  private final Map<String, String> grasBranchOfServiceMap =
      Map.ofEntries(
          new AbstractMap.SimpleEntry<>("N", "National Guard"),
          new AbstractMap.SimpleEntry<>("V", "Reserve"),
          new AbstractMap.SimpleEntry<>("Q", "Reserve"));

  private final Map<String, String> serviceHistoryBranchOfServiceMap =
      Map.ofEntries(
          new AbstractMap.SimpleEntry<>("O", "NOAA"),
          new AbstractMap.SimpleEntry<>("H", "Public Health Service"),
          new AbstractMap.SimpleEntry<>("A", "Army"),
          new AbstractMap.SimpleEntry<>("C", "Coast Guard"),
          new AbstractMap.SimpleEntry<>("F", "Air Force"),
          new AbstractMap.SimpleEntry<>("M", "Marine Corps"),
          new AbstractMap.SimpleEntry<>("N", "Navy"));

  private final String[] title32StatuteCodes = {"J", "N", "P", "Q", "Z"};

  @NonNull String icn;

  @NonNull EMISdeploymentResponseType deploymentResponse;

  EMISserviceEpisodeResponseType serviceEpisodeResponseType;

  @NonNull PRPAIN201306UV02 mpiResponse;

  EMISguardReserveServicePeriodsResponseType grasResponse;

  private List<Deployment> buildDeployments(
      List<Deployment> deployments,
      LocalDate servicePeriodStartDate,
      LocalDate servicePeriodEndDate) {
    return deployments.stream()
        .filter(
            deployment ->
                deployment.startDate() != null
                    && deployment.endDate() != null
                    && isBeforeOrEqualTo(servicePeriodStartDate, deployment.startDate())
                    && (servicePeriodEndDate == null
                        || isBeforeOrEqualTo(deployment.endDate(), servicePeriodEndDate)))
        .collect(Collectors.toList());
  }

  /**
   * Returns filtered list of active ServiceHistoryEpisodes from Emis's serviceEpisodesRequest
   * response.
   */
  private AbstractList<ServiceHistoryResponse.ServiceHistoryEpisode> getActiveServicePeriods() {
    if (serviceEpisodeResponseType == null) {
      return new ArrayList<>();
    }
    Deque<ServiceHistoryResponse.ServiceHistoryEpisode> episodes = new ArrayDeque<>();
    List<Deployment> unusedDeployments = makeDeploymentList(deploymentResponse);
    Collections.sort(
        serviceEpisodeResponseType.getMilitaryServiceEpisode(),
        (episodeOne, episodeTwo) ->
            episodeTwo
                .getMilitaryServiceEpisodeData()
                .getServiceEpisodeStartDate()
                .compare(episodeOne.getMilitaryServiceEpisodeData().getServiceEpisodeStartDate()));
    serviceEpisodeResponseType
        .getMilitaryServiceEpisode()
        .removeIf(
            episode -> !"A".equalsIgnoreCase(episode.getKeyData().getPersonnelCategoryTypeCode()));
    for (MilitaryServiceEpisode militaryServiceEpisode :
        serviceEpisodeResponseType.getMilitaryServiceEpisode()) {
      LocalDate startDate =
          Transformers.xmlGregorianToLocalDate(
              Optional.ofNullable(militaryServiceEpisode.getMilitaryServiceEpisodeData())
                  .map(value -> value.getServiceEpisodeStartDate())
                  .orElse(null));
      LocalDate endDate =
          Transformers.xmlGregorianToLocalDate(
              Optional.ofNullable(militaryServiceEpisode.getMilitaryServiceEpisodeData())
                  .map(value -> value.getServiceEpisodeEndDate())
                  .orElse(null));
      String branchOfService =
          serviceHistoryBranchOfServiceMap.get(
              militaryServiceEpisode.getMilitaryServiceEpisodeData().getBranchOfServiceCode());
      ServiceHistoryResponse.ServiceHistoryAttributes attributes =
          ServiceHistoryResponse.ServiceHistoryAttributes.builder()
              .firstName(MpiLookupUtils.getFirstName(mpiResponse))
              .lastName(MpiLookupUtils.getLastName(mpiResponse))
              .branchOfService(branchOfService == null ? "Unknown" : branchOfService)
              .startDate(startDate)
              .endDate(endDate)
              .payGrade(
                  makePayGradeString(
                      militaryServiceEpisode.getMilitaryServiceEpisodeData().getPayPlanCode(),
                      militaryServiceEpisode.getMilitaryServiceEpisodeData().getPayGradeCode()))
              .dischargeStatus(
                  ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.codeToEnum(
                      militaryServiceEpisode
                          .getMilitaryServiceEpisodeData()
                          .getDischargeCharacterOfServiceCode()))
              .separationReason(
                  militaryServiceEpisode
                      .getMilitaryServiceEpisodeData()
                      .getNarrativeReasonForSeparationTxt())
              .deployments(buildDeployments(unusedDeployments, startDate, endDate))
              .build();
      String serviceHistoryId =
          UuidV5.nameUuidFromNamespaceAndString(
                  "gov.vets.service-history-episodes",
                  String.format(
                      "%s-%s-%s",
                      icn.trim(),
                      startDate != null ? startDate.toString() : null,
                      endDate != null ? endDate.toString() : null))
              .toString();
      episodes.push(
          ServiceHistoryResponse.ServiceHistoryEpisode.builder()
              .attributes(attributes)
              .id(serviceHistoryId)
              .build());
      unusedDeployments.removeAll(attributes.deployments);
    }
    return new ArrayList<>(episodes);
  }

  /**
   * Returns filtered list of non title32StatuteCodes reserve ServiceHistoryEpisodes from Emis's
   * guardReserveServicePeriodsResponse response.
   */
  private List<ServiceHistoryResponse.ServiceHistoryEpisode> getNonTitle32ReservePeriods() {
    List<ServiceHistoryResponse.ServiceHistoryEpisode> nonTitle32ReservePeriods = new ArrayList<>();
    List<GuardReserveServicePeriods> periods =
        Optional.ofNullable(grasResponse)
            .map(response -> response.getGuardReserveServicePeriods())
            .orElse(null);
    if (periods == null) {
      return nonTitle32ReservePeriods;
    }
    periods.removeIf(
        period ->
            period.getGuardReserveServicePeriodsData() == null
                || "Y"
                    .equalsIgnoreCase(
                        period.getGuardReserveServicePeriodsData().getTrainingIndicatorCode())
                || Arrays.stream(title32StatuteCodes)
                    .toList()
                    .contains(
                        period
                            .getGuardReserveServicePeriodsData()
                            .getGuardReservePeriodStatuteCode()));
    for (GuardReserveServicePeriods period : periods) {
      LocalDate startDate =
          Transformers.xmlGregorianToLocalDate(
              Optional.ofNullable(period.getGuardReserveServicePeriodsData())
                  .map(value -> value.getGuardReservePeriodStartDate())
                  .orElse(null));
      LocalDate endDate =
          Transformers.xmlGregorianToLocalDate(
              Optional.ofNullable(period.getGuardReserveServicePeriodsData())
                  .map(value -> value.getGuardReservePeriodEndDate())
                  .orElse(null));
      String branchOfService =
          grasBranchOfServiceMap.get(period.getKeyData().getPersonnelCategoryTypeCode());
      ServiceHistoryResponse.ServiceHistoryAttributes attributes =
          ServiceHistoryResponse.ServiceHistoryAttributes.builder()
              .firstName(MpiLookupUtils.getFirstName(mpiResponse))
              .lastName(MpiLookupUtils.getLastName(mpiResponse))
              .branchOfService(branchOfService == null ? "Unknown" : branchOfService)
              .startDate(startDate)
              .endDate(endDate)
              .dischargeStatus(
                  ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.codeToEnum(
                      period
                          .getGuardReserveServicePeriodsData()
                          .getGuardReservePeriodCharacterOfServiceCode()))
              .separationReason(
                  period.getGuardReserveServicePeriodsData().getNarrativeReasonForSeparationTxt())
              .build();
      String serviceHistoryId =
          UuidV5.nameUuidFromNamespaceAndString(
                  "gov.vets.service-history-episodes",
                  String.format(
                      "%s-%s-%s",
                      icn.trim(),
                      startDate != null ? startDate.toString() : null,
                      endDate != null ? endDate.toString() : null))
              .toString();
      nonTitle32ReservePeriods.add(
          ServiceHistoryResponse.ServiceHistoryEpisode.builder()
              .attributes(attributes)
              .id(serviceHistoryId)
              .build());
    }
    return nonTitle32ReservePeriods;
  }

  private boolean isBeforeOrEqualTo(LocalDate dateOne, LocalDate dateTwo) {
    return dateOne.isBefore(dateTwo) || dateOne.isEqual(dateTwo);
  }

  private List<Deployment> makeDeploymentList(EMISdeploymentResponseType deploymentResponse) {
    List<Deployment> list = new ArrayList<>();
    for (gov.va.viers.cdi.emis.commonservice.v2.Deployment deployment :
        deploymentResponse.getDeployment()) {
      LocalDate startDate =
          Transformers.xmlGregorianToLocalDate(
              Optional.ofNullable(deployment.getDeploymentData())
                  .map(value -> value.getDeploymentStartDate())
                  .orElse(null));
      LocalDate endDate =
          Transformers.xmlGregorianToLocalDate(
              Optional.ofNullable(deployment.getDeploymentData())
                  .map(value -> value.getDeploymentEndDate())
                  .orElse(null));
      String location = null;
      if (deployment.getDeploymentData() != null
          && deployment.getDeploymentData().getDeploymentLocation().size() > 0) {
        location =
            deployment
                .getDeploymentData()
                .getDeploymentLocation()
                .get(0)
                .getDeploymentISOAlpha3Country();
      }
      list.add(
          Deployment.builder().endDate(endDate).startDate(startDate).location(location).build());
    }
    return list;
  }

  private String makePayGradeString(String payPlanCode, String payGradeCode) {
    if (payPlanCode == null
        || payPlanCode.trim().isEmpty()
        || payGradeCode == null
        || payGradeCode.trim().isEmpty()) {
      return "unknown";
    }
    return StringUtils.normalizeSpace(
        String.format(
            "%s%s",
            StringUtils.normalizeSpace(payPlanCode).charAt(1),
            StringUtils.normalizeSpace(payGradeCode)));
  }

  /** Builds service history response from EMIS and MPI responses. */
  public ServiceHistoryResponse serviceHistoryTransformer() {
    AbstractList<ServiceHistoryResponse.ServiceHistoryEpisode> episodes = getActiveServicePeriods();
    episodes.addAll(getNonTitle32ReservePeriods());
    Collections.sort(episodes, Comparator.comparing(episode -> episode.attributes().startDate()));
    return ServiceHistoryResponse.builder().data(episodes).build();
  }
}
