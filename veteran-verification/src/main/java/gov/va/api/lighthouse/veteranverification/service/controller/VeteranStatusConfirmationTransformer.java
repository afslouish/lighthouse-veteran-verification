package gov.va.api.lighthouse.veteranverification.service.controller;

import gov.va.api.lighthouse.veteranverification.api.VeteranStatusConfirmation;
import io.micrometer.core.instrument.util.StringUtils;
import java.util.Optional;
import lombok.Builder;
import lombok.NonNull;
import org.hl7.v3.PRPAIN201306UV02;

@Builder
public class VeteranStatusConfirmationTransformer {
  @NonNull private final PRPAIN201306UV02 response;

  VeteranStatusConfirmation toVeteranStatus() {
    String responseCode =
        Optional.ofNullable(response)
            .map(value -> value.getControlActProcess())
            .map(controlActProcess -> controlActProcess.getQueryAck())
            .map(queryAck -> queryAck.getQueryResponseCode())
            .map(code -> code.getCode())
            .orElse(null);
    if (responseCode != null && StringUtils.isNotBlank(responseCode)) {
      if (responseCode.equalsIgnoreCase("OK")) {
        return VeteranStatusConfirmation.builder().status("confirmed").build();
      }
    }
    return VeteranStatusConfirmation.builder().status("not confirmed").build();
  }
}
