package gov.va.api.lighthouse.veteranverification.api;

import static java.util.Collections.singletonList;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** The error response is the payload returned to the caller should a failure occur. */
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonAutoDetect(
    fieldVisibility = JsonAutoDetect.Visibility.ANY,
    isGetterVisibility = JsonAutoDetect.Visibility.NONE)
@Schema(description = "API invocation or processing error", type = "object", name = "APIError")
public abstract class ApiError {
  private static final String BAD_REQUEST_ERROR_CODE = "400";

  @ArraySchema(schema = @Schema(implementation = ApiErrorDetails.class))
  List<ApiErrorDetails> errors;

  private static String toSnakeCase(String field) {
    return field.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
  }

  /** Error class for a missing parameter. */
  @NoArgsConstructor
  public static class MissingParameterApiError extends ApiError {
    /** Build Api Error for missing parameter with error message parameter. */
    @Builder
    public MissingParameterApiError(String field) {
      super(
          singletonList(
              ApiErrorDetails.builder()
                  .title("Missing parameter")
                  .detail("The required parameter \"" + toSnakeCase(field) + "\", is missing")
                  .code("108")
                  .status(BAD_REQUEST_ERROR_CODE)
                  .build()));
    }
  }

  /** Error class for an invalid parameter. */
  @NoArgsConstructor
  public static class InvalidParameterApiError extends ApiError {
    /** Build Api Error for invalid parameter error using value and field name parameters. */
    @Builder
    public InvalidParameterApiError(String value, String field) {
      super(
          singletonList(
              ApiErrorDetails.builder()
                  .title("Invalid field value")
                  .detail(value + " is not a valid value for \"" + toSnakeCase(field) + "\"")
                  .code("103")
                  .status(BAD_REQUEST_ERROR_CODE)
                  .build()));
    }
  }

  /** Error class for an no service history found. */
  public static class NoServiceHistoryFoundApiError extends ApiError {
    /** Build Api Error for invalid parameter error using value and field name parameters. */
    @Builder
    public NoServiceHistoryFoundApiError() {
      super(
          singletonList(
              ApiErrorDetails.builder()
                  .title("Not Implemented")
                  .detail("No service history found.")
                  .code("404")
                  .status("404")
                  .build()));
    }
  }

  /** Error class for inaccessible WSDL. */
  public static class InaccessibleWsdlErrorApiError extends ApiError {
    /** Build API Error for General Soap Response Error. */
    @Builder
    public InaccessibleWsdlErrorApiError() {
      super(
          singletonList(
              ApiErrorDetails.builder()
                  .title("Service unavailable")
                  .detail("An external service is unavailable.")
                  .code("503")
                  .status("503")
                  .build()));
    }
  }

  /** Error class for inaccessible eMIS WSDL. */
  public static class EmisInaccessibleWsdlErrorApiError extends ApiError {
    /** Build API Error for inaccessible eMIS WSDL. */
    @Builder
    public EmisInaccessibleWsdlErrorApiError() {
      super(
          singletonList(
              ApiErrorDetails.builder()
                  .title("Unexpected response body")
                  .detail(
                      "EMIS service responded with something other than veteran "
                          + "status information.")
                  .code("EMIS_STATUS502")
                  .status("502")
                  .build()));
    }
  }

  /** Error class for server soap fault exception. */
  public static class ServerSoapFaultApiError extends ApiError {
    /** Build API Error for server soap fault exception. */
    @Builder
    public ServerSoapFaultApiError() {
      super(
          singletonList(
              ApiErrorDetails.builder()
                  .title("Internal server error")
                  .detail("Internal server error")
                  .code("500")
                  .status("500")
                  .build()));
    }
  }
}
