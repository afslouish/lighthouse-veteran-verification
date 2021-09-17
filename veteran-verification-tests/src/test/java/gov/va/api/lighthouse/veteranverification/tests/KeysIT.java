package gov.va.api.lighthouse.veteranverification.tests;

import static gov.va.api.lighthouse.veteranverification.tests.Requestor.veteranVerificationGetRequest;

import gov.va.api.health.sentinel.ExpectedResponse;
import org.junit.jupiter.api.Test;

public class KeysIT {
  @Test
  public void happyPath() {
    ExpectedResponse response = veteranVerificationGetRequest("v1/keys", "application/json", 200);
  }
}
