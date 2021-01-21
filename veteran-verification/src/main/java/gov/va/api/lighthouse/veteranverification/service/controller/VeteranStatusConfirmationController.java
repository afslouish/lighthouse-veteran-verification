package gov.va.api.lighthouse.veteranverification.service.controller;

import static gov.va.api.lighthouse.veteranverification.service.MpiLookupUtils.getInputEdipiOrIcn;

import gov.va.api.lighthouse.emis.EmisConfigV1;
import gov.va.api.lighthouse.emis.SoapEmisVeteranStatusServiceClient;
import gov.va.api.lighthouse.mpi.Mpi1305RequestAttributes;
import gov.va.api.lighthouse.mpi.MpiConfig;
import gov.va.api.lighthouse.mpi.SoapMasterPatientIndexClient;
import gov.va.api.lighthouse.veteranverification.api.VeteranStatusConfirmation;
import gov.va.api.lighthouse.veteranverification.api.VeteranStatusRequest;
import gov.va.viers.cdi.emis.requestresponse.v1.EMISveteranStatusResponseType;
import gov.va.viers.cdi.emis.requestresponse.v1.InputEdiPiOrIcn;
import java.util.function.Function;
import javax.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hl7.v3.PRPAIN201306UV02;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = {"application/json"})
@Slf4j
public class VeteranStatusConfirmationController {
  @Getter private final MpiConfig mpiConfig;

  @Getter private final EmisConfigV1 emisConfig;

  @Setter private Function<Mpi1305RequestAttributes, PRPAIN201306UV02> mpi1305Request;

  @Setter private Function<InputEdiPiOrIcn, EMISveteranStatusResponseType> emisVeteranStatusRequest;

  /** Controller constructor. */
  public VeteranStatusConfirmationController(
      @Autowired MpiConfig mpiConfig, @Autowired EmisConfigV1 emisConfig) {
    this.mpiConfig = mpiConfig;
    this.emisConfig = emisConfig;
    log.info("Accessing MPI at {}", mpiConfig.getUrl());
  }

  /** Lazy Getter. */
  public Function<InputEdiPiOrIcn, EMISveteranStatusResponseType> emisVeteranStatusRequest() {
    if (emisVeteranStatusRequest == null) {
      return (inputEdiPiOrIcn) ->
          SoapEmisVeteranStatusServiceClient.of(emisConfig).veteranStatusRequest(inputEdiPiOrIcn);
    }
    return emisVeteranStatusRequest;
  }

  /** Lazy Getter. */
  public Function<Mpi1305RequestAttributes, PRPAIN201306UV02> mpi1305Request() {
    if (mpi1305Request == null) {
      return (attributes) ->
          SoapMasterPatientIndexClient.of(mpiConfig).request1305ByAttributes(attributes);
    }
    return mpi1305Request;
  }

  /** Get response from MPI 1305 request. */
  @PostMapping({"/v0/status"})
  public VeteranStatusConfirmation veteranStatusConfirmationResponse(
      @Valid @RequestBody VeteranStatusRequest attributes) {
    PRPAIN201306UV02 mpiResponse = mpi1305Request().apply(attributes.toMpi1305RequestAttributes());
    if (getInputEdipiOrIcn(mpiResponse) == null) {
      return VeteranStatusConfirmation.builder().veteranStatus("not confirmed").build();
    }
    EMISveteranStatusResponseType emisResponse =
        emisVeteranStatusRequest().apply(getInputEdipiOrIcn(mpiResponse));
    return VeteranStatusConfirmationTransformer.builder()
        .response(emisResponse)
        .build()
        .toVeteranStatus();
  }
}
