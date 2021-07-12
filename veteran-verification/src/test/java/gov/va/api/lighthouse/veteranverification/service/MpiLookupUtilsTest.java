package gov.va.api.lighthouse.veteranverification.service;

import static gov.va.api.lighthouse.veteranverification.service.MpiLookupUtils.getSsn;
import static org.assertj.core.api.Assertions.assertThat;

import org.hl7.v3.PRPAIN201306UV02;
import org.junit.jupiter.api.Test;

public class MpiLookupUtilsTest {
  @Test
  public void getSsnTest() {
    PRPAIN201306UV02 response = TestUtils.createMpiResponse("mpi_profile_icn_response_body.xml");
    assertThat(getSsn(response)).isEqualTo("796220828");
    PRPAIN201306UV02 mpiProfileMissingSsn =
        TestUtils.createMpiResponse("mpi_profile_missing_ssn.xml");
    assertThat(getSsn(mpiProfileMissingSsn)).isNull();
    assertThat(getSsn(null)).isNull();
  }
}
