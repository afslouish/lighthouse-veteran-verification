package gov.va.api.lighthouse.veteranverification.service.utils;

import gov.va.api.lighthouse.veteranverification.api.v0.Deployment;
import gov.va.api.lighthouse.veteranverification.api.v0.ServiceHistoryResponse;
import gov.va.viers.cdi.emis.commonservice.v2.MilitaryServiceEpisode;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISdeploymentResponseType;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISserviceEpisodeResponseType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.hl7.v3.PRPAIN201306UV02;

@UtilityClass
public class ServiceHistoryResponseBuilder {
  DateTimeFormatter dateTimeFormatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd").withLocale(Locale.US);

  /**
   * Builds service history response from EMIS and MPI responses.
   *
   * @param deploymentResponse EMIS deployment history response.
   * @param serviceEpisodeResponseType EMIS service episode response.
   * @param mpiResponse MPI user response.
   * @return ServiceHistoryResponse object.
   */
  public ServiceHistoryResponse buildServiceHistoryResponse(
      @NonNull String uuid,
      @NonNull EMISdeploymentResponseType deploymentResponse,
      @NonNull EMISserviceEpisodeResponseType serviceEpisodeResponseType,
      @NonNull PRPAIN201306UV02 mpiResponse) {

    LinkedList<ServiceHistoryResponse.ServiceHistoryEpisode> episodes = new LinkedList<>();
    ArrayList<Deployment> unusedDeployments = makeDeploymentList(deploymentResponse);
    Collections.sort(serviceEpisodeResponseType.getMilitaryServiceEpisode(), (episodeOne, episodeTwo) ->
            episodeTwo.getMilitaryServiceEpisodeData().getServiceEpisodeStartDate().compare(episodeOne.getMilitaryServiceEpisodeData().getServiceEpisodeStartDate()));
    for (MilitaryServiceEpisode militaryServiceEpisode :
        serviceEpisodeResponseType.getMilitaryServiceEpisode()) {
      ServiceHistoryResponse.ServiceHistoryAttributes attributes =
          MilitaryServiceEpisodeAttributeBuilder.buildMilitaryServiceEpisode(
              militaryServiceEpisode, mpiResponse, unusedDeployments);
      unusedDeployments = removeUsedDeployments(unusedDeployments, attributes.deployments);
      episodes.push(
          ServiceHistoryResponse.ServiceHistoryEpisode.builder()
              .attributes(attributes)
              .id(
                  ServiceEpisodeIdBuilder.buildServiceEpisodeId(
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
    return ServiceHistoryResponse.builder().data(episodes).build();
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
}
