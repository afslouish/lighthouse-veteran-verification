package gov.va.api.lighthouse.veteranverification.service;

import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import gov.va.api.lighthouse.veteranverification.api.ApiError.InvalidParameterApiError;
import gov.va.api.lighthouse.veteranverification.api.ApiError.MissingParameterApiError;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
}