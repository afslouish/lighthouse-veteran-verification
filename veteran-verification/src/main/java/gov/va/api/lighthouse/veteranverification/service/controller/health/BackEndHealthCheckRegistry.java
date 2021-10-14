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

  /**
   * Contains a map of all backend health checks. To extend add a new health check entry to the
   * list. The key should be the backend service's name. The value of each key contains a Callable
   * command object. This object's call method should return the health check command returned as a
   * ResponseEntity/<String/> for its respective service.
   */
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
