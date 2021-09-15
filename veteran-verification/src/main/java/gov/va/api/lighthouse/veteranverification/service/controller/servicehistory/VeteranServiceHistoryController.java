package gov.va.api.lighthouse.veteranverification.service.controller.servicehistory;

import static gov.va.api.lighthouse.veteranverification.service.Exceptions.EmisInaccesibleWsdlException;
import static gov.va.api.lighthouse.veteranverification.service.Exceptions.NoServiceHistoryFoundException;
import static gov.va.api.lighthouse.veteranverification.service.MpiLookupUtils.getInputEdipiOrIcnV2;

import com.sun.xml.ws.fault.ServerSOAPFaultException;
import com.sun.xml.ws.wsdl.parser.InaccessibleWSDLException;
import gov.va.api.lighthouse.emis.EmisMilitaryInformationServiceClient;
import gov.va.api.lighthouse.mpi.MasterPatientIndexClient;
import gov.va.api.lighthouse.veteranverification.api.v0.ServiceHistoryResponse;
import gov.va.api.lighthouse.veteranverification.service.utils.Notary;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISdeploymentResponseType;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISguardReserveServicePeriodsResponseType;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISserviceEpisodeResponseType;
import gov.va.viers.cdi.emis.requestresponse.v2.InputEdiPiOrIcn;
import lombok.NonNull;
import org.hl7.v3.PRPAIN201306UV02;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VeteranServiceHistoryController {
  private final MasterPatientIndexClient mpiClient;

  private final EmisMilitaryInformationServiceClient emisClient;

  private final Notary notary;

  /** Controller constructor. */
  public VeteranServiceHistoryController(
      @Autowired MasterPatientIndexClient mpiClient,
      @Autowired EmisMilitaryInformationServiceClient emisClient,
      @Autowired Notary notary) {
    this.mpiClient = mpiClient;
    this.emisClient = emisClient;
    this.notary = notary;
  }

  /**
   * Get veteran verification service history jwt from eMIS using an EDIPI or ICN from MPI lookup.
   */
  @GetMapping(
      value = {"/v0/service_history/{icn}"},
      produces = {"application/jwt"})
  public String veteranServiceHistoryJwtResponse(@NonNull @PathVariable("icn") String icn) {
    ServiceHistoryResponse serviceHistoryResponse = veteranServiceHistoryResponse(icn);
    return notary.objectToJwt(serviceHistoryResponse);
  }

  /** Get veteran verification service history from eMIS using an EDIPI or ICN from MPI lookup. */
  @GetMapping(
      value = {"/v0/service_history/{icn}"},
      produces = {"application/json"})
  public ServiceHistoryResponse veteranServiceHistoryResponse(
      @NonNull @PathVariable("icn") String icn) {
    PRPAIN201306UV02 mpiResponse = mpiClient.request1305ByIcn(icn);
    InputEdiPiOrIcn ediPiOrIcn = getInputEdipiOrIcnV2(mpiResponse);
    if (ediPiOrIcn == null) {
      throw new NoServiceHistoryFoundException();
    }

    EMISdeploymentResponseType deployments;
    try {
      deployments = emisClient.deploymentRequest(ediPiOrIcn);
    } catch (ServerSOAPFaultException exception) {
      throw new NoServiceHistoryFoundException();
    } catch (InaccessibleWSDLException exception) {
      throw new EmisInaccesibleWsdlException();
    }

    EMISserviceEpisodeResponseType episodes;
    try {
      episodes = emisClient.serviceEpisodesRequest(ediPiOrIcn);
    } catch (ServerSOAPFaultException exception) {
      episodes = null;
    } catch (InaccessibleWSDLException exception) {
      throw new EmisInaccesibleWsdlException();
    }

    EMISguardReserveServicePeriodsResponseType grasResponse;
    try {
      grasResponse = emisClient.guardReserveServiceRequest(ediPiOrIcn);
    } catch (ServerSOAPFaultException exception) {
      grasResponse = null;
    } catch (InaccessibleWSDLException exception) {
      throw new EmisInaccesibleWsdlException();
    }

    if (episodes == null && grasResponse == null) {
      throw new NoServiceHistoryFoundException();
    }

    return VeteranServiceHistoryTransformer.builder()
        .icn(icn)
        .serviceEpisodeResponseType(episodes)
        .mpiResponse(mpiResponse)
        .deploymentResponse(deployments)
        .grasResponse(grasResponse)
        .build()
        .serviceHistoryTransformer();
  }
}
