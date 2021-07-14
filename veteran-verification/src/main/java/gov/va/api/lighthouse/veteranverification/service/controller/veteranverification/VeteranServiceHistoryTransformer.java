package gov.va.api.lighthouse.veteranverification.service.controller.veteranverification;

import gov.va.api.lighthouse.veteranverification.api.v0.Deployment;
import gov.va.api.lighthouse.veteranverification.api.v0.ServiceHistoryResponse;
import gov.va.api.lighthouse.veteranverification.service.utils.ServiceEpisodeAttributeBuilder;
import gov.va.api.lighthouse.veteranverification.service.utils.UuidV5;
import gov.va.viers.cdi.emis.commonservice.v2.MilitaryServiceEpisode;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISdeploymentResponseType;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISserviceEpisodeResponseType;
import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
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

  private String buildServiceEpisodeId(String uuid, String beginDate, String endDate) {
    String str = String.format("%s-%s-%s", uuid.trim(), beginDate, endDate);
    return UuidV5.nameUuidFromNamespaceAndString("gov.vets.service-history-episodes", str)
        .toString();
  }

  private LocalDate getEmisDeploymentEndDate(
      gov.va.viers.cdi.emis.commonservice.v2.Deployment deployment) {
    LocalDate endDate = null;
    if (deployment.getDeploymentData() != null
        && deployment.getDeploymentData().getDeploymentEndDate() != null) {
      endDate = LocalDate.parse(deployment.getDeploymentData().getDeploymentEndDate().toString());
    }
    return endDate;
  }

  private LocalDate getEmisDeploymentStartDate(
      gov.va.viers.cdi.emis.commonservice.v2.Deployment deployment) {
    LocalDate startDate = null;
    if (deployment.getDeploymentData() != null
        && deployment.getDeploymentData().getDeploymentStartDate() != null) {
      startDate =
          LocalDate.parse(deployment.getDeploymentData().getDeploymentStartDate().toString());
    }
    return startDate;
  }

  private LocalDate getMilitaryEpisodeEndDate(MilitaryServiceEpisode episode) {
    LocalDate endDate = null;
    if (episode.getMilitaryServiceEpisodeData() != null
        && episode.getMilitaryServiceEpisodeData().getServiceEpisodeEndDate() != null) {
      endDate =
          LocalDate.parse(
              episode.getMilitaryServiceEpisodeData().getServiceEpisodeEndDate().toString());
    }
    return endDate;
  }

  private LocalDate getMilitaryEpisodeStartDate(MilitaryServiceEpisode episode) {
    LocalDate startDate = null;
    if (episode.getMilitaryServiceEpisodeData() != null
        && episode.getMilitaryServiceEpisodeData().getServiceEpisodeStartDate() != null) {
      startDate =
          LocalDate.parse(
              episode.getMilitaryServiceEpisodeData().getServiceEpisodeStartDate().toString());
    }
    return startDate;
  }

  private List<Deployment> makeDeploymentList(EMISdeploymentResponseType deploymentResponse) {
    List<Deployment> list = new ArrayList<>();
    for (gov.va.viers.cdi.emis.commonservice.v2.Deployment deployment :
        deploymentResponse.getDeployment()) {
      LocalDate startDate = getEmisDeploymentStartDate(deployment);
      LocalDate endDate = getEmisDeploymentEndDate(deployment);
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
    ArrayDeque<ServiceHistoryResponse.ServiceHistoryEpisode> episodes = new ArrayDeque<>();
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
          ServiceEpisodeAttributeBuilder.buildMilitaryServiceEpisode(
              militaryServiceEpisode, mpiResponse, unusedDeployments);
      unusedDeployments = removeUsedDeployments(unusedDeployments, attributes.deployments);
      episodes.push(
          ServiceHistoryResponse.ServiceHistoryEpisode.builder()
              .attributes(attributes)
              .id(
                  buildServiceEpisodeId(
                      uuid,
                      getMilitaryEpisodeStartDate(militaryServiceEpisode) != null
                          ? getMilitaryEpisodeStartDate(militaryServiceEpisode).toString()
                          : null,
                      getMilitaryEpisodeEndDate(militaryServiceEpisode) != null
                          ? getMilitaryEpisodeEndDate(militaryServiceEpisode).toString()
                          : null))
              .build());
    }
    return ServiceHistoryResponse.builder().data(episodes.stream().toList()).build();
  }
}
