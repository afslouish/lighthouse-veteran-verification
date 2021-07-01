package gov.va.api.lighthouse.veteranverification.api;

import gov.va.api.lighthouse.veteranverification.api.v0.ServiceHistoryResponse;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Data
public class VeteranVerificationResponseTest {
  @Test
  public void DataIsNonNullable() {
    ServiceHistoryResponse veteranVerificationResponse = TestUtils.makeServiceHistoryResponse();
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          veteranVerificationResponse.data(null);
        });
  }

  @Test
  public void HappyPath() {
    ServiceHistoryResponse serviceHistoryResponseResponse = TestUtils.makeServiceHistoryResponse();
    Assertions.assertNotNull(serviceHistoryResponseResponse.data());
  }
}
