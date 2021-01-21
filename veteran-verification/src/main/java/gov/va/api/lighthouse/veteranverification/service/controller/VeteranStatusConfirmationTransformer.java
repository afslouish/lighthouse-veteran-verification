package gov.va.api.lighthouse.veteranverification.service.controller;

import static gov.va.api.lighthouse.veteranverification.service.controller.Transformers.allBlank;

import gov.va.api.lighthouse.veteranverification.api.VeteranStatusConfirmation;
import gov.va.viers.cdi.emis.requestresponse.v1.EMISveteranStatusResponseType;
import lombok.Builder;
import lombok.NonNull;

@Builder
public class VeteranStatusConfirmationTransformer {
  @NonNull private final EMISveteranStatusResponseType response;

  VeteranStatusConfirmation toVeteranStatus() {
    if (response.getVeteranStatus() == null || allBlank(response.getVeteranStatus())) {
      return VeteranStatusConfirmation.builder().veteranStatus("not confirmed").build();
    } else {
      return VeteranStatusConfirmation.builder().veteranStatus("confirmed").build();
    }
  }
}
