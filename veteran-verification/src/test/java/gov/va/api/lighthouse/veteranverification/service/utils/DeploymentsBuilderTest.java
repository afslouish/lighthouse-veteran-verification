package gov.va.api.lighthouse.veteranverification.service.utils;

import gov.va.api.lighthouse.veteranverification.api.v0.Deployment;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DeploymentsBuilderTest {
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
  public void HappyPath() {
    List<Deployment> deploymentList =
        DeploymentsBuilder.buildDeployments(
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
  public void HappyPathEmptyList() {
    List<Deployment> deploymentList =
        DeploymentsBuilder.buildDeployments(
            new ArrayList(), LocalDate.of(2001, 2, 1), LocalDate.of(2006, 2, 1));
    Assertions.assertEquals(deploymentList.size(), 0);
  }

  @Test
  public void HappyPathNullEndDate() {
    List<Deployment> deploymentList =
        DeploymentsBuilder.buildDeployments(
            Arrays.stream(deployments).toList(), LocalDate.of(2001, 2, 1), null);
    Assertions.assertEquals(deploymentList.size(), 3);
    Assertions.assertTrue(deploymentList.get(0).endDate().equals(LocalDate.of(2003, 1, 1)));
    Assertions.assertTrue(deploymentList.get(1).startDate().equals(LocalDate.of(2004, 1, 1)));
    Assertions.assertTrue(deploymentList.get(1).endDate().equals(LocalDate.of(2005, 1, 1)));
  }

  @Test
  public void HappyPathNullList() {
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          List<Deployment> deploymentList =
              DeploymentsBuilder.buildDeployments(
                  null, LocalDate.of(2001, 2, 1), LocalDate.of(2006, 2, 1));
        });
  }

  @Test
  public void NullEpisodeEndDate() {
    Deployment[] deploymentsLocal = deployments.clone();
    deploymentsLocal[1].endDate(null);
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          List<Deployment> deploymentList =
              DeploymentsBuilder.buildDeployments(
                  Arrays.stream(deploymentsLocal).toList(),
                  LocalDate.of(2001, 2, 1),
                  LocalDate.of(2006, 2, 1));
        });
  }

  @Test
  public void NullEpisodeStartDate() {
    Deployment[] deploymentsLocal = deployments.clone();
    deploymentsLocal[1].startDate(null);
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          List<Deployment> deploymentList =
              DeploymentsBuilder.buildDeployments(
                  Arrays.stream(deploymentsLocal).toList(),
                  LocalDate.of(2001, 2, 1),
                  LocalDate.of(2006, 2, 1));
        });
  }

  @Test
  public void NullStartDate() {
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          DeploymentsBuilder.buildDeployments(
              Arrays.stream(deployments).toList(), null, LocalDate.of(2006, 2, 1));
        });
  }
}
