package gov.va.api.lighthouse.veteranverification.api;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;

import gov.va.api.lighthouse.veteranverification.api.v0.Deployment;
import gov.va.api.lighthouse.veteranverification.api.v0.ServiceHistoryResponse;
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
    ServiceHistoryResponse.ServiceHistoryAttributes serviceHistoryAttributes =
        TestUtils.makeServiceHistoryAttributes();
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          serviceHistoryAttributes.branchOfService(null);
        });
  }

  @Test
  public void DeploymentsIsNonNullable() {
    ServiceHistoryResponse.ServiceHistoryAttributes serviceHistoryAttributes =
        TestUtils.makeServiceHistoryAttributes();
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          serviceHistoryAttributes.deployments(null);
        });
  }

  @Test
  public void DischargeStatusNullable() {
    ServiceHistoryResponse.ServiceHistoryAttributes serviceHistoryAttributes =
        TestUtils.makeServiceHistoryAttributes();
    serviceHistoryAttributes.dischargeStatus(null);
  }

  @Test
  public void EndDateIsNullable() {
    ServiceHistoryResponse.ServiceHistoryAttributes serviceHistoryAttributes =
        TestUtils.makeServiceHistoryAttributes();
    serviceHistoryAttributes.endDate(null);
  }

  @Test
  public void FirstNameIsNonNullable() {
    ServiceHistoryResponse.ServiceHistoryAttributes serviceHistoryAttributes =
        TestUtils.makeServiceHistoryAttributes();
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          serviceHistoryAttributes.firstName(null);
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
    ServiceHistoryResponse.ServiceHistoryAttributes serviceHistoryAttributes =
        TestUtils.makeServiceHistoryAttributes();

    Assertions.assertEquals(serviceHistoryAttributes.firstName(), "John");
    Assertions.assertEquals(serviceHistoryAttributes.lastName(), "Doe");
    Assertions.assertEquals(serviceHistoryAttributes.branchOfService(), "BranchOfService");
    Assertions.assertEquals(
        serviceHistoryAttributes.startDate().toString(), startCalendar.getTime().toString());
    Assertions.assertEquals(
        serviceHistoryAttributes.endDate().toString(), endCalendar.getTime().toString());
    Assertions.assertEquals(serviceHistoryAttributes.payGrade(), "PayGrade");
    Assertions.assertEquals(
        serviceHistoryAttributes.dischargeStatus(),
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.HONORABLE);
    Assertions.assertEquals(
        serviceHistoryAttributes.separationReason(),
        ServiceHistoryResponse.ServiceHistoryAttributes.SeparationReason.BUFFER);
    assertThat(
        serviceHistoryAttributes.deployments().stream().findFirst(),
        samePropertyValuesAs(Arrays.stream(deployments).findFirst()));
  }

  @Test
  public void LastNameIsNonNullable() {
    ServiceHistoryResponse.ServiceHistoryAttributes serviceHistoryAttributes =
        TestUtils.makeServiceHistoryAttributes();
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          serviceHistoryAttributes.lastName(null);
        });
  }

  @Test
  public void PayGradeIsNonNullable() {
    ServiceHistoryResponse.ServiceHistoryAttributes serviceHistoryAttributes =
        TestUtils.makeServiceHistoryAttributes();
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          serviceHistoryAttributes.payGrade(null);
        });
  }

  @Test
  public void SeparationReasonIsNonNullable() {
    ServiceHistoryResponse.ServiceHistoryAttributes serviceHistoryAttributes =
        TestUtils.makeServiceHistoryAttributes();
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          serviceHistoryAttributes.separationReason(null);
        });
  }

  @Test
  public void StartDateIsNullable() {
    ServiceHistoryResponse.ServiceHistoryAttributes serviceHistoryAttributes =
        TestUtils.makeServiceHistoryAttributes();
    serviceHistoryAttributes.startDate(null);
  }
}
