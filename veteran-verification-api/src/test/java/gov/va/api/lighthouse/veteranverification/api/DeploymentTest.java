package gov.va.api.lighthouse.veteranverification.api;

import static gov.va.api.lighthouse.veteranverification.api.RoundTrip.assertRoundTrip;

import gov.va.api.lighthouse.veteranverification.api.v0.Deployment;
import org.junit.jupiter.api.Test;

public class DeploymentTest {
  @Test
  public void allFieldsAreNullable() {
    Deployment deployment =
        Deployment.builder().startDate(null).endDate(null).location(null).build();
    assertRoundTrip(deployment);
  }

  @Test
  public void happyPath() {
    Deployment deployment = TestUtils.makeDeployment();
    assertRoundTrip(deployment);
  }
}
