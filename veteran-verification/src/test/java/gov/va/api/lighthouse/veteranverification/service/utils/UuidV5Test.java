package gov.va.api.lighthouse.veteranverification.service.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UuidV5Test {
  @Test
  public void happyPath() {
    // These values come from test user va.api.user+idme.001@gmail.com
    String string = "ccb134182ad14a82b53083e3cb2ed211-1966-11-30-1972-03-30";

    String expected = "ca84d640-456b-59e1-a5cc-49bbdcb64d02";
    String actual =
        UuidV5.nameUuidFromNamespaceAndString("gov.vets.service-history-episodes", string)
            .toString();
    Assertions.assertEquals(expected, actual);
  }
}
