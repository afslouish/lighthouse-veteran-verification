package gov.va.api.lighthouse.veteranverification.service.controller;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.lighthouse.emis.EmisConfigV1;
import gov.va.api.lighthouse.mpi.Mpi1305RequestAttributes;
import gov.va.api.lighthouse.mpi.MpiConfig;
import gov.va.api.lighthouse.veteranverification.api.VeteranStatusConfirmation;
import gov.va.api.lighthouse.veteranverification.api.VeteranStatusRequest;
import gov.va.viers.cdi.emis.requestresponse.v1.EMISveteranStatusResponseType;
import gov.va.viers.cdi.emis.requestresponse.v1.InputEdiPiOrIcn;
import java.util.function.Function;
import org.hl7.v3.PRPAIN201306UV02;
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

  @Mock MpiConfig mpiConfig;

  @Mock PRPAIN201306UV02 mpiMockResponse;

  @Mock EmisConfigV1 emisConfig;

  @Mock EMISveteranStatusResponseType emisMockResponse;

  @BeforeEach
  void _init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void emisVeteranStatusRequestTest() {
    var controller = new VeteranStatusConfirmationController(mpiConfig, emisConfig);
    assertThat(controller.emisVeteranStatusRequest()).isInstanceOf(Function.class);
    Function<InputEdiPiOrIcn, EMISveteranStatusResponseType> emisMockFunction =
        (m) -> emisMockResponse;
    var controllerFunctionOverride =
        new VeteranStatusConfirmationController(mpiConfig, emisConfig);
    controllerFunctionOverride.emisVeteranStatusRequest(emisMockFunction);
    assertThat(controllerFunctionOverride.emisVeteranStatusRequest()).isEqualTo(emisMockFunction);
  }

  @Test
  void mpi1305RequestTest() {
    var controller = new VeteranStatusConfirmationController(mpiConfig, emisConfig);
    assertThat(controller.mpi1305Request()).isInstanceOf(Function.class);
    Function<Mpi1305RequestAttributes, PRPAIN201306UV02> mpiMockFunction = (s) -> mpiMockResponse;
    var controllerFunctionOverride =
        new VeteranStatusConfirmationController(mpiConfig, emisConfig);
    controllerFunctionOverride.mpi1305Request(mpiMockFunction);
    assertThat(controllerFunctionOverride.mpi1305Request()).isEqualTo(mpiMockFunction);
    assertThat(controllerFunctionOverride.veteranStatusConfirmationResponse(attributes))
        .isInstanceOf(VeteranStatusConfirmation.class);
  }

  @Test
  void happyPathRetrieveByEDIPITest() {
    var controller = new VeteranStatusConfirmationController(mpiConfig, emisConfig);
    throw new UnsupportedOperationException();
  }

  @Test
  void happyPathRetrieveByICNTest() {
    var controller = new VeteranStatusConfirmationController(mpiConfig, emisConfig);
    throw new UnsupportedOperationException();
  }

  @Test
  void NoMpiResponse() {
    var controller = new VeteranStatusConfirmationController(mpiConfig, emisConfig);
    throw new UnsupportedOperationException();
  }

  @Test
  void MpiResponseError() {
    var controller = new VeteranStatusConfirmationController(mpiConfig, emisConfig);
    throw new UnsupportedOperationException();
  }

  @Test
  void EmisResponseError() {
    var controller = new VeteranStatusConfirmationController(mpiConfig, emisConfig);
    throw new UnsupportedOperationException();
  }
}
