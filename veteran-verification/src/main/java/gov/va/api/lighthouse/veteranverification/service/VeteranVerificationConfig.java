package gov.va.api.lighthouse.veteranverification.service;

import gov.va.api.lighthouse.emis.EmisConfigV1;
import gov.va.api.lighthouse.emis.EmisVeteranStatusServiceClient;
import gov.va.api.lighthouse.emis.SoapEmisVeteranStatusServiceClient;
import gov.va.api.lighthouse.mpi.MasterPatientIndexClient;
import gov.va.api.lighthouse.mpi.MpiConfig;
import gov.va.api.lighthouse.mpi.SoapMasterPatientIndexClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VeteranVerificationConfig {
  MpiConfig mpiConfig;

  EmisConfigV1 emisConfigV1;

  VeteranVerificationConfig(@Autowired MpiConfig mpiConfig, @Autowired EmisConfigV1 emisConfigV1) {
    this.mpiConfig = mpiConfig;
    this.emisConfigV1 = emisConfigV1;
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
