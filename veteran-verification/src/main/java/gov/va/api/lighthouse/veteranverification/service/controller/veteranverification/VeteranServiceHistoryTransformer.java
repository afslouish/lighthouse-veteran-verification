package gov.va.api.lighthouse.veteranverification.service.controller.veteranverification;

import gov.va.api.lighthouse.veteranverification.api.v0.ServiceHistoryResponse;
import gov.va.api.lighthouse.veteranverification.service.utils.ServiceHistoryResponseBuilder;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISdeploymentResponseType;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISserviceEpisodeResponseType;
import lombok.Builder;
import lombok.NonNull;
import org.hl7.v3.PRPAIN201306UV02;

@Builder
public class VeteranServiceHistoryTransformer {
  public ServiceHistoryResponse serviceHistoryTransformer(
      @NonNull String uuid,
      @NonNull EMISdeploymentResponseType deploymentResponse,
      @NonNull EMISserviceEpisodeResponseType serviceEpisodeResponseType,
      @NonNull PRPAIN201306UV02 mpiResponse) {
    return ServiceHistoryResponseBuilder.buildServiceHistoryResponse(
        uuid, deploymentResponse, serviceEpisodeResponseType, mpiResponse);
  }
}
