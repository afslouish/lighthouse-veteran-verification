package gov.va.api.lighthouse.veteranverification.service.controller.health;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.sun.xml.ws.wsdl.parser.InaccessibleWSDLException;
import java.net.MalformedURLException;
import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

public class BackendHealthControllerTest {
  private final Logger logger = (Logger) LoggerFactory.getLogger(BackendHealthController.class);

  AbstractMap.SimpleEntry<String, Callable<ResponseEntity<String>>> upService;

  AbstractMap.SimpleEntry<String, Callable<ResponseEntity<String>>> downService;

  @Mock Callable<ResponseEntity<String>> restClientExceptionResponse;

  @Mock Callable<ResponseEntity<String>> inaccessibleWSDLExceptionResponse;

  @Mock Callable<ResponseEntity<String>> malformedURLExceptionResponse;

  private ListAppender<ILoggingEvent> listAppender;

  @BeforeEach
  void beforeEach() throws Exception {
    // This allows logging spies
    listAppender = new ListAppender<>();
    listAppender.start();
    logger.addAppender(listAppender);

    upService = new AbstractMap.SimpleEntry<>("Up Service", () -> ResponseEntity.ok("Success"));

    restClientExceptionResponse = mock(Callable.class);
    when(restClientExceptionResponse.call()).thenThrow(mock(RestClientException.class));

    inaccessibleWSDLExceptionResponse = mock(Callable.class);
    when(inaccessibleWSDLExceptionResponse.call()).thenThrow(mock(InaccessibleWSDLException.class));

    malformedURLExceptionResponse = mock(Callable.class);
    when(malformedURLExceptionResponse.call()).thenThrow(mock(MalformedURLException.class));
  }

  @Test
  public void clearBackendHealthCacheHasCachedBackendHealthFalse() {
    BackendHealthController backendHealthController =
        new BackendHealthController(Map.ofEntries(upService));
    backendHealthController.clearBackendHealthCache();
    Assertions.assertEquals(0, listAppender.list.size());
  }

  @Test
  public void clearBackendHealthCacheHasCachedBackendHealthTrue() {
    BackendHealthController backendHealthController =
        new BackendHealthController(Map.ofEntries(upService));
    backendHealthController.collectBackendHealth();
    backendHealthController.clearBackendHealthCache();
    Assertions.assertEquals(1, listAppender.list.size());
  }

  @Test
  public void collectBackendHealthHappyPath() {
    ResponseEntity<Health> health =
        new BackendHealthController(Map.ofEntries(upService)).collectBackendHealth();
    Assertions.assertEquals(HttpStatus.OK, health.getStatusCode());
    Assertions.assertEquals(
        "Veteran-Verification Backend Health Check", health.getBody().getStatus().getDescription());
    Assertions.assertEquals("UP", health.getBody().getStatus().getCode());
    Assertions.assertNotNull(health.getBody().getDetails().get("time"));
    List<Health> dependencies = (List<Health>) health.getBody().getDetails().get("backendServices");
    Assertions.assertEquals(1, dependencies.size());
    Assertions.assertEquals("Up Service", dependencies.get(0).getStatus().getDescription());
    Assertions.assertEquals("UP", dependencies.get(0).getStatus().getCode());
    Assertions.assertEquals("Up Service", dependencies.get(0).getDetails().get("name"));
    Assertions.assertEquals(200, dependencies.get(0).getDetails().get("httpCode"));
    Assertions.assertEquals(HttpStatus.OK, dependencies.get(0).getDetails().get("status"));
    Assertions.assertNotNull(dependencies.get(0).getDetails().get("time"));
  }

  @Test
  public void collectBackendHealthOneBackendServiceReturnInaccessibleWSDLExceptionResponse() {
    Map<String, Callable<ResponseEntity<String>>> map = new LinkedHashMap<>();
    map.put(upService.getKey(), upService.getValue());
    downService = new AbstractMap.SimpleEntry<>("Down Service", inaccessibleWSDLExceptionResponse);
    map.put(downService.getKey(), downService.getValue());
    ResponseEntity<Health> health = new BackendHealthController(map).collectBackendHealth();
    Assertions.assertEquals(HttpStatus.SERVICE_UNAVAILABLE, health.getStatusCode());
    Assertions.assertEquals(
        "Veteran-Verification Backend Health Check", health.getBody().getStatus().getDescription());
    Assertions.assertEquals("DOWN", health.getBody().getStatus().getCode());
    Assertions.assertNotNull(health.getBody().getDetails().get("time"));
    List<Health> dependencies = (List<Health>) health.getBody().getDetails().get("backendServices");
    Assertions.assertEquals(2, dependencies.size());
    Assertions.assertEquals("Up Service", dependencies.get(0).getStatus().getDescription());
    Assertions.assertEquals("UP", dependencies.get(0).getStatus().getCode());
    Assertions.assertEquals("Up Service", dependencies.get(0).getDetails().get("name"));
    Assertions.assertEquals(200, dependencies.get(0).getDetails().get("httpCode"));
    Assertions.assertEquals(HttpStatus.OK, dependencies.get(0).getDetails().get("status"));
    Assertions.assertNotNull(dependencies.get(0).getDetails().get("time"));
    Assertions.assertEquals("Down Service", dependencies.get(1).getStatus().getDescription());
    Assertions.assertEquals("DOWN", dependencies.get(1).getStatus().getCode());
    Assertions.assertEquals("Down Service", dependencies.get(1).getDetails().get("name"));
    Assertions.assertEquals(500, dependencies.get(1).getDetails().get("httpCode"));
    Assertions.assertEquals(
        HttpStatus.INTERNAL_SERVER_ERROR, dependencies.get(1).getDetails().get("status"));
    Assertions.assertNotNull(dependencies.get(1).getDetails().get("time"));
  }

