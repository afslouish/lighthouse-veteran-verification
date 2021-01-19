package gov.va.api.lighthouse.veteranverification.api;

import static gov.va.api.lighthouse.veteranverification.api.RoundTrip.assertRoundTrip;

import gov.va.api.lighthouse.veteranverification.api.ApiError.InvalidParameterApiError;
import gov.va.api.lighthouse.veteranverification.api.ApiError.MissingParameterApiError;
import org.junit.jupiter.api.Test;

public class ApiErrorTest {
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
}
