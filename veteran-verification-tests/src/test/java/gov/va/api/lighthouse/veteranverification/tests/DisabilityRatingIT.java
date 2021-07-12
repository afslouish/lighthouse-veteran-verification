package gov.va.api.lighthouse.veteranverification.tests;

import static gov.va.api.lighthouse.veteranverification.tests.Requestor.veteranVerificationGetRequest;
import static gov.va.api.lighthouse.veteranverification.tests.SystemDefinitions.systemDefinition;

import gov.va.api.health.sentinel.ExpectedResponse;
import gov.va.vba.benefits.share.services.FindRatingDataResponse;
import org.junit.jupiter.api.Test;

public class DisabilityRatingIT {
  @Test
  void disabilityRatingRecordFound() {
    String request =
        String.format("v0/disability_rating/%s", systemDefinition().icns().disabilityRatingIcn());
    ExpectedResponse response = veteranVerificationGetRequest(request, 200);
    response.expectValid(FindRatingDataResponse.class);
  }
}
