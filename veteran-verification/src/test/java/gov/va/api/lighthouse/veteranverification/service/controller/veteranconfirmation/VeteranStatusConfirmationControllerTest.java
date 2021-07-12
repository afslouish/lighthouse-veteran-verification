package gov.va.api.lighthouse.veteranverification.service.controller.veteranconfirmation;

import static org.assertj.core.api.Assertions.assertThat;

import com.sun.xml.ws.fault.ServerSOAPFaultException;
import com.sun.xml.ws.wsdl.parser.InaccessibleWSDLException;
import gov.va.api.lighthouse.emis.EmisVeteranStatusServiceClient;
import gov.va.api.lighthouse.mpi.MasterPatientIndexClient;
import gov.va.api.lighthouse.veteranverification.api.VeteranStatusConfirmation;
import gov.va.api.lighthouse.veteranverification.api.VeteranStatusRequest;
import gov.va.api.lighthouse.veteranverification.service.TestUtils;
import gov.va.viers.cdi.emis.commonservice.v1.VeteranStatus;
import java.util.Arrays;
import javax.xml.soap.SOAPFault;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class VeteranStatusConfirmationControllerTest {
  final VeteranStatusRequest attributes =
      VeteranStatusRequest.builder()
          .birthDate("1111-11-11")
          .firstName("test")
          .lastName("test")
          .ssn("111111111")
          .gender("M")
          .middleName("test")
          .build();

  @Mock private MasterPatientIndexClient mpiClient;

  @Mock private EmisVeteranStatusServiceClient emisClient;

  @Mock private SOAPFault soapFault;

  @BeforeEach
  void before() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void emisInaccessibleWSDLException() {
    VeteranStatusConfirmationController controller =
        new VeteranStatusConfirmationController(mpiClient, emisClient);
    TestUtils.setMpiMockResponse(mpiClient, "mpi_profile_response_body.xml");
    TestUtils.setEmisResponseException(
        emisClient, new InaccessibleWSDLException(Arrays.asList(new Error(""))));
    Assertions.assertThrows(
        InaccessibleWSDLException.class,
        () -> {
          controller.veteranStatusConfirmationResponse(attributes);
        });
  }

  @Test
  void emisSoapFaultException() {
    VeteranStatusConfirmationController controller =
        new VeteranStatusConfirmationController(mpiClient, emisClient);
    TestUtils.setMpiMockResponse(mpiClient, "mpi_profile_response_body.xml");
    TestUtils.setEmisResponseException(emisClient, new ServerSOAPFaultException(soapFault));
    VeteranStatusConfirmation confirmation =
        controller.veteranStatusConfirmationResponse(attributes);
    assertThat(confirmation.getVeteranStatus()).isEqualTo("not confirmed");
  }

  @Test
  void happyPathRetrieveByEDIPITest() {
    VeteranStatusConfirmationController controller =
        new VeteranStatusConfirmationController(mpiClient, emisClient);
    TestUtils.setMpiMockResponse(mpiClient, "mpi_profile_response_body.xml");
    TestUtils.setEmisMockResponse(
        emisClient,
        VeteranStatus.builder()
            .title38StatusCode("V1")
            .edipi("1005396162")
            .pre911DeploymentIndicator("Y")
            .post911DeploymentIndicator("N")
            .post911CombatIndicator("N")
            .build());
    VeteranStatusConfirmation confirmation =
        controller.veteranStatusConfirmationResponse(attributes);
    assertThat(confirmation.getVeteranStatus()).isEqualTo("confirmed");
  }

  @Test
  void happyPathRetrieveByICNTest() {
    VeteranStatusConfirmationController controller =
        new VeteranStatusConfirmationController(mpiClient, emisClient);
    TestUtils.setMpiMockResponse(mpiClient, "mpi_profile_icn_response_body.xml");
    TestUtils.setEmisMockResponse(
        emisClient,
        VeteranStatus.builder()
            .title38StatusCode("V1")
            .edipi("1005396162")
            .pre911DeploymentIndicator("Y")
            .post911DeploymentIndicator("N")
            .post911CombatIndicator("N")
            .build());
    VeteranStatusConfirmation confirmation =
        controller.veteranStatusConfirmationResponse(attributes);
    assertThat(confirmation.getVeteranStatus()).isEqualTo("confirmed");
  }

  @Test
  void happyPathRetrieveByICNTestNotConfirmed() {
    VeteranStatusConfirmationController controller =
        new VeteranStatusConfirmationController(mpiClient, emisClient);
    TestUtils.setMpiMockResponse(mpiClient, "mpi_profile_response_body.xml");
    TestUtils.setEmisMockResponse(
        emisClient,
        VeteranStatus.builder()
            .title38StatusCode("")
            .edipi("1005396162")
            .pre911DeploymentIndicator("N")
            .post911DeploymentIndicator("N")
            .post911CombatIndicator("N")
            .build());
    VeteranStatusConfirmation confirmation =
        controller.veteranStatusConfirmationResponse(attributes);
    assertThat(confirmation.getVeteranStatus()).isEqualTo("not confirmed");
  }

  @Test
  void mpiInvalidRequest() {
    VeteranStatusConfirmationController controller =
        new VeteranStatusConfirmationController(mpiClient, emisClient);
    TestUtils.setMpiMockResponse(mpiClient, "mpi_profile_invalid_request_response.xml");
    VeteranStatusConfirmation confirmation =
        controller.veteranStatusConfirmationResponse(attributes);
    assertThat(confirmation.getVeteranStatus()).isEqualTo("not confirmed");
  }

  @Test
  void mpiNullResponse() {
    VeteranStatusConfirmationController controller =
        new VeteranStatusConfirmationController(mpiClient, emisClient);
    TestUtils.setNullMpiMockResponse(mpiClient);
    VeteranStatusConfirmation confirmation =
        controller.veteranStatusConfirmationResponse(attributes);
    assertThat(confirmation.getVeteranStatus()).isEqualTo("not confirmed");
  }

  @Test
  void mpiProfileNotFound() {
    VeteranStatusConfirmationController controller =
        new VeteranStatusConfirmationController(mpiClient, emisClient);
    TestUtils.setMpiMockResponse(mpiClient, "mpi_profile_not_found_response.xml");
    VeteranStatusConfirmation confirmation =
        controller.veteranStatusConfirmationResponse(attributes);
    assertThat(confirmation.getVeteranStatus()).isEqualTo("not confirmed");
  }

  @Test
  void mpiResponseError() {
    VeteranStatusConfirmationController controller =
        new VeteranStatusConfirmationController(mpiClient, emisClient);
    TestUtils.setMpiResponseException(mpiClient, new Exception("Test Exception"));
    Assertions.assertThrows(
        Exception.class,
        () -> {
          controller.veteranStatusConfirmationResponse(attributes);
        });
  }

  @Test
  void nullEmisResponse() {
    VeteranStatusConfirmationController controller =
        new VeteranStatusConfirmationController(mpiClient, emisClient);
    TestUtils.setMpiMockResponse(mpiClient, "mpi_profile_response_body.xml");
    TestUtils.setEmisMockResponse(emisClient, null);
    VeteranStatusConfirmation confirmation =
        controller.veteranStatusConfirmationResponse(attributes);
    assertThat(confirmation.getVeteranStatus()).isEqualTo("not confirmed");
  }
}