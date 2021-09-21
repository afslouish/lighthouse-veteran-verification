package gov.va.api.lighthouse.veteranverification.api.v1;

import com.fasterxml.jackson.annotation.JsonFormat;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@Accessors(fluent = true)
@Schema(type = "Object", description = "Deployment during a service history episode")
@SuppressFBWarnings({"EI_EXPOSE_REP2", "EI_EXPOSE_REP"})
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PUBLIC)
public class Deployment {

  @Schema(
      name = "start_date",
      description = "beginning of deployment (YYYY-mm-dd)",
      example = "1948-10-10",
      required = true,
      nullable = true)
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  LocalDate startDate;

  @Schema(
      name = "end_date",
      description = "end of deployment (YYYY-mm-dd)",
      example = "1949-10-09",
      required = true,
      nullable = true)
  @JsonFormat(shape = JsonFormat.Shape.STRING)
  LocalDate endDate;

  @Schema(
      name = "location",
      description = "Three letter ISO country code of deployment location",
      example = "KOR",
      required = true,
      nullable = true)
  String location;
}
