package gov.va.api.lighthouse.veteranverification.tests;

import static gov.va.api.health.sentinel.EnvironmentAssumptions.assumeEnvironmentNotIn;
import static gov.va.api.lighthouse.veteranverification.tests.Requestor.veteranVerificationGetRequest;
import static gov.va.api.lighthouse.veteranverification.tests.SystemDefinitions.systemDefinition;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import gov.va.api.health.sentinel.Environment;
import gov.va.api.health.sentinel.ExpectedResponse;
import gov.va.api.lighthouse.veteranverification.api.VeteranStatusVerification;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@Slf4j
public class VeteranVerificationStatusIT {
  @BeforeAll
  static void assumeEnvironment() {
    // These tests would require PII to run in environments that do not use mock data.
    // Tests must be limited to QA, Staging Lab, and Lab environments where mock applications run.
    assumeEnvironmentNotIn(Environment.PROD, Environment.STAGING);
  }

  @Test
  void veteranVerificationEmisV5Status() {
    String request = String.format("v0/status/%s", systemDefinition().icns().v5StatusIcn());
    ExpectedResponse response = veteranVerificationGetRequest(request, 200);
    response.expectValid(VeteranStatusVerification.class);
    VeteranStatusVerification status =
        response.response().getBody().as(VeteranStatusVerification.class);
    assertThat(status.getData().getAttributes().getVeteranStatus()).isEqualTo("not confirmed");
  }

  @Test
  void veteranVerificationStatusFound() {
    String request = String.format("v0/status/%s", systemDefinition().icns().confirmedStatusIcn());
    ExpectedResponse response = veteranVerificationGetRequest(request, 200);
    response.expectValid(VeteranStatusVerification.class);
    VeteranStatusVerification status =
        response.response().getBody().as(VeteranStatusVerification.class);
    assertThat(status.getData().getAttributes().getVeteranStatus()).isEqualTo("confirmed");
  }

  @Test
  void veteranVerificationStatusNoEmisUser() {
    String request = String.format("v0/status/%s", systemDefinition().icns().noEmisUserStatusIcn());
    ExpectedResponse response = veteranVerificationGetRequest(request, 200);
    response.expectValid(VeteranStatusVerification.class);
    VeteranStatusVerification status =
        response.response().getBody().as(VeteranStatusVerification.class);
    assertThat(status.getData().getAttributes().getVeteranStatus()).isEqualTo("not confirmed");
  }

  @Test
  void veteranVerificationStatusNotFound() {
    String request = String.format("v0/status/%s", "not_found");
    ExpectedResponse response = veteranVerificationGetRequest(request, 200);
    response.expectValid(VeteranStatusVerification.class);
    VeteranStatusVerification status =
        response.response().getBody().as(VeteranStatusVerification.class);
    assertThat(status.getData().getAttributes().getVeteranStatus()).isEqualTo("not confirmed");
  }
}
