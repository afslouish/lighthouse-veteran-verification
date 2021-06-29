package gov.va.api.lighthouse.veteranverification.api;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;

@Schema(type = "Object", description = "military episode deployment")
public class Deployment {
  @Schema(name = "start_date", description = "deployment start date", example = "2000-01-20")
  Date startDate;

  @Schema(name = "end_date", description = "deployment end date", example = "2001-01-20")
  Date endDate;

  @Schema(name = "location", description = "deployment location", example = "AFG")
  Location location;

  enum Location {}
}
