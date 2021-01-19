package gov.va.api.lighthouse.veteranverification.api;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Builder
@Accessors(fluent = false)
public class VeteranStatusConfirmation {
  @NonNull String status;
}
