package gov.va.api.lighthouse.veteranverification.api.v0;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;

@Builder
public class PayGrade {
  private String payPlanCode;
  private String payGradeCode;

  @Override
  @JsonValue
  public String toString() {
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
