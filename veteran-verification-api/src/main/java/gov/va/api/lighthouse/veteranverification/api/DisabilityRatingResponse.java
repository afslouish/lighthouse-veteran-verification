package gov.va.api.lighthouse.veteranverification.api;

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
  @Schema(
      description = "The disability rating percentage applied to the Veteran.\n",
      requiredProperties = {"id", "type", "attributes"})
  public static class DisabilityRatingData {
    @Schema(
        description = "JSON API identifier",
        nullable = false,
        type = "string",
        example = "12303")
    @NonNull
    @Builder.Default
    String id = "0";

    @Schema(
        description = "JSON API type specification",
        nullable = false,
        type = "string",
        example = "disability_ratings")
    @NonNull
    @Builder.Default
    String type = "disability_ratings";

    @Schema(
        description = "Body of the disability rating response\n",
        implementation = DisabilityRatingAttributes.class)
    @NonNull
    DisabilityRatingAttributes attributes;
  }

  /** Attributes object model. */
  @Data
  @Builder
  @Accessors(fluent = false)
  @AllArgsConstructor
  @NoArgsConstructor
  public static class DisabilityRatingAttributes {
    @Schema(
        description =
            "Overall severity rating determined by VA calculation of each service related "
                + "conditions. Used to help determine disability compensation (pay) and "
                + "related benefits.",
        required = true,
        nullable = true,
        type = "integer",
        example = "100")
    @Nullable
    @JsonProperty("combined_disability_rating")
    Integer combinedDisabilityRating;

    @Schema(
        description = "The effective date of the latest combined disability rating for the Veteran",
        required = true,
        nullable = true,
        type = "localdate",
        example = "2018-03-07")
    @Nullable
    @JsonProperty("combined_effective_date")
    LocalDate combinedEffectiveDate;

    @Schema(
        description =
            "When the Veteran could begin claiming benefits for combined disability rating",
        required = true,
        nullable = true,
        type = "localdate",
        example = "2018-03-07")
    @Nullable
    @JsonProperty("legal_effective_date")
    LocalDate legalEffectiveDate;

    @Schema(
        description = "list of individual service connected disabilty ratings",
        required = true,
        type = "array")
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
    @Schema(
        description = "Whether the disability is service connected or not.",
        nullable = true,
        required = true,
        example = "Service Connected")
    @Nullable
    String decision;

    @Schema(
        description = "When the Veteran could begin claiming benefits related to this disability",
        nullable = true,
        required = true,
        example = "2018-03-07")
    @Nullable
    @JsonProperty("effective_date")
    LocalDate effectiveDate;

    @Schema(
        description =
            "Severity rating determined by VA that indicates how disabling an "
                + "illness or injury is for the Veteran. Used to help determine disability "
                + "compensation (pay) and related benefits.",
        nullable = true,
        required = true,
        example = "50")
    @Nullable
    @JsonProperty("rating_percentage")
    Integer ratingPercentage;
  }
}
