package gov.va.api.lighthouse.veteranverification.tests;

import static gov.va.api.health.sentinel.EnvironmentAssumptions.assumeEnvironmentIn;
import static gov.va.api.lighthouse.veteranverification.tests.Requestor.veteranVerificationGetRequest;
import static gov.va.api.lighthouse.veteranverification.tests.TestUtils.toServiceEpisodesResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import gov.va.api.health.sentinel.Environment;
import gov.va.api.health.sentinel.ExpectedResponse;
import gov.va.api.lighthouse.veteranverification.api.ApiError;
import gov.va.api.lighthouse.veteranverification.api.v1.ServiceHistoryResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ServiceHistoryIT {
  public static final String SERVICE_HISTORY_ICN = "1012832025V743496";

  public static final String SERVICE_HISTORY_ICN_NULL_END_DATE = "1012845631V882122";

  public static final String NO_MPI_USER_ICN = "1012855585V634865";

  public static final String NO_EMIS_EPISODES_ICN = "1012667122V019349";

  @BeforeAll
  static void assumeEnvironment() {
    // Tests are only ran in environments that are not blocked by the kong gateway (ie Localhost).
    assumeEnvironmentIn(Environment.LOCAL);
  }

  @Test
  void serviceHistoryHappyJwtPath() {
    String request = String.format("v1/service_history/%s", SERVICE_HISTORY_ICN);
    ExpectedResponse response = veteranVerificationGetRequest(request, "application/jwt", 200);
    String serviceHistory = response.response().asString();
    assertNotNull(serviceHistory);
  }

  @Test
  @SneakyThrows
  void serviceHistoryHappyPath() {
    String request = String.format("v1/service_history/%s", SERVICE_HISTORY_ICN);
    ExpectedResponse response = veteranVerificationGetRequest(request, "application/json", 200);
    ServiceHistoryResponse serviceHistory =
        toServiceEpisodesResponse(response.response().getBody().print());
    assertEquals("e0088dcf-eb43-54f4-9169-8e5d9b2edfbf", serviceHistory.data().get(0).id());
    assertEquals("service-history-episodes", serviceHistory.data().get(0).type());
    assertEquals("Wesley", serviceHistory.data().get(0).attributes().firstName());
    assertEquals("Ford", serviceHistory.data().get(0).attributes().lastName());
    assertEquals("Air Force", serviceHistory.data().get(0).attributes().branchOfService());
    assertEquals("2005-04-12", serviceHistory.data().get(0).attributes().startDate().toString());
    assertEquals("2009-04-11", serviceHistory.data().get(0).attributes().endDate().toString());
    assertEquals("E04", serviceHistory.data().get(0).attributes().payGrade());
    assertEquals(
        "honorable", serviceHistory.data().get(0).attributes().dischargeStatus().serializer());
    assertEquals(
        "COMPLETION OF REQUIRED ACTIVE SERVICE",
        serviceHistory.data().get(0).attributes().separationReason());
    assertEquals(
        "2008-07-25",
        serviceHistory.data().get(0).attributes().deployments().get(0).startDate().toString());
    assertEquals(
        "2009-01-22",
        serviceHistory.data().get(0).attributes().deployments().get(0).endDate().toString());
    assertEquals("QAT", serviceHistory.data().get(0).attributes().deployments().get(0).location());
    assertEquals(
        "2008-05-01",
        serviceHistory.data().get(0).attributes().deployments().get(1).startDate().toString());
    assertEquals(
        "2008-05-31",
        serviceHistory.data().get(0).attributes().deployments().get(1).endDate().toString());
    assertEquals("AX1", serviceHistory.data().get(0).attributes().deployments().get(1).location());

    assertEquals("91d0334e-7122-5bf3-af10-4f2200c0a840", serviceHistory.data().get(1).id());
    assertEquals("service-history-episodes", serviceHistory.data().get(1).type());
    assertEquals("Wesley", serviceHistory.data().get(1).attributes().firstName());
    assertEquals("Ford", serviceHistory.data().get(1).attributes().lastName());
    assertEquals("Unknown", serviceHistory.data().get(1).attributes().branchOfService());
    assertEquals("2008-05-01", serviceHistory.data().get(1).attributes().startDate().toString());
    assertEquals("2008-05-31", serviceHistory.data().get(1).attributes().endDate().toString());
    assertNull(serviceHistory.data().get(1).attributes().payGrade());
    assertEquals(
        "honorable-absence-of-negative-report",
        serviceHistory.data().get(1).attributes().dischargeStatus().serializer());
    assertEquals("UNKNOWN", serviceHistory.data().get(1).attributes().separationReason());
    assertEquals(0, serviceHistory.data().get(1).attributes().deployments().size());

    assertEquals("0dc8158c-97aa-516e-9af3-fcff910d09ae", serviceHistory.data().get(2).id());
    assertEquals("service-history-episodes", serviceHistory.data().get(2).type());
    assertEquals("Wesley", serviceHistory.data().get(2).attributes().firstName());
    assertEquals("Ford", serviceHistory.data().get(2).attributes().lastName());
    assertEquals("Unknown", serviceHistory.data().get(2).attributes().branchOfService());
    assertEquals("2008-07-25", serviceHistory.data().get(2).attributes().startDate().toString());
    assertEquals("2009-01-22", serviceHistory.data().get(2).attributes().endDate().toString());
    assertNull(serviceHistory.data().get(2).attributes().payGrade());
    assertEquals(
        "honorable-absence-of-negative-report",
        serviceHistory.data().get(2).attributes().dischargeStatus().serializer());
    assertEquals("UNKNOWN", serviceHistory.data().get(2).attributes().separationReason());
    assertEquals(0, serviceHistory.data().get(2).attributes().deployments().size());
  }

  @Test
  public void serviceHistoryNoMpiResponse() {
    String request = String.format("v1/service_history/%s", NO_MPI_USER_ICN);
    ExpectedResponse response = veteranVerificationGetRequest(request, "application/json", 502);
    ApiError.NoServiceHistoryFoundApiError serviceHistory =
        response.response().getBody().as(ApiError.NoServiceHistoryFoundApiError.class);
    assertEquals("Unexpected response body", serviceHistory.errors().get(0).getTitle());
    assertEquals(
        "EMIS service responded with something other than the expected array of service "
            + "history hashes.",
        serviceHistory.errors().get(0).getDetail());
    assertEquals("EMIS_HIST502", serviceHistory.errors().get(0).getCode());
    assertEquals("502", serviceHistory.errors().get(0).getStatus());
  }

  @Test
  public void serviceHistoryNotFound() {
    String request = String.format("v1/service_history/%s", NO_EMIS_EPISODES_ICN);
    ExpectedResponse response = veteranVerificationGetRequest(request, "application/json", 200);
    ServiceHistoryResponse serviceHistory =
        toServiceEpisodesResponse(response.response().getBody().print());
    assertThat(serviceHistory.data()).isEmpty();
  }

  @Test
  public void serviceHistoryNullEndDate() {
    String request = String.format("v1/service_history/%s", SERVICE_HISTORY_ICN_NULL_END_DATE);
    ExpectedResponse response = veteranVerificationGetRequest(request, "application/json", 200);
    ServiceHistoryResponse serviceHistory =
        toServiceEpisodesResponse(response.response().getBody().print());
    assertEquals("45e762ad-89bd-5eac-95e1-2d18c6ea08c4", serviceHistory.data().get(0).id());
    assertEquals("service-history-episodes", serviceHistory.data().get(0).type());
    assertEquals("Herbert", serviceHistory.data().get(0).attributes().firstName());
    assertEquals("Gardner", serviceHistory.data().get(0).attributes().lastName());
    assertEquals("Air Force", serviceHistory.data().get(0).attributes().branchOfService());
    assertEquals("2007-05-29", serviceHistory.data().get(0).attributes().startDate().toString());
    assertNull(serviceHistory.data().get(0).attributes().endDate());
    assertEquals("E06", serviceHistory.data().get(0).attributes().payGrade());
    assertEquals(
        "honorable", serviceHistory.data().get(0).attributes().dischargeStatus().serializer());
    assertEquals(
        "FAILED MEDICAL/PHYSICAL PROCUREMENT STANDARDS",
        serviceHistory.data().get(0).attributes().separationReason());
    assertEquals(0, serviceHistory.data().get(0).attributes().deployments().size());
  }
}
