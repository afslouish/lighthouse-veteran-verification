package gov.va.api.lighthouse.veteranverification.service.controller.veteranconfirmation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import gov.va.api.lighthouse.veteranverification.api.ApiError.InvalidParameterApiError;
import gov.va.api.lighthouse.veteranverification.api.ApiError.MissingParameterApiError;
import gov.va.api.lighthouse.veteranverification.service.WebExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

public class WebExceptionHandlerTest {

  private WebExceptionHandler exceptionHandler = new WebExceptionHandler();

  @Test
  void invalidParameterTest() {
    BindingResult bindingResult = mock(BindingResult.class);
    FieldError fieldError = mock(FieldError.class);
    when(bindingResult.getFieldError()).thenReturn(fieldError);
    when(fieldError.getRejectedValue()).thenReturn("John");
    when(fieldError.getField()).thenReturn("firstName");
    MethodArgumentNotValidException exception =
        new MethodArgumentNotValidException(null, bindingResult);
    assertThat(exceptionHandler.handleInvalidParameter(exception))
        .isInstanceOf(InvalidParameterApiError.class);
  }

  @Test
  void missingParametersTest() {
    NullPointerException nullPointerException = mock(NullPointerException.class);
    ValueInstantiationException valueInstantiationException =
        mock(ValueInstantiationException.class);
    when(valueInstantiationException.getCause()).thenReturn(nullPointerException);
    when(nullPointerException.getMessage()).thenReturn("firstName is marked non-null but is null");
    assertThat(exceptionHandler.handleMissingParameter(valueInstantiationException))
        .isInstanceOf(MissingParameterApiError.class);
  }
}
