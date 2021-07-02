package gov.va.api.lighthouse.veteranverification.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

/** Veteran Status Confirmation response object model. */
@Data
@Builder
@Accessors(fluent = false)
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Veteran status confirmation for an individual", type = "object")
public class VeteranStatusConfirmation {
  @Schema(
      type = "string",
      description =
          "Whether the system could confirm the Veteran status of an individual based on traits\n",
      allowableValues = {"confirmed", "not confirmed"})
  @NonNull
  @JsonProperty("veteran_status")
  String veteranStatus;
}
