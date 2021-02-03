package gov.va.api.lighthouse.veteranverification.service.controller;

import static gov.va.api.lighthouse.veteranverification.service.MpiLookupUtils.getInputEdipiOrIcn;

import gov.va.api.lighthouse.emis.EmisVeteranStatusServiceClient;
import gov.va.api.lighthouse.mpi.MasterPatientIndexClient;
import gov.va.api.lighthouse.mpi.Mpi1305RequestAttributes;
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
  @Getter private final MasterPatientIndexClient mpiClient;

  @Getter private final EmisVeteranStatusServiceClient emisClient;

  @Setter private Function<Mpi1305RequestAttributes, PRPAIN201306UV02> mpi1305Request;

  @Setter private Function<InputEdiPiOrIcn, EMISveteranStatusResponseType> emisVeteranStatusRequest;

  /** Controller constructor. */
  public VeteranStatusConfirmationController(
      @Autowired MasterPatientIndexClient mpiClient,
      @Autowired EmisVeteranStatusServiceClient emisClient) {
    this.mpiClient = mpiClient;
    this.emisClient = emisClient;
  }

  /** Get response from MPI 1305 request. */
  @PostMapping({"/v0/status"})
  public VeteranStatusConfirmation veteranStatusConfirmationResponse(
      @Valid @RequestBody VeteranStatusRequest attributes) {
    PRPAIN201306UV02 mpiResponse =
        mpiClient.request1305ByAttributes(attributes.toMpi1305RequestAttributes());
    InputEdiPiOrIcn ediPiOrIcn = getInputEdipiOrIcn(mpiResponse);
    if (ediPiOrIcn == null) {
      return VeteranStatusConfirmation.builder().veteranStatus("not confirmed").build();
    }
    EMISveteranStatusResponseType emisResponse = emisClient.veteranStatusRequest(ediPiOrIcn);
    return VeteranStatusConfirmationTransformer.builder()
        .response(emisResponse)
        .build()
        .toVeteranStatus();
  }
}
