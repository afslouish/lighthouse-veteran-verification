package gov.va.api.lighthouse.veteranverification.service;

import org.hl7.v3.PRPAIN201306UV02;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MpiLookupUtilsTest {

  @Test
  public void getFirstNameHappyPath() {
    PRPAIN201306UV02 mpiResponse = TestUtils.createMpiResponse("mpi/mpi_profile_response_body.xml");
    String actual = MpiLookupUtils.getFirstName(mpiResponse);
    String expected = "Alfredo";

    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void getFirstNameMpiResponseIsNull() {
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          MpiLookupUtils.getFirstName(null);
        });
  }

  @Test
  public void getLastNameHappyPath() {
    PRPAIN201306UV02 mpiResponse = TestUtils.createMpiResponse("mpi/mpi_profile_response_body.xml");
    String actual = MpiLookupUtils.getLastName(mpiResponse);
    String expected = "Armstrong";

    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void getLastNameMpiResponseIsNull() {
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          MpiLookupUtils.getLastName(null);
        });
  }
}
