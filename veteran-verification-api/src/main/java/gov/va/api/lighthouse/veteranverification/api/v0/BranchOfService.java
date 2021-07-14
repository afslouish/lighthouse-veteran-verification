package gov.va.api.lighthouse.veteranverification.api.v0;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Builder;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

@Builder
public class BranchOfService {
  private String branchOfService;
  private String personnelCategory;

  @Override
  @JsonValue
  @SneakyThrows
  public String toString() {
    String branch;
    switch (StringUtils.normalizeSpace(branchOfService.toUpperCase())) {
      case "O":
        branch = "NOAA";
        break;
      case "H":
        branch = "Public Health Service";
        break;
      case "A":
        branch = "Army";
        break;
      case "C":
        branch = "Coast Guard";
        break;
      case "F":
        branch = "Air Force";
        break;
      case "M":
        branch = "Marine Corps";
        break;
      case "N":
        branch = "Navy";
        break;
      default:
        throw new Exception("Invalid Branch Of Service");
    }
    String category;
    switch (StringUtils.normalizeSpace(personnelCategory)) {
      case "N":
        category = "National Guard";
        break;
      case "V":
      case "Q":
        category = "Reserve";
        break;
      default:
        category = "";
    }
    return StringUtils.normalizeSpace(String.format("%s %s", branch, category));
  }
}
