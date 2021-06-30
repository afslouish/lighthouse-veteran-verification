package gov.va.api.lighthouse.veteranverification.api;

import static gov.va.api.lighthouse.veteranverification.api.RoundTrip.assertRoundTrip;

import java.util.Calendar;
import org.junit.jupiter.api.Test;

public class DeploymentTest {
  @Test
  public void AllFieldsAreNullable() {
    Deployment deployment =
        Deployment.builder().startDate(null).endDate(null).location(null).build();
    assertRoundTrip(deployment);
  }

  @Test
  public void HappyPath() {
    Calendar startCalendar = Calendar.getInstance();
    startCalendar.set(2000, 0, 1);
    Calendar endCalendar = Calendar.getInstance();
    endCalendar.set(2001, 0, 1);
    Deployment deployment =
        Deployment.builder()
            .startDate(startCalendar.getTime())
            .endDate(endCalendar.getTime())
            .location(Deployment.Location.AFG)
            .build();
    assertRoundTrip(deployment);
  }
}
