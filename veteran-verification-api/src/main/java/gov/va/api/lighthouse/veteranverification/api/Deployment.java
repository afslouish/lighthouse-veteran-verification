package gov.va.api.lighthouse.veteranverification.api;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Builder
@Accessors(fluent = false)
@Schema(type = "Object", description = "military episode deployment")
@SuppressFBWarnings({"EI_EXPOSE_REP2", "EI_EXPOSE_REP"})
public class Deployment {

  @Schema(
      name = "start_date",
      description = "deployment start date",
      example = "2000-01-20",
      required = true,
      nullable = true)
  Date startDate;

  @Schema(
      name = "end_date",
      description = "deployment end date",
      example = "2001-01-20",
      required = true,
      nullable = true)
  Date endDate;

  @Schema(
      name = "location",
      description = "deployment location",
      example = "AFG",
      required = true,
      nullable = true)
  Location location;

  enum Location {}
}
