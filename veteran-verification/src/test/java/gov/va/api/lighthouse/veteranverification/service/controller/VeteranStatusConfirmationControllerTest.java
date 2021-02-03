package gov.va.api.lighthouse.veteranverification.service.controller;

import gov.va.api.lighthouse.veteranverification.api.VeteranStatusRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

  @Test
  void EmisResponseError() {
    throw new UnsupportedOperationException();
  }

  @Test
  void MpiResponseError() {
    throw new UnsupportedOperationException();
  }

  @Test
  void NoMpiResponse() {
    throw new UnsupportedOperationException();
  }

  @BeforeEach
  void _init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void happyPathRetrieveByEDIPITest() {
    throw new UnsupportedOperationException();
  }

  @Test
  void happyPathRetrieveByICNTest() {
    throw new UnsupportedOperationException();
  }
}
