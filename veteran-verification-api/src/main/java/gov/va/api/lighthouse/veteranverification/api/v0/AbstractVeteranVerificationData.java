package gov.va.api.lighthouse.veteranverification.api.v0;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
@Schema(
    name = "VeteranVerificationData",
    description = "Generic response object for veteran verification calls")
@FieldDefaults(level = AccessLevel.PUBLIC)
public class AbstractVeteranVerificationData {
  @NonNull
  @Schema(
      type = "string",
      description = "Confirmation ICN",
      example = "1012667145V762142",
      required = true)
  String id;

  @NonNull
  @Schema(example = "veteran_status_confirmations", required = true)
  String type;
}
