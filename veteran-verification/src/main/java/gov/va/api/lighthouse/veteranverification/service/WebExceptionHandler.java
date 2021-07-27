package gov.va.api.lighthouse.veteranverification.service;

import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import com.sun.xml.ws.fault.ServerSOAPFaultException;
import com.sun.xml.ws.wsdl.parser.InaccessibleWSDLException;
import gov.va.api.lighthouse.veteranverification.api.ApiError;
import gov.va.api.lighthouse.veteranverification.api.ApiError.EmisInaccessibleWsdlErrorApiError;
import gov.va.api.lighthouse.veteranverification.api.ApiError.InaccessibleWsdlErrorApiError;
import gov.va.api.lighthouse.veteranverification.api.ApiError.InvalidParameterApiError;
import gov.va.api.lighthouse.veteranverification.api.ApiError.MissingParameterApiError;
import gov.va.api.lighthouse.veteranverification.api.ApiError.NoServiceHistoryFoundApiError;
import gov.va.api.lighthouse.veteranverification.service.Exceptions.EmisInaccesibleWsdlException;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Handler for all web exceptions that returns a meaningful error response object. */
@RestControllerAdvice
@RequestMapping(produces = "application/json")
public class WebExceptionHandler {
  /** Null check and return field name of MethodArgumentNotValidException. */
  private String getFieldName(MethodArgumentNotValidException e) {
    String fieldName =
        Optional.ofNullable(e)
            .map(bindingResult -> bindingResult.getBindingResult())
            .map(fieldError -> fieldError.getFieldError())
            .map(field -> field.getField())
            .orElse("");
    return fieldName;
  }

  /** Null check and return field value of MethodArgumentNotValidException. */
  private String getFieldValue(MethodArgumentNotValidException e) {
    String fieldValue =
        Optional.ofNullable(e)
            .map(bindingResult -> bindingResult.getBindingResult())
            .map(fieldError -> fieldError.getFieldError())
            .map(field -> field.getRejectedValue().toString())
            .orElse("");
    return fieldValue;
  }

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

  /** Return error for invalid format on parameter (ssn, gender, or birth_date). */
  @ExceptionHandler({MethodArgumentNotValidException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public InvalidParameterApiError handleInvalidParameter(MethodArgumentNotValidException e) {
    return new InvalidParameterApiError(getFieldValue(e), getFieldName(e));
  }

  /** Return error for missing parameter (required: ssn, first_name, last_name, birth_date). */
  @ExceptionHandler({ValueInstantiationException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public MissingParameterApiError handleMissingParameter(ValueInstantiationException e) {
    String missingField =
        e.getCause()
            .getMessage()
            .substring(0, e.getCause().getMessage().indexOf(" is marked non-null but is null"));
    return new MissingParameterApiError(missingField);
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
