package gov.va.api.lighthouse.veteranverification.api;

import gov.va.api.lighthouse.veteranverification.api.v1.ServiceHistoryResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DischargeStatusTest {
  @Test
  public void happyPathBadConduct() {
    ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus dischargeStatus =
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.codeToEnum("D");
    Assertions.assertEquals(
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.BAD_CONDUCT,
        dischargeStatus);
    Assertions.assertEquals("bad-conduct", dischargeStatus.serializer());
  }

  @Test
  public void happyPathDishonorable() {
    ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus dischargeStatus =
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.codeToEnum("F");
    Assertions.assertEquals(
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.DISHONORABLE,
        dischargeStatus);
    Assertions.assertEquals("dishonorable", dischargeStatus.serializer());
  }

  @Test
  public void happyPathDishonorableForVaPurposes() {
    ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus dischargeStatus =
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.codeToEnum("K");
    Assertions.assertEquals(
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus
            .DISHONORABLE_FOR_VA_PURPOSES,
        dischargeStatus);
    Assertions.assertEquals("dishonorable-for-va-purposes", dischargeStatus.serializer());
  }

  @Test
  public void happyPathGeneral() {
    ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus dischargeStatus =
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.codeToEnum("B");
    Assertions.assertEquals(
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.GENERAL, dischargeStatus);
    Assertions.assertEquals("general", dischargeStatus.serializer());
  }

  @Test
  public void happyPathHonorable() {
    ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus dischargeStatus =
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.codeToEnum("A");
    Assertions.assertEquals(
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.HONORABLE, dischargeStatus);
    Assertions.assertEquals("honorable", dischargeStatus.serializer());
  }

  @Test
  public void happyPathHonorableAbsenceOfNegativeReport() {
    ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus dischargeStatus =
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.codeToEnum("H");
    Assertions.assertEquals(
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus
            .HONORABLE_ABSENCE_OF_NEGATIVE_REPORT,
        dischargeStatus);
    Assertions.assertEquals("honorable-absence-of-negative-report", dischargeStatus.serializer());
  }

  @Test
  public void happyPathHonorableForVaPurposes() {
    ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus dischargeStatus =
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.codeToEnum("J");
    Assertions.assertEquals(
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.HONORABLE_FOR_VA_PURPOSES,
        dischargeStatus);
    Assertions.assertEquals("honorable-for-va-purposes", dischargeStatus.serializer());
  }

  @Test
  public void happyPathLowerCaseCode() {
    ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus dischargeStatus =
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.codeToEnum("k");
    Assertions.assertEquals(
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus
            .DISHONORABLE_FOR_VA_PURPOSES,
        dischargeStatus);
    Assertions.assertEquals("dishonorable-for-va-purposes", dischargeStatus.serializer());
  }

  @Test
  public void happyPathOtherThanHonorable() {
    ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus dischargeStatus =
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.codeToEnum("E");
    Assertions.assertEquals(
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.OTHER_THAN_HONORABLE,
        dischargeStatus);
    Assertions.assertEquals("other-than-honorable", dischargeStatus.serializer());
  }

  @Test
  public void happyPathUncharacterized() {
    ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus dischargeStatus =
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.codeToEnum("Y");
    Assertions.assertEquals(
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.UNCHARACTERIZED,
        dischargeStatus);
    Assertions.assertEquals("uncharacterized", dischargeStatus.serializer());
  }

  @Test
  public void happyPathUnknown() {
    ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus dischargeStatus =
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.codeToEnum("Z");
    Assertions.assertEquals(
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.UNKNOWN, dischargeStatus);
    Assertions.assertEquals("unknown", dischargeStatus.serializer());
  }
}
