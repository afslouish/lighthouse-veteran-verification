package gov.va.api.lighthouse.veteranverification.api;

import gov.va.api.lighthouse.veteranverification.api.v0.Deployment;
import gov.va.api.lighthouse.veteranverification.api.v0.ServiceHistoryResponse;
import java.time.LocalDate;
import java.util.Arrays;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestUtils {
  public Deployment makeDeployment() {
    return Deployment.builder()
        .startDate(LocalDate.of(2000, 1, 1))
        .endDate(LocalDate.of(2001, 1, 1))
        .location("AFG")
        .build();
  }

  public ServiceHistoryResponse.ServiceHistoryAttributes makeServiceHistoryAttributes() {
    Deployment[] deployments = {makeDeployment()};
    return ServiceHistoryResponse.ServiceHistoryAttributes.builder()
        .firstName("John")
        .lastName("Doe")
        .branchOfService("BranchOfService")
        .startDate(LocalDate.of(2000, 1, 1))
        .endDate(LocalDate.of(2001, 1, 1))
        .payGrade("PayGrade")
        .dischargeStatus(
            ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.ThisWillBeRemoved)
        .separationReason("SeparationReason")
        .deployments(Arrays.stream(deployments).toList())
        .build();
  }

  public ServiceHistoryResponse.ServiceHistoryEpisode makeServiceHistoryData() {
    return ServiceHistoryResponse.ServiceHistoryEpisode.builder()
        .id("mock")
        .attributes(makeServiceHistoryAttributes())
        .build();
  }

  public ServiceHistoryResponse makeServiceHistoryResponse() {
    ServiceHistoryResponse.ServiceHistoryEpisode[] data = {makeServiceHistoryData()};
    return ServiceHistoryResponse.builder().data(Arrays.stream(data).toList()).build();
  }
}
