package gov.va.api.lighthouse.veteranverification.service.controller.veteranstatus;

import gov.va.api.lighthouse.veteranverification.api.v1.VeteranStatusResponse;
import gov.va.api.lighthouse.veteranverification.api.v1.VeteranStatusResponse.VeteranStatusAttributes;
import gov.va.api.lighthouse.veteranverification.api.v1.VeteranStatusResponse.VeteranStatusDetails;
import gov.va.viers.cdi.emis.requestresponse.v1.EMISveteranStatusResponseType;
import lombok.Builder;
import lombok.NonNull;

/** Transformer for Veteran Status Verification response. */
@Builder
public class VeteranStatusTransformer {
  @NonNull private final EMISveteranStatusResponseType response;

  VeteranStatusResponse toVeteranStatus(String icn) {
    String status =
        response.getVeteranStatus() != null
                && response.getVeteranStatus().getTitle38StatusCode().equalsIgnoreCase("V1")
            ? "confirmed"
            : "not confirmed";
    return VeteranStatusResponse.builder()
        .data(
            VeteranStatusDetails.builder()
                .id(icn)
                .attributes(VeteranStatusAttributes.builder().veteranStatus(status).build())
                .build())
        .build();
  }
}
