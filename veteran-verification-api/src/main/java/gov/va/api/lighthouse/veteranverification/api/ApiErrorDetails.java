package gov.va.api.lighthouse.veteranverification.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

/** Schema for Api Error Details. */
@Value
@Builder
@Accessors(fluent = false)
public class ApiErrorDetails {
  @Schema(type = "string", example = "Error title")
  String title;

  @Schema(type = "string", example = "Detailed error message")
  String detail;

  @Schema(type = "string", example = "103")
  String code;

  @Schema(type = "string", example = "400")
  String status;
}
