package gov.va.api.lighthouse.veteranverification.service.controller.disabilityrating;

import static gov.va.api.lighthouse.veteranverification.service.controller.Transformers.formatDateString;

import gov.va.api.lighthouse.veteranverification.api.v1.DisabilityRatingResponse;
import gov.va.api.lighthouse.veteranverification.api.v1.DisabilityRatingResponse.IndividualRating;
import gov.va.vba.benefits.share.services.DisabilityRatingRecord;
import gov.va.vba.benefits.share.services.FindRatingDataResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import lombok.NonNull;

/** Transformer for Disability Rating response. */
@Builder
public class DisabilityRatingTransformer {
  @NonNull private final FindRatingDataResponse response;

  DisabilityRatingRecord getDisabilityRatingRecord() {
    return Optional.ofNullable(response)
        .map(value -> value.getReturn())
        .map(returnValue -> returnValue.getDisabilityRatingRecord())
        .orElse(null);
  }

  List<IndividualRating> individualRatings() {
    List<gov.va.vba.benefits.share.services.DisabilityRating> disabilityRatings =
        Optional.ofNullable(response)
            .map(value -> value.getReturn())
            .map(returnValue -> returnValue.getDisabilityRatingRecord())
            .map(disabilityRatingRecord -> disabilityRatingRecord.getRatings())
            .orElse(null);
    if (disabilityRatings == null || disabilityRatings.isEmpty()) {
      return null;
    }
    List<IndividualRating> individualRatings = new ArrayList<>();
    for (gov.va.vba.benefits.share.services.DisabilityRating disabilityRating : disabilityRatings) {
      individualRatings.add(
          IndividualRating.builder()
              .decision(disabilityRating.getDisabilityDecisionTypeName())
              .effectiveDate(formatDateString(disabilityRating.getBeginDate()))
              .ratingPercentage(parseInteger(disabilityRating.getDiagnosticPercent()))
              .build());
    }
    return individualRatings;
  }

  private Integer parseInteger(String integer) {
    return (integer == null) ? null : Integer.parseInt(integer);
  }

  private Integer serviceConnectedCombinedDegree() {
    if (getDisabilityRatingRecord() == null
        || getDisabilityRatingRecord().getServiceConnectedCombinedDegree() == null) {
      return null;
    } else {
      return parseInteger(getDisabilityRatingRecord().getServiceConnectedCombinedDegree());
    }
  }

  DisabilityRatingResponse toDisabilityRating(String icn) {
    return DisabilityRatingResponse.builder()
        .data(
            DisabilityRatingResponse.DisabilityRatingData.builder()
                .id(icn)
                .attributes(
                    DisabilityRatingResponse.DisabilityRatingAttributes.builder()
                        .combinedDisabilityRating(serviceConnectedCombinedDegree())
                        .combinedEffectiveDate(
                            formatDateString(
                                Optional.ofNullable(getDisabilityRatingRecord())
                                    .map(DisabilityRatingRecord::getCombinedDegreeEffectiveDate)
                                    .orElse(null)))
                        .legalEffectiveDate(
                            formatDateString(
                                Optional.of(getDisabilityRatingRecord())
                                    .map(DisabilityRatingRecord::getLegalEffectiveDate)
                                    .orElse(null)))
                        .individualRatings(individualRatings())
                        .build())
                .build())
        .build();
  }
}
