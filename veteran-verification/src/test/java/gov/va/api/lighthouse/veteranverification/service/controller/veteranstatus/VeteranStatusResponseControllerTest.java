package gov.va.api.lighthouse.veteranverification.service.controller.veteranstatus;

import static org.assertj.core.api.Assertions.assertThat;

import com.sun.xml.ws.fault.ServerSOAPFaultException;
import com.sun.xml.ws.wsdl.parser.InaccessibleWSDLException;
import gov.va.api.lighthouse.emis.EmisVeteranStatusServiceClient;
import gov.va.api.lighthouse.mpi.MasterPatientIndexClient;
import gov.va.api.lighthouse.veteranverification.api.v0.VeteranStatusResponse;
import gov.va.api.lighthouse.veteranverification.service.Exceptions.EmisInaccesibleWsdlException;
import gov.va.api.lighthouse.veteranverification.service.TestUtils;
import gov.va.viers.cdi.emis.commonservice.v1.VeteranStatus;
import java.util.Arrays;
import javax.xml.soap.SOAPFault;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class VeteranStatusResponseControllerTest {
  @Mock private MasterPatientIndexClient mpiClient;

  @Mock private EmisVeteranStatusServiceClient emisClient;

  @Mock private SOAPFault soapFault;

  @BeforeEach
  void before() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void emisInaccessibleWSDLException() {
    VeteranStatusController controller = new VeteranStatusController(mpiClient, emisClient);
    TestUtils.setMpiMockResponse(mpiClient, "mpi/mpi_profile_response_body.xml");
    TestUtils.setEmisResponseException(
        emisClient, new InaccessibleWSDLException(Arrays.asList(new Error(""))));
    Assertions.assertThrows(
        EmisInaccesibleWsdlException.class,
        () -> {
          controller.veteranStatusVerificationResponse("1111");
        });
  }

  @Test
  void emisSoapFaultException() {
    VeteranStatusController controller = new VeteranStatusController(mpiClient, emisClient);
    TestUtils.setMpiMockResponse(mpiClient, "mpi/mpi_profile_response_body.xml");
    TestUtils.setEmisResponseException(emisClient, new ServerSOAPFaultException(soapFault));
    VeteranStatusResponse confirmation = controller.veteranStatusVerificationResponse("1111");
    assertThat(confirmation.getData().getAttributes().getVeteranStatus())
        .isEqualTo("not confirmed");
  }

  @Test
  void happyPathConfirmed() {
    VeteranStatusController controller = new VeteranStatusController(mpiClient, emisClient);
    TestUtils.setMpiMockResponse(mpiClient, "mpi/mpi_profile_icn_response_body.xml");
    TestUtils.setEmisMockResponse(
        emisClient,
        VeteranStatus.builder()
            .title38StatusCode("V1")
            .edipi("1005396162")
            .pre911DeploymentIndicator("Y")
            .post911DeploymentIndicator("N")
            .post911CombatIndicator("N")
            .build());
    VeteranStatusResponse status =
        controller.veteranStatusVerificationResponse("1012861229V078999");
    assertThat(status.getData().getAttributes().getVeteranStatus()).isEqualTo("confirmed");
  }

  @Test
  void happyPathRetrieveByEDIPITest() {
    VeteranStatusController controller = new VeteranStatusController(mpiClient, emisClient);
    TestUtils.setMpiMockResponse(mpiClient, "mpi/mpi_profile_response_body.xml");
    TestUtils.setEmisMockResponse(
        emisClient,
        VeteranStatus.builder()
            .title38StatusCode("V1")
            .edipi("1005396162")
            .pre911DeploymentIndicator("Y")
            .post911DeploymentIndicator("N")
            .post911CombatIndicator("N")
            .build());
    VeteranStatusResponse status = controller.veteranStatusVerificationResponse("1005396162");
    assertThat(status.getData().getAttributes().getVeteranStatus()).isEqualTo("confirmed");
  }

  @Test
  void happyPathRetrieveByICNTest() {
    VeteranStatusController controller = new VeteranStatusController(mpiClient, emisClient);
    TestUtils.setMpiMockResponse(mpiClient, "mpi/mpi_profile_icn_response_body.xml");
    TestUtils.setEmisMockResponse(
        emisClient,
        VeteranStatus.builder()
            .title38StatusCode("V1")
            .edipi("1005396162")
            .pre911DeploymentIndicator("Y")
            .post911DeploymentIndicator("N")
            .post911CombatIndicator("N")
            .build());
    VeteranStatusResponse status =
        controller.veteranStatusVerificationResponse("1012861229V078999");
    assertThat(status.getData().getAttributes().getVeteranStatus()).isEqualTo("confirmed");
  }

  @Test
  void happyPathRetrieveByICNTestNotConfirmed() {
    VeteranStatusController controller = new VeteranStatusController(mpiClient, emisClient);
    TestUtils.setMpiMockResponse(mpiClient, "mpi/mpi_profile_response_body.xml");
    TestUtils.setEmisMockResponse(
        emisClient,
        VeteranStatus.builder()
            .title38StatusCode("")
            .edipi("1005396162")
            .pre911DeploymentIndicator("N")
            .post911DeploymentIndicator("N")
            .post911CombatIndicator("N")
            .build());
    VeteranStatusResponse status = controller.veteranStatusVerificationResponse("1111");
    assertThat(status.getData().getAttributes().getVeteranStatus()).isEqualTo("not confirmed");
  }

  @Test
  void mpiInvalidRequest() {
    VeteranStatusController controller = new VeteranStatusController(mpiClient, emisClient);
    TestUtils.setMpiMockResponse(mpiClient, "mpi/mpi_profile_invalid_request_response.xml");
    VeteranStatusResponse status = controller.veteranStatusVerificationResponse("invalid");
    assertThat(status.getData().getAttributes().getVeteranStatus()).isEqualTo("not confirmed");
  }

  @Test
  void mpiNullResponse() {
    VeteranStatusController controller = new VeteranStatusController(mpiClient, emisClient);
    TestUtils.setNullMpiMockResponse(mpiClient);
    VeteranStatusResponse status = controller.veteranStatusVerificationResponse("1111");
    assertThat(status.getData().getAttributes().getVeteranStatus()).isEqualTo("not confirmed");
  }

  @Test
  void mpiProfileNotFound() {
    VeteranStatusController controller = new VeteranStatusController(mpiClient, emisClient);
    TestUtils.setMpiMockResponse(mpiClient, "mpi/mpi_profile_not_found_response.xml");
    VeteranStatusResponse status = controller.veteranStatusVerificationResponse("not_found");
    assertThat(status.getData().getAttributes().getVeteranStatus()).isEqualTo("not confirmed");
  }

  @Test
  void mpiResponseError() {
    VeteranStatusController controller = new VeteranStatusController(mpiClient, emisClient);
    TestUtils.setMpiResponseException(mpiClient, new Exception("Test Exception"));
    Assertions.assertThrows(
        Exception.class,
        () -> {
          controller.veteranStatusVerificationResponse("1111");
        });
  }

  @Test
  void nullEmisResponse() {
    VeteranStatusController controller = new VeteranStatusController(mpiClient, emisClient);
    TestUtils.setMpiMockResponse(mpiClient, "mpi/mpi_profile_response_body.xml");
    TestUtils.setEmisMockResponse(emisClient, null);
    VeteranStatusResponse status = controller.veteranStatusVerificationResponse("1111");
    assertThat(status.getData().getAttributes().getVeteranStatus()).isEqualTo("not confirmed");
  }
}
