package gov.va.api.lighthouse.veteranverification.service.controller.veteranverification;

import static gov.va.api.lighthouse.veteranverification.service.MpiLookupUtils.getSsn;

import gov.va.api.lighthouse.bgs.BenefitsGatewayServicesClient;
import gov.va.api.lighthouse.mpi.MasterPatientIndexClient;
import gov.va.vba.benefits.share.services.FindRatingData;
import gov.va.vba.benefits.share.services.FindRatingDataResponse;
import lombok.NonNull;
import org.hl7.v3.PRPAIN201306UV02;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = {"application/json"})
public class DisabilityRatingController {
  private final MasterPatientIndexClient mpiClient;
  private final BenefitsGatewayServicesClient bgsClient;

  /** Controller constructor. */
  public DisabilityRatingController(
      @Autowired MasterPatientIndexClient mpiClient,
      @Autowired BenefitsGatewayServicesClient bgsClient) {
    this.mpiClient = mpiClient;
    this.bgsClient = bgsClient;
  }

  /** Get rating data from BGS using the SSN from MPI lookup. */
  @GetMapping({"/v0/disability_rating/{icn}"})
  public FindRatingDataResponse findRatingDataResponse(@NonNull @PathVariable("icn") String icn) {
    PRPAIN201306UV02 response = mpiClient.request1305ByIcn(icn);
    String fileNumber = getSsn(response);
    return bgsClient.ratingServiceRequest(FindRatingData.builder().fileNumber(fileNumber).build());
  }
}
