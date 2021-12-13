package gov.va.api.lighthouse.veteranverification.tests;

import static gov.va.api.health.sentinel.EnvironmentAssumptions.assumeEnvironmentNotIn;
import static gov.va.api.lighthouse.veteranverification.tests.Requestor.veteranVerificationTokenGetRequest;
import static gov.va.api.lighthouse.veteranverification.tests.SystemDefinitions.systemDefinition;
import static gov.va.api.lighthouse.veteranverification.tests.TestUtils.BAD_SCOPES;
import static gov.va.api.lighthouse.veteranverification.tests.TestUtils.SCOPES;
import static gov.va.api.lighthouse.veteranverification.tests.TestUtils.buildOauthClient;
import static gov.va.api.lighthouse.veteranverification.tests.TestUtils.createVaOauthRobotConfiguration;
import static gov.va.api.lighthouse.veteranverification.tests.TestUtils.toServiceEpisodesResponse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import gov.va.api.health.sentinel.Environment;
import gov.va.api.health.sentinel.ExpectedResponse;
import gov.va.api.lighthouse.testclients.RetryingOauthClient;
import gov.va.api.lighthouse.veteranverification.api.v1.ServiceHistoryResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@Slf4j
public class ServiceHistoryOauthIT {
  @BeforeAll
  static void assumeEnvironment() {
    // Tests are only ran in environments that have the kong gateway providing OAuth token and
    // scope validation (excluding production).
    assumeEnvironmentNotIn(Environment.LOCAL, Environment.PROD);
  }

  @Test
  void serviceHistoryBadScopes() {
    RetryingOauthClient client =
        buildOauthClient(
            createVaOauthRobotConfiguration(systemDefinition().users().serviceHistoryUser()),
            BAD_SCOPES);
    String token = client.requestToken().accessToken();
    veteranVerificationTokenGetRequest("v1/service_history", "application/json", 401, token);
  }

  @Test
  public void serviceHistoryNotFound() {
    RetryingOauthClient client =
        buildOauthClient(
            createVaOauthRobotConfiguration(systemDefinition().users().noEmisEpisodesUser()),
            SCOPES);
    String token = client.requestToken().accessToken();
    ExpectedResponse response =
        veteranVerificationTokenGetRequest("v1/service_history", "application/json", 200, token);
    ServiceHistoryResponse serviceHistory =
        toServiceEpisodesResponse(response.response().getBody().print());
    assertNotNull(serviceHistory);
  }

  @Test
  void serviceHistoryOauthHappyPaths() {
    RetryingOauthClient client =
        buildOauthClient(
            createVaOauthRobotConfiguration(systemDefinition().users().serviceHistoryUser()),
            SCOPES);
    String token = client.requestToken().accessToken();
    ExpectedResponse response =
        veteranVerificationTokenGetRequest("v1/service_history", "application/json", 200, token);
    ServiceHistoryResponse serviceHistory =
        toServiceEpisodesResponse(response.response().getBody().print());
    assertNotNull(serviceHistory);
    ExpectedResponse jwtResponse =
        veteranVerificationTokenGetRequest("v1/service_history", "application/jwt", 200, token);
    String serviceHistoryJwt = jwtResponse.response().asString();
    assertNotNull(serviceHistoryJwt);
  }
}
