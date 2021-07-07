package gov.va.api.lighthouse.veteranverification.service.utils;

import gov.va.api.lighthouse.veteranverification.api.v0.Deployment;
import gov.va.api.lighthouse.veteranverification.api.v0.ServiceHistoryResponse;
import gov.va.api.lighthouse.veteranverification.service.TestUtils;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISserviceEpisodeResponseType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.hl7.v3.PRPAIN201306UV02;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MilitaryServiceEpisodeAttributeBuilderTest {
  @Test
  public void HappyPath() {
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
        TestUtils.createServiceHistoryResponse("emis/service_episodes_response.xml");
    PRPAIN201306UV02 mpiResponse = TestUtils.createMpiResponse("mpi/mpi_profile_response_body.xml");
    List<Deployment> deployments = Arrays.stream(TestUtils.deploymentArray).toList();
    ServiceHistoryResponse.ServiceHistoryAttributes attribute =
        MilitaryServiceEpisodeAttributeBuilder.buildMilitaryServiceEpisode(
            serviceEpisodeResponse.getMilitaryServiceEpisode().get(0), mpiResponse, deployments);

    Assertions.assertEquals(attribute.firstName(), "John");
    Assertions.assertEquals(attribute.lastName(), "Doe");
    Assertions.assertEquals(attribute.branchOfService(), "Army");
    Assertions.assertTrue(attribute.startDate().equals(LocalDate.of(2000,12,5)));
    Assertions.assertTrue(attribute.endDate().equals(LocalDate.of(2004,4,5)));
    Assertions.assertEquals(attribute.payGrade(), "05");
    Assertions.assertEquals(attribute.dischargeStatus(), ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.ThisWillBeRemoved);
    Assertions.assertEquals(attribute.separationReason(), "SUFFICIENT SERVICE FOR RETIREMENT");
    Assertions.assertEquals(attribute.deployments().size(), 1);
    Assertions.assertSame(attribute.deployments.get(0), deployments.get(1));
  }

  @Test
  public void HappyPathDeploymentsEmpty() {
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
            TestUtils.createServiceHistoryResponse("emis/service_episodes_response.xml");
    PRPAIN201306UV02 mpiResponse = TestUtils.createMpiResponse("mpi/mpi_profile_response_body.xml");
    List<Deployment> deployments = new ArrayList<>();
    ServiceHistoryResponse.ServiceHistoryAttributes attribute =
            MilitaryServiceEpisodeAttributeBuilder.buildMilitaryServiceEpisode(
                    serviceEpisodeResponse.getMilitaryServiceEpisode().get(0), mpiResponse, deployments);

    Assertions.assertEquals(attribute.firstName(), "John");
    Assertions.assertEquals(attribute.lastName(), "Doe");
    Assertions.assertEquals(attribute.branchOfService(), "Army");
    Assertions.assertTrue(attribute.startDate().equals(LocalDate.of(2000,12,5)));
    Assertions.assertTrue(attribute.endDate().equals(LocalDate.of(2004,4,5)));
    Assertions.assertEquals(attribute.payGrade(), "05");
    Assertions.assertEquals(attribute.dischargeStatus(), ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.ThisWillBeRemoved);
    Assertions.assertEquals(attribute.separationReason(), "SUFFICIENT SERVICE FOR RETIREMENT");
    Assertions.assertEquals(attribute.deployments().size(), 0);
  }

  @Test
  public void NullServiceEpisodeResponse() {
    PRPAIN201306UV02 mpiResponse = TestUtils.createMpiResponse("mpi/mpi_profile_response_body.xml");
    List<Deployment> deployments = Arrays.stream(TestUtils.deploymentArray).toList();
    Assertions.assertThrows(
            NullPointerException.class,
            () -> {
              MilitaryServiceEpisodeAttributeBuilder.buildMilitaryServiceEpisode(
                      null, mpiResponse, deployments);
            });
  }

  @Test
  public void NullMpiResponse() {
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
            TestUtils.createServiceHistoryResponse("emis/service_episodes_response.xml");
    List<Deployment> deployments = Arrays.stream(TestUtils.deploymentArray).toList();
    Assertions.assertThrows(
            NullPointerException.class,
            () -> {
              MilitaryServiceEpisodeAttributeBuilder.buildMilitaryServiceEpisode(
                      serviceEpisodeResponse.getMilitaryServiceEpisode().get(0), null, deployments);
            });
  }

  @Test
  public void NullDeployments() {
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
            TestUtils.createServiceHistoryResponse("emis/service_episodes_response.xml");
    PRPAIN201306UV02 mpiResponse = TestUtils.createMpiResponse("mpi/mpi_profile_response_body.xml");

    Assertions.assertThrows(
            NullPointerException.class,
            () -> {
              MilitaryServiceEpisodeAttributeBuilder.buildMilitaryServiceEpisode(
                      serviceEpisodeResponse.getMilitaryServiceEpisode().get(0), mpiResponse, null);
            });
  }
}
