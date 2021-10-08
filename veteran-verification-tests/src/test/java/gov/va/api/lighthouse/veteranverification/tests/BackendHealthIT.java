package gov.va.api.lighthouse.veteranverification.tests;

import static gov.va.api.lighthouse.veteranverification.tests.Requestor.veteranVerificationGetRequest;

import gov.va.api.health.sentinel.ExpectedResponse;
import org.junit.jupiter.api.Test;

public class BackendHealthIT {
  @Test
  public void HappyPath() {
    ExpectedResponse response =
        veteranVerificationGetRequest("backend/health", "application/json", 200);
  }
}
