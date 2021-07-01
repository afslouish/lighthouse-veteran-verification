package gov.va.api.lighthouse.veteranverification.tests;

import static gov.va.api.lighthouse.veteranverification.tests.Requestor.veteranVerificationGetRequest;

import org.junit.jupiter.api.Test;

public class OpenApiIT {
  @Test
  void veteranConfirmationDocs() {
    veteranVerificationGetRequest("v0/docs/veteran_confirmation", 200);
  }

  @Test
  void veteranVerificationDocs() {
    veteranVerificationGetRequest("v0/docs/veteran_verification", 200);
  }
}
