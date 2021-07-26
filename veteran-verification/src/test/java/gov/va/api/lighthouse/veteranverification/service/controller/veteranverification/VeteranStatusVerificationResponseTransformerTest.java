package gov.va.api.lighthouse.veteranverification.service.controller.veteranverification;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.lighthouse.veteranverification.api.v0.VeteranStatusVerificationResponse;
import gov.va.api.lighthouse.veteranverification.api.v0.VeteranStatusVerificationResponse.VeteranStatusVerificationDetails;
import gov.va.viers.cdi.emis.commonservice.v1.VeteranStatus;
import gov.va.viers.cdi.emis.requestresponse.v1.EMISveteranStatusResponseType;
import org.junit.jupiter.api.Test;

public class VeteranStatusVerificationResponseTransformerTest {
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
            VeteranStatusVerificationTransformer.builder()
                .response(response)
                .build()
                .toVeteranStatus("1111"))
        .isEqualTo(
            VeteranStatusVerificationResponse.builder()
                .data(
                    VeteranStatusVerificationDetails.builder()
                        .id("1111")
                        .attributes(
                            VeteranStatusVerificationResponse.VeteranStatusAttributes.builder()
                                .veteranStatus("confirmed")
                                .build())
                        .build())
                .build());
  }

  @Test
  public void notConfirmed() {
    assertThat(
            VeteranStatusVerificationTransformer.builder()
                .response(EMISveteranStatusResponseType.builder().build())
                .build()
                .toVeteranStatus("1111"))
        .isEqualTo(
            VeteranStatusVerificationResponse.builder()
                .data(
                    VeteranStatusVerificationDetails.builder()
                        .id("1111")
                        .attributes(
                            VeteranStatusVerificationResponse.VeteranStatusAttributes.builder()
                                .veteranStatus("not confirmed")
                                .build())
                        .build())
                .build());
  }
}
