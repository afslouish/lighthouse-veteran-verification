package gov.va.api.lighthouse.veteranverification.tests;

import static gov.va.api.health.sentinel.EnvironmentAssumptions.assumeEnvironmentIn;
import static gov.va.api.lighthouse.veteranverification.tests.Requestor.veteranVerificationGetRequest;
import static gov.va.api.lighthouse.veteranverification.tests.SystemDefinitions.systemDefinition;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import gov.va.api.health.sentinel.Environment;
import gov.va.api.health.sentinel.ExpectedResponse;
import gov.va.api.lighthouse.veteranverification.api.ApiError;
import gov.va.api.lighthouse.veteranverification.api.v1.DisabilityRatingResponse;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DisabilityRatingIT {
  @BeforeAll
  static void assumeEnvironment() {
    // Tests are only ran in environments that are not blocked by the kong gateway (ie Localhost).
    assumeEnvironmentIn(Environment.LOCAL);
  }

  @Test
  void disabilityRatingHappyJwtPath() {
    String request =
        String.format("v1/disability_rating/%s", systemDefinition().icns().disabilityRatingIcn());
    ExpectedResponse response = veteranVerificationGetRequest(request, "application/jwt", 200);
    String disabilityRating = response.response().asString();
    assertNotNull(disabilityRating);
  }

  @Test
  void disabilityRatingRecordFound() {
    String request =
        String.format("v1/disability_rating/%s", systemDefinition().icns().disabilityRatingIcn());
    ExpectedResponse response = veteranVerificationGetRequest(request, "application/json", 200);
    response.expectValid(DisabilityRatingResponse.class);
    DisabilityRatingResponse disabilityRatingResponse =
        response.response().getBody().as(DisabilityRatingResponse.class);
    assertThat(disabilityRatingResponse.getData().getId()).isEqualTo("1012829620V654328");
    assertThat(disabilityRatingResponse.getData().getType()).isEqualTo("disability_ratings");
    assertThat(disabilityRatingResponse.getData().getAttributes().getCombinedDisabilityRating())
        .isEqualTo(40);
    assertThat(
            disabilityRatingResponse
                .getData()
                .getAttributes()
                .getCombinedEffectiveDate()
                .toString())
        .isEqualTo("2010-08-01");
    assertThat(disabilityRatingResponse.getData().getAttributes().getCombinedEffectiveDate())
        .isInstanceOf(LocalDate.class);
    assertThat(
            disabilityRatingResponse.getData().getAttributes().getLegalEffectiveDate().toString())
        .isEqualTo("2010-07-11");
    assertThat(disabilityRatingResponse.getData().getAttributes().getLegalEffectiveDate())
        .isInstanceOf(LocalDate.class);
    assertThat(
            disabilityRatingResponse
                .getData()
                .getAttributes()
                .getIndividualRatings()
                .get(0)
                .getDecision())
        .isEqualTo("Service Connected");
    assertThat(
            disabilityRatingResponse
                .getData()
                .getAttributes()
                .getIndividualRatings()
                .get(0)
                .getEffectiveDate())
        .isInstanceOf(LocalDate.class);
    assertThat(
            disabilityRatingResponse
                .getData()
                .getAttributes()
                .getIndividualRatings()
                .get(0)
                .getEffectiveDate()
                .toString())
        .isEqualTo("2010-07-11");
    assertThat(
            disabilityRatingResponse
                .getData()
                .getAttributes()
                .getIndividualRatings()
                .get(0)
                .getRatingPercentage())
        .isEqualTo(30);
    assertThat(
            disabilityRatingResponse
                .getData()
                .getAttributes()
                .getIndividualRatings()
                .get(1)
                .getDecision())
        .isEqualTo("Service Connected");
    assertThat(
            disabilityRatingResponse
                .getData()
                .getAttributes()
                .getIndividualRatings()
                .get(1)
                .getEffectiveDate())
        .isInstanceOf(LocalDate.class);
    assertThat(
            disabilityRatingResponse
                .getData()
                .getAttributes()
                .getIndividualRatings()
                .get(1)
                .getEffectiveDate()
                .toString())
        .isEqualTo("2010-07-11");
    assertThat(
            disabilityRatingResponse
                .getData()
                .getAttributes()
                .getIndividualRatings()
                .get(1)
                .getRatingPercentage())
        .isEqualTo(20);
  }

  @Test
  void noBgsUser() {
    String request =
        String.format(
            "v1/disability_rating/%s", systemDefinition().icns().noBgsUserDisabilityRatingIcn());
    ExpectedResponse response = veteranVerificationGetRequest(request, "application/json", 500);
    response.expectValid(ApiError.ServerSoapFaultApiError.class);
  }
}
