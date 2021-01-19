package gov.va.api.lighthouse.veteranverification.service.controller;

import static gov.va.api.lighthouse.mpi.Creators.csWithCode;
import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.lighthouse.veteranverification.api.VeteranStatusConfirmation;
import org.hl7.v3.MFMIMT700711UV01QueryAck;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01ControlActProcess;
import org.junit.Test;

public class VeteranStatusConfirmationTransformerTest {
  @Test
  public void confirmed() {
    PRPAIN201306UV02 response = PRPAIN201306UV02.pRPAIN201306UV02Builder().build();
    response.setControlActProcess(
        PRPAIN201306UV02MFMIMT700711UV01ControlActProcess.builder()
            .queryAck(
                MFMIMT700711UV01QueryAck.builder().queryResponseCode(csWithCode("OK")).build())
            .build());
    assertThat(
            VeteranStatusConfirmationTransformer.builder()
                .response(response)
                .build()
                .toVeteranStatus())
        .isEqualTo(VeteranStatusConfirmation.builder().status("confirmed").build());
  }

  @Test
  public void notConfirmed() {
    assertThat(
            VeteranStatusConfirmationTransformer.builder()
                .response(PRPAIN201306UV02.pRPAIN201306UV02Builder().build())
                .build()
                .toVeteranStatus())
        .isEqualTo(VeteranStatusConfirmation.builder().status("not confirmed").build());
  }
}
