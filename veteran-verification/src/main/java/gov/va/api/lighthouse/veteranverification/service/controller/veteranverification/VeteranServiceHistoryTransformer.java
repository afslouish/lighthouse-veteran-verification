package gov.va.api.lighthouse.veteranverification.service.controller.veteranverification;

import gov.va.api.lighthouse.veteranverification.api.v0.Deployment;
import gov.va.api.lighthouse.veteranverification.api.v0.ServiceHistoryResponse;
import gov.va.api.lighthouse.veteranverification.service.MpiLookupUtils;
import gov.va.api.lighthouse.veteranverification.service.controller.Transformers;
import gov.va.api.lighthouse.veteranverification.service.utils.UuidV5;
import gov.va.viers.cdi.emis.commonservice.v2.MilitaryServiceEpisode;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISdeploymentResponseType;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISserviceEpisodeResponseType;
import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.hl7.v3.PRPAIN201306UV02;

@Builder
public class VeteranServiceHistoryTransformer {
  @NonNull String icn;

  @NonNull EMISdeploymentResponseType deploymentResponse;

  @NonNull EMISserviceEpisodeResponseType serviceEpisodeResponseType;

  @NonNull PRPAIN201306UV02 mpiResponse;

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

  private boolean isBeforeOrEqualTo(LocalDate dateOne, LocalDate dateTwo) {
    return dateOne.isBefore(dateTwo) || dateOne.isEqual(dateTwo);
  }

  private String makeBranchOfService(String branchOfService, String personnelCategory) {
    String branch;
    switch (StringUtils.normalizeSpace(branchOfService.toUpperCase())) {
      case "O":
        branch = "NOAA";
        break;
      case "H":
        branch = "Public Health Service";
        break;
      case "A":
        branch = "Army";
        break;
      case "C":
        branch = "Coast Guard";
        break;
      case "F":
        branch = "Air Force";
        break;
      case "M":
        branch = "Marine Corps";
        break;
      case "N":
        branch = "Navy";
        break;
      default:
        branch = "Unknown";
    }
    String category;
    switch (StringUtils.normalizeSpace(personnelCategory)) {
      case "N":
        category = "National Guard";
        break;
      case "V":
      case "Q":
        category = "Reserve";
        break;
      default:
        category = "";
    }
    return StringUtils.normalizeSpace(String.format("%s %s", branch, category));
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

  /**
   * Builds service history response from EMIS and MPI responses.
   *
   * @return ServiceHistoryResponse object.
   */
  public ServiceHistoryResponse serviceHistoryTransformer() {
    Deque<ServiceHistoryResponse.ServiceHistoryEpisode> episodes = new ArrayDeque<>();
    List<Deployment> unusedDeployments = makeDeploymentList(deploymentResponse);

    Collections.sort(
        serviceEpisodeResponseType.getMilitaryServiceEpisode(),
        (episodeOne, episodeTwo) ->
            episodeTwo
                .getMilitaryServiceEpisodeData()
                .getServiceEpisodeStartDate()
                .compare(episodeOne.getMilitaryServiceEpisodeData().getServiceEpisodeStartDate()));

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

      ServiceHistoryResponse.ServiceHistoryAttributes attributes =
          ServiceHistoryResponse.ServiceHistoryAttributes.builder()
              .firstName(MpiLookupUtils.getFirstName(mpiResponse))
              .lastName(MpiLookupUtils.getLastName(mpiResponse))
              .branchOfService(
                  makeBranchOfService(
                      militaryServiceEpisode
                          .getMilitaryServiceEpisodeData()
                          .getBranchOfServiceCode(),
                      militaryServiceEpisode.getKeyData().getPersonnelCategoryTypeCode()))
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
    return ServiceHistoryResponse.builder().data(episodes.stream().toList()).build();
  }
}