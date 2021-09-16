package gov.va.api.lighthouse.veteranverification.tests;

import static gov.va.api.health.sentinel.EnvironmentAssumptions.assumeEnvironmentNotIn;
import static gov.va.api.lighthouse.veteranverification.tests.Requestor.veteranVerificationGetRequest;
import static gov.va.api.lighthouse.veteranverification.tests.SystemDefinitions.systemDefinition;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import gov.va.api.health.sentinel.Environment;
import gov.va.api.health.sentinel.ExpectedResponse;
import gov.va.api.lighthouse.veteranverification.api.ApiError;
import gov.va.api.lighthouse.veteranverification.api.v0.ServiceHistoryResponse;
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
  void serviceHistoryHappyPath() {
    String request =
        String.format("v0/service_history/%s", systemDefinition().icns().serviceHistoryIcn());
    ExpectedResponse response = veteranVerificationGetRequest(request, "application/json", 200);
    ServiceHistoryResponse serviceHistory =
        response.response().getBody().as(ServiceHistoryResponse.class);
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
    assertEquals("0cbc4143-6d60-5d7a-8c7f-9887a4478c94", serviceHistory.data().get(1).id());
    assertEquals("service-history-episodes", serviceHistory.data().get(1).type());
    assertEquals("Wesley", serviceHistory.data().get(1).attributes().firstName());
    assertEquals("Ford", serviceHistory.data().get(1).attributes().lastName());
    assertEquals("Air Force Reserve", serviceHistory.data().get(1).attributes().branchOfService());
    assertEquals("2009-04-12", serviceHistory.data().get(1).attributes().startDate().toString());
    assertEquals("2013-04-11", serviceHistory.data().get(1).attributes().endDate().toString());
    assertEquals("E04", serviceHistory.data().get(1).attributes().payGrade());
    assertEquals(
        "honorable", serviceHistory.data().get(1).attributes().dischargeStatus().serializer());
    assertEquals(
        "COMPLETION OF REQUIRED ACTIVE SERVICE",
        serviceHistory.data().get(1).attributes().separationReason());
    assertEquals(
        "2009-05-01",
        serviceHistory.data().get(1).attributes().deployments().get(0).startDate().toString());
    assertEquals(
        "2009-05-31",
        serviceHistory.data().get(1).attributes().deployments().get(0).endDate().toString());
    assertEquals("QAT", serviceHistory.data().get(1).attributes().deployments().get(0).location());
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
        response.response().getBody().as(ServiceHistoryResponse.class);
    assertEquals("0f33ef25-241a-57bb-998c-4c06ded1be8b", serviceHistory.data().get(0).id());
    assertEquals("service-history-episodes", serviceHistory.data().get(0).type());
    assertEquals("Sam", serviceHistory.data().get(0).attributes().firstName());
    assertEquals("Gardner", serviceHistory.data().get(0).attributes().lastName());
    assertEquals("Navy", serviceHistory.data().get(0).attributes().branchOfService());
    assertEquals("1989-04-03", serviceHistory.data().get(0).attributes().startDate().toString());
    assertEquals("1993-04-02", serviceHistory.data().get(0).attributes().endDate().toString());
    assertEquals("E05", serviceHistory.data().get(0).attributes().payGrade());
    assertEquals(
        "honorable", serviceHistory.data().get(0).attributes().dischargeStatus().serializer());
    assertEquals(
        "COMPLETION OF REQUIRED ACTIVE SERVICE",
        serviceHistory.data().get(0).attributes().separationReason());
    assertEquals(0, serviceHistory.data().get(0).attributes().deployments().size());
    assertEquals("3c69adaf-819a-5834-9fbf-3327ce3990ef", serviceHistory.data().get(1).id());
    assertEquals("service-history-episodes", serviceHistory.data().get(1).type());
    assertEquals("Sam", serviceHistory.data().get(1).attributes().firstName());
    assertEquals("Gardner", serviceHistory.data().get(1).attributes().lastName());
    assertEquals("Navy Reserve", serviceHistory.data().get(1).attributes().branchOfService());
    assertEquals("1993-04-03", serviceHistory.data().get(1).attributes().startDate().toString());
    assertEquals("1997-04-30", serviceHistory.data().get(1).attributes().endDate().toString());
    assertEquals("E05", serviceHistory.data().get(1).attributes().payGrade());
    assertEquals(
        "honorable", serviceHistory.data().get(1).attributes().dischargeStatus().serializer());
    assertEquals(
        "COMPLETION OF REQUIRED ACTIVE SERVICE",
        serviceHistory.data().get(1).attributes().separationReason());
    assertEquals(0, serviceHistory.data().get(1).attributes().deployments().size());
    assertEquals("fcc93b2c-9aa8-5d7b-ab26-8e5577b660b8", serviceHistory.data().get(2).id());
    assertEquals("service-history-episodes", serviceHistory.data().get(2).type());
    assertEquals("Sam", serviceHistory.data().get(2).attributes().firstName());
    assertEquals("Gardner", serviceHistory.data().get(2).attributes().lastName());
    assertEquals(
        "Army National Guard", serviceHistory.data().get(2).attributes().branchOfService());
    assertEquals("2007-03-15", serviceHistory.data().get(2).attributes().startDate().toString());
    assertEquals(null, serviceHistory.data().get(2).attributes().endDate());
    assertEquals("E07", serviceHistory.data().get(2).attributes().payGrade());
    assertEquals(
        "unknown", serviceHistory.data().get(2).attributes().dischargeStatus().serializer());
    assertEquals("NOT APPLICABLE", serviceHistory.data().get(2).attributes().separationReason());
    assertEquals(
        "2017-12-05",
        serviceHistory.data().get(2).attributes().deployments().get(0).startDate().toString());
    assertEquals(
        "2018-08-28",
        serviceHistory.data().get(2).attributes().deployments().get(0).endDate().toString());
    assertEquals("AX1", serviceHistory.data().get(2).attributes().deployments().get(0).location());
    assertEquals(
        "2016-12-01",
        serviceHistory.data().get(2).attributes().deployments().get(1).startDate().toString());
    assertEquals(
        "2016-12-31",
        serviceHistory.data().get(2).attributes().deployments().get(1).endDate().toString());
    assertEquals("NN9", serviceHistory.data().get(2).attributes().deployments().get(1).location());
    assertEquals(
        "2011-12-01",
        serviceHistory.data().get(2).attributes().deployments().get(2).startDate().toString());
    assertEquals(
        "2011-12-31",
        serviceHistory.data().get(2).attributes().deployments().get(2).endDate().toString());
    assertEquals("AX1", serviceHistory.data().get(2).attributes().deployments().get(2).location());
    assertEquals(
        "2011-01-20",
        serviceHistory.data().get(2).attributes().deployments().get(3).startDate().toString());
    assertEquals(
        "2011-11-04",
        serviceHistory.data().get(2).attributes().deployments().get(3).endDate().toString());
    assertEquals("KWT", serviceHistory.data().get(2).attributes().deployments().get(3).location());
  }
}
