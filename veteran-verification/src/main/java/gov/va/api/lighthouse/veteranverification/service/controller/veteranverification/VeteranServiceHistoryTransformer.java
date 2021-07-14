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

  private ArrayList<Deployment> makeDeploymentList(EMISdeploymentResponseType deploymentResponse) {
    ArrayList<Deployment> list = new ArrayList<>();
    for (gov.va.viers.cdi.emis.commonservice.v2.Deployment deployment :
        deploymentResponse.getDeployment()) {
      list.add(
          Deployment.builder()
              .endDate(
                  LocalDate.parse(deployment.getDeploymentData().getDeploymentEndDate().toString()))
              .startDate(
                  LocalDate.parse(
                      deployment.getDeploymentData().getDeploymentStartDate().toString()))
              .location(
                  deployment
                      .getDeploymentData()
                      .getDeploymentLocation()
                      .get(0)
                      .getDeploymentISOAlpha3Country())
              .build());
    }
    return list;
  }

  private ArrayList<Deployment> removeUsedDeployments(
      ArrayList<Deployment> fullDeploymentList, List<Deployment> usedDeploymentList) {
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
    ArrayList<Deployment> unusedDeployments = makeDeploymentList(deploymentResponse);
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
                      militaryServiceEpisode
                          .getMilitaryServiceEpisodeData()
                          .getServiceEpisodeStartDate()
                          .toString(),
                      militaryServiceEpisode
                                  .getMilitaryServiceEpisodeData()
                                  .getServiceEpisodeEndDate()
                              == null
                          ? null
                          : militaryServiceEpisode
                              .getMilitaryServiceEpisodeData()
                              .getServiceEpisodeEndDate()
                              .toString()))
              .build());
    }
    return ServiceHistoryResponse.builder().data(episodes.stream().toList()).build();
  }
}
