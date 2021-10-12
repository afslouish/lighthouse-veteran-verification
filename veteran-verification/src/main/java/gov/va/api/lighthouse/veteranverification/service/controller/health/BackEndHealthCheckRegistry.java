package gov.va.api.lighthouse.veteranverification.service.controller.health;

import gov.va.api.lighthouse.bgs.BenefitsGatewayServicesClient;
import gov.va.api.lighthouse.emis.EmisVeteranStatusServiceClient;
import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.Callable;
import lombok.Builder;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;

@Configuration
public class BackEndHealthCheckRegistry {
  private Map<String, Callable<ResponseEntity<String>>> registry;

  /** constructor. */
  @Builder
  public BackEndHealthCheckRegistry(
      @NonNull @Autowired BenefitsGatewayServicesClient bgsClient,
      @NonNull @Autowired EmisVeteranStatusServiceClient emisClient) {
    this.registry =
        Map.ofEntries(
            new AbstractMap.SimpleEntry<String, Callable<ResponseEntity<String>>>(
                "BGS", () -> bgsClient.health()),
            new AbstractMap.SimpleEntry<String, Callable<ResponseEntity<String>>>(
                "EMIS", () -> emisClient.health()));
  }

  @Bean
  @Qualifier("healthCheckRegistry")
  public Map<String, Callable<ResponseEntity<String>>> registry() {
    return registry;
  }
}
