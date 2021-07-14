package gov.va.api.lighthouse.veteranverification.api;

import gov.va.api.lighthouse.veteranverification.api.v0.PayGrade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PayGradeTest {
  @Test
  public void payGradeCodeIsEmpty() {
    String expected = "unknown";
    String actual = PayGrade.builder().payPlanCode("ME").payGradeCode("").build().toString();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void payGradeCodeIsNull() {
    String expected = "unknown";
    String actual = PayGrade.builder().payPlanCode("ME").payGradeCode(null).build().toString();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void payPlanCodeIsEmpty() {
    String expected = "unknown";
    String actual = PayGrade.builder().payPlanCode("").payGradeCode("05").build().toString();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void payPlanCodeIsNull() {
    String expected = "unknown";
    String actual = PayGrade.builder().payPlanCode(null).payGradeCode("05").build().toString();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPath() {
    String expected = "E05";
    String actual = PayGrade.builder().payPlanCode("ME").payGradeCode("05").build().toString();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathExtraSpace() {
    String expected = "E05";
    String actual =
        PayGrade.builder().payPlanCode("   ME ").payGradeCode("  05   ").build().toString();
    Assertions.assertEquals(expected, actual);
  }
}
