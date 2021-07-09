package gov.va.api.lighthouse.veteranverification.service.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ServiceEpisodeIdBuilderTest {
  @Test
  public void HappyPath() {
    // These values come from test user va.api.user+idme.001@gmail.com
    String uuid = "ccb134182ad14a82b53083e3cb2ed211";
    String beginDate = "1966-11-30";
    String endDate = "1972-03-30";

    String expected = "ca84d640-456b-59e1-a5cc-49bbdcb64d02";
    String actual = ServiceEpisodeIdBuilder.buildServiceEpisodeId(uuid, beginDate, endDate);
    Assertions.assertEquals(expected, actual);
  }
}