  @Test
  public void collectBackendHealthOneBackendServiceReturnMalformedURLExceptionResponse() {
    Map<String, Callable<ResponseEntity<String>>> map = new LinkedHashMap<>();
    map.put(upService.getKey(), upService.getValue());
    downService = new AbstractMap.SimpleEntry<>("Down Service", malformedURLExceptionResponse);
    map.put(downService.getKey(), downService.getValue());
    ResponseEntity<Health> health = new BackendHealthController(map).collectBackendHealth();
    Assertions.assertEquals(HttpStatus.SERVICE_UNAVAILABLE, health.getStatusCode());
    Assertions.assertEquals(
        "Veteran-Verification Backend Health Check", health.getBody().getStatus().getDescription());
    Assertions.assertEquals("DOWN", health.getBody().getStatus().getCode());
    Assertions.assertNotNull(health.getBody().getDetails().get("time"));
    List<Health> dependencies = (List<Health>) health.getBody().getDetails().get("backendServices");
    Assertions.assertEquals(2, dependencies.size());
    Assertions.assertEquals("Up Service", dependencies.get(0).getStatus().getDescription());
    Assertions.assertEquals("UP", dependencies.get(0).getStatus().getCode());
    Assertions.assertEquals("Up Service", dependencies.get(0).getDetails().get("name"));
    Assertions.assertEquals(200, dependencies.get(0).getDetails().get("httpCode"));
    Assertions.assertEquals(HttpStatus.OK, dependencies.get(0).getDetails().get("status"));
    Assertions.assertNotNull(dependencies.get(0).getDetails().get("time"));
    Assertions.assertEquals("Down Service", dependencies.get(1).getStatus().getDescription());
    Assertions.assertEquals("DOWN", dependencies.get(1).getStatus().getCode());
    Assertions.assertEquals("Down Service", dependencies.get(1).getDetails().get("name"));
    Assertions.assertEquals(500, dependencies.get(1).getDetails().get("httpCode"));
    Assertions.assertEquals(
        HttpStatus.INTERNAL_SERVER_ERROR, dependencies.get(1).getDetails().get("status"));
    Assertions.assertNotNull(dependencies.get(1).getDetails().get("time"));
  }

  @Test
  public void collectBackendHealthOneBackendServiceReturnRestClientException() {
    Map<String, Callable<ResponseEntity<String>>> map = new LinkedHashMap<>();
    map.put(upService.getKey(), upService.getValue());
    downService = new AbstractMap.SimpleEntry<>("Down Service", restClientExceptionResponse);
    map.put(downService.getKey(), downService.getValue());
    ResponseEntity<Health> health = new BackendHealthController(map).collectBackendHealth();
    Assertions.assertEquals(HttpStatus.SERVICE_UNAVAILABLE, health.getStatusCode());
    Assertions.assertEquals(
        "Veteran-Verification Backend Health Check", health.getBody().getStatus().getDescription());
    Assertions.assertEquals("DOWN", health.getBody().getStatus().getCode());
    Assertions.assertNotNull(health.getBody().getDetails().get("time"));
    List<Health> dependencies = (List<Health>) health.getBody().getDetails().get("backendServices");
    Assertions.assertEquals(2, dependencies.size());
    Assertions.assertEquals("Up Service", dependencies.get(0).getStatus().getDescription());
    Assertions.assertEquals("UP", dependencies.get(0).getStatus().getCode());
    Assertions.assertEquals("Up Service", dependencies.get(0).getDetails().get("name"));
    Assertions.assertEquals(200, dependencies.get(0).getDetails().get("httpCode"));
    Assertions.assertEquals(HttpStatus.OK, dependencies.get(0).getDetails().get("status"));
    Assertions.assertNotNull(dependencies.get(0).getDetails().get("time"));
    Assertions.assertEquals("Down Service", dependencies.get(1).getStatus().getDescription());
    Assertions.assertEquals("DOWN", dependencies.get(1).getStatus().getCode());
    Assertions.assertEquals("Down Service", dependencies.get(1).getDetails().get("name"));
    Assertions.assertEquals(500, dependencies.get(1).getDetails().get("httpCode"));
    Assertions.assertEquals(
        HttpStatus.INTERNAL_SERVER_ERROR, dependencies.get(1).getDetails().get("status"));
    Assertions.assertNotNull(dependencies.get(1).getDetails().get("time"));
  }
}
