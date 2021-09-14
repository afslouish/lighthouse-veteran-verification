package gov.va.api.lighthouse.veteranverification.tests;

import static gov.va.api.health.sentinel.EnvironmentAssumptions.assumeEnvironmentNotIn;
import static gov.va.api.lighthouse.veteranverification.tests.Requestor.veteranVerificationGetRequest;
import static gov.va.api.lighthouse.veteranverification.tests.SystemDefinitions.systemDefinition;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
    assertEquals(
        "eyJraWQiOiJmYWtlIiwidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYifQ.eyJkYXRhIjpbeyJpZCI6ImUyOGEyMzU5LT"
            + "Q4YTUtNTVjZS04OTBjLTc2YjUxYzc0OWI2YiIsInR5cGUiOiJzZXJ2aWNlLWhpc3RvcnktZXBpc29kZXMiLCJhdHRy"
            + "aWJ1dGVzIjp7ImZpcnN0X25hbWUiOiJXZXNsZXkiLCJsYXN0X25hbWUiOiJGb3JkIiwiYnJhbmNoX29mX3NlcnZpY2"
            + "UiOiJBaXIgRm9yY2UiLCJzdGFydF9kYXRlIjoiMjAwNS0wNC0xMiIsImVuZF9kYXRlIjoiMjAwOS0wNC0xMSIsInBh"
            + "eV9ncmFkZSI6IkUwNCIsImRpc2NoYXJnZV9zdGF0dXMiOiJob25vcmFibGUiLCJzZXBhcmF0aW9uX3JlYXNvbiI6Ik"
            + "NPTVBMRVRJT04gT0YgUkVRVUlSRUQgQUNUSVZFIFNFUlZJQ0UiLCJkZXBsb3ltZW50cyI6W3sic3RhcnRfZGF0ZSI6"
            + "IjIwMDgtMDctMjUiLCJlbmRfZGF0ZSI6IjIwMDktMDEtMjIiLCJsb2NhdGlvbiI6IlFBVCJ9LHsic3RhcnRfZGF0ZS"
            + "I6IjIwMDgtMDUtMDEiLCJlbmRfZGF0ZSI6IjIwMDgtMDUtMzEiLCJsb2NhdGlvbiI6IkFYMSJ9XX19LHsiaWQiOiJi"
            + "NjI2NzQ3NC0wOGEzLTU2ZmMtOTMyZi01ODJjZWUwYmYwMmMiLCJ0eXBlIjoic2VydmljZS1oaXN0b3J5LWVwaXNvZG"
            + "VzIiwiYXR0cmlidXRlcyI6eyJmaXJzdF9uYW1lIjoiV2VzbGV5IiwibGFzdF9uYW1lIjoiRm9yZCIsImJyYW5jaF9v"
            + "Zl9zZXJ2aWNlIjoiVW5rbm93biIsInN0YXJ0X2RhdGUiOiIyMDA4LTA1LTAxIiwiZW5kX2RhdGUiOiIyMDA4LTA1LT"
            + "MxIiwicGF5X2dyYWRlIjpudWxsLCJkaXNjaGFyZ2Vfc3RhdHVzIjoiaG9ub3JhYmxlLWFic2VuY2Utb2YtbmVnYXRp"
            + "dmUtcmVwb3J0Iiwic2VwYXJhdGlvbl9yZWFzb24iOiJVTktOT1dOIiwiZGVwbG95bWVudHMiOltdfX0seyJpZCI6Im"
            + "YzZDMzNTg3LTUxNWEtNWMyYS05MjJhLTBjMzE2YWNkY2I0MCIsInR5cGUiOiJzZXJ2aWNlLWhpc3RvcnktZXBpc29k"
            + "ZXMiLCJhdHRyaWJ1dGVzIjp7ImZpcnN0X25hbWUiOiJXZXNsZXkiLCJsYXN0X25hbWUiOiJGb3JkIiwiYnJhbmNoX2"
            + "9mX3NlcnZpY2UiOiJVbmtub3duIiwic3RhcnRfZGF0ZSI6IjIwMDgtMDctMjUiLCJlbmRfZGF0ZSI6IjIwMDktMDEt"
            + "MjIiLCJwYXlfZ3JhZGUiOm51bGwsImRpc2NoYXJnZV9zdGF0dXMiOiJob25vcmFibGUtYWJzZW5jZS1vZi1uZWdhdG"
            + "l2ZS1yZXBvcnQiLCJzZXBhcmF0aW9uX3JlYXNvbiI6IlVOS05PV04iLCJkZXBsb3ltZW50cyI6W119fV19.HaJ9JGF"
            + "dOHBdv2FOstBDJWvG3lY0L_OQlfJaA8-GvBc9ipYZqPbIyLbwPHO3HHiv3TsmhiWAJqFckxNgVCFgOB6d2eKtYnFhD"
            + "QuyVN0LG4pHsNnbFD_KJB9t1f5jTsLJD8br_u1BEAYIIVvw_JP2kDtOlRoulLd2-mAnAaK24r79KzMUukOroMEJkk2"
            + "qfQhQje2Muw0A3Fl3Jd4iL5jGUqNdnhIQXFuWIRvCsQYXKz7T4ulnD2AkRcpSiuGip9nJp_70b3CGSBRxSojceayPU"
            + "ZRJ4QGdY9iG-upTRko-yCyVxL9uxOGTwA-gtxOnC5CIz0sTMlkJIRiEXPHYEowQCQ",
        serviceHistory);
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
        response.response().getBody().as(ServiceHistoryResponse.class);
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
}
