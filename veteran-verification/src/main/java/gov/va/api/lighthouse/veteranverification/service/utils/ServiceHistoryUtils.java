package gov.va.api.lighthouse.veteranverification.service.utils;

import gov.va.api.lighthouse.veteranverification.api.v0.Deployment;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
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
   * Creates a list of deployments in range of the provided start and end dates.
   *
   * @param deployments Super list of deployments.
   * @param startDate Service episode startDate.
   * @param endDate Service episode endDate.
   * @return All deployments in range of start and end dates.
   */
  public List<Deployment> buildDeployments(
      List<Deployment> deployments, LocalDate startDate, LocalDate endDate) {
    return deployments.stream()
        .filter(
            deployment ->
                isBeforeOrEqualTo(startDate, deployment.startDate())
                    && (endDate == null || isBeforeOrEqualTo(deployment.endDate(), endDate)))
        .collect(Collectors.toList());
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

  private boolean isBeforeOrEqualTo(LocalDate dateOne, LocalDate dateTwo) {
    return dateOne.isBefore(dateTwo) || dateOne.isEqual(dateTwo);
  }
}
