package gov.va.api.lighthouse.veteranverification.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Accessors;

/** Veteran Status Verification object model. */
@Data
@Builder
@Accessors(fluent = false)
public class VeteranStatusVerification {

  VeteranStatusVerificationDetails data;

  /** Attributes object model. */
  @Value
  @Builder
  @Accessors(fluent = false)
  public static class Attributes {
    @NonNull
    @JsonProperty("veteran_status")
    String veteranStatus;
  }

  /** Attributes object model. */
  @Value
  @Builder
  @Accessors(fluent = false)
  public static class VeteranStatusVerificationDetails {
    String id;

    @Builder.Default String type = "veteran_status_confirmations";

    Attributes attributes;
  }
}
