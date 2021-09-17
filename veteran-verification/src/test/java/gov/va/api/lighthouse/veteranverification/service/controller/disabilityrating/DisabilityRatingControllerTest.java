package gov.va.api.lighthouse.veteranverification.service.controller.disabilityrating;

import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.lighthouse.bgs.BenefitsGatewayServicesClient;
import gov.va.api.lighthouse.mpi.MasterPatientIndexClient;
import gov.va.api.lighthouse.veteranverification.api.v1.DisabilityRatingResponse;
import gov.va.api.lighthouse.veteranverification.service.TestUtils;
import gov.va.api.lighthouse.veteranverification.service.utils.JwksProperties;
import gov.va.api.lighthouse.veteranverification.service.utils.Notary;
import gov.va.vba.benefits.share.services.DisabilityRating;
import gov.va.vba.benefits.share.services.DisabilityRatingRecord;
import gov.va.vba.benefits.share.services.RatingRecord;
import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class DisabilityRatingControllerTest {
  @Mock private MasterPatientIndexClient mpiClient;

  @Mock private BenefitsGatewayServicesClient bgsClient;

  private Notary notary;

  @Test
  public void HappyPathJwt() {
    DisabilityRatingController controller =
        new DisabilityRatingController(mpiClient, bgsClient, notary);
    TestUtils.setMpiMockResponse(mpiClient, "mpi/mpi_profile_response_body.xml");
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
                    .serviceConnectedCombinedDegree("40")
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
    String response = controller.disabilityRatingJwtResponse("icn");
    Assertions.assertEquals(
        "eyJraWQiOiJmYWtlIiwidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYifQ.eyJkYXRhIjp7ImlkIjoiaWN"
            + "uIiwidHlwZSI6ImRpc2FiaWxpdHlfcmF0aW5ncyIsImF0dHJpYnV0ZXMiOnsiY29tYmluZWRfZGlzYW"
            + "JpbGl0eV9yYXRpbmciOjQwLCJjb21iaW5lZF9lZmZlY3RpdmVfZGF0ZSI6IjIwMTAtMDgtMDEiLCJsZ"
            + "WdhbF9lZmZlY3RpdmVfZGF0ZSI6IjIwMTAtMDctMTEiLCJpbmRpdmlkdWFsX3JhdGluZ3MiOlt7ImRl"
            + "Y2lzaW9uIjoiU2VydmljZSBDb25uZWN0ZWQiLCJlZmZlY3RpdmVfZGF0ZSI6IjIwMTAtMDctMTEiLCJy"
            + "YXRpbmdfcGVyY2VudGFnZSI6MzB9XX19fQ.jtySyZ4uUdVdapcwQoVU9ypgxUM4voTBfVGKtxUz6QSm"
            + "XpeLHMP0gLIVDOYngSoB3H_J0WYOqK6zOp4ep3FPXTca4PxETibS3vFsolCmuiFqM5sNV2C3lmFoJQf"
            + "aLBlbyJUy35TsZX4wKihJMX4sUTdhvNEgBdtP02HmA-8f99fq18-s_iS-c5yNj1qP_moTLtuf3luUGE"
            + "yGC1BjoVQNH8zqMmWle5Xg4LZayngJn4O7p3tsn73UD_czPPrfCh-ZFoaLVElfdJ7YVFCRUnFyoa4oBJ"
            + "MdMRjTnT0NeLuIy4FILnXOhjU59-_aaWdXNe57HIjp4tF3gAjgqxtKR0M5jQ",
        response);
  }

  @BeforeEach
  void before() {
    MockitoAnnotations.initMocks(this);
    notary =
        new Notary(
            JwksProperties.builder()
                .keyStorePath("src/test/resources/fakekeystore.jks")
                .keyStorePassword("secret")
                .currentKeyId("fake")
                .currentKeyPassword("secret")
                .build());
  }

  @Test
  void happyPathRetrieveBySsnTest() {
    DisabilityRatingController controller =
        new DisabilityRatingController(mpiClient, bgsClient, notary);
    TestUtils.setMpiMockResponse(mpiClient, "mpi/mpi_profile_icn_response_body.xml");
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
                    .serviceConnectedCombinedDegree("40")
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
    String icn = "1012829620V654328";
    DisabilityRatingResponse disabilityRatingResponse = controller.findRatingDataResponse(icn);
    assertThat(disabilityRatingResponse).isInstanceOf(DisabilityRatingResponse.class);
    assertThat(disabilityRatingResponse.getData().getId()).isEqualTo(icn);
  }
}
