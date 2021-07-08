package gov.va.api.lighthouse.veteranverification.service.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ServiceHistoryUtilsTest {
  // buildBranchOfServiceString tests
  @Test
  public void buildBranchOrServiceHappyPathAirForceNationalGuard() {
    String expected = "Air Force National Guard";
    String actual = ServiceHistoryUtils.buildBranchOfServiceString("F", "N");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void buildBranchOrServiceHappyPathAirForceNoCategory() {
    String expected = "Air Force";
    String actual = ServiceHistoryUtils.buildBranchOfServiceString("F", "A");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void buildBranchOrServiceHappyPathAirForceReserveQ() {
    String expected = "Air Force Reserve";
    String actual = ServiceHistoryUtils.buildBranchOfServiceString("F", "Q");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void buildBranchOrServiceHappyPathAirForceReserveV() {
    String expected = "Air Force Reserve";
    String actual = ServiceHistoryUtils.buildBranchOfServiceString("F", "V");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void buildBranchOrServiceHappyPathArmyNationalGuard() {
    String expected = "Army National Guard";
    String actual = ServiceHistoryUtils.buildBranchOfServiceString("A", "N");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void buildBranchOrServiceHappyPathArmyNoCategory() {
    String expected = "Army";
    String actual = ServiceHistoryUtils.buildBranchOfServiceString("A", "A");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void buildBranchOrServiceHappyPathArmyReserveQ() {
    String expected = "Army Reserve";
    String actual = ServiceHistoryUtils.buildBranchOfServiceString("A", "Q");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void buildBranchOrServiceHappyPathArmyReserveV() {
    String expected = "Army Reserve";
    String actual = ServiceHistoryUtils.buildBranchOfServiceString("A", "V");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void buildBranchOrServiceHappyPathCoastGuardNationalGuard() {
    String expected = "Coast Guard National Guard";
    String actual = ServiceHistoryUtils.buildBranchOfServiceString("C", "N");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void buildBranchOrServiceHappyPathCoastGuardNoCategory() {
    String expected = "Coast Guard";
    String actual = ServiceHistoryUtils.buildBranchOfServiceString("C", "A");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void buildBranchOrServiceHappyPathCoastGuardReserveQ() {
    String expected = "Coast Guard Reserve";
    String actual = ServiceHistoryUtils.buildBranchOfServiceString("C", "Q");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void buildBranchOrServiceHappyPathCoastGuardReserveV() {
    String expected = "Coast Guard Reserve";
    String actual = ServiceHistoryUtils.buildBranchOfServiceString("C", "V");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void buildBranchOrServiceHappyPathExcessWhiteSpace() {
    String expected = "Marine Corps Reserve";
    String actual = ServiceHistoryUtils.buildBranchOfServiceString(" m   ", "  Q ");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void buildBranchOrServiceHappyPathMarineNationalGuard() {
    String expected = "Marine Corps National Guard";
    String actual = ServiceHistoryUtils.buildBranchOfServiceString("M", "N");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void buildBranchOrServiceHappyPathMarineNoCategory() {
    String expected = "Marine Corps";
    String actual = ServiceHistoryUtils.buildBranchOfServiceString("M", "A");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void buildBranchOrServiceHappyPathMarineReserveQ() {
    String expected = "Marine Corps Reserve";
    String actual = ServiceHistoryUtils.buildBranchOfServiceString("M", "Q");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void buildBranchOrServiceHappyPathMarineReserveV() {
    String expected = "Marine Corps Reserve";
    String actual = ServiceHistoryUtils.buildBranchOfServiceString("M", "V");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void buildBranchOrServiceHappyPathNoaaNationalGuard() {
    String expected = "NOAA National Guard";
    String actual = ServiceHistoryUtils.buildBranchOfServiceString("O", "N");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void buildBranchOrServiceHappyPathNoaaNoCategory() {
    String expected = "NOAA";
    String actual = ServiceHistoryUtils.buildBranchOfServiceString("O", "A");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void buildBranchOrServiceHappyPathNoaaReserveQ() {
    String expected = "NOAA Reserve";
    String actual = ServiceHistoryUtils.buildBranchOfServiceString("O", "Q");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void buildBranchOrServiceHappyPathNoaaReserveV() {
    String expected = "NOAA Reserve";
    String actual = ServiceHistoryUtils.buildBranchOfServiceString("O", "V");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void buildBranchOrServiceHappyPathOtherBranchOfService() {
    Assertions.assertThrows(
        Exception.class,
        () -> {
          ServiceHistoryUtils.buildBranchOfServiceString("P", "V");
        });
  }

  @Test
  public void buildBranchOrServiceHappyPathUsphsNationalGuard() {
    String expected = "Public Health Service National Guard";
    String actual = ServiceHistoryUtils.buildBranchOfServiceString("H", "N");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void buildBranchOrServiceHappyPathUsphsNoCategory() {
    String expected = "Public Health Service";
    String actual = ServiceHistoryUtils.buildBranchOfServiceString("H", "A");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void buildBranchOrServiceHappyPathUsphsReserveQ() {
    String expected = "Public Health Service Reserve";
    String actual = ServiceHistoryUtils.buildBranchOfServiceString("H", "Q");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void buildBranchOrServiceHappyPathUsphsReserveV() {
    String expected = "Public Health Service Reserve";
    String actual = ServiceHistoryUtils.buildBranchOfServiceString("H", "V");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void buildPayGradeStingPayGradeCodeIsEmpty() {
    String expected = "unknown";
    String actual = ServiceHistoryUtils.buildPayGradeString("ME", "");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void buildPayGradeStingPayGradeCodeIsNull() {
    String expected = "unknown";
    String actual = ServiceHistoryUtils.buildPayGradeString("ME", null);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void buildPayGradeStingPayPlanCodeIsEmpty() {
    String expected = "unknown";
    String actual = ServiceHistoryUtils.buildPayGradeString("", "05");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void buildPayGradeStingPayPlanCodeIsNull() {
    String expected = "unknown";
    String actual = ServiceHistoryUtils.buildPayGradeString(null, "05");
    Assertions.assertEquals(expected, actual);
  }

  // buildPayGradeString tests
  @Test
  public void buildPayGradeStringHappyPath() {
    String expected = "E05";
    String actual = ServiceHistoryUtils.buildPayGradeString("ME", "05");
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void buildPayGradeStringHappyPathExtraSpace() {
    String expected = "E05";
    String actual = ServiceHistoryUtils.buildPayGradeString("   ME ", "  05   ");
    Assertions.assertEquals(expected, actual);
  }
}
