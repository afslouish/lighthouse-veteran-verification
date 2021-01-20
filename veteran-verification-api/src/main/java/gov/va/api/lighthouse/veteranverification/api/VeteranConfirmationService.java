package gov.va.api.lighthouse.veteranverification.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.servers.ServerVariable;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@OpenAPIDefinition(
    info =
        @Info(
            version = "0.0.1",
            title = "Veteran Confirmation",
            description =
                "The Veteran Confirmation API allows you to confirm Veteran status for a given "
                    + "person. This can be useful for offering Veterans discounts or other "
                    + "benefits."
                    + "\n\nThe API will only return \"Confirmed\" or \"Not Confirmed\".\n"
                    + "\n## Quickstart Guide\n### 1. Get Access Credentials\nGet started by "
                    + "filling out the form on the "
                    + "[Apply for VA Lighthouse Developer Access](https://developer.va.gov/apply) "
                    + "page.\n\nAfter submitting a request, you will receive your credentials for "
                    + "using the API in the Development environment, which allows you to try it "
                    + "out with mock data before moving to the Production environment.\n\n"
                    + "### 2. Test the API\nIn the endpoint documentation below, we've provided a "
                    + "curl command builder for trying out the API before implementation with your "
                    + "app.\nUse [Test User](https://github.com/department-of-veterans-affairs/vets-api-clients/blob/master/test_accounts.md) "
                    + "attributes to populate the request body.\n\n### 3. Build your app\nThe base "
                    + "URI for the Veteran Confirmation API in the Sandbox environment is:\n\n"
                    + "https://sandbox-api.va.gov/services/veteran_confirmation/v0\n\nIn this "
                    + "environment, use attributes from the list of "
                    + "[Test Users](https://github.com/department-of-veterans-affairs/vets-api-clients/blob/master/test_accounts.md). "
                    + "Only Test Users can return a `\"confirmed\"` response.\n\n"
                    + "Check out some of our [sample apps](https://github.com/department-of-veterans-affairs/vets-api-clients). "
                    + "Please visit our VA Lighthouse [Support portal](https://developer.va.gov/support) "
                    + "should you need further assistance.\n\n### 4. Show us a quick demo and get "
                    + "access to the Production environment\nAfter building your app, we ask that "
                    + "you give us a quick demo before we set you up with production credentials. "
                    + "Please see the [Path to Production](https://developer.va.gov/go-live) page "
                    + "for more details.\n\n## Authorization\nThis API requires an API key in "
                    + "combination with identifiable information for the person being confirmed "
                    + "listed below. API requests are authorized through a symmetric API token "
                    + "provided in an HTTP header with name `apikey`. Including more information "
                    + "has a better chance of making a match and returning a Confirmed status.\n"
                    + "### Required information:\n* First Name\n* Last Name\n* Date of Birth\n* "
                    + "Social Security Number\n\n### Optional information:\n* Middle Name\n* "
                    + "Gender\n\n## Reference\n### Sandbox vs. Production Data\nAPIs accessed via "
                    + "the Sandbox environment are using the same underlying logic as VA’s "
                    + "production APIs; only the underlying data store is different.\n\n"
                    + "### Master Veteran Index (MVI)\nThe Master Veteran Index confirms a user's "
                    + "identity. In Production, several factors are considered to confirm "
                    + "identity. These include: a user’s first name, last name, date of birth "
                    + "and Social Security number. The MVI is mocked in the Sandbox environment. "
                    + "In this environment, the only factor used to confirm identity is the "
                    + "Social Security number.\n\n"
                    + "### Rate Limiting\nWe implemented basic rate limiting of 60 "
                    + "requests per minute. If you exceed this quota, your request will return a "
                    + "429 status code. You may petition for increased rate limits by emailing and "
                    + "requests will be decided on a case by case basis.\n\n"
                    + "### Raw Open API Spec\nhttps://api.va.gov/services/veteran_confirmation/docs/v0/api\n",
            termsOfService = "",
            contact = @Contact(name = "developer.va.gov")),
    tags =
        @Tag(
            name = "veteran_confirmation_status",
            description = "Veteran Confirmation - Veteran Status"),
    servers = {
      @Server(
          url = "https://sandbox-api.va.gov/services/veteran_confirmation/{version}",
          description = "VA.gov API sandbox environment",
          variables = {@ServerVariable(name = "version", defaultValue = "v0")}),
      @Server(
          url = "https://api.va.gov/services/veteran_confirmation/{version}",
          description = "VA.gov API production environment",
          variables = {@ServerVariable(name = "version", defaultValue = "v0")})
    })
@SecuritySchemes(
    value =
        @SecurityScheme(
            type = SecuritySchemeType.APIKEY,
            name = "apiKey",
            in = SecuritySchemeIn.HEADER))
@Path("/")
public interface VeteranConfirmationService {
  @POST
  @Path("status")
  @ApiResponse(
      responseCode = "200",
      description = "Confirmation status successfully retrieved",
      content = {
        @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = VeteranStatusConfirmation.class))
      })
  @ApiResponse(
      responseCode = "400",
      description = "Bad request - invalid or missing query parameters",
      content = {
        @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
      })
  @ApiResponse(
      responseCode = "401",
      description = "Missing API token",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = AuthorizationError.class)))
  @ApiResponse(
      responseCode = "403",
      description = "Invalid API token",
      content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = AuthorizationError.class)))
  @ApiResponse(
      responseCode = "503",
      description = "We encountered a temporary error. Check back in the future.")
  @Tag(name = "veteran_confirmation_status")
  @Operation(
      operationId = "getVeteranStatus",
      summary = "Get confirmation about an individual's Veteran status according to the VA",
      security = {@SecurityRequirement(name = "apiKey")})
  @RequestBody(
      required = true,
      content = {
        @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = VeteranStatusRequest.class))
      })
  VeteranStatusConfirmation status();
}
