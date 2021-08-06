package gov.va.api.lighthouse.veteranverification.service;

import com.sun.xml.ws.fault.ServerSOAPFaultException;
import com.sun.xml.ws.wsdl.parser.InaccessibleWSDLException;
import gov.va.api.lighthouse.veteranverification.api.ApiError;
import gov.va.api.lighthouse.veteranverification.api.ApiError.EmisInaccessibleWsdlErrorApiError;
import gov.va.api.lighthouse.veteranverification.api.ApiError.InaccessibleWsdlErrorApiError;
import gov.va.api.lighthouse.veteranverification.api.ApiError.NoServiceHistoryFoundApiError;
import gov.va.api.lighthouse.veteranverification.service.Exceptions.EmisInaccesibleWsdlException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Handler for all web exceptions that returns a meaningful error response object. */
@RestControllerAdvice
@RequestMapping(produces = "application/json")
public class WebExceptionHandler {
  /** Return error for EMIS inaccessible wsdl exception. */
  @ExceptionHandler({EmisInaccesibleWsdlException.class})
  @ResponseStatus(HttpStatus.BAD_GATEWAY)
  public EmisInaccessibleWsdlErrorApiError handleEmisInaccessibleWsdlException() {
    return new EmisInaccessibleWsdlErrorApiError();
  }

  /** Return error for inaccessible wsdl exception. */
  @ExceptionHandler({InaccessibleWSDLException.class})
  @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
  public InaccessibleWsdlErrorApiError handleInaccessibleWsdlException() {
    return new InaccessibleWsdlErrorApiError();
  }

  /** Return error for EMIS inaccessible wsdl exception. */
  @ExceptionHandler({Exceptions.NoServiceHistoryFoundException.class})
  @ResponseStatus(HttpStatus.BAD_GATEWAY)
  public NoServiceHistoryFoundApiError handleNoServiceHistoryFoundException() {
    return new NoServiceHistoryFoundApiError();
  }

  @ExceptionHandler({ServerSOAPFaultException.class})
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ApiError handleSoapFaultException(ServerSOAPFaultException e) {
    return new ApiError.ServerSoapFaultApiError();
  }
}
