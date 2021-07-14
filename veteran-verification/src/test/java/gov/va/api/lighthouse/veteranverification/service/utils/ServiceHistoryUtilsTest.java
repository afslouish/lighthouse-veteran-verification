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
          ServiceHistoryUtils.buildDeployments(
              null, LocalDate.of(2001, 2, 1), LocalDate.of(2006, 2, 1));
        });
  }

  @Test
  public void buildDeploymentsNullEpisodeEndDate() {
    Deployment[] deploymentsLocal = deployments.clone();
    deploymentsLocal[1].endDate(null);

    List<Deployment> deploymentList =
        ServiceHistoryUtils.buildDeployments(
            Arrays.stream(deploymentsLocal).toList(), LocalDate.of(2001, 2, 1), null);
    Assertions.assertEquals(2, deploymentList.size());

    Assertions.assertEquals("2004-01-01", deploymentList.get(0).startDate().toString());
    Assertions.assertEquals("2005-01-01", deploymentList.get(0).endDate().toString());
    Assertions.assertEquals("AFG", deploymentList.get(0).location());

    Assertions.assertEquals("2006-01-01", deploymentList.get(1).startDate().toString());
    Assertions.assertEquals("2007-01-01", deploymentList.get(1).endDate().toString());
    Assertions.assertEquals("AFG", deploymentList.get(1).location());
  }

  @Test
  public void buildDeploymentsNullEpisodeStartDate() {
    Deployment[] deploymentsLocal = deployments.clone();
    deploymentsLocal[1].startDate(null);

    List<Deployment> deploymentList =
        ServiceHistoryUtils.buildDeployments(
            Arrays.stream(deploymentsLocal).toList(), LocalDate.of(2001, 2, 1), null);
    Assertions.assertEquals(2, deploymentList.size());

    Assertions.assertEquals("2004-01-01", deploymentList.get(0).startDate().toString());
    Assertions.assertEquals("2005-01-01", deploymentList.get(0).endDate().toString());
    Assertions.assertEquals("AFG", deploymentList.get(0).location());

    Assertions.assertEquals("2006-01-01", deploymentList.get(1).startDate().toString());
    Assertions.assertEquals("2007-01-01", deploymentList.get(1).endDate().toString());
    Assertions.assertEquals("AFG", deploymentList.get(1).location());
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
}
