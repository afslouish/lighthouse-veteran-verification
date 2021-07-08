package gov.va.api.lighthouse.veteranverification.service.utils;

import gov.va.api.lighthouse.veteranverification.api.v0.Deployment;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ServiceHistoryUtilsTest {
  private Deployment[] deployments = {
    Deployment.builder()
        .location("AFG")
        .startDate(LocalDate.of(2000, 1, 1))
        .endDate(LocalDate.of(2001, 1, 1))
        .build(),
    Deployment.builder()
        .location("AFG")
        .startDate(LocalDate.of(2002, 1, 1))
        .endDate(LocalDate.of(2003, 1, 1))
        .build(),
    Deployment.builder()
        .location("AFG")
        .startDate(LocalDate.of(2004, 1, 1))
        .endDate(LocalDate.of(2005, 1, 1))
        .build(),
    Deployment.builder()
        .location("AFG")
        .startDate(LocalDate.of(2006, 1, 1))
        .endDate(LocalDate.of(2007, 1, 1))
        .build()
  };

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
  public void buildDeploymentsHappyPath() {
    List<Deployment> deploymentList =
        ServiceHistoryUtils.buildDeployments(
            Arrays.stream(deployments).toList(),
            LocalDate.of(2001, 2, 1),
            LocalDate.of(2006, 2, 1));
    Assertions.assertEquals(deploymentList.size(), 2);
    Assertions.assertTrue(deploymentList.get(0).startDate().equals(LocalDate.of(2002, 1, 1)));
    Assertions.assertTrue(deploymentList.get(0).endDate().equals(LocalDate.of(2003, 1, 1)));
    Assertions.assertTrue(deploymentList.get(1).startDate().equals(LocalDate.of(2004, 1, 1)));
    Assertions.assertTrue(deploymentList.get(1).endDate().equals(LocalDate.of(2005, 1, 1)));
  }

  @Test
  public void buildDeploymentsHappyPathEmptyList() {
    List<Deployment> deploymentList =
        ServiceHistoryUtils.buildDeployments(
            new ArrayList(), LocalDate.of(2001, 2, 1), LocalDate.of(2006, 2, 1));
    Assertions.assertEquals(deploymentList.size(), 0);
  }

  @Test
  public void buildDeploymentsHappyPathbuildDeploymentsNullEndDate() {
    List<Deployment> deploymentList =
        ServiceHistoryUtils.buildDeployments(
            Arrays.stream(deployments).toList(), LocalDate.of(2001, 2, 1), null);
    Assertions.assertEquals(deploymentList.size(), 3);
    Assertions.assertTrue(deploymentList.get(0).endDate().equals(LocalDate.of(2003, 1, 1)));
    Assertions.assertTrue(deploymentList.get(1).startDate().equals(LocalDate.of(2004, 1, 1)));
    Assertions.assertTrue(deploymentList.get(1).endDate().equals(LocalDate.of(2005, 1, 1)));
  }

  @Test
  public void buildDeploymentsHappyPathbuildDeploymentsNullList() {
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          List<Deployment> deploymentList =
              ServiceHistoryUtils.buildDeployments(
                  null, LocalDate.of(2001, 2, 1), LocalDate.of(2006, 2, 1));
        });
  }

  @Test
  public void buildDeploymentsNullEpisodeEndDate() {
    Deployment[] deploymentsLocal = deployments.clone();
    deploymentsLocal[1].endDate(null);
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          List<Deployment> deploymentList =
              ServiceHistoryUtils.buildDeployments(
                  Arrays.stream(deploymentsLocal).toList(),
                  LocalDate.of(2001, 2, 1),
                  LocalDate.of(2006, 2, 1));
        });
  }

  @Test
  public void buildDeploymentsNullEpisodeStartDate() {
    Deployment[] deploymentsLocal = deployments.clone();
    deploymentsLocal[1].startDate(null);
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          List<Deployment> deploymentList =
              ServiceHistoryUtils.buildDeployments(
                  Arrays.stream(deploymentsLocal).toList(),
                  LocalDate.of(2001, 2, 1),
                  LocalDate.of(2006, 2, 1));
        });
  }

  @Test
  public void buildDeploymentsNullStartDate() {
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          ServiceHistoryUtils.buildDeployments(
              Arrays.stream(deployments).toList(), null, LocalDate.of(2006, 2, 1));
        });
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
