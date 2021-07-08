package gov.va.api.lighthouse.veteranverification.service.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ServiceEpisodeIdBuilderTest {
  @Test
  public void HappyPath() {
    // These values come from the vets-api spec tests.
    String uuid = "b2fab2b5-6af0-45e1-a9e2-394347af91ef";
    String beginDate = "2002-02-02";
    String endDate = "2008-12-01";

    String expected = "78b4b3f1-da08-5e0b-b3a1-88ccf85822a2";
    String actual = ServiceEpisodeIdBuilder.buildServiceEpisodeId(uuid, beginDate, endDate);
    Assertions.assertEquals(expected, actual);
  }
}
