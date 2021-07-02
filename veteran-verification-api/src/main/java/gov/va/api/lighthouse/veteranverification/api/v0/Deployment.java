package gov.va.api.lighthouse.veteranverification.api.v0;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(fluent = false)
@Schema(type = "Object", description = "military episode deployment")
@SuppressFBWarnings({"EI_EXPOSE_REP2", "EI_EXPOSE_REP"})
public class Deployment {

  @Schema(
      name = "start_date",
      description = "beginning of deployment (YYYY-mm-dd)",
      example = "1948-10-10",
      required = true,
      nullable = true)
  LocalDate startDate;

  @Schema(
      name = "end_date",
      description = "end of deployment (YYYY-mm-dd)",
      example = "1949-10-09",
      required = true,
      nullable = true)
  LocalDate endDate;

  @Schema(
      name = "location",
      description = "Three letter ISO country code of deployment location",
      example = "KOR",
      required = true,
      nullable = true)
  String location;
}
