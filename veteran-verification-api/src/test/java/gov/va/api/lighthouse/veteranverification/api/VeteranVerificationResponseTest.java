package gov.va.api.lighthouse.veteranverification.api;

import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Data
public class VeteranVerificationResponseTest {
  @Test
  public void DataIsNonNullable() {
    VeteranVerificationResponse veteranVerificationResponse =
        TestUtils.makeVeteranVerificationResponse();
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          veteranVerificationResponse.setData(null);
        });
  }

  @Test
  public void HappyPath() {
    VeteranVerificationResponse veteranVerificationResponse =
        TestUtils.makeVeteranVerificationResponse();
    Assertions.assertNotNull(veteranVerificationResponse.data);
  }
}
