package gov.va.api.lighthouse.veteranverification.api;

import gov.va.api.lighthouse.veteranverification.api.v0.PayGrade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PayGradeTest {
  @Test
  public void PayGradeCodeIsEmpty() {
    String expected = "unknown";
    String actual = PayGrade.builder().payPlanCode("ME").payGradeCode("").build().toString();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void PayGradeCodeIsNull() {
    String expected = "unknown";
    String actual = PayGrade.builder().payPlanCode("ME").payGradeCode(null).build().toString();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void PayPlanCodeIsEmpty() {
    String expected = "unknown";
    String actual = PayGrade.builder().payPlanCode("").payGradeCode("05").build().toString();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void PayPlanCodeIsNull() {
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
