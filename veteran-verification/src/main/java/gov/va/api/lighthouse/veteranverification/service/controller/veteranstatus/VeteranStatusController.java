package gov.va.api.lighthouse.veteranverification.service.controller.veteranstatus;

import static gov.va.api.lighthouse.veteranverification.service.MpiLookupUtils.getInputEdipiOrIcn;

import com.sun.xml.ws.fault.ServerSOAPFaultException;
import com.sun.xml.ws.wsdl.parser.InaccessibleWSDLException;
import gov.va.api.lighthouse.emis.EmisVeteranStatusServiceClient;
import gov.va.api.lighthouse.mpi.MasterPatientIndexClient;
import gov.va.api.lighthouse.veteranverification.api.v0.VeteranStatusResponse;
import gov.va.api.lighthouse.veteranverification.api.v0.VeteranStatusResponse.VeteranStatusDetails;
import gov.va.api.lighthouse.veteranverification.service.Exceptions.EmisInaccesibleWsdlException;
import gov.va.viers.cdi.emis.requestresponse.v1.EMISveteranStatusResponseType;
import gov.va.viers.cdi.emis.requestresponse.v1.InputEdiPiOrIcn;
import lombok.NonNull;
import org.hl7.v3.PRPAIN201306UV02;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Controller for Veteran Status Confirmation endpoint. */
@RestController
@RequestMapping(produces = {"application/json"})
public class VeteranStatusController {
  private final MasterPatientIndexClient mpiClient;

  private final EmisVeteranStatusServiceClient emisClient;

  /** Controller constructor. */
  public VeteranStatusController(
      @Autowired MasterPatientIndexClient mpiClient,
      @Autowired EmisVeteranStatusServiceClient emisClient) {
    this.mpiClient = mpiClient;
    this.emisClient = emisClient;
  }

  private VeteranStatusResponse notConfirmed(String icn) {
    return VeteranStatusResponse.builder()
        .data(
            VeteranStatusDetails.builder()
                .id(icn)
                .attributes(
                    VeteranStatusResponse.VeteranStatusAttributes.builder()
                        .veteranStatus("not confirmed")
                        .build())
                .build())
        .build();
  }

  /** Get veteran verification status from eMIS using an EDIPI or ICN from MPI lookup. */
  @GetMapping({"/v0/status/{icn}"})
  public VeteranStatusResponse veteranStatusVerificationResponse(
      @NonNull @PathVariable("icn") String icn) {
    PRPAIN201306UV02 mpiResponse = mpiClient.request1305ByIcn(icn);
    InputEdiPiOrIcn ediPiOrIcn = getInputEdipiOrIcn(mpiResponse);
    if (ediPiOrIcn == null) {
      return notConfirmed(icn);
    }
    EMISveteranStatusResponseType status = null;
    try {
      status = emisClient.veteranStatusRequest(ediPiOrIcn);
    } catch (ServerSOAPFaultException exception) {
      return notConfirmed(icn);
    } catch (InaccessibleWSDLException exception) {
      throw new EmisInaccesibleWsdlException();
    }
    return VeteranStatusTransformer.builder().response(status).build().toVeteranStatus(icn);
  }
}
