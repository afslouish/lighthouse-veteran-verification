package gov.va.api.lighthouse.veteranverification.api.v0;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@Accessors(fluent = true)
@Schema(description = "verification service_history endpoint")
@NoArgsConstructor
@AllArgsConstructor
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
    @Schema(
        name = "first_name",
        description = "Veteran first name",
        example = "Abraham",
        required = true,
        nullable = true)
    String firstName;

    @Schema(
        name = "last_name",
        description = "Veteran last name",
        example = "Lincoln",
        required = true,
        nullable = true)
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
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    LocalDate startDate;

    @Schema(
        name = "end_date",
        description = "end date of a service history episode (YYYY-mm-dd)",
        example = "1950-05-10",
        required = true,
        nullable = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    LocalDate endDate;

    @NonNull
    @Schema(
        name = "pay_grade",
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

    public enum DischargeStatus {
      HONORABLE("A", "honorable"),
      GENERAL("B", "general"),
      BAD_CONDUCT("D", "bad-conduct"),
      OTHER_THAN_HONORABLE("E", "other-than-honorable"),
      DISHONORABLE("F", "dishonorable"),
      HONORABLE_ABSENCE_OF_NEGATIVE_REPORT("H", "honorable-absence-of-negative-report"),
      HONORABLE_FOR_VA_PURPOSES("J", "honorable-for-va-purposes"),
      DISHONORABLE_FOR_VA_PURPOSES("K", "dishonorable-for-va-purposes"),
      UNCHARACTERIZED("Y", "uncharacterized"),
      UNKNOWN("Z", "unknown");

      private final String description;
      private final String code;

      DischargeStatus(String code, String description) {
        this.code = code;
        this.description = description;
      }

      /**
       * Will return enum based on single letter code.
       *
       * @param statusCode Single letter code.
       * @return DischargeStatus Enum.
       */
      @SneakyThrows
      public static DischargeStatus codeToEnum(String statusCode) {
        EnumSet<DischargeStatus> enumSet = EnumSet.allOf(DischargeStatus.class);
        DischargeStatus matchingEnum = DischargeStatus.UNKNOWN;
        for (DischargeStatus dischargeStatus : enumSet) {
          if (statusCode.toUpperCase().strip().equals(dischargeStatus.code)) {
            matchingEnum = dischargeStatus;
          }
        }
        return matchingEnum;
      }

      @JsonValue
      public String serializer() {
        return this.description;
      }
    }
  }

  @Builder
  @Accessors(fluent = true)
  @Schema(description = "Service History for authorized Veteran")
  @Data
  @FieldDefaults(level = AccessLevel.PUBLIC)
  public static class ServiceHistoryEpisode {
    @NonNull
    @Schema(
        type = "string",
        description = "Service History Episode ID",
        example = "12312AASDf",
        required = true)
    String id;

    @Schema(example = "service-history-episodes", required = true)
    @NonNull
    @Builder.Default
    String type = "service-history-episodes";

    @Schema(implementation = ServiceHistoryAttributes.class, required = true)
    ServiceHistoryAttributes attributes;
  }
}
