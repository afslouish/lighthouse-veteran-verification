package gov.va.api.lighthouse.veteranverification.api;

import static gov.va.api.lighthouse.veteranverification.api.RoundTrip.assertRoundTrip;
import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.lighthouse.mpi.Mpi1305RequestAttributes;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

public class VeteranStatusRequestTest {
  VeteranStatusRequest attributes =
      VeteranStatusRequest.builder()
          .firstName("John")
          .lastName("Doe")
          .middleName("J")
          .ssn("111111111")
          .birthDate(LocalDate.now().toString())
          .gender("F")
          .build();

  @Test
  public void toMpi1305RequestAttributesTest() {
    assertThat(attributes.toMpi1305RequestAttributes())
        .isEqualTo(
            Mpi1305RequestAttributes.builder()
                .firstName("John")
                .lastName("Doe")
                .middleName("J")
                .ssn("111111111")
                .birthTime(LocalDate.now())
                .gender("F")
                .build());
  }

  @Test
  public void veteranStatusRequestAttributesTest() {
    assertRoundTrip(attributes);
  }
}
