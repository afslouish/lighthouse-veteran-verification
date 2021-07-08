package gov.va.api.lighthouse.veteranverification.service.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class ServiceHistoryUtils {
  /**
   * Converts branch of service code and personnellCategory from emis call into appropriate
   * BranchOfService String.
   *
   * @param branchOfService branch of service code from emis response.
   * @param personnelCategory personnel_category_type_code from emis response.
   * @return String BranchOfService.
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

  /**
   * Builds payGrade string from payPlanCode and payGradeCode.
   *
   * @param payPlanCode payPlanCode from emis response.
   * @param payGradeCode payGradeCode from emis response.
   * @return String payGrade
   */
  public String buildPayGradeString(String payPlanCode, String payGradeCode) {
    if (payPlanCode == null
        || payPlanCode.trim().isEmpty()
        || payGradeCode == null
        || payGradeCode.trim().isEmpty()) {
      return "unknown";
    }

    return StringUtils.normalizeSpace(
        String.format(
            "%s%s",
            StringUtils.normalizeSpace(payPlanCode).charAt(1),
            StringUtils.normalizeSpace(payGradeCode)));
  }
}
