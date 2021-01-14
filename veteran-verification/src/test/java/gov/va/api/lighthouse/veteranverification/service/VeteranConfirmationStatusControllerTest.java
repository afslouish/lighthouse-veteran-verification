package gov.va.api.lighthouse.veteranverification.service;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.lighthouse.mpi.Mpi1305RequestAttributes;
import gov.va.api.lighthouse.mpi.MpiConfig;
import gov.va.api.lighthouse.veteranverification.api.VeteranStatusRequestAttributes;
import gov.va.api.lighthouse.veteranverification.service.controller.VeteranConfirmationStatusController;
import java.util.function.Function;
import org.hl7.v3.PRPAIN201306UV02;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class VeteranConfirmationStatusControllerTest {
  final VeteranStatusRequestAttributes attributes =
      VeteranStatusRequestAttributes.builder()
          .birthDate("1111-11-11")
          .firstName("test")
          .lastName("test")
          .ssn("111111111")
          .gender("M")
          .middleName("test")
          .build();

  @Mock MpiConfig mockConfig;

  @Mock PRPAIN201306UV02 mockResponse;

  @BeforeEach
  void _init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void veteranConfirmationStatusRequestTest() {
    var controller = new VeteranConfirmationStatusController(mockConfig);
    assertThat(controller.veteranStatusRequest()).isInstanceOf(Function.class);
    Function<Mpi1305RequestAttributes, PRPAIN201306UV02> mockFunction =
        (s) -> {
          return mockResponse;
        };
    var controllerFunctionOverride = new VeteranConfirmationStatusController(mockConfig);
    controllerFunctionOverride.veteranStatusRequest(mockFunction);
    assertThat(controllerFunctionOverride.veteranStatusRequest()).isEqualTo(mockFunction);
    assertThat(controllerFunctionOverride.veteranConfirmationStatusResponse(attributes))
        .isInstanceOf(PRPAIN201306UV02.class);
  }
}
