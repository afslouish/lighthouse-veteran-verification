package gov.va.api.lighthouse.veteranverification.service.controller.veteranverification;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.lighthouse.bgs.BenefitsGatewayServicesClient;
import gov.va.api.lighthouse.mpi.MasterPatientIndexClient;
import gov.va.api.lighthouse.veteranverification.service.TestUtils;
import gov.va.vba.benefits.share.services.DisabilityRating;
import gov.va.vba.benefits.share.services.DisabilityRatingRecord;
import gov.va.vba.benefits.share.services.FindRatingDataResponse;
import gov.va.vba.benefits.share.services.RatingRecord;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class DisabilityRatingControllerTest {
  @Mock private MasterPatientIndexClient mpiClient;

  @Mock private BenefitsGatewayServicesClient bgsClient;

  @BeforeEach
  void before() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void happyPathRetrieveBySsnTest() {
    DisabilityRatingController controller = new DisabilityRatingController(mpiClient, bgsClient);
    TestUtils.setMpiMockResponse(mpiClient, "mpi_profile_icn_response_body.xml");
    TestUtils.setBgsMockResponse(
        bgsClient,
        RatingRecord.builder()
            .disabilityRatingRecord(
                DisabilityRatingRecord.builder()
                    .combinedDegreeEffectiveDate("08012010")
                    .legalEffectiveDate("07112010")
                    .nonServiceConnectedCombinedDegree("40")
                    .numberOfRecords("1")
                    .promulgationDate("01012014")
                    .ratings(
                        Collections.singletonList(
                            DisabilityRating.builder()
                                .beginDate("07112010")
                                .combatIndicator("N")
                                .diagnosticPercent("30")
                                .disabilityDate("Renal Failure")
                                .diagnosticTypeCode("7502")
                                .diagnosticTypeName("Chronic nephritis")
                                .disabilityDate("06192013")
                                .disabilityDecisionTypeCode("SVCCONNCTED")
                                .disabilityDecisionTypeName("Service Connected")
                                .disabilityID("1071465")
                                .majorIndicator("N")
                                .militaryServicePeriodTypeName("Vietnam Era")
                                .build()))
                    .build())
            .build());
    FindRatingDataResponse status = controller.findRatingDataResponse("1012829620V654328");
    assertThat(status).isInstanceOf(FindRatingDataResponse.class);
  }
}
