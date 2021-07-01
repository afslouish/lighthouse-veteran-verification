package gov.va.api.lighthouse.veteranverification.api;

import java.util.Arrays;
import java.util.Calendar;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestUtils {
  public Deployment makeDeployment() {
    Calendar startCalendar = Calendar.getInstance();
    startCalendar.set(2000, 0, 1, 0, 0, 0);
    Calendar endCalendar = Calendar.getInstance();
    endCalendar.set(2001, 0, 1, 0, 0, 0);
    return Deployment.builder()
        .startDate(startCalendar.getTime())
        .endDate(endCalendar.getTime())
        .location(Deployment.Location.AFG)
        .build();
  }

  public ServiceHistoryAttributes makeServiceHistoryAttributes() {
    Calendar startCalendar = Calendar.getInstance();
    startCalendar.set(2000, 0, 1, 0, 0, 0);
    Calendar endCalendar = Calendar.getInstance();
    endCalendar.set(2001, 0, 1, 0, 0, 0);
    Deployment[] deployments = {makeDeployment()};
    return ServiceHistoryAttributes.builder()
        .firstName("John")
        .lastName("Doe")
        .branchOfService("BranchOfService")
        .startDate(startCalendar.getTime())
        .endDate(endCalendar.getTime())
        .payGrade("PayGrade")
        .dischargeStatus(ServiceHistoryAttributes.DischargeStatus.HONORABLE)
        .separationReason(ServiceHistoryAttributes.SeparationReason.BUFFER)
        .deployments(Arrays.stream(deployments).toList())
        .build();
  }

  public VeteranVerificationData makeVeteranVerificationData() {
    return VeteranVerificationData.builder()
        .id("ID")
        .type("Mock")
        .attributes(makeServiceHistoryAttributes())
        .build();
  }

  public VeteranVerificationResponse makeVeteranVerificationResponse() {
    return VeteranVerificationResponse.builder().data(makeVeteranVerificationData()).build();
  }
}
