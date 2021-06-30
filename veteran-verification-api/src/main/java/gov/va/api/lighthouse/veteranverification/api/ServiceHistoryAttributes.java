package gov.va.api.lighthouse.veteranverification.api;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(fluent = false)
@Schema(
    name = "ServiceHistoryAttributes",
    type = "Object",
    description = "Attributes for veteran verification service_history endpoint")
@SuppressFBWarnings({"EI_EXPOSE_REP2", "EI_EXPOSE_REP"})
public class ServiceHistoryAttributes implements Attributes {
  @NonNull
  @Schema(name = "first_name", description = "first name", example = "John", required = true)
  String firstName;

  @NonNull
  @Schema(name = "last_name", description = "last name", example = "Doe", required = true)
  String lastName;

  @NonNull
  @Schema(
      name = "branch_of_service",
      description = "military branch of service",
      example = "Air Force",
      required = true,
      type = "string")
  @ApiModelProperty(dataType = "java.lang.String")
  BranchOfService branchOfService;

  @Schema(
      name = "start_date",
      description = "military episode start date",
      example = "2000-01-20",
      required = true,
      nullable = true)
  Date startDate;

  @Schema(
      name = "end_date",
      description = "military episode end date",
      example = "2001-01-20",
      required = true,
      nullable = true)
  Date endDate;

  @NonNull
  @Schema(
      type = "string",
      description = "pay grade for military service episode",
      example = "E06",
      required = true)
  @ApiModelProperty(dataType = "java.lang.String")
  PayGrade payGrade;

  @Schema(
      name = "discharge_status",
      description = "discharge status for military episode",
      example = "honorable",
      nullable = true,
      required = true)
  DischargeStatus dischargeStatus;

  @NonNull
  @Schema(
      name = "separation_reason",
      description = "separation reason for military episode",
      example = "SUFFICIENT SERVICE FOR RETIREMENT",
      required = true)
  SeparationReason separationReason;

  @NonNull
  @ArraySchema(schema = @Schema(implementation = Deployment.class))
          @Builder.Default
  List<Deployment> deployments = new ArrayList<>();

  enum DischargeStatus {
    HONORABLE
  }

  enum SeparationReason {
    BUFFER
  }
}
