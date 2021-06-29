package gov.va.api.lighthouse.veteranverification.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;

/** Attributes object model. */
@Data
@Builder
@Accessors(fluent = false)
@Schema(
    name = "VeteranVerificationResponse",
    description = "Generic response wrapper for veteran verification calls")
public class VeteranVerificationResponse {
  @NonNull
  @Schema(implementation = VeteranVerificationData.class, required = true)
  VeteranVerificationData data;
}
