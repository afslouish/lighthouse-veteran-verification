package gov.va.api.lighthouse.veteranverification.service.controller.veteranverification;

import gov.va.api.lighthouse.veteranverification.api.v0.Deployment;
import gov.va.api.lighthouse.veteranverification.api.v0.ServiceHistoryResponse;
import gov.va.api.lighthouse.veteranverification.service.MpiLookupUtils;
import gov.va.api.lighthouse.veteranverification.service.utils.EmisUtils;
import gov.va.api.lighthouse.veteranverification.service.utils.ServiceHistoryUtils;
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
import lombok.Builder;
import lombok.NonNull;
import org.hl7.v3.PRPAIN201306UV02;

@Builder
public class VeteranServiceHistoryTransformer {
  @NonNull String uuid;

  @NonNull EMISdeploymentResponseType deploymentResponse;

  @NonNull EMISserviceEpisodeResponseType serviceEpisodeResponseType;

  @NonNull PRPAIN201306UV02 mpiResponse;

  private ServiceHistoryResponse.ServiceHistoryAttributes buildMilitaryServiceEpisode(
      @NonNull MilitaryServiceEpisode serviceEpisode,
      @NonNull PRPAIN201306UV02 mpiResponse,
      @NonNull List<Deployment> deployments) {
    LocalDate startDate = EmisUtils.getMilitaryEpisodeStartDate(serviceEpisode);
    LocalDate endDate = EmisUtils.getMilitaryEpisodeEndDate(serviceEpisode);
    return ServiceHistoryResponse.ServiceHistoryAttributes.builder()
        .firstName(MpiLookupUtils.getFirstName(mpiResponse))
        .lastName(MpiLookupUtils.getLastName(mpiResponse))
        .branchOfService(
            ServiceHistoryUtils.buildBranchOfServiceString(
                serviceEpisode.getMilitaryServiceEpisodeData().getBranchOfServiceCode(),
                serviceEpisode.getKeyData().getPersonnelCategoryTypeCode()))
        .startDate(startDate)
        .endDate(endDate)
        .payGrade(
            ServiceHistoryUtils.buildPayGradeString(
                serviceEpisode.getMilitaryServiceEpisodeData().getPayPlanCode(),
                serviceEpisode.getMilitaryServiceEpisodeData().getPayGradeCode()))
        .dischargeStatus(
            ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.codeToEnum(
                serviceEpisode
                    .getMilitaryServiceEpisodeData()
                    .getDischargeCharacterOfServiceCode()))
        .separationReason(
            serviceEpisode.getMilitaryServiceEpisodeData().getNarrativeReasonForSeparationTxt())
        .deployments(ServiceHistoryUtils.buildDeployments(deployments, startDate, endDate))
        .build();
  }

  private String buildServiceEpisodeId(String uuid, LocalDate beginDate, LocalDate endDate) {
    String str =
        String.format(
            "%s-%s-%s",
            uuid.trim(),
            beginDate != null ? beginDate.toString() : null,
            endDate != null ? endDate.toString() : null);
    return UuidV5.nameUuidFromNamespaceAndString("gov.vets.service-history-episodes", str)
        .toString();
  }

  private List<Deployment> makeDeploymentList(EMISdeploymentResponseType deploymentResponse) {
    List<Deployment> list = new ArrayList<>();
    for (gov.va.viers.cdi.emis.commonservice.v2.Deployment deployment :
        deploymentResponse.getDeployment()) {
      LocalDate startDate = EmisUtils.getEmisDeploymentStartDate(deployment);
      LocalDate endDate = EmisUtils.getEmisDeploymentEndDate(deployment);
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

  private List<Deployment> removeUsedDeployments(
      List<Deployment> fullDeploymentList, List<Deployment> usedDeploymentList) {
    fullDeploymentList.removeAll(usedDeploymentList);
    return fullDeploymentList;
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
      ServiceHistoryResponse.ServiceHistoryAttributes attributes =
          buildMilitaryServiceEpisode(militaryServiceEpisode, mpiResponse, unusedDeployments);
      unusedDeployments = removeUsedDeployments(unusedDeployments, attributes.deployments);
      episodes.push(
          ServiceHistoryResponse.ServiceHistoryEpisode.builder()
              .attributes(attributes)
              .id(
                  buildServiceEpisodeId(
                      uuid,
                      EmisUtils.getMilitaryEpisodeStartDate(militaryServiceEpisode),
                      EmisUtils.getMilitaryEpisodeEndDate(militaryServiceEpisode)))
              .build());
    }
    return ServiceHistoryResponse.builder().data(episodes.stream().toList()).build();
  }
}
