package gov.va.api.lighthouse.veteranverification.service;

import gov.va.api.lighthouse.bgs.BenefitsGatewayServicesClient;
import gov.va.api.lighthouse.bgs.BgsConfig;
import gov.va.api.lighthouse.bgs.SoapBenefitsGatewayServicesClient;
import gov.va.api.lighthouse.emis.EmisConfigV1;
import gov.va.api.lighthouse.emis.EmisConfigV2;
import gov.va.api.lighthouse.emis.EmisMilitaryInformationServiceClient;
import gov.va.api.lighthouse.emis.EmisVeteranStatusServiceClient;
import gov.va.api.lighthouse.emis.SoapEmisMilitaryInformationServiceClient;
import gov.va.api.lighthouse.emis.SoapEmisVeteranStatusServiceClient;
import gov.va.api.lighthouse.mpi.MasterPatientIndexClient;
import gov.va.api.lighthouse.mpi.MpiConfig;
import gov.va.api.lighthouse.mpi.SoapMasterPatientIndexClient;
import gov.va.api.lighthouse.veteranverification.service.utils.JwksProperties;
import gov.va.api.lighthouse.veteranverification.service.utils.Notary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Veteran Verification Configuration model. */
@Configuration
public class VeteranVerificationConfig {
  private MpiConfig mpiConfig;

  private EmisConfigV1 emisConfigV1;

  private EmisConfigV2 emisConfigV2;

  private BgsConfig bgsConfig;

  private String currentKeyId;

  private String currentKeyPassword;

  private String keyStorePassword;

  private String keyStorePath;

  VeteranVerificationConfig(
      @Autowired MpiConfig mpiConfig,
      @Autowired EmisConfigV1 emisConfigV1,
      @Autowired EmisConfigV2 emisConfigV2,
      @Autowired BgsConfig bgsConfig,
      @Value("${jwk-set.current-key-id}") String currentKeyId,
      @Value("${jwk-set.current-password}") String currentKeyPassword,
      @Value("${jwk-set.keystore-password}") String keyStorePassword,
      @Value("${jwk-set.keystore-path}") String keyStorePath) {
    this.mpiConfig = mpiConfig;
    this.emisConfigV1 = emisConfigV1;
    this.emisConfigV2 = emisConfigV2;
    this.bgsConfig = bgsConfig;
    this.currentKeyId = currentKeyId;
    this.currentKeyPassword = currentKeyPassword;
    this.keyStorePassword = keyStorePassword;
    this.keyStorePath = keyStorePath;
  }

  @Bean
  public BenefitsGatewayServicesClient benefitsGatewayServicesClient() {
    return SoapBenefitsGatewayServicesClient.of(bgsConfig);
  }

  @Bean
  public EmisMilitaryInformationServiceClient emisMilitaryInformationServiceClient() {
    return SoapEmisMilitaryInformationServiceClient.of(emisConfigV2);
  }

  @Bean
  public EmisVeteranStatusServiceClient emisVeteranStatusServiceClient() {
    return SoapEmisVeteranStatusServiceClient.of(emisConfigV1);
  }

  @Bean
  JwksProperties jwksProperties() {
    return JwksProperties.builder()
        .currentKeyId(currentKeyId)
        .currentKeyPassword(currentKeyPassword)
        .keyStorePassword(keyStorePassword)
        .keyStorePath(keyStorePath)
        .build();
  }

  @Bean
  public MasterPatientIndexClient masterPatientIndexClient() {
    return SoapMasterPatientIndexClient.of(mpiConfig);
  }

  @Bean
  Notary notary() {
    return new Notary(
        JwksProperties.builder()
            .currentKeyId(currentKeyId)
            .currentKeyPassword(currentKeyPassword)
            .keyStorePassword(keyStorePassword)
            .keyStorePath(keyStorePath)
            .build());
  }
}
