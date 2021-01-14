package gov.va.api.lighthouse.veteranverification.service.controller;

import gov.va.api.lighthouse.mpi.Mpi1305RequestAttributes;
import gov.va.api.lighthouse.mpi.MpiConfig;
import gov.va.api.lighthouse.mpi.SoapMasterPatientIndexClient;
import gov.va.api.lighthouse.veteranverification.api.VeteranStatusRequestAttributes;
import java.util.function.Function;
import javax.ws.rs.NotFoundException;
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
public class VeteranConfirmationStatusController {
  @Getter private final MpiConfig mpiConfig;

  @Setter private Function<Mpi1305RequestAttributes, PRPAIN201306UV02> veteranStatusRequest;

  public VeteranConfirmationStatusController(@Autowired MpiConfig mpiConfig) {
    this.mpiConfig = mpiConfig;
    log.info("Accessing MPI at {}", mpiConfig.getUrl());
  }

  /** Get response from MPI 1305 request. */
  @PostMapping({"/v0/status"})
  public PRPAIN201306UV02 veteranConfirmationStatusResponse(
      @RequestBody VeteranStatusRequestAttributes attributes) {
    PRPAIN201306UV02 response =
        veteranStatusRequest().apply(attributes.toMpi1305RequestAttributes());
    return response;
  }

  /** Lazy Getter. */
  public Function<Mpi1305RequestAttributes, PRPAIN201306UV02> veteranStatusRequest() {
    if (veteranStatusRequest == null) {
      return (attributes) -> {
        try {
          return SoapMasterPatientIndexClient.of(mpiConfig).request1305ByAttributes(attributes);
        } catch (Exception e) {
          throw new NotFoundException(e);
        }
      };
    }
    return veteranStatusRequest;
  }
}
