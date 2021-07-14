package gov.va.api.lighthouse.veteranverification.service.utils;

import gov.va.api.lighthouse.veteranverification.api.v0.Deployment;
import gov.va.api.lighthouse.veteranverification.api.v0.ServiceHistoryResponse;
import gov.va.api.lighthouse.veteranverification.service.MpiLookupUtils;
import gov.va.viers.cdi.emis.commonservice.v2.MilitaryServiceEpisode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.hl7.v3.PRPAIN201306UV02;

@UtilityClass
public class ServiceEpisodeAttributeBuilder {
  DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  /**
   * Creates militaryServiceEpisodeAttribute object.
   *
   * @param serviceEpisode EMIS service response.
   * @param mpiResponse Mpi patient response.
   * @param deployments List of deployments gathered from EMIS.
   * @return Single military Service Episode.
   */
  public ServiceHistoryResponse.ServiceHistoryAttributes buildMilitaryServiceEpisode(
      @NonNull MilitaryServiceEpisode serviceEpisode,
      @NonNull PRPAIN201306UV02 mpiResponse,
      @NonNull List<Deployment> deployments) {
    LocalDate startDate = EmisUtils.getMilitaryEpisodeStartDate(serviceEpisode);
    LocalDate endDate = EmisUtils.getMilitaryEpisodeEndDate(serviceEpisode);

    return ServiceHistoryResponse.ServiceHistoryAttributes.builder()
        .firstName(MpiLookupUtils.getFirstName(mpiResponse))
        .lastName(MpiLookupUtils.getLastName(mpiResponse))
        .branchOfService(
            ServiceHistoryUtils.buildBranchOfServiceString(
                serviceEpisode.getMilitaryServiceEpisodeData().getBranchOfServiceCode(),
                serviceEpisode.getKeyData().getPersonnelCategoryTypeCode()))
        .startDate(startDate)
        .endDate(endDate)
        .payGrade(
            ServiceHistoryUtils.buildPayGradeString(
                serviceEpisode.getMilitaryServiceEpisodeData().getPayPlanCode(),
                serviceEpisode.getMilitaryServiceEpisodeData().getPayGradeCode()))
        .dischargeStatus(
            ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.codeToEnum(
                serviceEpisode
                    .getMilitaryServiceEpisodeData()
                    .getDischargeCharacterOfServiceCode()))
        .separationReason(
            serviceEpisode.getMilitaryServiceEpisodeData().getNarrativeReasonForSeparationTxt())
        .deployments(ServiceHistoryUtils.buildDeployments(deployments, startDate, endDate))
        .build();
  }
}
