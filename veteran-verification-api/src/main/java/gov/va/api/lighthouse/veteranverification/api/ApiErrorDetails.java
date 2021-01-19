package gov.va.api.lighthouse.veteranverification.api;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Builder
@Accessors(fluent = false)
public class ApiErrorDetails {
  String title;
  String detail;
  String code;
  String status;
}
