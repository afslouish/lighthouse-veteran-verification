package gov.va.api.lighthouse.veteranverification.api;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;

import gov.va.api.lighthouse.veteranverification.api.v0.Deployment;
import gov.va.api.lighthouse.veteranverification.api.v0.ServiceHistoryResponse;
import java.time.LocalDate;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ServiceHistoryAttributesTest {
  @Test
  public void branchOfServiceIsNonNullable() {
    ServiceHistoryResponse.ServiceHistoryAttributes serviceHistoryAttributes =
        TestUtils.makeServiceHistoryAttributes();
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          serviceHistoryAttributes.branchOfService(null);
        });
  }

  @Test
  public void deploymentsIsNonNullable() {
    ServiceHistoryResponse.ServiceHistoryAttributes serviceHistoryAttributes =
        TestUtils.makeServiceHistoryAttributes();
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          serviceHistoryAttributes.deployments(null);
        });
  }

  @Test
  public void dischargeStatusNullable() {
    ServiceHistoryResponse.ServiceHistoryAttributes serviceHistoryAttributes =
        TestUtils.makeServiceHistoryAttributes();
    serviceHistoryAttributes.dischargeStatus(null);
  }

  @Test
  public void endDateIsNullable() {
    ServiceHistoryResponse.ServiceHistoryAttributes serviceHistoryAttributes =
        TestUtils.makeServiceHistoryAttributes();
    serviceHistoryAttributes.endDate(null);
  }

  @Test
  public void firstNameIsNonNullable() {
    ServiceHistoryResponse.ServiceHistoryAttributes serviceHistoryAttributes =
        TestUtils.makeServiceHistoryAttributes();
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          serviceHistoryAttributes.firstName(null);
        });
  }

  @Test
  public void happyPath() {
    Deployment[] deployments = {TestUtils.makeDeployment()};
    ServiceHistoryResponse.ServiceHistoryAttributes serviceHistoryAttributes =
        TestUtils.makeServiceHistoryAttributes();

    Assertions.assertEquals(serviceHistoryAttributes.firstName(), "John");
    Assertions.assertEquals(serviceHistoryAttributes.lastName(), "Doe");
    Assertions.assertEquals(serviceHistoryAttributes.branchOfService().toString(), "Army");
    Assertions.assertEquals(
        serviceHistoryAttributes.startDate().toString(), LocalDate.of(2000, 1, 1).toString());
    Assertions.assertEquals(
        serviceHistoryAttributes.endDate().toString(), LocalDate.of(2001, 1, 1).toString());
    Assertions.assertEquals(serviceHistoryAttributes.payGrade().toString(), "E05");
    Assertions.assertEquals(
        serviceHistoryAttributes.dischargeStatus(),
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.HONORABLE);
    Assertions.assertEquals(serviceHistoryAttributes.separationReason(), "SeparationReason");
    assertThat(
        serviceHistoryAttributes.deployments().stream().findFirst(),
        samePropertyValuesAs(Arrays.stream(deployments).findFirst()));
  }

  @Test
  public void lastNameIsNonNullable() {
    ServiceHistoryResponse.ServiceHistoryAttributes serviceHistoryAttributes =
        TestUtils.makeServiceHistoryAttributes();
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          serviceHistoryAttributes.lastName(null);
        });
  }

  @Test
  public void payGradeIsNonNullable() {
    ServiceHistoryResponse.ServiceHistoryAttributes serviceHistoryAttributes =
        TestUtils.makeServiceHistoryAttributes();
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          serviceHistoryAttributes.payGrade(null);
        });
  }

  @Test
  public void separationReasonIsNonNullable() {
    ServiceHistoryResponse.ServiceHistoryAttributes serviceHistoryAttributes =
        TestUtils.makeServiceHistoryAttributes();
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          serviceHistoryAttributes.separationReason(null);
        });
  }

  @Test
  public void startDateIsNullable() {
    ServiceHistoryResponse.ServiceHistoryAttributes serviceHistoryAttributes =
        TestUtils.makeServiceHistoryAttributes();
    serviceHistoryAttributes.startDate(null);
  }
}
