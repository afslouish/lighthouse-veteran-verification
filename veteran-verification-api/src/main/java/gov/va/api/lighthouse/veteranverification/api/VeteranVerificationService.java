package gov.va.api.lighthouse.veteranverification.api;

import gov.va.api.lighthouse.veteranverification.api.v0.ServiceHistoryResponse;
import gov.va.api.lighthouse.veteranverification.api.v0.VeteranStatusVerificationResponse;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.servers.ServerVariable;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/** Open API information for Veteran Verification Service. */
@OpenAPIDefinition(
    info =
        @Info(
            version = "0.0.1",
            title = "Veteran Verification",
            description =
                "The Veteran Verification API allows you to confirm the Veteran status for a given"
                    + " person, or retrieve a Veteran's service history or disability rating.\n\n"
                    + "When confirming Veteran Status, the Status endpoint will return "
                    + "\"Confirmed\" or \"Not Confirmed\".\n\n"
                    + "## Quickstart Guide\n\n"
                    + "### 1. Get Access Credentials\n\n"
                    + "Get started by filling out the form on the [Apply for VA Lighthouse "
                    + "Developer Access](/apply) page.\n\nAfter submitting a request, you will "
                    + "receive your credentials for using the API in the Sandbox environment, "
                    + "which allows you to try it out with mock data before moving to the "
                    + "Production environment.\n\n"
                    + "### 2. Test the  API\n\n"
                    + "In the endpoint documentation below, we've provided a curl command builder "
                    + "for trying out the API before implementation with your app.\n\n"
                    + "### 3. Build your app\n\n"
                    + "The base URI for the Veteran Verification API in the Sandbox environment "
                    + "is:\n\n"
                    + "https://sandbox-api.va.gov/services/veteran_verification/v0\n\n"
                    + "Check out some of our [sample apps]"
                    + "(https://github.com/department-of-veterans-affairs/vets-api-clients/tree/master/samples/). "
                    + "Please visit our VA Lighthouse [Support portal](/support) should you need "
                    + "further assistance.\n\n"
                    + "### 4. Show us a demo and get set up in the Production environment\n\n"
                    + "After building your app, we ask that you give us a demo before we set you "
                    + "up with production credentials. "
                    + "Please see the [Path to Production](/go-live) page for more details.\n\n"
                    + "## Reference\n\n"
                    + "### Sandbox vs. Production Data\n"
                    + "APIs accessed via the Sandbox environment are using the same underlying "
                    + "logic as VA???s production APIs; only the underlying data store is "
                    + "different.\n\n"
                    + "### Rate Limiting\n"
                    + "We implemented basic rate limiting of 60 requests per minute. If you exceed "
                    + "this quota, your request will return a 429 status code. You may petition "
                    + "for increased rate limits by emailing, and requests will be decided on a "
                    + "case by case basis.\n\n"
                    + "### Raw Open API Spec\n"
                    + "https://api.va.gov/services/veteran_verification/docs/v0/veteran_verification",
            termsOfService = "",
            contact = @Contact(name = "VA.gov")),
    servers = {
      @Server(
          url = "https://sandbox-api.va.gov/services/veteran_verification/{version}",
          description = "VA.gov API sandbox environment",
          variables = {@ServerVariable(name = "version", defaultValue = "v0")}),
      @Server(
          url = "https://api.va.gov/services/veteran_verification/{version}",
          description = "VA.gov API production environment",
          variables = {@ServerVariable(name = "version", defaultValue = "v0")})
    })
