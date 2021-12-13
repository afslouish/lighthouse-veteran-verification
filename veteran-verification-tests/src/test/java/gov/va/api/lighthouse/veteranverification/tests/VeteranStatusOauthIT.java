package gov.va.api.lighthouse.veteranverification.tests;

import static gov.va.api.health.sentinel.EnvironmentAssumptions.assumeEnvironmentNotIn;
import static gov.va.api.lighthouse.veteranverification.tests.Requestor.veteranVerificationTokenGetRequest;
import static gov.va.api.lighthouse.veteranverification.tests.SystemDefinitions.systemDefinition;
import static gov.va.api.lighthouse.veteranverification.tests.TestUtils.BAD_SCOPES;
import static gov.va.api.lighthouse.veteranverification.tests.TestUtils.SCOPES;
import static gov.va.api.lighthouse.veteranverification.tests.TestUtils.buildOauthClient;
import static gov.va.api.lighthouse.veteranverification.tests.TestUtils.createVaOauthRobotConfiguration;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import gov.va.api.health.sentinel.Environment;
import gov.va.api.health.sentinel.ExpectedResponse;
import gov.va.api.lighthouse.testclients.RetryingOauthClient;
import gov.va.api.lighthouse.veteranverification.api.v1.VeteranStatusResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@Slf4j
public class VeteranStatusOauthIT {
  @BeforeAll
  static void assumeEnvironment() {
    // Tests are only ran in environments that have the kong gateway providing OAuth token and
    // scope validation (excluding production).
    assumeEnvironmentNotIn(Environment.LOCAL, Environment.PROD);
  }

  @Test
  void veteranStatusBadScopes() {
    RetryingOauthClient client =
        buildOauthClient(
            createVaOauthRobotConfiguration(systemDefinition().users().confirmedStatusUser()),
            BAD_SCOPES);
    String token = client.requestToken().accessToken();
    veteranVerificationTokenGetRequest("v1/status", "application/json", 401, token);
  }

  @Test
  void veteranStatusNotConfirmed() {
    RetryingOauthClient client =
        buildOauthClient(
            createVaOauthRobotConfiguration(systemDefinition().users().noEmisUserStatusUser()),
            SCOPES);
    String token = client.requestToken().accessToken();
    ExpectedResponse response =
        veteranVerificationTokenGetRequest("v1/status", "application/json", 200, token);
    response.expectValid(VeteranStatusResponse.class);
    VeteranStatusResponse status = response.response().getBody().as(VeteranStatusResponse.class);
    assertThat(status.getData().getAttributes().getVeteranStatus()).isEqualTo("not confirmed");
  }

  @Test
  void veteranStatusOauthHappyPaths() {
    RetryingOauthClient client =
        buildOauthClient(
            createVaOauthRobotConfiguration(systemDefinition().users().confirmedStatusUser()),
            SCOPES);
    String token = client.requestToken().accessToken();
    ExpectedResponse response =
        veteranVerificationTokenGetRequest("v1/status", "application/json", 200, token);
    response.expectValid(VeteranStatusResponse.class);
    ExpectedResponse jwtResponse =
        veteranVerificationTokenGetRequest("v1/status", "application/jwt", 200, token);
    String statusJwt = jwtResponse.response().asString();
    assertNotNull(statusJwt);
  }

  @Test
  void veteranStatusV5OauthHappyPaths() {
    RetryingOauthClient client =
        buildOauthClient(
            createVaOauthRobotConfiguration(systemDefinition().users().v5StatusUser()), SCOPES);
    String token = client.requestToken().accessToken();
    ExpectedResponse response =
        veteranVerificationTokenGetRequest("v1/status", "application/json", 200, token);
    response.expectValid(VeteranStatusResponse.class);
    ExpectedResponse jwtResponse =
        veteranVerificationTokenGetRequest("v1/status", "application/jwt", 200, token);
    String statusJwt = jwtResponse.response().asString();
    assertNotNull(statusJwt);
  }
}
