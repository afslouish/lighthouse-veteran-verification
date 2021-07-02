package gov.va.api.lighthouse.veteranverification.service.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BranchOfServiceStringBuilderTest {
  @Test
  public void happyPathExcessWhiteSpace() {
    String expected = "Other Branch Reserve";
    String actual =
        BranchOfServiceStringBuilder.buildBranchOfServiceString(" other    branch   ", "  Q ");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathNoaaNationalGuard() {
    String expected = "NOAA National Guard";
    String actual = BranchOfServiceStringBuilder.buildBranchOfServiceString("noaa", "N");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathNoaaNoCategory() {
    String expected = "NOAA";
    String actual = BranchOfServiceStringBuilder.buildBranchOfServiceString("noaa", "A");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathNoaaReserveQ() {
    String expected = "NOAA Reserve";
    String actual = BranchOfServiceStringBuilder.buildBranchOfServiceString("noaa", "Q");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathNoaaReserveV() {
    String expected = "NOAA Reserve";
    String actual = BranchOfServiceStringBuilder.buildBranchOfServiceString("noaa", "V");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathOtherAllLowerCaseNoCategory() {
    String expected = "Other Branch";
    String actual = BranchOfServiceStringBuilder.buildBranchOfServiceString("other branch", "A");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathOtherAllUpperCaseNoCategory() {
    String expected = "Other Branch";
    String actual = BranchOfServiceStringBuilder.buildBranchOfServiceString("OTHER BRANCH", "A");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathOtherNationalGuard() {
    String expected = "Other Branch National Guard";
    String actual = BranchOfServiceStringBuilder.buildBranchOfServiceString("other branch", "N");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathOtherReserveQ() {
    String expected = "Other Branch Reserve";
    String actual = BranchOfServiceStringBuilder.buildBranchOfServiceString("other branch", "Q");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathOtherReserveV() {
    String expected = "Other Branch Reserve";
    String actual = BranchOfServiceStringBuilder.buildBranchOfServiceString("other branch", "V");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathUsphsNationalGuard() {
    String expected = "Public Health Service National Guard";
    String actual = BranchOfServiceStringBuilder.buildBranchOfServiceString("usphs", "N");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathUsphsNoCategory() {
    String expected = "Public Health Service";
    String actual = BranchOfServiceStringBuilder.buildBranchOfServiceString("usphs", "A");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathUsphsReserveQ() {
    String expected = "Public Health Service Reserve";
    String actual = BranchOfServiceStringBuilder.buildBranchOfServiceString("usphs", "Q");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathUsphsReserveV() {
    String expected = "Public Health Service Reserve";
    String actual = BranchOfServiceStringBuilder.buildBranchOfServiceString("usphs", "V");
    Assertions.assertEquals(expected, actual);
  }
}