@Path("/")
public interface VeteranVerificationService {
  @GET
  @Path("disability_rating")
  @ApiResponse(
      responseCode = "200",
      description = "Disability Rating retrieved successfully",
      content = {
        @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = DisabilityRatingResponse.class)),
        @Content(
            mediaType = "application/jwt",
            schema =
                @Schema(
                    description =
                        "A token containing signed claims about a Veteran's disability rating "
                            + "percentage\n",
                    example =
                        "eyJhbGciOiJSUzI1NiIsImtpZCI6IjA4OGQyNDIzMmZmNmZhYTRjZ"
                            + "DRjZmVjMTI2YWQwNDMxZGZmMWVhMDI4YWZkYjFjODZiMz"
                            + "cxOGQ3MDE3MWFlZDYifQ.eyJkYXRhIjp7ImlkIjoxMjMwM"
                            + "ywidHlwZSI6ImRvY3VtZW50X3VwbG9hZCIsImF0dHJpYnV"
                            + "0ZXMiOnsiZGVjaXNpb24iOiJTZXJ2aWNlIENvbm5lY3RlZ"
                            + "CIsImVmZmVjdGl2ZV9kYXRlIjoiMjAxOC0wMy0yN1QyMTo"
                            + "wMDo0MS4wMDArMDAwMCIsInJhdGluZ19wZXJjZW50YWdlI"
                            + "jo1MH19fQ.z3EeYFixwdmG_4_LFdzy6fdF2Y7nj3y9uOPR"
                            + "TwLqsXVcLNUDIN72atn0SSI-hkF-rRkbFdyzLDaY2AWtQ-"
                            + "LmBPdeWqyv4U2ZnjynAwlCnG0VNG3x4Wz2qSW7BW1cQVB"
                            + "x0yvWag_NmQ74AfOBNz7K2qz8aEOvYIJaicRr2hSAu7A"))
      })
  @ApiResponse(responseCode = "401", description = "Not authorized")
  @ApiResponse(
      responseCode = "502",
      description =
          "BGS Service responded with something other "
              + "than the expected disability rating response.")
  @Operation(
      operationId = "getDisabilityRating",
      summary = "Retrieve disability rating of authorized Veteran",
      security = {@SecurityRequirement(name = "bearer_token")})
  @Tag(name = "Veteran Verification")
  DisabilityRatingResponse disabilityRating();

  @GET
  @Path("service_history")
  @ApiResponse(
      responseCode = "200",
      description = "Service History retrieved successfully",
      content = {
        @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = ServiceHistoryResponse.class)),
        @Content(
            mediaType = "application/jwt",
            schema =
                @Schema(
                    description =
                        "A token containing signed claims about a Veteran's service history",
                    example =
                        "eyJhbGciOiJSUzI1NiIsImtpZCI6IjA4OGQyNDIzMmZmNmZhYTRjZDRjZmVjMTI2YWQwNDMxZ"
                            + "GZmMWVhMDI4YWZkYjFjODZiMzcxOGQ3MDE3MWFlZDYifQ.eyJkYXRhIjp7ImlkIjoiM"
                            + "TIzMTJBQVNEZiIsInR5cGUiOiJzZXJ2aWNlLWhpc3RvcnktZXBpc29kZXMiLCJhdHRy"
                            + "aWJ1dGVzIjp7InN0YXJ0X2RhdGUiOiIxOTQ4LTA0LTA4IiwiZW5kX2RhdGUiOiIxOTU"
                            + "wLTA1LTEwIiwiYnJhbmNoIjoiQWlyIEZvcmNlIiwiZGlzY2hhcmdlX3N0YXR1cyI6Im"
                            + "hvbm9yYWJsZSIsImRlcGxveW1lbnRzIjpbeyJzdGFydF9kYXRlIjoiMTk0OC0xMC0xM"
                            + "CIsImVuZF9kYXRlIjoiMTk0OS0xMC0wOSIsImxvY2F0aW9uIjoiS09SIn1dfX19.S39"
                            + "hmL-5nxvdJnJ_PSHdfiP744dTcGzrUKsqcVQa1FomUU15Dr2A2gQMF7XWO2DCLrdM0b"
                            + "CtIS8mCSwI00nqQFqXGjZVGQTIyzQJDPHxV6jnYwwaXD1vzbxlsycWF2ZQ_5Wx6TIhn"
                            + "XiEkbn_rzW_VleVnHjLCRQwzmpwGGDsL_I"))
      })
  @ApiResponse(responseCode = "401", description = "Not authorized")
  @ApiResponse(responseCode = "404", description = "No service history found")
  @Operation(
      operationId = "getServiceHistory",
      summary = "Retrieve service history of authorized Veteran",
      security = {@SecurityRequirement(name = "bearer_token")})
  @Tag(name = "Veteran Verification")
  ServiceHistoryResponse serviceHistory();

  @GET
  @Path("status")
  @ApiResponse(
      responseCode = "200",
      description = "Confirmation status successfully retrieved",
      content = {
        @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = VeteranStatusVerificationResponse.class))
      })
  @ApiResponse(responseCode = "401", description = "Not authorized")
  @ApiResponse(
      responseCode = "502",
      description = "eMIS failed to respond or responded in a way we cannot handle")
  @Operation(
      operationId = "getVeteranStatus",
      summary = "Get confirmation about an individual's Veteran status according to the VA",
      security = {@SecurityRequirement(name = "bearer_token")})
  @Tag(name = "Veteran Verification")
  VeteranStatusVerificationResponse status();
}
