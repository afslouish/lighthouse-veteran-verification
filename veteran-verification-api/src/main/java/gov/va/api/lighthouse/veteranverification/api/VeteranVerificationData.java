package gov.va.api.lighthouse.veteranverification.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(fluent = false)
@Schema(
    name = "VeteranVerificationData",
    description = "Generic response object for veteran verification calls")
public class VeteranVerificationData {
  @NonNull
  @Schema(
      type = "string",
      description = "Confirmation ICN",
      example = "1012667145V762142",
      required = true)
  String id;

  @NonNull
  @Schema(type = "string", example = "veteran_status_confirmations", required = true)
  String type;

  @NonNull
  @Schema(implementation = Attributes.class, required = true)
  Attributes attributes;
}
