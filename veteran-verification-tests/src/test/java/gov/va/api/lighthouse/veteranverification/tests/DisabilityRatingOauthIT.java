package gov.va.api.lighthouse.veteranverification.tests;

import static gov.va.api.health.sentinel.EnvironmentAssumptions.assumeEnvironmentNotIn;
import static gov.va.api.lighthouse.veteranverification.tests.Requestor.veteranVerificationTokenGetRequest;
import static gov.va.api.lighthouse.veteranverification.tests.SystemDefinitions.systemDefinition;
import static gov.va.api.lighthouse.veteranverification.tests.TestUtils.BAD_SCOPES;
import static gov.va.api.lighthouse.veteranverification.tests.TestUtils.SCOPES;
import static gov.va.api.lighthouse.veteranverification.tests.TestUtils.buildOauthClient;
import static gov.va.api.lighthouse.veteranverification.tests.TestUtils.createVaOauthRobotConfiguration;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import gov.va.api.health.sentinel.Environment;
import gov.va.api.health.sentinel.ExpectedResponse;
import gov.va.api.lighthouse.testclients.RetryingOauthClient;
import gov.va.api.lighthouse.veteranverification.api.ApiError;
import gov.va.api.lighthouse.veteranverification.api.v1.DisabilityRatingResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DisabilityRatingOauthIT {
  @BeforeAll
  static void assumeEnvironment() {
    // Tests are only ran in environments that have the kong gateway providing OAuth token and
    // scope validation (excluding production).
    assumeEnvironmentNotIn(Environment.LOCAL, Environment.PROD);
  }

  @Test
  void disabilityRatingBadScopes() {
    RetryingOauthClient client =
        buildOauthClient(
            createVaOauthRobotConfiguration(systemDefinition().users().disabilityRatingUser()),
            BAD_SCOPES);
    String token = client.requestToken().accessToken();
    veteranVerificationTokenGetRequest("v1/disability_rating", "application/json", 403, token);
  }

  @Test
  void disabilityRatingOauthHappyPaths() {
    RetryingOauthClient client =
        buildOauthClient(
            createVaOauthRobotConfiguration(systemDefinition().users().disabilityRatingUser()),
            SCOPES);
    String token = client.requestToken().accessToken();
    ExpectedResponse response =
        veteranVerificationTokenGetRequest("v1/disability_rating", "application/json", 200, token);
    response.expectValid(DisabilityRatingResponse.class);
    ExpectedResponse jwtResponse =
        veteranVerificationTokenGetRequest("v1/disability_rating", "application/jwt", 200, token);
    String disabilityRatingJwt = jwtResponse.response().asString();
    assertNotNull(disabilityRatingJwt);
  }

  @Test
  void noBgsUser() {
    RetryingOauthClient client =
        buildOauthClient(
            createVaOauthRobotConfiguration(systemDefinition().users().noBgsUser()), SCOPES);
    String token = client.requestToken().accessToken();
    ExpectedResponse response =
        veteranVerificationTokenGetRequest("v1/disability_rating", "application/json", 500, token);
    response.expectValid(ApiError.ServerSoapFaultApiError.class);
  }
}
