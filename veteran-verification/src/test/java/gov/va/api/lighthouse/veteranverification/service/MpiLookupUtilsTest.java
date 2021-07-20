package gov.va.api.lighthouse.veteranverification.service;

import static gov.va.api.lighthouse.veteranverification.service.MpiLookupUtils.getSsn;
import static org.assertj.core.api.Assertions.assertThat;

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
  public void getFirstNameMpiResponseHasNoNames() {
    PRPAIN201306UV02 mpiResponse = TestUtils.createMpiResponse("mpi/mpi_profile_no_names.xml");
    String actual = MpiLookupUtils.getFirstName(mpiResponse);
    Assertions.assertNull(actual);
  }

  @Test
  public void getFirstNameMpiResponseHasNoSubject() {
    PRPAIN201306UV02 mpiResponse = TestUtils.createMpiResponse("mpi/profile_no_subject.xml");
    String actual = MpiLookupUtils.getFirstName(mpiResponse);
    Assertions.assertNull(actual);
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
  public void getLastNameMpiResponseHasNoNames() {
    PRPAIN201306UV02 mpiResponse = TestUtils.createMpiResponse("mpi/mpi_profile_no_names.xml");
    String actual = MpiLookupUtils.getLastName(mpiResponse);
    Assertions.assertNull(actual);
  }

  @Test
  public void getLastNameMpiResponseHasNoSubject() {
    PRPAIN201306UV02 mpiResponse = TestUtils.createMpiResponse("mpi/profile_no_subject.xml");
    String actual = MpiLookupUtils.getLastName(mpiResponse);
    Assertions.assertNull(actual);
  }

  @Test
  public void getLastNameMpiResponseIsNull() {
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          MpiLookupUtils.getLastName(null);
        });
  }

  @Test
  public void getSsnTest() {
    PRPAIN201306UV02 response =
        TestUtils.createMpiResponse("mpi/mpi_profile_icn_response_body.xml");
    assertThat(getSsn(response)).isEqualTo("796220828");
    PRPAIN201306UV02 mpiProfileMissingSsn =
        TestUtils.createMpiResponse("mpi/mpi_profile_missing_ssn.xml");
    assertThat(getSsn(mpiProfileMissingSsn)).isNull();
    assertThat(getSsn(null)).isNull();
  }
}
