package gov.va.api.lighthouse.veteranverification.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.sun.xml.ws.fault.ServerSOAPFaultException;
import gov.va.api.lighthouse.veteranverification.api.ApiError;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;

public class WebExceptionHandlerTest {
  private static WebExceptionHandler webExceptionHandler;
  @Mock ServerSOAPFaultException soapFaultException;

  @BeforeAll
  public static void initialize() {
    webExceptionHandler = new WebExceptionHandler();
  }

  @Test
  void handleEmisInaccessibleWsdlExceptionTest() {
    ResponseEntity<ApiError.EmisInaccessibleWsdlErrorApiError> response =
        webExceptionHandler.handleEmisInaccessibleWsdlException();
    assertThat(response).isInstanceOf(ResponseEntity.class);
  }

  @Test
  void handleInaccessibleWsdlExceptionTest() {
    ResponseEntity<ApiError.InaccessibleWsdlErrorApiError> response =
        webExceptionHandler.handleInaccessibleWsdlException();
    assertThat(response).isInstanceOf(ResponseEntity.class);
  }

  @Test
  void handleNoServiceHistoryFoundExceptionTest() {
    ResponseEntity<ApiError.NoServiceHistoryFoundApiError> response =
        webExceptionHandler.handleNoServiceHistoryFoundException();
    assertThat(response).isInstanceOf(ResponseEntity.class);
  }

  @Test
  void handleSoapFaultExceptionTest() {
    ResponseEntity<ApiError.ServerSoapFaultApiError> response =
        webExceptionHandler.handleSoapFaultException(soapFaultException);
    assertThat(response).isInstanceOf(ResponseEntity.class);
  }
}
