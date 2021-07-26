package gov.va.api.lighthouse.veteranverification.service;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.lighthouse.bgs.SoapBenefitsGatewayServicesClient;
import gov.va.api.lighthouse.emis.SoapEmisMilitaryInformationServiceClient;
import gov.va.api.lighthouse.emis.SoapEmisVeteranStatusServiceClient;
import gov.va.api.lighthouse.mpi.SoapMasterPatientIndexClient;
import org.junit.jupiter.api.Test;

public class VeteranVerificationConfigTest {
  private final VeteranVerificationConfig veteranVerificationConfig =
      new VeteranVerificationConfig(
          TestUtils.makeMpiConfig(),
          TestUtils.makeEmisConfig(),
          TestUtils.makeEmisConfigV2(),
          TestUtils.makeBgsConfig());

  @Test
  void bgsClient() {
    assertThat(veteranVerificationConfig.benefitsGatewayServicesClient())
        .isInstanceOf(SoapBenefitsGatewayServicesClient.class);
  }

  @Test
  void emisClient() {
    assertThat(veteranVerificationConfig.emisVeteranStatusServiceClient())
        .isInstanceOf(SoapEmisVeteranStatusServiceClient.class);
  }

  @Test
  void emisMilitaryInformationClient() {
    assertThat(veteranVerificationConfig.emisMilitaryInformationServiceClient())
        .isInstanceOf(SoapEmisMilitaryInformationServiceClient.class);
  }

  @Test
  void mpiClient() {
    assertThat(veteranVerificationConfig.masterPatientIndexClient())
        .isInstanceOf(SoapMasterPatientIndexClient.class);
  }
}
