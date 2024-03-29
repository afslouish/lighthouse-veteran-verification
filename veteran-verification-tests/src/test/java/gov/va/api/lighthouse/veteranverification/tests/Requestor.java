package gov.va.api.lighthouse.veteranverification.tests;

import static gov.va.api.health.sentinel.ExpectedResponse.logAllWithTruncatedBody;
import static gov.va.api.lighthouse.veteranverification.tests.SystemDefinitions.systemDefinition;

import gov.va.api.health.sentinel.ExpectedResponse;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
final class Requestor {
  static ExpectedResponse veteranVerificationGetRequest(
      @NonNull String request, String contentType, int expectedStatus) {
    SystemDefinitions.Service svc = systemDefinition().veteranVerification();
    log.info("Expect {} is status code ({})", svc.apiPath() + request, expectedStatus);
    return ExpectedResponse.of(
            RestAssured.given()
                .accept(contentType)
                .baseUri(svc.url())
                .port(svc.port())
                .relaxedHTTPSValidation()
                .request(Method.GET, svc.urlWithApiPath() + request))
        .logAction(logAllWithTruncatedBody(2000))
        .expect(expectedStatus);
  }

  static ExpectedResponse veteranVerificationTokenGetRequest(
      @NonNull String request, String contentType, int expectedStatus, String token) {
    SystemDefinitions.Service svc = systemDefinition().veteranVerification();
    log.info("Making request: {}", svc.urlWithApiPath() + request);
    log.info("Expect {} is status code ({})", svc.apiPath() + request, expectedStatus);
    ExpectedResponse response =
        ExpectedResponse.of(
                RestAssured.given()
                    .accept(contentType)
                    .baseUri(svc.url())
                    .port(svc.port())
                    .relaxedHTTPSValidation()
                    .header("Authorization", "Bearer " + token)
                    .request(Method.GET, svc.urlWithApiPath() + request))
            .logAction(logAllWithTruncatedBody(2000))
            .expect(expectedStatus);
    return response;
  }
}
