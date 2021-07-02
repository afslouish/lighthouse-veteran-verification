package gov.va.api.lighthouse.veteranverification.tests;

import static gov.va.api.health.sentinel.EnvironmentAssumptions.assumeEnvironmentNotIn;
import static gov.va.api.lighthouse.veteranverification.tests.Requestor.veteranVerificationPostRequest;
import static gov.va.api.lighthouse.veteranverification.tests.SystemDefinitions.systemDefinition;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.va.api.health.sentinel.Environment;
import gov.va.api.health.sentinel.ExpectedResponse;
import gov.va.api.lighthouse.veteranverification.api.ApiError;
import gov.va.api.lighthouse.veteranverification.api.VeteranStatusConfirmation;
import gov.va.api.lighthouse.veteranverification.api.VeteranStatusRequest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class VeteranConfirmationStatusIT {

  @BeforeAll
  static void assumeEnvironment() {
    // Do not run these tests in non-mocked environments: production and staging.
    assumeEnvironmentNotIn(Environment.PROD, Environment.STAGING);
  }

  private String buildVeteranStatusRequest(
      String firstName,
      String middleName,
      String lastName,
      String ssn,
      String birthDate,
      String gender)
      throws JsonProcessingException {
    VeteranStatusRequest requestBody =
        VeteranStatusRequest.builder()
            .firstName(firstName)
            .middleName(middleName)
            .lastName(lastName)
            .ssn(ssn)
            .birthDate(birthDate)
            .gender(gender)
            .build();
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(requestBody);
  }

  @Test
  @SneakyThrows
  void veteranConfirmationEmisV5Status() {
    String requestBody =
        buildVeteranStatusRequest(
            systemDefinition().attributes().v5StatusFirstName(),
            null,
            systemDefinition().attributes().v5StatusLastName(),
            systemDefinition().attributes().v5StatusSsn(),
            systemDefinition().attributes().v5StatusBirthDate(),
            null);
    ExpectedResponse response = veteranVerificationPostRequest("v0/status", requestBody, 200);
    response.expectValid(VeteranStatusConfirmation.class);
    VeteranStatusConfirmation status =
        response.response().getBody().as(VeteranStatusConfirmation.class);
    assertThat(status.getVeteranStatus()).isEqualTo("not confirmed");
  }

  @Test
  @SneakyThrows
  void veteranConfirmationStatusFoundByEdipi() {
    String requestBody =
        buildVeteranStatusRequest(
            systemDefinition().attributes().edipiConfirmedFirstName(),
            null,
            systemDefinition().attributes().edipiConfirmedLastName(),
            systemDefinition().attributes().edipiConfirmedSsn(),
            systemDefinition().attributes().edipiConfirmedBirthDate(),
            null);
    ExpectedResponse response = veteranVerificationPostRequest("v0/status", requestBody, 200);
    response.expectValid(VeteranStatusConfirmation.class);
    VeteranStatusConfirmation status =
        response.response().getBody().as(VeteranStatusConfirmation.class);
    assertThat(status.getVeteranStatus()).isEqualTo("confirmed");
  }

  @Test
  @SneakyThrows
  void veteranConfirmationStatusFoundByIcn() {
    String requestBody =
        buildVeteranStatusRequest(
            systemDefinition().attributes().icnConfirmedFirstName(),
            null,
            systemDefinition().attributes().icnConfirmedFirstName(),
            systemDefinition().attributes().icnConfirmedSsn(),
            systemDefinition().attributes().icnConfirmedBirthDate(),
            null);
    ExpectedResponse response = veteranVerificationPostRequest("v0/status", requestBody, 200);
    response.expectValid(VeteranStatusConfirmation.class);
    VeteranStatusConfirmation status =
        response.response().getBody().as(VeteranStatusConfirmation.class);
    assertThat(status.getVeteranStatus()).isEqualTo("confirmed");
  }

  @Test
  @SneakyThrows
  void veteranConfirmationStatusNotFound() {
    String requestBody =
        buildVeteranStatusRequest("NOT", null, "FOUND", "123456789", "1900-01-01", null);
    ExpectedResponse response = veteranVerificationPostRequest("v0/status", requestBody, 200);
    response.expectValid(VeteranStatusConfirmation.class);
    VeteranStatusConfirmation status =
        response.response().getBody().as(VeteranStatusConfirmation.class);
    assertThat(status.getVeteranStatus()).isEqualTo("not confirmed");
  }

  @Test
  @SneakyThrows
  void veteranStatusMissingParameter() {
    ExpectedResponse response = veteranVerificationPostRequest("v0/status", "{}", 400);
    response.expectValid(ApiError.MissingParameterApiError.class);
    ApiError.MissingParameterApiError status =
        response.response().getBody().as(ApiError.MissingParameterApiError.class);
    assertThat(status.errors().get(0).getDetail())
        .isEqualTo("The required parameter \"ssn\", is missing");
  }

  @Test
  @SneakyThrows
  void veteranStatusNoEmisUser() {
    String requestBody =
        buildVeteranStatusRequest(
            systemDefinition().attributes().noEmisUserFirstName(),
            null,
            systemDefinition().attributes().noEmisUserLastName(),
            systemDefinition().attributes().noEmisUserSsn(),
            systemDefinition().attributes().noEmisUserBirthDate(),
            null);
    ExpectedResponse response = veteranVerificationPostRequest("v0/status", requestBody, 200);
    response.expectValid(VeteranStatusConfirmation.class);
    VeteranStatusConfirmation status =
        response.response().getBody().as(VeteranStatusConfirmation.class);
    assertThat(status.getVeteranStatus()).isEqualTo("not confirmed");
  }
}
