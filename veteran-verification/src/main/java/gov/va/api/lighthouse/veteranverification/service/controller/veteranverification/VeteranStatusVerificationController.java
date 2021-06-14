package gov.va.api.lighthouse.veteranverification.service.controller.veteranverification;

import static gov.va.api.lighthouse.veteranverification.service.MpiLookupUtils.getInputEdipiOrIcn;

import com.sun.xml.ws.fault.ServerSOAPFaultException;
import com.sun.xml.ws.wsdl.parser.InaccessibleWSDLException;
import gov.va.api.lighthouse.emis.EmisVeteranStatusServiceClient;
import gov.va.api.lighthouse.mpi.MasterPatientIndexClient;
import gov.va.api.lighthouse.veteranverification.api.VeteranStatusVerification;
import gov.va.api.lighthouse.veteranverification.api.VeteranStatusVerification.VeteranStatusVerificationDetails;
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
public class VeteranStatusVerificationController {
  private final MasterPatientIndexClient mpiClient;

  private final EmisVeteranStatusServiceClient emisClient;

  /** Controller constructor. */
  public VeteranStatusVerificationController(
      @Autowired MasterPatientIndexClient mpiClient,
      @Autowired EmisVeteranStatusServiceClient emisClient) {
    this.mpiClient = mpiClient;
    this.emisClient = emisClient;
  }

  private VeteranStatusVerification notConfirmed(String icn) {
    return VeteranStatusVerification.builder()
        .data(
            VeteranStatusVerificationDetails.builder()
                .id(icn)
                .attributes(
                    VeteranStatusVerification.VeteranStatusAttributes.builder()
                        .veteranStatus("not confirmed")
                        .build())
                .build())
        .build();
  }

  /** Get veteran verification status from eMIS using an EDIPI or ICN from MPI lookup. */
  @GetMapping({"/v0/status/{icn}"})
  public VeteranStatusVerification veteranStatusVerificationResponse(
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
    return VeteranStatusVerificationTransformer.builder()
        .response(status)
        .build()
        .toVeteranStatus(icn);
  }
}
