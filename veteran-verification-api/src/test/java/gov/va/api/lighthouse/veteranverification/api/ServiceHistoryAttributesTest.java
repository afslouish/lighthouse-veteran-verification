package gov.va.api.lighthouse.veteranverification.api;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;

import java.util.Arrays;
import java.util.Calendar;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ServiceHistoryAttributesTest {
  @Test
  public void BranchOfServiceIsNonNullable() {
    ServiceHistoryAttributes serviceHistoryAttributes = TestUtils.makeServiceHistoryAttributes();
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          serviceHistoryAttributes.setBranchOfService(null);
        });
  }

  @Test
  public void DeploymentsIsNonNullable() {
    ServiceHistoryAttributes serviceHistoryAttributes = TestUtils.makeServiceHistoryAttributes();
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          serviceHistoryAttributes.setDeployments(null);
        });
  }

  @Test
  public void DischargeStatusNullable() {
    ServiceHistoryAttributes serviceHistoryAttributes = TestUtils.makeServiceHistoryAttributes();
    serviceHistoryAttributes.setDischargeStatus(null);
  }

  @Test
  public void EndDateIsNullable() {
    ServiceHistoryAttributes serviceHistoryAttributes = TestUtils.makeServiceHistoryAttributes();
    serviceHistoryAttributes.setEndDate(null);
  }

  @Test
  public void FirstNameIsNonNullable() {
    ServiceHistoryAttributes serviceHistoryAttributes = TestUtils.makeServiceHistoryAttributes();
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          serviceHistoryAttributes.setFirstName(null);
        });
  }

  @Test
  public void HappyPath() {
    // Round trip does not work on objects who have objects as fields
    Calendar startCalendar = Calendar.getInstance();
    startCalendar.set(2000, 0, 1, 0, 0, 0);
    Calendar endCalendar = Calendar.getInstance();
    endCalendar.set(2001, 0, 1, 0, 0, 0);
    Deployment[] deployments = {TestUtils.makeDeployment()};
    ServiceHistoryAttributes serviceHistoryAttributes = TestUtils.makeServiceHistoryAttributes();
    Assertions.assertEquals(serviceHistoryAttributes.firstName, "John");
    Assertions.assertEquals(serviceHistoryAttributes.lastName, "Doe");
    assertThat(
        serviceHistoryAttributes.branchOfService,
        samePropertyValuesAs(TestUtils.makeBranchOfService()));
    Assertions.assertEquals(
        serviceHistoryAttributes.startDate.toString(), startCalendar.getTime().toString());
    Assertions.assertEquals(
        serviceHistoryAttributes.endDate.toString(), endCalendar.getTime().toString());
    assertThat(serviceHistoryAttributes.payGrade, samePropertyValuesAs(TestUtils.makePayGrade()));
    Assertions.assertEquals(
        serviceHistoryAttributes.dischargeStatus,
        ServiceHistoryAttributes.DischargeStatus.HONORABLE);
    Assertions.assertEquals(
        serviceHistoryAttributes.separationReason,
        ServiceHistoryAttributes.SeparationReason.BUFFER);
    assertThat(
        serviceHistoryAttributes.deployments.stream().findFirst(),
        samePropertyValuesAs(Arrays.stream(deployments).findFirst()));
  }

  @Test
  public void LastNameIsNonNullable() {
    ServiceHistoryAttributes serviceHistoryAttributes = TestUtils.makeServiceHistoryAttributes();
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          serviceHistoryAttributes.setLastName(null);
        });
  }

  @Test
  public void PayGradeIsNonNullable() {
    ServiceHistoryAttributes serviceHistoryAttributes = TestUtils.makeServiceHistoryAttributes();
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          serviceHistoryAttributes.setPayGrade(null);
        });
  }

  @Test
  public void SeparationReasonIsNonNullable() {
    ServiceHistoryAttributes serviceHistoryAttributes = TestUtils.makeServiceHistoryAttributes();
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          serviceHistoryAttributes.setSeparationReason(null);
        });
  }

  @Test
  public void StartDateIsNullable() {
    ServiceHistoryAttributes serviceHistoryAttributes = TestUtils.makeServiceHistoryAttributes();
    serviceHistoryAttributes.setStartDate(null);
  }
}
