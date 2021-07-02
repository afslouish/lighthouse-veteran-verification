package gov.va.api.lighthouse.veteranverification.service.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PayGradeStringBuilderTest {
  @Test
  public void HappyPath() {
    String expected = "E05";
    String actual = PayGradeStringBuilder.buildPayGradeString("ME", "05");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void HappyPathExtraSpace() {
    String expected = "E05";
    String actual = PayGradeStringBuilder.buildPayGradeString("   ME ", "  05   ");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void payGradeCodeIsEmpty() {
    String expected = "unknown";
    String actual = PayGradeStringBuilder.buildPayGradeString("ME", "");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void payGradeCodeIsNull() {
    String expected = "unknown";
    String actual = PayGradeStringBuilder.buildPayGradeString("ME", null);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void payPlanCodeIsEmpty() {
    String expected = "unknown";
    String actual = PayGradeStringBuilder.buildPayGradeString("", "05");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void payPlanCodeIsNull() {
    String expected = "unknown";
    String actual = PayGradeStringBuilder.buildPayGradeString(null, "05");
    Assertions.assertEquals(expected, actual);
  }
}
