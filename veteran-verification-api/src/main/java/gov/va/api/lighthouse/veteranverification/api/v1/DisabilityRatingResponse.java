package gov.va.api.lighthouse.veteranverification.api.v1;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.springframework.lang.Nullable;

/** Disability Rating object model. */
@Data
@Builder
@Accessors(fluent = false)
@AllArgsConstructor
@NoArgsConstructor
@Schema(requiredProperties = "data")
public class DisabilityRatingResponse {

  @NonNull DisabilityRatingData data;

  /** Data object model. */
  @Data
  @Builder
  @Accessors(fluent = false)
  @AllArgsConstructor
  @NoArgsConstructor
  public static class DisabilityRatingData {
    @NonNull @Builder.Default String id = "0";

    @NonNull @Builder.Default String type = "disability_ratings";

    @NonNull DisabilityRatingAttributes attributes;
  }

  /** Attributes object model. */
  @Data
  @Builder
  @Accessors(fluent = false)
  @AllArgsConstructor
  @NoArgsConstructor
  public static class DisabilityRatingAttributes {
    @Nullable
    @JsonProperty("combined_disability_rating")
    Integer combinedDisabilityRating;

    @Nullable
    @JsonProperty("combined_effective_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    LocalDate combinedEffectiveDate;

    @Nullable
    @JsonProperty("legal_effective_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    LocalDate legalEffectiveDate;

    @Nullable
    @JsonProperty("individual_ratings")
    List<IndividualRating> individualRatings;
  }

  /** Individual ratings object model. */
  @Data
  @Builder
  @Accessors(fluent = false)
  @AllArgsConstructor
  @NoArgsConstructor
  public static class IndividualRating {
    @Nullable String decision;

    @Nullable
    @JsonProperty("effective_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    LocalDate effectiveDate;

    @Nullable
    @JsonProperty("rating_percentage")
    Integer ratingPercentage;
  }
}
