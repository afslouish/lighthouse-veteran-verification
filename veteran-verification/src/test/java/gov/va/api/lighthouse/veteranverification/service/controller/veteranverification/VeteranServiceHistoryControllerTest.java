package gov.va.api.lighthouse.veteranverification.service.controller.veteranverification;

import static gov.va.api.lighthouse.veteranverification.service.Exceptions.EmisInaccesibleWsdlException;

import com.sun.xml.ws.fault.ServerSOAPFaultException;
import com.sun.xml.ws.wsdl.parser.InaccessibleWSDLException;
import gov.va.api.lighthouse.emis.EmisMilitaryInformationServiceClient;
import gov.va.api.lighthouse.mpi.MasterPatientIndexClient;
import gov.va.api.lighthouse.veteranverification.api.v0.ServiceHistoryResponse;
import gov.va.api.lighthouse.veteranverification.service.Exceptions;
import gov.va.api.lighthouse.veteranverification.service.TestUtils;
import gov.va.api.lighthouse.veteranverification.service.utils.Notary;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISdeploymentResponseType;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISserviceEpisodeResponseType;
import java.io.File;
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

  private Notary notary;

  @Test
  public void HappyPath() {
    VeteranServiceHistoryController controller =
        new VeteranServiceHistoryController(mpiClient, emisClient, notary);
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

  @Test
  public void HappyPathveteranServiceHistoryJwtResponse() {
    VeteranServiceHistoryController controller =
        new VeteranServiceHistoryController(mpiClient, emisClient, notary);
    TestUtils.setMpiMockResponse(mpiClient, "mpi/mpi_profile_response_body.xml");
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
        TestUtils.createServiceHistoryResponse("emis/service-episodes/ascending.xml");
    EMISdeploymentResponseType deployments =
        TestUtils.createDeploymentResponse("emis/deployments_response.xml");
    TestUtils.setServiceHistoryMockResponse(emisClient, serviceEpisodeResponse);
    TestUtils.setDeploymentsMockResponse(emisClient, deployments);
    String response = controller.veteranServiceHistoryJwtResponse("icn");
    Assertions.assertEquals(
        "eyJraWQiOiIwODhkMjQyMzJmZjZmYWE0Y2Q0Y2ZlYzEyNmFkMDQzMWRmZjFlYTAyOGFmZGIxYzg2YjM3MThkNzAxN"
            + "zFhZWQ2IiwidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYifQ.eyJkYXRhIjpbeyJpZCI6IjAwMzAwMWZiLTIwMjMtNTg5Mi"
            + "1iY2I3LWFhNDFhNmI0ODVjMCIsInR5cGUiOiJzZXJ2aWNlLWhpc3RvcnktZXBpc29kZXMiLCJhdHRyaWJ1dGVzIjp7Im"
            + "ZpcnN0X25hbWUiOiJBbGZyZWRvIiwibGFzdF9uYW1lIjoiQXJtc3Ryb25nIiwiYnJhbmNoX29mX3NlcnZpY2UiOiJBcm"
            + "15Iiwic3RhcnRfZGF0ZSI6IjIwMDAtMDEtMDEiLCJlbmRfZGF0ZSI6IjIwMDEtMDItMDEiLCJwYXlfZ3JhZGUiOiJFMD"
            + "UiLCJkaXNjaGFyZ2Vfc3RhdHVzIjoiaG9ub3JhYmxlIiwic2VwYXJhdGlvbl9yZWFzb24iOiJTVUZGSUNJRU5UIFNFUl"
            + "ZJQ0UgRk9SIFJFVElSRU1FTlQiLCJkZXBsb3ltZW50cyI6W3sic3RhcnRfZGF0ZSI6IjIwMDAtMDItMDEiLCJlbmRfZG"
            + "F0ZSI6IjIwMDEtMDEtMDEiLCJsb2NhdGlvbiI6IlFBVCJ9XX19LHsiaWQiOiI0NTdmNWEyNS0wMDY5LTU5Y2QtOTU5OS"
            + "02Mzc4NGU0OTVlZGUiLCJ0eXBlIjoic2VydmljZS1oaXN0b3J5LWVwaXNvZGVzIiwiYXR0cmlidXRlcyI6eyJmaXJzdF"
            + "9uYW1lIjoiQWxmcmVkbyIsImxhc3RfbmFtZSI6IkFybXN0cm9uZyIsImJyYW5jaF9vZl9zZXJ2aWNlIjoiQWlyIEZvcm"
            + "NlIiwic3RhcnRfZGF0ZSI6IjIwMDItMDEtMDEiLCJlbmRfZGF0ZSI6IjIwMDMtMDItMDEiLCJwYXlfZ3JhZGUiOiJFMD"
            + "UiLCJkaXNjaGFyZ2Vfc3RhdHVzIjoiaG9ub3JhYmxlIiwic2VwYXJhdGlvbl9yZWFzb24iOiJTVUZGSUNJRU5UIFNFUl"
            + "ZJQ0UgRk9SIFJFVElSRU1FTlQiLCJkZXBsb3ltZW50cyI6W3sic3RhcnRfZGF0ZSI6IjIwMDItMDItMDEiLCJlbmRfZG"
            + "F0ZSI6IjIwMDMtMDEtMDEiLCJsb2NhdGlvbiI6IlFBVCJ9XX19XX0.to888f8peRk9QPJJCB9PldMsWoVdLtllHgEKy0"
            + "nEWLHVi7JgLr85iZmnvK4XaiUK8NNRDMrD48EsJkArh2kbfUAMDzXMQBk5Ht1vzI9Sc6s_KrIab5B6QVJhl3JgvpYpaY"
            + "Hp3eOi2L_RnfV0z2ucDel_6dw2mzQcXfyb1SgNu1c",
        response);
  }

  @BeforeEach
  public void before() {
    MockitoAnnotations.initMocks(this);
    notary = new Notary(new File("src/test/resources/verification_test_private.pem"));
  }

  @Test
  public void deploymentsRequestThrowsException() {
    VeteranServiceHistoryController controller =
        new VeteranServiceHistoryController(mpiClient, emisClient, notary);
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
        new VeteranServiceHistoryController(mpiClient, emisClient, notary);
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
        new VeteranServiceHistoryController(mpiClient, emisClient, notary);
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
        new VeteranServiceHistoryController(mpiClient, emisClient, notary);
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
        new VeteranServiceHistoryController(mpiClient, emisClient, notary);
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
        new VeteranServiceHistoryController(mpiClient, emisClient, notary);
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
        new VeteranServiceHistoryController(mpiClient, emisClient, notary);
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
        new VeteranServiceHistoryController(mpiClient, emisClient, notary);
    TestUtils.setMpiResponseException(mpiClient, new Exception("Test Exception"));
    Assertions.assertThrows(
        Exception.class,
        () -> {
          controller.veteranServiceHistoryResponse("icn");
        });
  }
}
