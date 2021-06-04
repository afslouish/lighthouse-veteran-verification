package gov.va.api.lighthouse.veteranverification.service.controller;

import static gov.va.api.lighthouse.veteranverification.service.MpiLookupUtils.getInputEdipiOrIcn;

import com.sun.xml.ws.fault.ServerSOAPFaultException;
import gov.va.api.lighthouse.emis.EmisVeteranStatusServiceClient;
import gov.va.api.lighthouse.mpi.MasterPatientIndexClient;
import gov.va.api.lighthouse.veteranverification.api.VeteranStatusConfirmation;
import gov.va.api.lighthouse.veteranverification.api.VeteranStatusRequest;
import gov.va.viers.cdi.emis.requestresponse.v1.EMISveteranStatusResponseType;
import gov.va.viers.cdi.emis.requestresponse.v1.InputEdiPiOrIcn;
import javax.validation.Valid;
import org.hl7.v3.PRPAIN201306UV02;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Controller for Veteran Status Confirmation endpoint. */
@RestController
@RequestMapping(produces = {"application/json"})
public class VeteranStatusConfirmationController {
  private final MasterPatientIndexClient mpiClient;

  private final EmisVeteranStatusServiceClient emisClient;

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

    EMISveteranStatusResponseType status = null;

    try {
      status = emisClient.veteranStatusRequest(ediPiOrIcn);
    } catch (ServerSOAPFaultException exception) {
      return VeteranStatusConfirmation.builder().veteranStatus("not confirmed").build();
    }

    return VeteranStatusConfirmationTransformer.builder()
        .response(status)
        .build()
        .toVeteranStatus();
  }
}
