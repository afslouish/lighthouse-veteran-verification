package gov.va.api.lighthouse.veteranverification.service.controller.veteranverification;

import static gov.va.api.lighthouse.veteranverification.service.Exceptions.EmisInaccesibleWsdlException;

import com.sun.xml.ws.fault.ServerSOAPFaultException;
import com.sun.xml.ws.wsdl.parser.InaccessibleWSDLException;
import gov.va.api.lighthouse.emis.EmisMilitaryInformationServiceClient;
import gov.va.api.lighthouse.mpi.MasterPatientIndexClient;
import gov.va.api.lighthouse.veteranverification.api.v0.ServiceHistoryResponse;
import gov.va.api.lighthouse.veteranverification.service.Exceptions;
import gov.va.api.lighthouse.veteranverification.service.TestUtils;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISdeploymentResponseType;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISserviceEpisodeResponseType;
import java.util.Arrays;
import javax.xml.soap.SOAPFault;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class VeteranServiceHistoryControllerTest {
  @Mock private MasterPatientIndexClient mpiClient;

  @Mock private EmisMilitaryInformationServiceClient emisClient;

  @Mock private SOAPFault soapFault;

  @Test
  public void HappyPath() {
    VeteranServiceHistoryController controller =
        new VeteranServiceHistoryController(mpiClient, emisClient);
    TestUtils.setMpiMockResponse(mpiClient, "mpi/mpi_profile_response_body.xml");
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
        TestUtils.createServiceHistoryResponse("emis/service-episodes/ascending.xml");
    EMISdeploymentResponseType deployments =
        TestUtils.createDeploymentResponse("emis/deployments_response.xml");
    TestUtils.setServiceHistoryMockResponse(emisClient, serviceEpisodeResponse);
    TestUtils.setDeploymentsMockResponse(emisClient, deployments);
    ServiceHistoryResponse response = controller.veteranServiceHistoryResponse("icn");
    Assertions.assertEquals(response.data().size(), 2);
    ServiceHistoryResponse.ServiceHistoryEpisode episodeOne = response.data().get(0);
    ServiceHistoryResponse.ServiceHistoryEpisode episodeTwo = response.data().get(1);
    Assertions.assertEquals(episodeOne.id(), "003001fb-2023-5892-bcb7-aa41a6b485c0");
    Assertions.assertEquals(episodeOne.type(), "service-history-episodes");
    Assertions.assertEquals(episodeTwo.id(), "457f5a25-0069-59cd-9599-63784e495ede");
    Assertions.assertEquals(episodeTwo.type(), "service-history-episodes");
    ServiceHistoryResponse.ServiceHistoryAttributes attributesOne = episodeOne.attributes();
    ServiceHistoryResponse.ServiceHistoryAttributes attributesTwo = episodeTwo.attributes();
    Assertions.assertEquals(attributesOne.firstName(), "Alfredo");
    Assertions.assertEquals(attributesOne.lastName(), "Armstrong");
    Assertions.assertEquals(attributesOne.branchOfService(), "Army");
    Assertions.assertEquals(attributesOne.startDate().toString(), "2000-01-01");
    Assertions.assertEquals(attributesOne.endDate().toString(), "2001-02-01");
    Assertions.assertEquals(attributesOne.payGrade(), "E05");
    Assertions.assertEquals(
        attributesOne.dischargeStatus(),
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.HONORABLE);
    Assertions.assertEquals(attributesOne.separationReason(), "SUFFICIENT SERVICE FOR RETIREMENT");
    Assertions.assertEquals(attributesOne.deployments().stream().count(), 1);
    Assertions.assertEquals(
        attributesOne.deployments().get(0).startDate().toString(), "2000-02-01");
    Assertions.assertEquals(attributesOne.deployments().get(0).endDate().toString(), "2001-01-01");
    Assertions.assertEquals(attributesOne.deployments().get(0).location(), "QAT");
    Assertions.assertEquals(attributesTwo.firstName(), "Alfredo");
    Assertions.assertEquals(attributesTwo.lastName(), "Armstrong");
    Assertions.assertEquals(attributesTwo.branchOfService(), "Air Force");
    Assertions.assertEquals(attributesTwo.startDate().toString(), "2002-01-01");
    Assertions.assertEquals(attributesTwo.endDate().toString(), "2003-02-01");
    Assertions.assertEquals(attributesTwo.payGrade(), "E05");
    Assertions.assertEquals(
        attributesTwo.dischargeStatus(),
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.HONORABLE);
    Assertions.assertEquals(attributesTwo.separationReason(), "SUFFICIENT SERVICE FOR RETIREMENT");
    Assertions.assertEquals(attributesTwo.deployments().stream().count(), 1);
    Assertions.assertEquals(
        attributesTwo.deployments().get(0).startDate().toString(), "2002-02-01");
    Assertions.assertEquals(attributesTwo.deployments().get(0).endDate().toString(), "2003-01-01");
    Assertions.assertEquals(attributesTwo.deployments().get(0).location(), "QAT");
  }

  @BeforeEach
  public void before() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void deploymentsRequestThrowsException() {
    VeteranServiceHistoryController controller =
        new VeteranServiceHistoryController(mpiClient, emisClient);
    TestUtils.setMpiMockResponse(mpiClient, "mpi/mpi_profile_response_body.xml");
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
        TestUtils.createServiceHistoryResponse("emis/service-episodes/ascending.xml");
    TestUtils.setServiceHistoryMockResponse(emisClient, serviceEpisodeResponse);
    TestUtils.setDeploymentsResponseException(emisClient, new Exception());
    // unhandled exceptions become 503 errors
    Assertions.assertThrows(
        Exception.class,
        () -> {
          controller.veteranServiceHistoryResponse("icn");
        });
  }

  @Test
  public void deploymentsRequestThrowsInaccessibleWsdlException() {
    VeteranServiceHistoryController controller =
        new VeteranServiceHistoryController(mpiClient, emisClient);
    TestUtils.setMpiMockResponse(mpiClient, "mpi/mpi_profile_response_body.xml");
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
        TestUtils.createServiceHistoryResponse("emis/service-episodes/ascending.xml");
    TestUtils.setDeploymentsResponseException(
        emisClient, new InaccessibleWSDLException(Arrays.asList(new Error(""))));
    Assertions.assertThrows(
        EmisInaccesibleWsdlException.class,
        () -> {
          controller.veteranServiceHistoryResponse("icn");
        });
  }

  @Test
  public void ediPiOrIcnIsNull() {
    VeteranServiceHistoryController controller =
        new VeteranServiceHistoryController(mpiClient, emisClient);
    TestUtils.setMpiMockResponse(mpiClient, "mpi/mpi_profile_not_found_response.xml");
    Assertions.assertThrows(
        Exception.class,
        () -> {
          controller.veteranServiceHistoryResponse("icn");
        });
  }

  @Test
  void emisSoapFaultException() {
    VeteranServiceHistoryController controller =
        new VeteranServiceHistoryController(mpiClient, emisClient);
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
        TestUtils.createServiceHistoryResponse("emis/service-episodes/ascending.xml");
    TestUtils.setServiceHistoryMockResponse(emisClient, serviceEpisodeResponse);
    TestUtils.setEpisodesResponseException(emisClient, new ServerSOAPFaultException(soapFault));
    Assertions.assertThrows(
        Exceptions.NoServiceHistoryFoundException.class,
        () -> {
          controller.veteranServiceHistoryResponse("icn");
        });
  }

  @Test
  public void episodesIsNull() {
    VeteranServiceHistoryController controller =
        new VeteranServiceHistoryController(mpiClient, emisClient);
    TestUtils.setMpiMockResponse(mpiClient, "mpi/mpi_profile_response_body.xml");
    EMISdeploymentResponseType deployments =
        TestUtils.createDeploymentResponse("emis/deployments_response.xml");
    TestUtils.setServiceHistoryMockResponse(emisClient, null);
    TestUtils.setDeploymentsMockResponse(emisClient, deployments);
    // unhandled exceptions become 503 errors
    Assertions.assertThrows(
        Exception.class,
        () -> {
          controller.veteranServiceHistoryResponse("icn");
        });
  }

  @Test
  public void episodesRequestThrowsException() {
    VeteranServiceHistoryController controller =
        new VeteranServiceHistoryController(mpiClient, emisClient);
    TestUtils.setMpiMockResponse(mpiClient, "mpi/mpi_profile_response_body.xml");
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
        TestUtils.createServiceHistoryResponse("emis/service-episodes/ascending.xml");
    TestUtils.setServiceHistoryMockResponse(emisClient, serviceEpisodeResponse);
    TestUtils.setEpisodesResponseException(emisClient, new Exception());
    // unhandled exceptions become 503 errors
    Assertions.assertThrows(
        Exception.class,
        () -> {
          controller.veteranServiceHistoryResponse("icn");
        });
  }

  @Test
  public void episodesRequestThrowsInaccessibleWsdlException() {
    VeteranServiceHistoryController controller =
        new VeteranServiceHistoryController(mpiClient, emisClient);
    TestUtils.setMpiMockResponse(mpiClient, "mpi/mpi_profile_response_body.xml");
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
        TestUtils.createServiceHistoryResponse("emis/service-episodes/ascending.xml");
    TestUtils.setServiceHistoryMockResponse(emisClient, serviceEpisodeResponse);
    TestUtils.setEpisodesResponseException(
        emisClient, new InaccessibleWSDLException(Arrays.asList(new Error(""))));
    Assertions.assertThrows(
        EmisInaccesibleWsdlException.class,
        () -> {
          controller.veteranServiceHistoryResponse("icn");
        });
  }

  @Test
  public void mpiThrowsError() {
    VeteranServiceHistoryController controller =
        new VeteranServiceHistoryController(mpiClient, emisClient);
    TestUtils.setMpiResponseException(mpiClient, new Exception("Test Exception"));
    Assertions.assertThrows(
        Exception.class,
        () -> {
          controller.veteranServiceHistoryResponse("icn");
        });
  }
}
