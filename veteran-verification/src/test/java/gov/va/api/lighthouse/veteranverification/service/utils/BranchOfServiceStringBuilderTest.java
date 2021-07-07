package gov.va.api.lighthouse.veteranverification.service.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BranchOfServiceStringBuilderTest {
  @Test
  public void happyPathAirForceNationalGuard() {
    String expected = "Air Force National Guard";
    String actual = BranchOfServiceStringBuilder.buildBranchOfServiceString("F", "N");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathAirForceNoCategory() {
    String expected = "Air Force";
    String actual = BranchOfServiceStringBuilder.buildBranchOfServiceString("F", "A");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathAirForceReserveQ() {
    String expected = "Air Force Reserve";
    String actual = BranchOfServiceStringBuilder.buildBranchOfServiceString("F", "Q");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathAirForceReserveV() {
    String expected = "Air Force Reserve";
    String actual = BranchOfServiceStringBuilder.buildBranchOfServiceString("F", "V");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathArmyNationalGuard() {
    String expected = "Army National Guard";
    String actual = BranchOfServiceStringBuilder.buildBranchOfServiceString("A", "N");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathArmyNoCategory() {
    String expected = "Army";
    String actual = BranchOfServiceStringBuilder.buildBranchOfServiceString("A", "A");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathArmyReserveQ() {
    String expected = "Army Reserve";
    String actual = BranchOfServiceStringBuilder.buildBranchOfServiceString("A", "Q");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathArmyReserveV() {
    String expected = "Army Reserve";
    String actual = BranchOfServiceStringBuilder.buildBranchOfServiceString("A", "V");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathCoastGuardNationalGuard() {
    String expected = "Coast Guard National Guard";
    String actual = BranchOfServiceStringBuilder.buildBranchOfServiceString("C", "N");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathCoastGuardNoCategory() {
    String expected = "Coast Guard";
    String actual = BranchOfServiceStringBuilder.buildBranchOfServiceString("C", "A");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathCoastGuardReserveQ() {
    String expected = "Coast Guard Reserve";
    String actual = BranchOfServiceStringBuilder.buildBranchOfServiceString("C", "Q");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathCoastGuardReserveV() {
    String expected = "Coast Guard Reserve";
    String actual = BranchOfServiceStringBuilder.buildBranchOfServiceString("C", "V");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathExcessWhiteSpace() {
    String expected = "Marine Corps Reserve";
    String actual = BranchOfServiceStringBuilder.buildBranchOfServiceString(" m   ", "  Q ");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathMarineNationalGuard() {
    String expected = "Marine Corps National Guard";
    String actual = BranchOfServiceStringBuilder.buildBranchOfServiceString("M", "N");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathMarineNoCategory() {
    String expected = "Marine Corps";
    String actual = BranchOfServiceStringBuilder.buildBranchOfServiceString("M", "A");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathMarineReserveQ() {
    String expected = "Marine Corps Reserve";
    String actual = BranchOfServiceStringBuilder.buildBranchOfServiceString("M", "Q");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathMarineReserveV() {
    String expected = "Marine Corps Reserve";
    String actual = BranchOfServiceStringBuilder.buildBranchOfServiceString("M", "V");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathNoaaNationalGuard() {
    String expected = "NOAA National Guard";
    String actual = BranchOfServiceStringBuilder.buildBranchOfServiceString("O", "N");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathNoaaNoCategory() {
    String expected = "NOAA";
    String actual = BranchOfServiceStringBuilder.buildBranchOfServiceString("O", "A");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathNoaaReserveQ() {
    String expected = "NOAA Reserve";
    String actual = BranchOfServiceStringBuilder.buildBranchOfServiceString("O", "Q");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathNoaaReserveV() {
    String expected = "NOAA Reserve";
    String actual = BranchOfServiceStringBuilder.buildBranchOfServiceString("O", "V");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathOtherBranchOfService() {
    String expected = "Other";
    String actual = BranchOfServiceStringBuilder.buildBranchOfServiceString("P", "V");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathUsphsNationalGuard() {
    String expected = "Public Health Service National Guard";
    String actual = BranchOfServiceStringBuilder.buildBranchOfServiceString("H", "N");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathUsphsNoCategory() {
    String expected = "Public Health Service";
    String actual = BranchOfServiceStringBuilder.buildBranchOfServiceString("H", "A");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathUsphsReserveQ() {
    String expected = "Public Health Service Reserve";
    String actual = BranchOfServiceStringBuilder.buildBranchOfServiceString("H", "Q");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathUsphsReserveV() {
    String expected = "Public Health Service Reserve";
    String actual = BranchOfServiceStringBuilder.buildBranchOfServiceString("H", "V");
    Assertions.assertEquals(expected, actual);
  }
}
