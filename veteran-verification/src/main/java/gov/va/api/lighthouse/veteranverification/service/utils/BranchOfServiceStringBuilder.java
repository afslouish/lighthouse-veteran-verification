package gov.va.api.lighthouse.veteranverification.service.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class BranchOfServiceStringBuilder {
  /**
   * Converts hcaServiceBranch and personnellCategory from emis call into appropriate
   * BranchOfService String.
   *
   * @param branchOfService hca_branch_of_service from emis response.
   * @param personnelCategory personnel_category_type_code from emis response.
   * @return String BranchOfService
   */
  @SneakyThrows
  public String buildBranchOfServiceString(String branchOfService, String personnelCategory) {
    String branch = null;

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

  private String toTitleCase(String input) {
    StringBuilder titleCase = new StringBuilder(input.length());
    boolean nextTitleCase = true;

    for (char c : input.toCharArray()) {
      if (Character.isSpaceChar(c)) {
        nextTitleCase = true;
      } else if (nextTitleCase) {
        c = Character.toTitleCase(c);
        nextTitleCase = false;
      }

      titleCase.append(c);
    }

    return titleCase.toString();
  }
}
