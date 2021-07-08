package gov.va.api.lighthouse.veteranverification.api.v0;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@Accessors(fluent = true)
@Schema(description = "verification service_history endpoint")
@FieldDefaults(level = AccessLevel.PUBLIC)
public class ServiceHistoryResponse {
  @NonNull
  @ArraySchema(schema = @Schema(implementation = ServiceHistoryEpisode.class))
  public List<ServiceHistoryEpisode> data;

  @Data
  @Builder
  @Accessors(fluent = true)
  @Schema(
      name = "ServiceHistoryAttributes",
      description = "Attributes for veteran verification service_history endpoint")
  @SuppressFBWarnings({"EI_EXPOSE_REP2", "EI_EXPOSE_REP"})
  @FieldDefaults(level = AccessLevel.PUBLIC)
  public static class ServiceHistoryAttributes {
    @NonNull
    @Schema(
        name = "first_name",
        description = "Veteran first name",
        example = "Abraham",
        required = true)
    String firstName;

    @NonNull
    @Schema(
        name = "last_name",
        description = "Veteran last name",
        example = "Lincoln",
        required = true)
    String lastName;

    @NonNull
    @Schema(
        name = "branch_of_service",
        description = "Branch of military including National Guard or Reserve status",
        example = "Air Force",
        required = true)
    String branchOfService;

    @Schema(
        name = "start_date",
        description = "start date of a service history episode (YYYY-mm-dd)",
        example = "1948-04-08",
        required = true,
        nullable = true)
    LocalDate startDate;

    @Schema(
        name = "end_date",
        description = "end date of a service history episode (YYYY-mm-dd)",
        example = "1950-05-10",
        required = true,
        nullable = true)
    LocalDate endDate;

    @NonNull
    @Schema(
        type = "string",
        description =
            "Defines the level of compensation for a position, normalized across military branches"
                + "\n\nPossible values include the concatenation of Pay Plan Code and Pay Grade "
                + "Code.\nPay Plan Code is represented as a two-character code, and can be one of "
                + "five values:\n"
                + "  - CC (Commissioned Corps)\n"
                + "  - MC (Cadet)\n"
                + "  - ME (Enlisted)\n"
                + "  - MO (Officer)\n"
                + "  - MW (Warrant Officer)\n"
                + "\n"
                + "Pay Grade Code is a value between 01 and 10.\n"
                + "Pay Plan Code is concatenated with Pay Grade Code to determine the full Pay "
                + "Grade value, with the leading character (M or C) stripped from Pay Plan Code.",
        example = "W01",
        required = true)
    @ApiModelProperty(dataType = "java.lang.String")
    String payGrade;

    @Schema(
        name = "discharge_status",
        description =
            "Character of discharge from service episode. Possible values are:\n"
                + "\n"
                + "Both \"honorable-for-va-purposes\" and \"dishonorable-for-va-purposes\" "
                + "represent a change in character of discharge based on an administrative "
                + "decision, for purposes of VA benefits administration. The original character "
                + "of discharge for other purposes was either \"bad-conduct\" or "
                + "\"other-than-honorable\". \"honorable-absence-of-negative-report\" represents "
                + "an unreported character of service that DoD classifies as honorable.",
        example = "honorable",
        allowableValues = {
          "honorable",
          "general",
          "bad-conduct",
          "other-than-honorable",
          "dishonorable",
          "honorable-absence-of-negative-report",
          "honorable-for-va-purposes",
          "dishonorable-for-va-purposes",
          "uncharacterized",
          "unknown"
        },
        nullable = true,
        required = true)
    DischargeStatus dischargeStatus;

    @NonNull
    @Schema(
        name = "separation_reason",
        description =
            "Additional text description for separation reason beyond discharge_status value",
        example = "SUFFICIENT SERVICE FOR RETIREMENT",
        required = true)
    String separationReason;

    @NonNull
    @ArraySchema(schema = @Schema(implementation = Deployment.class))
    @Builder.Default
    List<Deployment> deployments = new ArrayList<>();

    public enum DischargeStatus
        implements gov.va.api.lighthouse.veteranverification.api.v0.DischargeStatus {
      ThisWillBeRemoved;

      public static DischargeStatus codeToEnum(String statusCode) {
        return ThisWillBeRemoved;
      }
    }
  }

  @Accessors(fluent = true)
  @Schema(description = "Service History for authorized Veteran")
  @Data
  @EqualsAndHashCode(callSuper = true)
  @FieldDefaults(level = AccessLevel.PUBLIC)
  public static class ServiceHistoryEpisode extends AbstractVeteranVerificationData {
    @Schema(implementation = ServiceHistoryAttributes.class, required = true)
    ServiceHistoryAttributes attributes;

    @Builder
    public ServiceHistoryEpisode(ServiceHistoryAttributes attributes, String id) {
      super(id, "service-history-episodes");
      this.attributes = attributes;
    }
  }
}
