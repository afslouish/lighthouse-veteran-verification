package gov.va.api.lighthouse.veteranverification.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/** Attributes object model. */
@Data
@Builder
@Accessors(fluent = false)
@Schema(
    name = "VeteranVerificationResponse",
    description = "Generic response object for veteran verification calls",
    type = "object")
public class VeteranVerificationResponse {
  @Schema(type = "string", description = "Confirmation ICN", example = "1012667145V762142")
  String id;

  @Schema(type = "string", example = "veteran_status_confirmations")
  String type;

  @Schema(implementation = Attributes.class)
  Attributes attributes;
}
