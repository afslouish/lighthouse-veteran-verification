package gov.va.api.lighthouse.veteranverification.api;

import gov.va.api.lighthouse.veteranverification.api.v0.BranchOfService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BranchOfServiceTest {
  @Test
  public void happyPathAirForceNationalGuard() {
    String expected = "Air Force National Guard";
    String actual =
        BranchOfService.builder().branchOfService("F").personnelCategory("N").build().toString();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathAirForceNoCategory() {
    String expected = "Air Force";
    String actual =
        BranchOfService.builder().branchOfService("F").personnelCategory("A").build().toString();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathAirForceReserveQ() {
    String expected = "Air Force Reserve";
    String actual =
        BranchOfService.builder().branchOfService("F").personnelCategory("Q").build().toString();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathAirForceReserveV() {
    String expected = "Air Force Reserve";
    String actual =
        BranchOfService.builder().branchOfService("F").personnelCategory("V").build().toString();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathArmyNationalGuard() {
    String expected = "Army National Guard";
    String actual =
        BranchOfService.builder().branchOfService("A").personnelCategory("N").build().toString();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathArmyNoCategory() {
    String expected = "Army";
    String actual =
        BranchOfService.builder().branchOfService("A").personnelCategory("A").build().toString();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathArmyReserveQ() {
    String expected = "Army Reserve";
    String actual =
        BranchOfService.builder().branchOfService("A").personnelCategory("Q").build().toString();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathArmyReserveV() {
    String expected = "Army Reserve";
    String actual =
        BranchOfService.builder().branchOfService("A").personnelCategory("V").build().toString();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathCoastGuardNationalGuard() {
    String expected = "Coast Guard National Guard";
    String actual =
        BranchOfService.builder().branchOfService("C").personnelCategory("N").build().toString();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathCoastGuardNoCategory() {
    String expected = "Coast Guard";
    String actual =
        BranchOfService.builder().branchOfService("C").personnelCategory("A").build().toString();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathCoastGuardReserveQ() {
    String expected = "Coast Guard Reserve";
    String actual =
        BranchOfService.builder().branchOfService("C").personnelCategory("Q").build().toString();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathCoastGuardReserveV() {
    String expected = "Coast Guard Reserve";
    String actual =
        BranchOfService.builder().branchOfService("C").personnelCategory("V").build().toString();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathExcessWhiteSpace() {
    String expected = "Marine Corps Reserve";
    String actual =
        BranchOfService.builder()
            .branchOfService(" m  ")
            .personnelCategory("  Q ")
            .build()
            .toString();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathMarineNationalGuard() {
    String expected = "Marine Corps National Guard";
    String actual =
        BranchOfService.builder().branchOfService("M").personnelCategory("N").build().toString();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathMarineNoCategory() {
    String expected = "Marine Corps";
    String actual =
        BranchOfService.builder().branchOfService("M").personnelCategory("A").build().toString();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathMarineReserveQ() {
    String expected = "Marine Corps Reserve";
    String actual =
        BranchOfService.builder().branchOfService("M").personnelCategory("Q").build().toString();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathMarineReserveV() {
    String expected = "Marine Corps Reserve";
    String actual =
        BranchOfService.builder().branchOfService("M").personnelCategory("V").build().toString();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathNoaaNationalGuard() {
    String expected = "NOAA National Guard";
    String actual =
        BranchOfService.builder().branchOfService("O").personnelCategory("N").build().toString();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathNoaaNoCategory() {
    String expected = "NOAA";
    String actual =
        BranchOfService.builder().branchOfService("O").personnelCategory("A").build().toString();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathNoaaReserveQ() {
    String expected = "NOAA Reserve";
    String actual =
        BranchOfService.builder().branchOfService("O").personnelCategory("Q").build().toString();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathNoaaReserveV() {
    String expected = "NOAA Reserve";
    String actual =
        BranchOfService.builder().branchOfService("O").personnelCategory("V").build().toString();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathUsphsNationalGuard() {
    String expected = "Public Health Service National Guard";
    String actual =
        BranchOfService.builder().branchOfService("H").personnelCategory("N").build().toString();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathUsphsNoCategory() {
    String expected = "Public Health Service";
    String actual =
        BranchOfService.builder().branchOfService("H").personnelCategory("A").build().toString();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathUsphsReserveQ() {
    String expected = "Public Health Service Reserve";
    String actual =
        BranchOfService.builder().branchOfService("H").personnelCategory("Q").build().toString();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void happyPathUsphsReserveV() {
    String expected = "Public Health Service Reserve";
    String actual =
        BranchOfService.builder().branchOfService("H").personnelCategory("V").build().toString();
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void otherBranchOfService() {
    Assertions.assertThrows(
        Exception.class,
        () -> {
          BranchOfService.builder().branchOfService("P").personnelCategory("V").build().toString();
        });
  }
}
