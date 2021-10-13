package gov.va.api.lighthouse.veteranverification.service.controller.health;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

/** Health endpoint that reports on the aggregate backend service health. */
@Slf4j
@RestController
@RequestMapping(
    value = "/backend/health",
    produces = {"application/json"})
public class BackendHealthController {
  private final AtomicBoolean hasCachedBackendHealth = new AtomicBoolean(false);

  private Map<String, Callable<ResponseEntity<String>>> healthChecks;

  BackendHealthController(
      @Autowired @Qualifier("healthCheckRegistry")
          Map<String, Callable<ResponseEntity<String>>> healthChecks) {
    this.healthChecks = healthChecks;
  }

  /** Clear cached resources. */
  @Scheduled(cron = "0 */5 * * * *")
  @CacheEvict(value = "backend-health")
  public void clearBackendHealthCache() {
    if (hasCachedBackendHealth.getAndSet(false)) {
      log.info("Clearing cache: backend-health");
    }
  }

  /** Get Backend Health. */
  @GetMapping
  @Cacheable("backend-health")
  public ResponseEntity<Health> collectBackendHealth() {
    Instant now = Instant.now();
    List<Health> backendHealths = new ArrayList<>();

    healthChecks.forEach((service, check) -> backendHealths.add(testHealth(service, check, now)));
    Status status =
        backendHealths.stream().anyMatch(h -> h.getStatus().equals(Status.DOWN))
            ? Status.DOWN
            : Status.UP;
    Health overallHealth =
        Health.status(new Status(status.getCode(), "Veteran-Verification Backend Health Check"))
            .withDetail("time", now)
            .withDetail("backendServices", backendHealths)
            .build();
    hasCachedBackendHealth.set(true);
    if (status.equals(Status.DOWN)) {
      return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(overallHealth);
    }
    return ResponseEntity.status(HttpStatus.OK).body(overallHealth);
  }

  @SneakyThrows
  private Health testHealth(
      String name, Callable<ResponseEntity<String>> healthCheck, Instant now) {
    HttpStatus status;
    try {
      ResponseEntity<String> response = healthCheck.call();
      status = response.getStatusCode();
    } catch (RestClientException e) {
      log.error("Failure occurred when getting {} health: {}", name, e.getMessage());
      status = HttpStatus.INTERNAL_SERVER_ERROR;
    }
    return Health.status(new Status(status.is2xxSuccessful() ? "UP" : "DOWN", name))
        .withDetail("name", name)
        .withDetail("httpCode", status.value())
        .withDetail("status", status)
        .withDetail("time", now)
        .build();
  }
}
