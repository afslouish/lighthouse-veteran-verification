package gov.va.api.lighthouse.veteranverification.api;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Builder
@Accessors(fluent = false)
@Schema(
    name = "ServiceHistoryAttributes",
    type = "Object",
    description = "Attributes for veteran verification service_history endpoint")
@SuppressFBWarnings({"EI_EXPOSE_REP2", "EI_EXPOSE_REP"})
public class ServiceHistoryAttributes implements Attributes {
  @Schema(name = "first_name", description = "first name", example = "John")
  String firstName;

  @Schema(name = "last_name", description = "last name", example = "Doe")
  String lastName;

  @Schema(
      name = "branch_of_service",
      description = "military branch of service",
      example = "Air Force")
  BranchOfService branchOfService;

  @Schema(name = "start_date", description = "military episode start date", example = "2000-01-20")
  Date startDate;

  @Schema(name = "end_date", description = "military episode end date", example = "2001-01-20")
  Date endDate;

  @Schema(implementation = PayGrade.class)
  PayGrade payGrade;

  @Schema(
      name = "discharge_status",
      description = "discharge status for military episode",
      example = "honorable")
  DischargeStatus dischargeStatus;

  @Schema(
      name = "separation_reason",
      description = "separation reason for military episode",
      example = "SUFFICIENT SERVICE FOR RETIREMENT")
  SeparationReason separationReason;

  @ArraySchema(schema = @Schema(implementation = Deployment.class))
  List<Deployment> deployments;

  enum DischargeStatus {}

  enum SeparationReason {}
}
