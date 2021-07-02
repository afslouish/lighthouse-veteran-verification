package gov.va.api.lighthouse.veteranverification.service.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class BranchOfServiceStringBuilder {
  /**
   * Converts hcaServiceBranch and personnellCategory from emis call into appropriate
   * BranchOfService String.
   *
   * @param hcaServiceBranch hca_branch_of_service from emis response.
   * @param personnelCategory personnel_category_type_code from emis response.
   * @return String BranchOfService
   */
  public String buildBranchOfServiceString(String hcaServiceBranch, String personnelCategory) {
    String branch;

    switch (StringUtils.normalizeSpace(hcaServiceBranch)) {
      case "noaa":
        branch = hcaServiceBranch.toUpperCase();
        break;
      case "usphs":
        branch = "Public Health Service";
        break;
      default:
        branch = toTitleCase(hcaServiceBranch.toLowerCase());
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
