package gov.va.api.lighthouse.veteranverification.api;

import static gov.va.api.lighthouse.veteranverification.api.RoundTrip.assertRoundTrip;

import gov.va.api.lighthouse.veteranverification.api.ApiError.EmisInaccessibleWsdlErrorApiError;
import gov.va.api.lighthouse.veteranverification.api.ApiError.InaccessibleWsdlErrorApiError;
import gov.va.api.lighthouse.veteranverification.api.ApiError.NoServiceHistoryFoundApiError;
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
  void noServiceHistoryFoundApiErrorError() {
    NoServiceHistoryFoundApiError noServiceHistoryFoundApiError =
        NoServiceHistoryFoundApiError.builder().build();
    assertRoundTrip(noServiceHistoryFoundApiError);
  }

  @Test
  void serverSoapFaultApiError() {
    ServerSoapFaultApiError serverSoapFaultApiError = ServerSoapFaultApiError.builder().build();
    assertRoundTrip(serverSoapFaultApiError);
  }
}
