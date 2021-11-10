package gov.va.api.lighthouse.veteranverification.service;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.lighthouse.bgs.SoapBgsRatingServiceClient;
import gov.va.api.lighthouse.emis.SoapEmisMilitaryInformationServiceClient;
import gov.va.api.lighthouse.emis.SoapEmisVeteranStatusServiceClient;
import gov.va.api.lighthouse.mpi.SoapMasterPatientIndexClient;
import gov.va.api.lighthouse.veteranverification.service.utils.JwksProperties;
import gov.va.api.lighthouse.veteranverification.service.utils.Notary;
import org.junit.jupiter.api.Test;

public class VeteranVerificationConfigTest {
  private final VeteranVerificationConfig veteranVerificationConfig =
      new VeteranVerificationConfig(
          TestUtils.makeMpiConfig(),
          TestUtils.makeEmisConfig(),
          TestUtils.makeEmisConfigV2(),
          TestUtils.makeBgsConfig(),
          "fake",
          "secret",
          "secret",
          "src/test/resources/fakekeystore.jks");

  @Test
  void bgsRatingServiceClient() {
    assertThat(veteranVerificationConfig.bgsRatingServiceClient())
        .isInstanceOf(SoapBgsRatingServiceClient.class);
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
  void jwksProperties() {
    assertThat(veteranVerificationConfig.jwksProperties()).isInstanceOf(JwksProperties.class);
  }

  @Test
  void mpiClient() {
    assertThat(veteranVerificationConfig.masterPatientIndexClient())
        .isInstanceOf(SoapMasterPatientIndexClient.class);
  }

  @Test
  void notary() {
    assertThat(veteranVerificationConfig.notary()).isInstanceOf(Notary.class);
  }
}
