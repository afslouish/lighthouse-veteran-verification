package gov.va.api.lighthouse.veteranverification.service.controller.veteranverification;

import gov.va.api.lighthouse.veteranverification.api.VeteranStatusVerification;
import gov.va.api.lighthouse.veteranverification.api.VeteranStatusVerification.VeteranStatusAttributes;
import gov.va.api.lighthouse.veteranverification.api.VeteranStatusVerification.VeteranStatusVerificationDetails;
import gov.va.viers.cdi.emis.requestresponse.v1.EMISveteranStatusResponseType;
import lombok.Builder;
import lombok.NonNull;

/** Transformer for Veteran Status Verification response. */
@Builder
public class VeteranStatusVerificationTransformer {
  @NonNull private final EMISveteranStatusResponseType response;

  VeteranStatusVerification toVeteranStatus(String icn) {
    String status =
        response.getVeteranStatus() != null
                && response.getVeteranStatus().getTitle38StatusCode().equalsIgnoreCase("V1")
            ? "confirmed"
            : "not confirmed";
    return VeteranStatusVerification.builder()
        .data(
            VeteranStatusVerificationDetails.builder()
                .id(icn)
                .attributes(VeteranStatusAttributes.builder().veteranStatus(status).build())
                .build())
        .build();
  }
}
