package gov.va.api.lighthouse.veteranverification.tests;

import static gov.va.api.health.sentinel.EnvironmentAssumptions.assumeEnvironmentNotIn;
import static gov.va.api.lighthouse.veteranverification.tests.Requestor.veteranVerificationGetRequest;
import static gov.va.api.lighthouse.veteranverification.tests.SystemDefinitions.systemDefinition;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gov.va.api.health.sentinel.Environment;
import gov.va.api.health.sentinel.ExpectedResponse;
import gov.va.api.lighthouse.veteranverification.api.ApiError;
import gov.va.api.lighthouse.veteranverification.api.v0.ServiceHistoryResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ServiceHistoryIT {
  @BeforeAll
  static void assumeEnvironment() {
    // These tests would require PII to run in environments that do not use mock data.
    // Tests must be limited to QA, Staging Lab, and Lab environments where mock applications run.
    assumeEnvironmentNotIn(Environment.PROD, Environment.STAGING);
  }

  @Test
  public void serviceHistoryEmisSoapFaultError() {
    String request =
        String.format("v0/service_history/%s", systemDefinition().icns().noEmisEpisodesUser());
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
  void serviceHistoryHappyJwtPath() {
    String request =
        String.format("v0/service_history/%s", systemDefinition().icns().serviceHistoryIcn());
    ExpectedResponse response = veteranVerificationGetRequest(request, "application/jwt", 200);
    String serviceHistory = response.response().asString();
    assertNotNull(serviceHistory);
  }

  @Test
  @SneakyThrows
  void serviceHistoryHappyPath() {
    String request =
        String.format("v0/service_history/%s", systemDefinition().icns().serviceHistoryIcn());
    ExpectedResponse response = veteranVerificationGetRequest(request, "application/json", 200);
    ServiceHistoryResponse serviceHistory =
        toServiceEpisodesResponse(response.response().getBody().print());
    assertEquals("e28a2359-48a5-55ce-890c-76b51c749b6b", serviceHistory.data().get(0).id());
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

    assertEquals("b6267474-08a3-56fc-932f-582cee0bf02c", serviceHistory.data().get(1).id());
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

    assertEquals("f3d33587-515a-5c2a-922a-0c316acdcb40", serviceHistory.data().get(2).id());
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
    String request =
        String.format("v0/service_history/%s", systemDefinition().icns().noMpiUserIcn());
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
  public void serviceHistoryNullEndDate() {
    String request =
        String.format(
            "v0/service_history/%s", systemDefinition().icns().serviceHistoryIcnNullEndDate());
    ExpectedResponse response = veteranVerificationGetRequest(request, "application/json", 200);
    ServiceHistoryResponse serviceHistory =
        toServiceEpisodesResponse(response.response().getBody().print());
    assertEquals("f4bce9c3-0aa1-5080-b6bf-7261914fffd5", serviceHistory.data().get(0).id());
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

  @SneakyThrows
  private ServiceHistoryResponse toServiceEpisodesResponse(String response) {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    return objectMapper.readValue(response, ServiceHistoryResponse.class);
  }
}
