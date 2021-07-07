package gov.va.api.lighthouse.veteranverification.service.utils;

import gov.va.api.lighthouse.veteranverification.api.v0.Deployment;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DeploymentsBuilder {
  /**
   * Creates a list of deployments in range of the provided start and end dates.
   *
   * @param deployments Super list of deployments.
   * @param startDate Service episode startDate.
   * @param endDate Service episode endDate.
   * @return All deployments in range of start and end dates.
   */
  public List<Deployment> buildDeployments(
      List<Deployment> deployments, LocalDate startDate, LocalDate endDate) {
    ArrayList<Deployment> deploymentsInRange = new ArrayList<>();
    for (Deployment deployment : deployments) {
      if (isBeforeOrEqualTo(startDate, deployment.startDate())
          && (endDate == null || isBeforeOrEqualTo(deployment.endDate(), endDate))) {
        deploymentsInRange.add(deployment);
      }
    }
    return deploymentsInRange;
  }

  private boolean isBeforeOrEqualTo(LocalDate dateOne, LocalDate dateTwo) {
    return dateOne.isBefore(dateTwo) || dateOne.isEqual(dateTwo);
  }
}
