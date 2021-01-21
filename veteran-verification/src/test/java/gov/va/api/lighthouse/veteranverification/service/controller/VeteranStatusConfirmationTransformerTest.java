package gov.va.api.lighthouse.veteranverification.service.controller;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.lighthouse.veteranverification.api.VeteranStatusConfirmation;
import gov.va.viers.cdi.emis.commonservice.v1.VeteranStatus;
import gov.va.viers.cdi.emis.requestresponse.v1.EMISveteranStatusResponseType;
import org.junit.Test;

public class VeteranStatusConfirmationTransformerTest {
  @Test
  public void confirmed() {
    EMISveteranStatusResponseType response =
        EMISveteranStatusResponseType.builder()
            .veteranStatus(
                VeteranStatus.builder()
                    .pre911DeploymentIndicator("N")
                    .post911CombatIndicator("N")
                    .edipi("1111111111")
                    .post911DeploymentIndicator("N")
                    .title38StatusCode("N")
                    .build())
            .build();
    assertThat(
            VeteranStatusConfirmationTransformer.builder()
                .response(response)
                .build()
                .toVeteranStatus())
        .isEqualTo(VeteranStatusConfirmation.builder().veteranStatus("confirmed").build());
  }

  @Test
  public void notConfirmed() {
    assertThat(
            VeteranStatusConfirmationTransformer.builder()
                .response(EMISveteranStatusResponseType.builder().build())
                .build()
                .toVeteranStatus())
        .isEqualTo(VeteranStatusConfirmation.builder().veteranStatus("not confirmed").build());
    assertThat(
            VeteranStatusConfirmationTransformer.builder()
                .response(EMISveteranStatusResponseType.builder().veteranStatus(null).build())
                .build()
                .toVeteranStatus())
        .isEqualTo(VeteranStatusConfirmation.builder().veteranStatus("not confirmed").build());
  }
}
