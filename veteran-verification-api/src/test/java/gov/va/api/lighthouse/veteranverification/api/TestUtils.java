package gov.va.api.lighthouse.veteranverification.api;

import gov.va.api.lighthouse.veteranverification.api.v0.Deployment;
import gov.va.api.lighthouse.veteranverification.api.v0.ServiceHistoryResponse;
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

  public ServiceHistoryResponse.ServiceHistoryAttributes makeServiceHistoryAttributes() {
    Calendar startCalendar = Calendar.getInstance();
    startCalendar.set(2000, 0, 1, 0, 0, 0);
    Calendar endCalendar = Calendar.getInstance();
    endCalendar.set(2001, 0, 1, 0, 0, 0);
    Deployment[] deployments = {makeDeployment()};
    return ServiceHistoryResponse.ServiceHistoryAttributes.builder()
        .firstName("John")
        .lastName("Doe")
        .branchOfService("BranchOfService")
        .startDate(startCalendar.getTime())
        .endDate(endCalendar.getTime())
        .payGrade("PayGrade")
        .dischargeStatus(ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.HONORABLE)
        .separationReason(ServiceHistoryResponse.ServiceHistoryAttributes.SeparationReason.BUFFER)
        .deployments(Arrays.stream(deployments).toList())
        .build();
  }

  public ServiceHistoryResponse.ServiceHistoryData makeServiceHistoryData() {
    return ServiceHistoryResponse.ServiceHistoryData.builder()
        .id("id")
        .type("mock")
        .attributes(makeServiceHistoryAttributes())
        .build();
  }

  public ServiceHistoryResponse makeServiceHistoryResponse() {
    ServiceHistoryResponse.ServiceHistoryData[] data = {makeServiceHistoryData()};
    return ServiceHistoryResponse.builder().data(Arrays.stream(data).toList()).build();
  }
}
