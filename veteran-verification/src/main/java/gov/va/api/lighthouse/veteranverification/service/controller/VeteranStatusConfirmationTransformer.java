package gov.va.api.lighthouse.veteranverification.service.controller;

import gov.va.api.lighthouse.veteranverification.api.VeteranStatusConfirmation;
import gov.va.viers.cdi.emis.requestresponse.v1.EMISveteranStatusResponseType;
import lombok.Builder;
import lombok.NonNull;

@Builder
public class VeteranStatusConfirmationTransformer {
  @NonNull private final EMISveteranStatusResponseType response;

  VeteranStatusConfirmation toVeteranStatus() {
    String status =
        response.getVeteranStatus() != null
                && response.getVeteranStatus().getTitle38StatusCode().equalsIgnoreCase("V1")
            ? "confirmed"
            : "not confirmed";
    return VeteranStatusConfirmation.builder().veteranStatus(status).build();
  }
}
