package gov.va.api.lighthouse.veteranverification.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VeteranVerificationDataTest {
  @Test
  public void AttributesIsNonNullable() {
    VeteranVerificationData veteranVerificationData = TestUtils.makeVeteranVerificationData();
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          veteranVerificationData.setAttributes(null);
        });
  }

  @Test
  public void HappyPath() {
    VeteranVerificationData veteranVerificationData = TestUtils.makeVeteranVerificationData();
    Assertions.assertEquals(veteranVerificationData.id, "ID");
    Assertions.assertEquals(veteranVerificationData.type, "Mock");
    Assertions.assertNotNull(veteranVerificationData.attributes);
  }

  @Test
  public void IdIsNonNullable() {
    VeteranVerificationData veteranVerificationData = TestUtils.makeVeteranVerificationData();
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          veteranVerificationData.setId(null);
        });
  }

  @Test
  public void TypeIsNonNullable() {
    VeteranVerificationData veteranVerificationData = TestUtils.makeVeteranVerificationData();
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          veteranVerificationData.setType(null);
        });
  }
}
