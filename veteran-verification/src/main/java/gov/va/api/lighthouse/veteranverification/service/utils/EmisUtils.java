package gov.va.api.lighthouse.veteranverification.service.utils;

import gov.va.viers.cdi.emis.commonservice.v2.MilitaryServiceEpisode;
import java.time.LocalDate;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EmisUtils {
  /**
   * Gets end date from Emis Deployment object.
   *
   * @param deployment Emis Deployment object.
   * @return Emis deployment end date or null.
   */
  public LocalDate getEmisDeploymentEndDate(
      gov.va.viers.cdi.emis.commonservice.v2.Deployment deployment) {
    LocalDate endDate = null;
    if (deployment.getDeploymentData() != null
        && deployment.getDeploymentData().getDeploymentEndDate() != null) {
      endDate = LocalDate.parse(deployment.getDeploymentData().getDeploymentEndDate().toString());
    }
    return endDate;
  }

  /**
   * Gets start date from Emis Deployment object.
   *
   * @param deployment Emis Deployment object.
   * @return Emis start deployment date or null.
   */
  public LocalDate getEmisDeploymentStartDate(
      gov.va.viers.cdi.emis.commonservice.v2.Deployment deployment) {
    LocalDate startDate = null;
    if (deployment.getDeploymentData() != null
        && deployment.getDeploymentData().getDeploymentStartDate() != null) {
      startDate =
          LocalDate.parse(deployment.getDeploymentData().getDeploymentStartDate().toString());
    }
    return startDate;
  }

  /**
   * Gets end date from Emis MilitaryServiceEpisode object.
   *
   * @param episode Emis MilitaryServiceEpisode object.
   * @return Emis end date or null.
   */
  public LocalDate getMilitaryEpisodeEndDate(MilitaryServiceEpisode episode) {
    LocalDate endDate = null;
    if (episode.getMilitaryServiceEpisodeData() != null
        && episode.getMilitaryServiceEpisodeData().getServiceEpisodeEndDate() != null) {
      endDate =
          LocalDate.parse(
              episode.getMilitaryServiceEpisodeData().getServiceEpisodeEndDate().toString());
    }
    return endDate;
  }

  /**
   * Gets start date from Emis MilitaryServiceEpisode object.
   *
   * @param episode Emis MilitaryServiceEpisode object.
   * @return Emis start date or null.
   */
  public LocalDate getMilitaryEpisodeStartDate(MilitaryServiceEpisode episode) {
    LocalDate startDate = null;
    if (episode.getMilitaryServiceEpisodeData() != null
        && episode.getMilitaryServiceEpisodeData().getServiceEpisodeStartDate() != null) {
      startDate =
          LocalDate.parse(
              episode.getMilitaryServiceEpisodeData().getServiceEpisodeStartDate().toString());
    }
    return startDate;
  }
}
