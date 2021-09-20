package gov.va.api.lighthouse.veteranverification.service.controller.disabilityrating;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.lighthouse.veteranverification.api.v1.DisabilityRatingResponse;
import gov.va.api.lighthouse.veteranverification.api.v1.DisabilityRatingResponse.DisabilityRatingAttributes;
import gov.va.api.lighthouse.veteranverification.api.v1.DisabilityRatingResponse.DisabilityRatingData;
import gov.va.api.lighthouse.veteranverification.api.v1.DisabilityRatingResponse.IndividualRating;
import gov.va.vba.benefits.share.services.DisabilityRating;
import gov.va.vba.benefits.share.services.DisabilityRatingRecord;
import gov.va.vba.benefits.share.services.FindRatingDataResponse;
import gov.va.vba.benefits.share.services.RatingRecord;
import java.time.LocalDate;
import java.time.Month;
import org.junit.jupiter.api.Test;

public class DisabilityRatingTransformerTest {
  private DisabilityRatingResponse buildDisabilityRatingResponse(
      String icn,
      Integer combinedDisabilityRating,
      LocalDate combinedEffectiveDate,
      LocalDate legalEffectiveDate,
      String decision,
      LocalDate effectiveDate,
      Integer ratingPercentage) {
    return DisabilityRatingResponse.builder()
        .data(
            DisabilityRatingData.builder()
                .id(icn)
                .type("disability_ratings")
                .attributes(
                    DisabilityRatingAttributes.builder()
                        .combinedDisabilityRating(combinedDisabilityRating)
                        .combinedEffectiveDate(buildFormattedLocalDate(combinedEffectiveDate))
                        .legalEffectiveDate(legalEffectiveDate)
                        .individualRatings(
                            singletonList(
                                IndividualRating.builder()
                                    .decision(decision)
                                    .effectiveDate(buildFormattedLocalDate(effectiveDate))
                                    .ratingPercentage(ratingPercentage)
                                    .build()))
                        .build())
                .build())
        .build();
  }

  private FindRatingDataResponse buildFindRatingDataResponse(
      String serviceConnectedCombinedDegree,
      String legalEffectiveDate,
      String combinedDegreeEffectiveDate,
      String decision,
      String beginDate,
      String diagnosticPercent) {
    return FindRatingDataResponse.builder()
        ._return(
            RatingRecord.builder()
                .disabilityRatingRecord(
                    DisabilityRatingRecord.builder()
                        .serviceConnectedCombinedDegree(serviceConnectedCombinedDegree)
                        .legalEffectiveDate(legalEffectiveDate)
                        .combinedDegreeEffectiveDate(combinedDegreeEffectiveDate)
                        .ratings(
                            singletonList(
                                DisabilityRating.builder()
                                    .disabilityDecisionTypeName(decision)
                                    .beginDate(beginDate)
                                    .diagnosticPercent(diagnosticPercent)
                                    .build()))
                        .build())
                .build())
        .build();
  }

  private LocalDate buildFormattedLocalDate(LocalDate localDate) {
    if (localDate != null) {
      return LocalDate.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth());
    }
    return null;
  }

  @Test
  void nullableChecks() {
    assertThat(
            DisabilityRatingTransformer.builder()
                .response(buildFindRatingDataResponse(null, null, null, null, null, null))
                .build()
                .toDisabilityRating("1234"))
        .isEqualTo(buildDisabilityRatingResponse("1234", null, null, null, null, null, null));
  }

  @Test
  void nullableRatings() {
    assertThat(
            DisabilityRatingTransformer.builder()
                .response(
                    FindRatingDataResponse.builder()
                        ._return(
                            RatingRecord.builder()
                                .disabilityRatingRecord(
                                    DisabilityRatingRecord.builder().ratings(null).build())
                                .build())
                        .build())
                .build()
                .toDisabilityRating("1234")
                .getData()
                .getAttributes()
                .getIndividualRatings())
        .isNull();
  }

  @Test
  void toDisabilityRating() {
    FindRatingDataResponse response =
        buildFindRatingDataResponse(
            "40", "01012001", "01012001", "Service Connected", "01012001", "40");
    assertThat(
            DisabilityRatingTransformer.builder()
                .response(response)
                .build()
                .toDisabilityRating("1234"))
        .isEqualTo(
            buildDisabilityRatingResponse(
                "1234",
                40,
                buildFormattedLocalDate(LocalDate.of(2001, Month.JANUARY, 01)),
                buildFormattedLocalDate(LocalDate.of(2001, Month.JANUARY, 01)),
                "Service Connected",
                buildFormattedLocalDate(LocalDate.of(2001, Month.JANUARY, 01)),
                40));
  }
}
