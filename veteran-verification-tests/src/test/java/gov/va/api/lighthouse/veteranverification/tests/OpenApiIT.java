package gov.va.api.lighthouse.veteranverification.tests;

import static gov.va.api.lighthouse.veteranverification.tests.Requestor.veteranVerificationGetRequest;

import org.junit.jupiter.api.Test;

public class OpenApiIT {
  @Test
  void veteranVerificationDocs() {
    veteranVerificationGetRequest("v0/docs/veteran_verification", "application/json", 200);
  }
}
