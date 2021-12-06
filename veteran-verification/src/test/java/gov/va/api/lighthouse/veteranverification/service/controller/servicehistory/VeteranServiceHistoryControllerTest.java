package gov.va.api.lighthouse.veteranverification.service.controller.servicehistory;

import static gov.va.api.lighthouse.veteranverification.service.Exceptions.EmisInaccesibleWsdlException;

import com.sun.xml.ws.fault.ServerSOAPFaultException;
import com.sun.xml.ws.wsdl.parser.InaccessibleWSDLException;
import gov.va.api.lighthouse.emis.EmisMilitaryInformationServiceClient;
import gov.va.api.lighthouse.mpi.MasterPatientIndexClient;
import gov.va.api.lighthouse.veteranverification.api.v1.ServiceHistoryResponse;
import gov.va.api.lighthouse.veteranverification.service.Exceptions;
import gov.va.api.lighthouse.veteranverification.service.TestUtils;
import gov.va.api.lighthouse.veteranverification.service.utils.JwksProperties;
import gov.va.api.lighthouse.veteranverification.service.utils.Notary;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISdeploymentResponseType;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISguardReserveServicePeriodsResponseType;
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
    EMISguardReserveServicePeriodsResponseType grasResponse =
        TestUtils.createGrasResponse("emis/gras/single_title_training_period.xml");
    TestUtils.setServiceHistoryMockResponse(emisClient, serviceEpisodeResponse);
    TestUtils.setDeploymentsMockResponse(emisClient, deployments);
    TestUtils.setGrasMockResponse(emisClient, grasResponse);
    ServiceHistoryResponse response = controller.veteranServiceHistoryResponse("icn");
    Assertions.assertEquals(response.data().size(), 2);
    ServiceHistoryResponse.ServiceHistoryEpisode episodeOne = response.data().get(0);
    ServiceHistoryResponse.ServiceHistoryEpisode episodeTwo = response.data().get(1);
    Assertions.assertEquals("8456c5a3-dfa4-5758-9693-414bdc234b4e", episodeOne.id());
    Assertions.assertEquals("service-history-episodes", episodeOne.type());
    Assertions.assertEquals("ffeeff47-e910-5df1-93f5-f77e846dbef0", episodeTwo.id());
    Assertions.assertEquals("service-history-episodes", episodeTwo.type());
    ServiceHistoryResponse.ServiceHistoryAttributes attributesOne = episodeOne.attributes();
    ServiceHistoryResponse.ServiceHistoryAttributes attributesTwo = episodeTwo.attributes();
    Assertions.assertEquals("Alfredo", attributesOne.firstName());
    Assertions.assertEquals("Armstrong", attributesOne.lastName());
    Assertions.assertEquals("Army", attributesOne.branchOfService());
    Assertions.assertEquals("2000-01-01", attributesOne.startDate().toString());
    Assertions.assertEquals("2001-02-01", attributesOne.endDate().toString());
    Assertions.assertEquals("E05", attributesOne.payGrade());
    Assertions.assertEquals(
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.HONORABLE,
        attributesOne.dischargeStatus());
    Assertions.assertEquals("SUFFICIENT SERVICE FOR RETIREMENT", attributesOne.separationReason());
    Assertions.assertEquals(1, attributesOne.deployments().stream().count());
    Assertions.assertEquals(
        "2000-02-01", attributesOne.deployments().get(0).startDate().toString());
    Assertions.assertEquals("2001-01-01", attributesOne.deployments().get(0).endDate().toString());
    Assertions.assertEquals("QAT", attributesOne.deployments().get(0).location());
    Assertions.assertEquals("Alfredo", attributesTwo.firstName());
    Assertions.assertEquals("Armstrong", attributesTwo.lastName());
    Assertions.assertEquals("Air Force", attributesTwo.branchOfService());
    Assertions.assertEquals("2002-01-01", attributesTwo.startDate().toString());
    Assertions.assertEquals("2003-02-01", attributesTwo.endDate().toString());
    Assertions.assertEquals("E05", attributesTwo.payGrade());
    Assertions.assertEquals(
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.HONORABLE,
        attributesTwo.dischargeStatus());
    Assertions.assertEquals("SUFFICIENT SERVICE FOR RETIREMENT", attributesTwo.separationReason());
    Assertions.assertEquals(1, attributesTwo.deployments().stream().count());
    Assertions.assertEquals(
        "2002-02-01", attributesTwo.deployments().get(0).startDate().toString());
    Assertions.assertEquals("2003-01-01", attributesTwo.deployments().get(0).endDate().toString());
    Assertions.assertEquals("QAT", attributesTwo.deployments().get(0).location());
  }

  @Test
  public void HappyPathJwt() {
    VeteranServiceHistoryController controller =
        new VeteranServiceHistoryController(mpiClient, emisClient, notary);
    TestUtils.setMpiMockResponse(mpiClient, "mpi/mpi_profile_response_body.xml");
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
        TestUtils.createServiceHistoryResponse("emis/service-episodes/ascending.xml");
    EMISdeploymentResponseType deployments =
        TestUtils.createDeploymentResponse("emis/deployments_response.xml");
    EMISguardReserveServicePeriodsResponseType grasResponse =
        TestUtils.createGrasResponse("emis/gras/single_title_training_period.xml");
    TestUtils.setServiceHistoryMockResponse(emisClient, serviceEpisodeResponse);
    TestUtils.setDeploymentsMockResponse(emisClient, deployments);
    TestUtils.setGrasMockResponse(emisClient, grasResponse);
    String response = controller.veteranServiceHistoryJwtResponse("icn");
    Assertions.assertEquals(
        "eyJraWQiOiJmYWtlIiwidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYifQ.eyJkYXRhIjpbeyJpZCI6Ijg"
            + "0NTZjNWEzLWRmYTQtNTc1OC05NjkzLTQxNGJkYzIzNGI0ZSIsInR5cGUiOiJzZXJ2aWNlLWhpc3Rvc"
            + "nktZXBpc29kZXMiLCJhdHRyaWJ1dGVzIjp7ImZpcnN0X25hbWUiOiJBbGZyZWRvIiwibGFzdF9uYW1l"
            + "IjoiQXJtc3Ryb25nIiwiYnJhbmNoX29mX3NlcnZpY2UiOiJBcm15Iiwic3RhcnRfZGF0ZSI6IjIwMDA"
            + "tMDEtMDEiLCJlbmRfZGF0ZSI6IjIwMDEtMDItMDEiLCJwYXlfZ3JhZGUiOiJFMDUiLCJkaXNjaGFyZ2V"
            + "fc3RhdHVzIjoiaG9ub3JhYmxlIiwic2VwYXJhdGlvbl9yZWFzb24iOiJTVUZGSUNJRU5UIFNFUlZJQ0"
            + "UgRk9SIFJFVElSRU1FTlQiLCJkZXBsb3ltZW50cyI6W3sic3RhcnRfZGF0ZSI6IjIwMDAtMDItMDEiL"
            + "CJlbmRfZGF0ZSI6IjIwMDEtMDEtMDEiLCJsb2NhdGlvbiI6IlFBVCJ9XX19LHsiaWQiOiJmZmVlZmY"
            + "0Ny1lOTEwLTVkZjEtOTNmNS1mNzdlODQ2ZGJlZjAiLCJ0eXBlIjoic2VydmljZS1oaXN0b3J5LWVwaX"
            + "NvZGVzIiwiYXR0cmlidXRlcyI6eyJmaXJzdF9uYW1lIjoiQWxmcmVkbyIsImxhc3RfbmFtZSI6IkFyb"
            + "XN0cm9uZyIsImJyYW5jaF9vZl9zZXJ2aWNlIjoiQWlyIEZvcmNlIiwic3RhcnRfZGF0ZSI6IjIwMDIt"
            + "MDEtMDEiLCJlbmRfZGF0ZSI6IjIwMDMtMDItMDEiLCJwYXlfZ3JhZGUiOiJFMDUiLCJkaXNjaGFyZ2V"
            + "fc3RhdHVzIjoiaG9ub3JhYmxlIiwic2VwYXJhdGlvbl9yZWFzb24iOiJTVUZGSUNJRU5UIFNFUlZJQ0"
            + "UgRk9SIFJFVElSRU1FTlQiLCJkZXBsb3ltZW50cyI6W3sic3RhcnRfZGF0ZSI6IjIwMDItMDItMDEiL"
            + "CJlbmRfZGF0ZSI6IjIwMDMtMDEtMDEiLCJsb2NhdGlvbiI6IlFBVCJ9XX19XX0.SoBVl41Ue0Dpf74Z0"
            + "Txx3OJVAZ1vVebRCidZ5sFoEhfZw0XUmUZW08fY82Vm2HYAG-l6oLL7xbMrbzuhWg498fZZ3pC2R7VxA"
            + "gJ5RKXTcQRvjpuWMicV8Lz0KFr7wezb_QrmBltKSmqwU5JjTDO_k67oP_p9c1Ijl17jDLuUrCZe_7YbU"
            + "i9fXZdUKg6tl6p6JZL6zIMHnPQFRIlxhpYT7XHz9T_6WRUCdQh_Wm5TyF0nDDuUHT01_1VHvHqDUgt43"
            + "hVP6uQmgUzq_v03Mpuj61MTltA56dW0XkPnE6H6CGvqeuERfL8HWO-6aG-82gsi-Z5o4S0x7Asb3VdI"
            + "H8O_Ng",
        response);
  }

  @BeforeEach
  public void before() {
    MockitoAnnotations.initMocks(this);
    notary =
        new Notary(
            JwksProperties.builder()
                .keyStorePath("src/test/resources/fakekeystore.jks")
                .keyStorePassword("secret")
                .currentKeyId("fake")
                .currentKeyPassword("secret")
                .build());
  }

  @Test
  public void deploymentsRequestThrowsException() {
    VeteranServiceHistoryController controller =
        new VeteranServiceHistoryController(mpiClient, emisClient, notary);
    TestUtils.setMpiMockResponse(mpiClient, "mpi/mpi_profile_response_body.xml");
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
        TestUtils.createServiceHistoryResponse("emis/service-episodes/ascending.xml");
    EMISguardReserveServicePeriodsResponseType grasResponse =
        TestUtils.createGrasResponse("emis/gras/single_title_training_period.xml");
    TestUtils.setServiceHistoryMockResponse(emisClient, serviceEpisodeResponse);
    TestUtils.setDeploymentsResponseException(emisClient, new Exception());
    TestUtils.setGrasMockResponse(emisClient, grasResponse);
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
    EMISguardReserveServicePeriodsResponseType grasResponse =
        TestUtils.createGrasResponse("emis/gras/single_title_training_period.xml");
    TestUtils.setServiceHistoryMockResponse(emisClient, serviceEpisodeResponse);
    TestUtils.setGrasMockResponse(emisClient, grasResponse);
    TestUtils.setEpisodesResponseException(emisClient, new ServerSOAPFaultException(soapFault));
    Assertions.assertThrows(
        Exceptions.NoServiceHistoryFoundException.class,
        () -> {
          controller.veteranServiceHistoryResponse("icn");
        });
  }

  @Test
  public void episodesAndGrasResponseAreNull() {
    VeteranServiceHistoryController controller =
        new VeteranServiceHistoryController(mpiClient, emisClient, notary);
    TestUtils.setMpiMockResponse(mpiClient, "mpi/mpi_profile_response_body.xml");
    EMISdeploymentResponseType deployments =
        TestUtils.createDeploymentResponse("emis/deployments_response.xml");
    TestUtils.setServiceHistoryMockResponse(emisClient, null);
    TestUtils.setDeploymentsMockResponse(emisClient, deployments);
    TestUtils.setGrasMockResponse(emisClient, null);
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
    EMISguardReserveServicePeriodsResponseType grasResponse =
        TestUtils.createGrasResponse("emis/gras/single_title_training_period.xml");
    TestUtils.setServiceHistoryMockResponse(emisClient, serviceEpisodeResponse);
    TestUtils.setEpisodesResponseException(emisClient, new Exception());
    TestUtils.setGrasMockResponse(emisClient, grasResponse);
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
    EMISguardReserveServicePeriodsResponseType grasResponse =
        TestUtils.createGrasResponse("emis/gras/single_title_training_period.xml");
    TestUtils.setServiceHistoryMockResponse(emisClient, serviceEpisodeResponse);
    TestUtils.setGrasMockResponse(emisClient, grasResponse);
    TestUtils.setEpisodesResponseException(
        emisClient, new InaccessibleWSDLException(Arrays.asList(new Error(""))));
    Assertions.assertThrows(
        EmisInaccesibleWsdlException.class,
        () -> {
          controller.veteranServiceHistoryResponse("icn");
        });
  }

  @Test
  public void grasRequestThrowsInaccessibleWsdlException() {
    VeteranServiceHistoryController controller =
        new VeteranServiceHistoryController(mpiClient, emisClient, notary);
    TestUtils.setMpiMockResponse(mpiClient, "mpi/mpi_profile_response_body.xml");
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
        TestUtils.createServiceHistoryResponse("emis/service-episodes/ascending.xml");
    EMISdeploymentResponseType deployments =
        TestUtils.createDeploymentResponse("emis/deployments_response.xml");
    TestUtils.setServiceHistoryMockResponse(emisClient, serviceEpisodeResponse);
    TestUtils.setDeploymentsMockResponse(emisClient, deployments);
    TestUtils.setGrasResponseException(
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
