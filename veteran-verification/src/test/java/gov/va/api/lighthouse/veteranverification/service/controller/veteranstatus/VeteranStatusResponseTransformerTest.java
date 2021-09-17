package gov.va.api.lighthouse.veteranverification.service.controller.veteranstatus;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.lighthouse.veteranverification.api.v1.VeteranStatusResponse;
import gov.va.api.lighthouse.veteranverification.api.v1.VeteranStatusResponse.VeteranStatusDetails;
import gov.va.viers.cdi.emis.commonservice.v1.VeteranStatus;
import gov.va.viers.cdi.emis.requestresponse.v1.EMISveteranStatusResponseType;
import org.junit.jupiter.api.Test;

public class VeteranStatusResponseTransformerTest {
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
                    .title38StatusCode("V1")
                    .build())
            .build();
    assertThat(
            VeteranStatusTransformer.builder().response(response).build().toVeteranStatus("1111"))
        .isEqualTo(
            VeteranStatusResponse.builder()
                .data(
                    VeteranStatusDetails.builder()
                        .id("1111")
                        .attributes(
                            VeteranStatusResponse.VeteranStatusAttributes.builder()
                                .veteranStatus("confirmed")
                                .build())
                        .build())
                .build());
  }

  @Test
  public void notConfirmed() {
    assertThat(
            VeteranStatusTransformer.builder()
                .response(EMISveteranStatusResponseType.builder().build())
                .build()
                .toVeteranStatus("1111"))
        .isEqualTo(
            VeteranStatusResponse.builder()
                .data(
                    VeteranStatusDetails.builder()
                        .id("1111")
                        .attributes(
                            VeteranStatusResponse.VeteranStatusAttributes.builder()
                                .veteranStatus("not confirmed")
                                .build())
                        .build())
                .build());
  }
}
