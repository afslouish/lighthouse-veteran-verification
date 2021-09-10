package gov.va.api.lighthouse.veteranverification.service;

import com.sun.xml.ws.fault.ServerSOAPFaultException;
import com.sun.xml.ws.wsdl.parser.InaccessibleWSDLException;
import gov.va.api.lighthouse.veteranverification.api.ApiError;
import gov.va.api.lighthouse.veteranverification.api.ApiError.EmisInaccessibleWsdlErrorApiError;
import gov.va.api.lighthouse.veteranverification.api.ApiError.InaccessibleWsdlErrorApiError;
import gov.va.api.lighthouse.veteranverification.api.ApiError.NoServiceHistoryFoundApiError;
import gov.va.api.lighthouse.veteranverification.service.Exceptions.EmisInaccesibleWsdlException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Handler for all web exceptions that returns a meaningful error response object. */
@RestControllerAdvice
public class WebExceptionHandler {
  /** Return error for EMIS inaccessible wsdl exception. */
  @ExceptionHandler({EmisInaccesibleWsdlException.class})
  public ResponseEntity handleEmisInaccessibleWsdlException() {
    return new ResponseEntity(new EmisInaccessibleWsdlErrorApiError(), createErrorHeaders(),HttpStatus.BAD_GATEWAY);
  }

  /** Return error for inaccessible wsdl exception. */
  @ExceptionHandler({InaccessibleWSDLException.class})
  public ResponseEntity handleInaccessibleWsdlException() {
    return new ResponseEntity(new InaccessibleWsdlErrorApiError(), createErrorHeaders(),HttpStatus.BAD_GATEWAY);
  }

  /** Return error for EMIS inaccessible wsdl exception. */
  @ExceptionHandler({Exceptions.NoServiceHistoryFoundException.class})
  public ResponseEntity handleNoServiceHistoryFoundException() {
    return new ResponseEntity(new NoServiceHistoryFoundApiError(), createErrorHeaders(),HttpStatus.BAD_GATEWAY);
  }

  @ExceptionHandler({ServerSOAPFaultException.class})
  public ResponseEntity handleSoapFaultException(ServerSOAPFaultException e) {
    return new ResponseEntity(new ApiError.ServerSoapFaultApiError(), createErrorHeaders(),HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private HttpHeaders createErrorHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return headers;
  }
}
