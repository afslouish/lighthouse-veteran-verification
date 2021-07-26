package gov.va.api.lighthouse.veteranverification.api;

import static gov.va.api.lighthouse.veteranverification.api.RoundTrip.assertRoundTrip;

import gov.va.api.lighthouse.veteranverification.api.ApiError.EmisInaccessibleWsdlErrorApiError;
import gov.va.api.lighthouse.veteranverification.api.ApiError.InaccessibleWsdlErrorApiError;
import gov.va.api.lighthouse.veteranverification.api.ApiError.InvalidParameterApiError;
import gov.va.api.lighthouse.veteranverification.api.ApiError.MissingParameterApiError;
import gov.va.api.lighthouse.veteranverification.api.ApiError.ServerSoapFaultApiError;
import org.junit.jupiter.api.Test;

public class ApiErrorTest {
  @Test
  void emisInaccessibleWsdlErrorApiError() {
    EmisInaccessibleWsdlErrorApiError emisInaccessibleWsdlErrorApiError =
        EmisInaccessibleWsdlErrorApiError.builder().build();
    assertRoundTrip(emisInaccessibleWsdlErrorApiError);
  }

  @Test
  void inaccessibleWsdlErrorApiError() {
    InaccessibleWsdlErrorApiError inaccessibleWsdlErrorApiError =
        InaccessibleWsdlErrorApiError.builder().build();
    assertRoundTrip(inaccessibleWsdlErrorApiError);
  }

  @Test
  void invalidParameterApiErrorTest() {
    InvalidParameterApiError invalidParameterApiError =
        InvalidParameterApiError.builder().value("N").field("firstName").build();
    assertRoundTrip(invalidParameterApiError);
  }

  @Test
  void missingParameterApiErrorTest() {
    MissingParameterApiError missingParameterApiError =
        MissingParameterApiError.builder().field("firstName").build();
    assertRoundTrip(missingParameterApiError);
  }

  @Test
  void serverSoapFaultApiError() {
    ServerSoapFaultApiError serverSoapFaultApiError = ServerSoapFaultApiError.builder().build();
    assertRoundTrip(serverSoapFaultApiError);
  }
}
