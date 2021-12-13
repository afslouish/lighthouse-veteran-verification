package gov.va.api.lighthouse.veteranverification.tests;

import static gov.va.api.health.sentinel.EnvironmentAssumptions.assumeEnvironmentIn;
import static gov.va.api.lighthouse.veteranverification.tests.Requestor.veteranVerificationGetRequest;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import gov.va.api.health.sentinel.Environment;
import gov.va.api.health.sentinel.ExpectedResponse;
import gov.va.api.lighthouse.veteranverification.api.v1.VeteranStatusResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@Slf4j
public class VeteranStatusIT {
  public static final String CONFIRMED_STATUS_ICN_DEFAULT = "1012667145V762142";

  public static final String V5_STATUS_ICN_DEFAULT = "1012666182V203559";

  public static final String NO_EMIS_USER_STATUS_ICN_DEFAULT = "1012829932V238054";

  @BeforeAll
  static void assumeEnvironment() {
    // Tests are only ran in environments that are not blocked by the kong gateway (ie Localhost).
    assumeEnvironmentIn(Environment.LOCAL);
  }

  @Test
  void veteranVerificationEmisV5Status() {
    String request = String.format("v1/status/%s", V5_STATUS_ICN_DEFAULT);
    ExpectedResponse response = veteranVerificationGetRequest(request, "application/json", 200);
    response.expectValid(VeteranStatusResponse.class);
    VeteranStatusResponse status = response.response().getBody().as(VeteranStatusResponse.class);
    assertThat(status.getData().getAttributes().getVeteranStatus()).isEqualTo("not confirmed");
  }

  @Test
  void veteranVerificationStatusFound() {
    String request = String.format("v1/status/%s", CONFIRMED_STATUS_ICN_DEFAULT);
    ExpectedResponse response = veteranVerificationGetRequest(request, "application/json", 200);
    response.expectValid(VeteranStatusResponse.class);
    VeteranStatusResponse status = response.response().getBody().as(VeteranStatusResponse.class);
    assertThat(status.getData().getAttributes().getVeteranStatus()).isEqualTo("confirmed");
  }

  @Test
  void veteranVerificationStatusNoEmisUser() {
    String request = String.format("v1/status/%s", NO_EMIS_USER_STATUS_ICN_DEFAULT);
    ExpectedResponse response = veteranVerificationGetRequest(request, "application/json", 200);
    response.expectValid(VeteranStatusResponse.class);
    VeteranStatusResponse status = response.response().getBody().as(VeteranStatusResponse.class);
    assertThat(status.getData().getAttributes().getVeteranStatus()).isEqualTo("not confirmed");
  }

  @Test
  void veteranVerificationStatusNotFound() {
    String request = String.format("v1/status/%s", "not_found");
    ExpectedResponse response = veteranVerificationGetRequest(request, "application/json", 200);
    response.expectValid(VeteranStatusResponse.class);
    VeteranStatusResponse status = response.response().getBody().as(VeteranStatusResponse.class);
    assertThat(status.getData().getAttributes().getVeteranStatus()).isEqualTo("not confirmed");
  }
}
