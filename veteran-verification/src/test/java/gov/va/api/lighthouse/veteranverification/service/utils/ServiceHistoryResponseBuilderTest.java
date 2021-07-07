package gov.va.api.lighthouse.veteranverification.service.utils;

import gov.va.api.lighthouse.veteranverification.api.v0.ServiceHistoryResponse;
import gov.va.api.lighthouse.veteranverification.service.TestUtils;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISdeploymentResponseType;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISserviceEpisodeResponseType;
import org.hl7.v3.PRPAIN201306UV02;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ServiceHistoryResponseBuilderTest {
  @Test
  public void HappyPathNoNulls() {
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
        TestUtils.createServiceHistoryResponse("emis/service_episodes_response_ascending.xml");
    PRPAIN201306UV02 mpiResponse = TestUtils.createMpiResponse("mpi/mpi_profile_response_body.xml");
    EMISdeploymentResponseType deployments =
        TestUtils.createDeploymentResponse("emis/deployments_response.xml");
    ServiceHistoryResponse response =
        ServiceHistoryResponseBuilder.buildServiceHistoryResponse(
            "uuid", deployments, serviceEpisodeResponse, mpiResponse);
    Assertions.assertEquals(response.data().size(), 2);
    ServiceHistoryResponse.ServiceHistoryEpisode episodeOne = response.data().get(0);
    ServiceHistoryResponse.ServiceHistoryEpisode episodeTwo = response.data().get(1);
    Assertions.assertEquals(episodeOne.id(), "2fd20706-557d-051d-a897-207ca828bf25");
    Assertions.assertEquals(episodeOne.type(), "service-history-episodes");
    Assertions.assertEquals(episodeTwo.id(), "aa753275-1e89-09c0-aa14-d867b6aaa0df");
    Assertions.assertEquals(episodeTwo.type(), "service-history-episodes");
    ServiceHistoryResponse.ServiceHistoryAttributes attributesOne = episodeOne.attributes();
    ServiceHistoryResponse.ServiceHistoryAttributes attributesTwo = episodeTwo.attributes();
    Assertions.assertEquals(attributesOne.firstName(), "John");
    Assertions.assertEquals(attributesOne.lastName(), "Doe");
    Assertions.assertEquals(attributesOne.branchOfService(), "Army");
    Assertions.assertEquals(attributesOne.startDate().toString(), "2000-01-01");
    Assertions.assertEquals(attributesOne.endDate().toString(), "2001-02-01");
    Assertions.assertEquals(attributesOne.payGrade(), "05");
    Assertions.assertEquals(
        attributesOne.dischargeStatus(),
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.ThisWillBeRemoved);
    Assertions.assertEquals(attributesOne.separationReason(), "SUFFICIENT SERVICE FOR RETIREMENT");
    Assertions.assertEquals(attributesOne.deployments().stream().count(), 1);
    Assertions.assertEquals(
        attributesOne.deployments().get(0).startDate().toString(), "2000-02-01");
    Assertions.assertEquals(attributesOne.deployments().get(0).endDate().toString(), "2001-01-01");
    Assertions.assertEquals(attributesOne.deployments().get(0).location(), "QAT");
    Assertions.assertEquals(attributesTwo.firstName(), "John");
    Assertions.assertEquals(attributesTwo.lastName(), "Doe");
    Assertions.assertEquals(attributesTwo.branchOfService(), "Air Force");
    Assertions.assertEquals(attributesTwo.startDate().toString(), "2002-01-01");
    Assertions.assertEquals(attributesTwo.endDate().toString(), "2003-02-01");
    Assertions.assertEquals(attributesTwo.payGrade(), "05");
    Assertions.assertEquals(
        attributesTwo.dischargeStatus(),
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.ThisWillBeRemoved);
    Assertions.assertEquals(attributesTwo.separationReason(), "SUFFICIENT SERVICE FOR RETIREMENT");
    Assertions.assertEquals(attributesTwo.deployments().stream().count(), 1);
    Assertions.assertEquals(
        attributesTwo.deployments().get(0).startDate().toString(), "2002-02-01");
    Assertions.assertEquals(attributesTwo.deployments().get(0).endDate().toString(), "2003-01-01");
    Assertions.assertEquals(attributesTwo.deployments().get(0).location(), "QAT");
  }

  @Test
  public void HappyPathNoNullsReverseEpisodes() {
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
        TestUtils.createServiceHistoryResponse("emis/service_episodes_response_descending.xml");
    PRPAIN201306UV02 mpiResponse = TestUtils.createMpiResponse("mpi/mpi_profile_response_body.xml");
    EMISdeploymentResponseType deployments =
        TestUtils.createDeploymentResponse("emis/deployments_response.xml");
    ServiceHistoryResponse response =
        ServiceHistoryResponseBuilder.buildServiceHistoryResponse(
            "uuid", deployments, serviceEpisodeResponse, mpiResponse);
    Assertions.assertEquals(response.data().size(), 2);
    ServiceHistoryResponse.ServiceHistoryEpisode episodeOne = response.data().get(0);
    ServiceHistoryResponse.ServiceHistoryEpisode episodeTwo = response.data().get(1);
    Assertions.assertEquals(episodeOne.id(), "2fd20706-557d-051d-a897-207ca828bf25");
    Assertions.assertEquals(episodeOne.type(), "service-history-episodes");
    Assertions.assertEquals(episodeTwo.id(), "aa753275-1e89-09c0-aa14-d867b6aaa0df");
    Assertions.assertEquals(episodeTwo.type(), "service-history-episodes");
    ServiceHistoryResponse.ServiceHistoryAttributes attributesOne = episodeOne.attributes();
    ServiceHistoryResponse.ServiceHistoryAttributes attributesTwo = episodeTwo.attributes();
    Assertions.assertEquals(attributesOne.firstName(), "John");
    Assertions.assertEquals(attributesOne.lastName(), "Doe");
    Assertions.assertEquals(attributesOne.branchOfService(), "Army");
    Assertions.assertEquals(attributesOne.startDate().toString(), "2000-01-01");
    Assertions.assertEquals(attributesOne.endDate().toString(), "2001-02-01");
    Assertions.assertEquals(attributesOne.payGrade(), "05");
    Assertions.assertEquals(
        attributesOne.dischargeStatus(),
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.ThisWillBeRemoved);
    Assertions.assertEquals(attributesOne.separationReason(), "SUFFICIENT SERVICE FOR RETIREMENT");
    Assertions.assertEquals(attributesOne.deployments().stream().count(), 1);
    Assertions.assertEquals(
        attributesOne.deployments().get(0).startDate().toString(), "2000-02-01");
    Assertions.assertEquals(attributesOne.deployments().get(0).endDate().toString(), "2001-01-01");
    Assertions.assertEquals(attributesOne.deployments().get(0).location(), "QAT");
    Assertions.assertEquals(attributesTwo.firstName(), "John");
    Assertions.assertEquals(attributesTwo.lastName(), "Doe");
    Assertions.assertEquals(attributesTwo.branchOfService(), "Air Force");
    Assertions.assertEquals(attributesTwo.startDate().toString(), "2002-01-01");
    Assertions.assertEquals(attributesTwo.endDate().toString(), "2003-02-01");
    Assertions.assertEquals(attributesTwo.payGrade(), "05");
    Assertions.assertEquals(
        attributesTwo.dischargeStatus(),
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.ThisWillBeRemoved);
    Assertions.assertEquals(attributesTwo.separationReason(), "SUFFICIENT SERVICE FOR RETIREMENT");
    Assertions.assertEquals(attributesTwo.deployments().stream().count(), 1);
    Assertions.assertEquals(
        attributesTwo.deployments().get(0).startDate().toString(), "2002-02-01");
    Assertions.assertEquals(attributesTwo.deployments().get(0).endDate().toString(), "2003-01-01");
    Assertions.assertEquals(attributesTwo.deployments().get(0).location(), "QAT");
  }

  @Test
  public void NullDeployments() {
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
        TestUtils.createServiceHistoryResponse("emis/service_episodes_response_ascending.xml");
    PRPAIN201306UV02 mpiResponse = TestUtils.createMpiResponse("mpi/mpi_profile_response_body.xml");
    EMISdeploymentResponseType deployments =
        TestUtils.createDeploymentResponse("emis/deployments_response.xml");
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          ServiceHistoryResponseBuilder.buildServiceHistoryResponse(
              "uuid", null, serviceEpisodeResponse, mpiResponse);
        });
  }

  @Test
  public void NullMpiResponse() {
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
        TestUtils.createServiceHistoryResponse("emis/service_episodes_response_ascending.xml");
    PRPAIN201306UV02 mpiResponse = TestUtils.createMpiResponse("mpi/mpi_profile_response_body.xml");
    EMISdeploymentResponseType deployments =
        TestUtils.createDeploymentResponse("emis/deployments_response.xml");
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          ServiceHistoryResponseBuilder.buildServiceHistoryResponse(
              "uuid", deployments, serviceEpisodeResponse, null);
        });
  }

  @Test
  public void NullServiceEpisodeResponse() {
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
        TestUtils.createServiceHistoryResponse("emis/service_episodes_response_ascending.xml");
    PRPAIN201306UV02 mpiResponse = TestUtils.createMpiResponse("mpi/mpi_profile_response_body.xml");
    EMISdeploymentResponseType deployments =
        TestUtils.createDeploymentResponse("emis/deployments_response.xml");
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          ServiceHistoryResponseBuilder.buildServiceHistoryResponse(
              "uuid", deployments, null, mpiResponse);
        });
  }

  @Test
  public void NullUuid() {
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
        TestUtils.createServiceHistoryResponse("emis/service_episodes_response_ascending.xml");
    PRPAIN201306UV02 mpiResponse = TestUtils.createMpiResponse("mpi/mpi_profile_response_body.xml");
    EMISdeploymentResponseType deployments =
        TestUtils.createDeploymentResponse("emis/deployments_response.xml");
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          ServiceHistoryResponseBuilder.buildServiceHistoryResponse(
              null, deployments, serviceEpisodeResponse, mpiResponse);
        });
  }

  @Test
  public void happyPathNullEndDate() {
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
        TestUtils.createServiceHistoryResponse("emis/service_episodes_response_ascending.xml");
    PRPAIN201306UV02 mpiResponse = TestUtils.createMpiResponse("mpi/mpi_profile_response_body.xml");
    EMISdeploymentResponseType deployments =
        TestUtils.createDeploymentResponse("emis/deployments_response.xml");
    serviceEpisodeResponse
        .getMilitaryServiceEpisode()
        .get(0)
        .getMilitaryServiceEpisodeData()
        .setServiceEpisodeEndDate(null);
    ServiceHistoryResponse response =
        ServiceHistoryResponseBuilder.buildServiceHistoryResponse(
            "uuid", deployments, serviceEpisodeResponse, mpiResponse);
    Assertions.assertEquals(response.data().size(), 2);
    ServiceHistoryResponse.ServiceHistoryEpisode episodeOne = response.data().get(0);
    ServiceHistoryResponse.ServiceHistoryEpisode episodeTwo = response.data().get(1);
    Assertions.assertEquals(episodeOne.id(), "b1f0be51-00f1-0029-8251-1cb251b0fd75");
    Assertions.assertEquals(episodeOne.type(), "service-history-episodes");
    Assertions.assertEquals(episodeTwo.id(), "aa753275-1e89-09c0-aa14-d867b6aaa0df");
    Assertions.assertEquals(episodeTwo.type(), "service-history-episodes");
    ServiceHistoryResponse.ServiceHistoryAttributes attributesOne = episodeOne.attributes();
    ServiceHistoryResponse.ServiceHistoryAttributes attributesTwo = episodeTwo.attributes();
    Assertions.assertEquals(attributesOne.firstName(), "John");
    Assertions.assertEquals(attributesOne.lastName(), "Doe");
    Assertions.assertEquals(attributesOne.branchOfService(), "Army");
    Assertions.assertEquals(attributesOne.startDate().toString(), "2000-01-01");
    Assertions.assertNull(attributesOne.endDate());
    Assertions.assertEquals(attributesOne.payGrade(), "05");
    Assertions.assertEquals(
        attributesOne.dischargeStatus(),
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.ThisWillBeRemoved);
    Assertions.assertEquals(attributesOne.separationReason(), "SUFFICIENT SERVICE FOR RETIREMENT");
    Assertions.assertEquals(attributesOne.deployments().stream().count(), 4);
    Assertions.assertEquals(
        attributesOne.deployments().get(0).startDate().toString(), "2000-02-01");
    Assertions.assertEquals(attributesOne.deployments().get(0).endDate().toString(), "2001-01-01");
    Assertions.assertEquals(attributesOne.deployments().get(0).location(), "QAT");
    Assertions.assertEquals(
        attributesOne.deployments().get(1).startDate().toString(), "2000-03-01");
    Assertions.assertEquals(attributesOne.deployments().get(1).endDate().toString(), "2003-01-01");
    Assertions.assertEquals(attributesOne.deployments().get(1).location(), "QAT");
    Assertions.assertEquals(
            attributesOne.deployments().get(1).startDate().toString(), "2000-03-01");
    Assertions.assertEquals(attributesOne.deployments().get(1).endDate().toString(), "2003-01-01");
    Assertions.assertEquals(attributesOne.deployments().get(1).location(), "QAT");
    Assertions.assertEquals(
            attributesOne.deployments().get(2).startDate().toString(), "2004-02-01");
    Assertions.assertEquals(attributesOne.deployments().get(2).endDate().toString(), "2005-01-01");
    Assertions.assertEquals(attributesOne.deployments().get(2).location(), "AX1");
    Assertions.assertEquals(
            attributesOne.deployments().get(3).startDate().toString(), "2006-02-01");
    Assertions.assertEquals(attributesOne.deployments().get(3).endDate().toString(), "2007-01-01");
    Assertions.assertEquals(attributesOne.deployments().get(3).location(), "AX1");
    Assertions.assertEquals(attributesTwo.firstName(), "John");
    Assertions.assertEquals(attributesTwo.lastName(), "Doe");
    Assertions.assertEquals(attributesTwo.branchOfService(), "Air Force");
    Assertions.assertEquals(attributesTwo.startDate().toString(), "2002-01-01");
    Assertions.assertEquals(attributesTwo.endDate().toString(), "2003-02-01");
    Assertions.assertEquals(attributesTwo.payGrade(), "05");
    Assertions.assertEquals(
        attributesTwo.dischargeStatus(),
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.ThisWillBeRemoved);
    Assertions.assertEquals(attributesTwo.separationReason(), "SUFFICIENT SERVICE FOR RETIREMENT");
    Assertions.assertEquals(attributesTwo.deployments().stream().count(), 1);
    Assertions.assertEquals(
        attributesTwo.deployments().get(0).startDate().toString(), "2002-02-01");
    Assertions.assertEquals(attributesTwo.deployments().get(0).endDate().toString(), "2003-01-01");
    Assertions.assertEquals(attributesTwo.deployments().get(0).location(), "QAT");
  }

  @Test
  public void happyPathNullEndDateEpisodesReverse() {
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
        TestUtils.createServiceHistoryResponse("emis/service_episodes_response_descending.xml");
    PRPAIN201306UV02 mpiResponse = TestUtils.createMpiResponse("mpi/mpi_profile_response_body.xml");
    EMISdeploymentResponseType deployments =
        TestUtils.createDeploymentResponse("emis/deployments_response.xml");
    serviceEpisodeResponse
        .getMilitaryServiceEpisode()
        .get(1)
        .getMilitaryServiceEpisodeData()
        .setServiceEpisodeEndDate(null);
    ServiceHistoryResponse response =
        ServiceHistoryResponseBuilder.buildServiceHistoryResponse(
            "uuid", deployments, serviceEpisodeResponse, mpiResponse);
    Assertions.assertEquals(response.data().size(), 2);
    ServiceHistoryResponse.ServiceHistoryEpisode episodeOne = response.data().get(0);
    ServiceHistoryResponse.ServiceHistoryEpisode episodeTwo = response.data().get(1);
    Assertions.assertEquals(episodeOne.id(), "b1f0be51-00f1-0029-8251-1cb251b0fd75");
    Assertions.assertEquals(episodeOne.type(), "service-history-episodes");
    Assertions.assertEquals(episodeTwo.id(), "aa753275-1e89-09c0-aa14-d867b6aaa0df");
    Assertions.assertEquals(episodeTwo.type(), "service-history-episodes");
    ServiceHistoryResponse.ServiceHistoryAttributes attributesOne = episodeOne.attributes();
    ServiceHistoryResponse.ServiceHistoryAttributes attributesTwo = episodeTwo.attributes();
    Assertions.assertEquals(attributesOne.firstName(), "John");
    Assertions.assertEquals(attributesOne.lastName(), "Doe");
    Assertions.assertEquals(attributesOne.branchOfService(), "Army");
    Assertions.assertEquals(attributesOne.startDate().toString(), "2000-01-01");
    Assertions.assertNull(attributesOne.endDate());
    Assertions.assertEquals(attributesOne.payGrade(), "05");
    Assertions.assertEquals(
        attributesOne.dischargeStatus(),
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.ThisWillBeRemoved);
    Assertions.assertEquals(attributesOne.separationReason(), "SUFFICIENT SERVICE FOR RETIREMENT");
    Assertions.assertEquals(attributesOne.deployments().stream().count(), 4);
    Assertions.assertEquals(
        attributesOne.deployments().get(0).startDate().toString(), "2000-02-01");
    Assertions.assertEquals(attributesOne.deployments().get(0).endDate().toString(), "2001-01-01");
    Assertions.assertEquals(attributesOne.deployments().get(0).location(), "QAT");
    Assertions.assertEquals(
        attributesOne.deployments().get(1).startDate().toString(), "2000-03-01");
    Assertions.assertEquals(attributesOne.deployments().get(1).endDate().toString(), "2003-01-01");
    Assertions.assertEquals(attributesOne.deployments().get(1).location(), "QAT");
    Assertions.assertEquals(
            attributesOne.deployments().get(2).startDate().toString(), "2004-02-01");
    Assertions.assertEquals(attributesOne.deployments().get(2).endDate().toString(), "2005-01-01");
    Assertions.assertEquals(attributesOne.deployments().get(2).location(), "AX1");
    Assertions.assertEquals(
            attributesOne.deployments().get(3).startDate().toString(), "2006-02-01");
    Assertions.assertEquals(attributesOne.deployments().get(3).endDate().toString(), "2007-01-01");
    Assertions.assertEquals(attributesOne.deployments().get(3).location(), "AX1");

    Assertions.assertEquals(attributesTwo.firstName(), "John");
    Assertions.assertEquals(attributesTwo.lastName(), "Doe");
    Assertions.assertEquals(attributesTwo.branchOfService(), "Air Force");
    Assertions.assertEquals(attributesTwo.startDate().toString(), "2002-01-01");
    Assertions.assertEquals(attributesTwo.endDate().toString(), "2003-02-01");
    Assertions.assertEquals(attributesTwo.payGrade(), "05");
    Assertions.assertEquals(
        attributesTwo.dischargeStatus(),
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.ThisWillBeRemoved);
    Assertions.assertEquals(attributesTwo.separationReason(), "SUFFICIENT SERVICE FOR RETIREMENT");
    Assertions.assertEquals(attributesTwo.deployments().stream().count(), 1);
    Assertions.assertEquals(
        attributesTwo.deployments().get(0).startDate().toString(), "2002-02-01");
    Assertions.assertEquals(attributesTwo.deployments().get(0).endDate().toString(), "2003-01-01");
    Assertions.assertEquals(attributesTwo.deployments().get(0).location(), "QAT");
  }
}
