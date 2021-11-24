package gov.va.api.lighthouse.veteranverification.service.controller.health;

import gov.va.api.lighthouse.bgs.BgsClient;
import gov.va.api.lighthouse.emis.EmisVeteranStatusServiceClient;
import java.util.Map;
import java.util.concurrent.Callable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

public class BackendHealthCheckRegistryTest {
  private AutoCloseable closeable;

  @Mock private EmisVeteranStatusServiceClient emisClient;

  @Mock private BgsClient bgsClient;

  @AfterEach
  void afterEach() throws Exception {
    closeable.close();
  }

  @BeforeEach
  void beforeEach() {
    closeable = MockitoAnnotations.openMocks(this);
  }

  @Test
  public void bgsClientIsNonNull() {
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          BackEndHealthCheckRegistry.builder().emisClient(emisClient).bgsClient(null).build();
        });
  }

  @Test
  public void emisClientIsNonNull() {
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          BackEndHealthCheckRegistry.builder().emisClient(null).bgsClient(bgsClient).build();
        });
  }

  @Test
  public void happyPath() {
    BackEndHealthCheckRegistry backEndHealthCheckRegistry =
        BackEndHealthCheckRegistry.builder().emisClient(emisClient).bgsClient(bgsClient).build();
    Map<String, Callable<ResponseEntity<String>>> registry = backEndHealthCheckRegistry.registry();
    Assertions.assertEquals(2, registry.size());
    Assertions.assertNotNull(registry.get("BGS"));
    Assertions.assertNotNull(registry.get("EMIS"));
  }
}
