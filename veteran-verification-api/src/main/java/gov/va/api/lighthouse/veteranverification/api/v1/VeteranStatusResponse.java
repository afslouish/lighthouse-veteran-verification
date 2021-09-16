package gov.va.api.lighthouse.veteranverification.api.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

/** Veteran Status Verification object model. */
@Data
@Builder
@Accessors(fluent = false)
@AllArgsConstructor
@NoArgsConstructor
@Schema(requiredProperties = "data")
public class VeteranStatusResponse {

  @NonNull VeteranStatusDetails data;

  /** Attributes object model. */
  @Data
  @Builder
  @Accessors(fluent = false)
  @AllArgsConstructor
  @NoArgsConstructor
  public static class VeteranStatusAttributes {
    @NonNull
    @JsonProperty("veteran_status")
    @Schema(
        type = "string",
        description =
            "Whether the system could confirm the Veteran status of the authorized individual\n",
        allowableValues = {"confirmed", "not confirmed"})
    String veteranStatus;
  }

  /** Attributes object model. */
  @Data
  @Builder
  @Accessors(fluent = false)
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(
      name = "VeteranStatusConfirmation",
      description = "Veteran status confirmation for an individual",
      type = "object")
  public static class VeteranStatusDetails {
    @NonNull
    @Schema(type = "string", description = "Confirmation ICN", example = "1012667145V762142")
    String id;

    @NonNull
    @Schema(type = "string", example = "veteran_status_confirmations")
    @Builder.Default
    String type = "veteran_status_confirmations";

    VeteranStatusAttributes attributes;
  }
}
