package gov.va.api.lighthouse.veteranverification.service.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

@UtilityClass
public class PayGradeStringBuilder {
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
