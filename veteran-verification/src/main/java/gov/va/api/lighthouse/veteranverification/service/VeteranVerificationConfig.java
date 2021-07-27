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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Veteran Verification Configuration model. */
@Configuration
public class VeteranVerificationConfig {
  private MpiConfig mpiConfig;

  private EmisConfigV1 emisConfigV1;

  private EmisConfigV2 emisConfigV2;

  private BgsConfig bgsConfig;

  VeteranVerificationConfig(
      @Autowired MpiConfig mpiConfig,
      @Autowired EmisConfigV1 emisConfigV1,
      @Autowired EmisConfigV2 emisConfigV2,
      @Autowired BgsConfig bgsConfig) {
    this.mpiConfig = mpiConfig;
    this.emisConfigV1 = emisConfigV1;
    this.emisConfigV2 = emisConfigV2;
    this.bgsConfig = bgsConfig;
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
  public MasterPatientIndexClient masterPatientIndexClient() {
    return SoapMasterPatientIndexClient.of(mpiConfig);
  }
}
