package gov.va.api.lighthouse.veteranverification.service.controller.veteranverification;

import gov.va.api.lighthouse.veteranverification.api.v0.ServiceHistoryResponse;
import gov.va.api.lighthouse.veteranverification.service.TestUtils;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISdeploymentResponseType;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISserviceEpisodeResponseType;
import org.hl7.v3.PRPAIN201306UV02;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VeteranServiceHistoryTransformerTest {
  @Test
  public void branchOfServiceTest() {
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
        TestUtils.createServiceHistoryResponse(
            "emis/service-episodes/pay_grade_and_branch_of_service_scenarios.xml");
    PRPAIN201306UV02 mpiResponse = TestUtils.createMpiResponse("mpi/mpi_profile_response_body.xml");
    EMISdeploymentResponseType deployments =
        TestUtils.createDeploymentResponse("emis/deployments_response.xml");
    VeteranServiceHistoryTransformer transformer =
        VeteranServiceHistoryTransformer.builder()
            .icn("uuid")
            .deploymentResponse(deployments)
            .serviceEpisodeResponseType(serviceEpisodeResponse)
            .mpiResponse(mpiResponse)
            .build();
    ServiceHistoryResponse response = transformer.serviceHistoryTransformer();
    // Happy Path Air Force National Guard
    // BranchOfServiceCode: F; PersonnelCategoryCode: N
    String happyPathAirForceNationGuardExpected = "Air Force National Guard";
    String happyPathAirForceNationGuardActual =
        response.data().get(0).attributes().branchOfService();
    Assertions.assertEquals(
        happyPathAirForceNationGuardExpected, happyPathAirForceNationGuardActual);
    // Happy Path Air Force No Category Expected
    // BranchOfServiceCode: F; PersonnelCategoryCode: A
    String happyPathAirForceNoCategoryExpected = "Air Force";
    String happyPathAirForceNoCategoryActual =
        response.data().get(1).attributes().branchOfService();
    Assertions.assertEquals(happyPathAirForceNoCategoryExpected, happyPathAirForceNoCategoryActual);
    // Happy Path Air Force Reserve Q Expected
    // BranchOfServiceCode: F; PersonnelCategoryCode: Q
    String happyPathAirForceReserveQExpected = "Air Force Reserve";
    String happyPathAirForceReserveQActual = response.data().get(2).attributes().branchOfService();
    Assertions.assertEquals(happyPathAirForceReserveQExpected, happyPathAirForceReserveQActual);
    // Happy Path Air Force Reserve V
    // BranchOfServiceCode: F; PersonnelCategoryCode: V
    String happyPathAirForceReserveVExpected = "Air Force Reserve";
    String happyPathAirForceReserveVActual = response.data().get(3).attributes().branchOfService();
    Assertions.assertEquals(happyPathAirForceReserveVExpected, happyPathAirForceReserveVActual);
    // Happy Path Army Nation Guard
    // BranchOfServiceCode: A; PersonnelCategoryCode: N
    String happyPathArmyNationalGuardExpected = "Army National Guard";
    String happyPathArmyNationalGuardActual = response.data().get(4).attributes().branchOfService();
    Assertions.assertEquals(happyPathArmyNationalGuardExpected, happyPathArmyNationalGuardActual);
    // Happy Path Army No Category Expected
    // BranchOfServiceCode: A; PersonnelCategoryCode: A
    String happyPathArmyNoCategoryExpected = "Army";
    String happyPathArmyNoCategoryActual = response.data().get(5).attributes().branchOfService();
    Assertions.assertEquals(happyPathArmyNoCategoryExpected, happyPathArmyNoCategoryActual);
    // Happy Path Army Reserve Q
    // BranchOfServiceCode: A; PersonnelCategoryCode: Q
    String happyPathArmyReserveQExpected = "Army Reserve";
    String happyPathArmyReserveQActual = response.data().get(6).attributes().branchOfService();
    Assertions.assertEquals(happyPathArmyReserveQExpected, happyPathArmyReserveQActual);
    // Happy Path Army Reserve V
    // BranchOfServiceCode: A; PersonnelCategoryCode: V
    String happyPathArmyReserveVExpected = "Army Reserve";
    String happyPathArmyReserveVActual = response.data().get(7).attributes().branchOfService();
    Assertions.assertEquals(happyPathArmyReserveVExpected, happyPathArmyReserveVActual);
    // Happy Path Coast Guard National Guard
    // BranchOfServiceCode: C; PersonnelCategoryCode: N
    String happyPathCoastGuardNationalGuardExpected = "Coast Guard National Guard";
    String happyPathCoastGuardNationalGuardActual =
        response.data().get(8).attributes().branchOfService();
    Assertions.assertEquals(
        happyPathCoastGuardNationalGuardExpected, happyPathCoastGuardNationalGuardActual);
    // Happy Path Coast Guard No Category
    // BranchOfServiceCode: C; PersonnelCategoryCode: A
    String happyPathCoastGuardNoCategoryExpected = "Coast Guard";
    String happyPathCoastGuardNoCategoryActual =
        response.data().get(9).attributes().branchOfService();
    Assertions.assertEquals(
        happyPathCoastGuardNoCategoryExpected, happyPathCoastGuardNoCategoryActual);
    // Happy PAth Coast Guard Reserve Q
    // BranchOfServiceCode: C; PersonnelCategoryCode: Q
    String happyPathCoastGuardReserveQExpected = "Coast Guard Reserve";
    String happyPathCoastGuardReserveQActual =
        response.data().get(10).attributes().branchOfService();
    Assertions.assertEquals(happyPathCoastGuardReserveQExpected, happyPathCoastGuardReserveQActual);
    // Happy Path Coast Guard Reserve V
    // BranchOfServiceCode: C; PersonnelCategoryCode: V
    String happyPathCoastGuardReserveVExpected = "Coast Guard Reserve";
    String happyPathCoastGuardReserveVActual =
        response.data().get(11).attributes().branchOfService();
    Assertions.assertEquals(happyPathCoastGuardReserveVExpected, happyPathCoastGuardReserveVActual);
    // Happy Path Excess White Space
    String happyPathExcessWhiteSpaceExpected = "Marine Corps Reserve";
    String happyPathExcessWhiteSpaceActual = response.data().get(12).attributes().branchOfService();
    Assertions.assertEquals(happyPathExcessWhiteSpaceExpected, happyPathExcessWhiteSpaceActual);
    // Happy Path Marine National Guard
    // BranchOfServiceCode: M; PersonnelCategoryCode: N
    String happyPathMarineNationalGuardExpected = "Marine Corps National Guard";
    String happyPathMarineNationalGuardActual =
        response.data().get(13).attributes().branchOfService();
    Assertions.assertEquals(
        happyPathMarineNationalGuardExpected, happyPathMarineNationalGuardActual);
    // Happy Path Marine No Category
    // BranchOfServiceCode: M; PersonnelCategoryCode: A
    String happyPathMarineNoCategoryExpected = "Marine Corps";
    String happyPathMarineNoCategoryActual = response.data().get(14).attributes().branchOfService();
    Assertions.assertEquals(happyPathMarineNoCategoryExpected, happyPathMarineNoCategoryActual);
    // Happy Path Marine Reserve Q
    // BranchOfServiceCode: M; PersonnelCategoryCode: Q
    String happyPathMarineReserveQExpected = "Marine Corps Reserve";
    String happyPathMarineReserveQActual = response.data().get(15).attributes().branchOfService();
    Assertions.assertEquals(happyPathMarineReserveQExpected, happyPathMarineReserveQActual);
    // Happy Path Marine Reserve V
    // BranchOfServiceCode: M; PersonnelCategoryCode: V
    String happyPathMarineReserveVExpected = "Marine Corps Reserve";
    String happyPathMarineReserveVActual = response.data().get(16).attributes().branchOfService();
    Assertions.assertEquals(happyPathMarineReserveVExpected, happyPathMarineReserveVActual);
    // Happy Path Noaa National Guard
    // BranchOfServiceCode: O; PersonnelCategoryCode: N
    String happyPathNoaaNationalGuardExpected = "NOAA National Guard";
    String happyPathNoaaNationalGuardActual =
        response.data().get(17).attributes().branchOfService();
    Assertions.assertEquals(happyPathNoaaNationalGuardExpected, happyPathNoaaNationalGuardActual);
    // Happy Path Noaa No Category
    // BranchOfServiceCode: O; PersonnelCategoryCode: A
    String happyPathNoaaNoCategoryExpected = "NOAA";
    String happyPathNoaaNoCategoryActual = response.data().get(18).attributes().branchOfService();
    Assertions.assertEquals(happyPathNoaaNoCategoryExpected, happyPathNoaaNoCategoryActual);
    // Happy Path Noaa Reserve Q
    // BranchOfServiceCode: O; PersonnelCategoryCode: Q
    String happyPathNoaaReserveQExpected = "NOAA Reserve";
    String happyPathNoaaReserveQActual = response.data().get(19).attributes().branchOfService();
    Assertions.assertEquals(happyPathNoaaReserveQExpected, happyPathNoaaReserveQActual);
    // Happy Path Noaa Reserve V
    // BranchOfServiceCode: O; PersonnelCategoryCode: V
    String happyPathNoaaReserveVExpected = "NOAA Reserve";
    String happyPathNoaaReserveVActual = response.data().get(20).attributes().branchOfService();
    Assertions.assertEquals(happyPathNoaaReserveVExpected, happyPathNoaaReserveVActual);
    // Happy Oath Usphs National Guard
    // BranchOfServiceCode: H; PersonnelCategoryCode: N
    String happyPathUsphsNationalGuardExpected = "Public Health Service National Guard";
    String happyPathUsphsNationalGuardActual =
        response.data().get(21).attributes().branchOfService();
    Assertions.assertEquals(happyPathUsphsNationalGuardExpected, happyPathUsphsNationalGuardActual);
    // Happy Path Usphs No Category
    // BranchOfServiceCode: H; PersonnelCategoryCode: A
    String happyPathUsphsNoCategoryExpected = "Public Health Service";
    String happyPathUsphsNoCategoryActual = response.data().get(22).attributes().branchOfService();
    Assertions.assertEquals(happyPathUsphsNoCategoryExpected, happyPathUsphsNoCategoryActual);
    // Happy Path Usphs Reserve Q
    // BranchOfServiceCode: H; PersonnelCategoryCode: Q
    String happyPathUsphsReserveQExpected = "Public Health Service Reserve";
    String happyPathUsphsReserveQActual = response.data().get(23).attributes().branchOfService();
    Assertions.assertEquals(happyPathUsphsReserveQExpected, happyPathUsphsReserveQActual);
    // Happy Path Usphs Reserve V
    // BranchOfServiceCode: H; PersonnelCategoryCode: V
    String happyPathUsphsReserveVExpected = "Public Health Service Reserve";
    String happyPathUsphsReserveVActual = response.data().get(24).attributes().branchOfService();
    Assertions.assertEquals(happyPathUsphsReserveVExpected, happyPathUsphsReserveVActual);
    // Other Branch Of Service
    String otherBranchOfServiceExpected = "Unknown";
    String otherBranchOfServiceActual = response.data().get(25).attributes().branchOfService();
    Assertions.assertEquals(otherBranchOfServiceExpected, otherBranchOfServiceActual);
  }

  @Test
  public void happyPathDeploymentDataIsNull() {
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
        TestUtils.createServiceHistoryResponse("emis/service-episodes/ascending.xml");
    PRPAIN201306UV02 mpiResponse = TestUtils.createMpiResponse("mpi/mpi_profile_response_body.xml");
    EMISdeploymentResponseType deployments =
        TestUtils.createDeploymentResponse("emis/deployments_response.xml");
    deployments.getDeployment().get(0).setDeploymentData(null);
    deployments.getDeployment().get(1).setDeploymentData(null);
    deployments.getDeployment().get(2).setDeploymentData(null);
    deployments.getDeployment().get(3).setDeploymentData(null);
    deployments.getDeployment().get(4).setDeploymentData(null);
    VeteranServiceHistoryTransformer transformer =
        VeteranServiceHistoryTransformer.builder()
            .icn("uuid")
            .deploymentResponse(deployments)
            .serviceEpisodeResponseType(serviceEpisodeResponse)
            .mpiResponse(mpiResponse)
            .build();
    ServiceHistoryResponse response = transformer.serviceHistoryTransformer();
    Assertions.assertEquals(response.data().size(), 2);
    ServiceHistoryResponse.ServiceHistoryEpisode episodeOne = response.data().get(0);
    ServiceHistoryResponse.ServiceHistoryEpisode episodeTwo = response.data().get(1);
    Assertions.assertEquals(episodeOne.id(), "90a16974-079a-563e-94dd-365a68b209d9");
    Assertions.assertEquals(episodeOne.type(), "service-history-episodes");
    Assertions.assertEquals(episodeTwo.id(), "eee59014-6bc4-5c7a-ab24-4bd64c090948");
    Assertions.assertEquals(episodeTwo.type(), "service-history-episodes");
    ServiceHistoryResponse.ServiceHistoryAttributes attributesOne = episodeOne.attributes();
    ServiceHistoryResponse.ServiceHistoryAttributes attributesTwo = episodeTwo.attributes();
    Assertions.assertEquals(attributesOne.firstName(), "Alfredo");
    Assertions.assertEquals(attributesOne.lastName(), "Armstrong");
    Assertions.assertEquals(attributesOne.branchOfService().toString(), "Army");
    Assertions.assertEquals(attributesOne.startDate().toString(), "2000-01-01");
    Assertions.assertEquals(attributesOne.endDate().toString(), "2001-02-01");
    Assertions.assertEquals(attributesOne.payGrade().toString(), "E05");
    Assertions.assertEquals(
        attributesOne.dischargeStatus(),
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.HONORABLE);
    Assertions.assertEquals(attributesOne.separationReason(), "SUFFICIENT SERVICE FOR RETIREMENT");
    Assertions.assertEquals(0, attributesOne.deployments().stream().count());
    Assertions.assertEquals(attributesTwo.firstName(), "Alfredo");
    Assertions.assertEquals(attributesTwo.lastName(), "Armstrong");
    Assertions.assertEquals(attributesTwo.branchOfService().toString(), "Air Force");
    Assertions.assertEquals(attributesTwo.startDate().toString(), "2002-01-01");
    Assertions.assertEquals(attributesTwo.endDate().toString(), "2003-02-01");
    Assertions.assertEquals(attributesTwo.payGrade().toString(), "E05");
    Assertions.assertEquals(
        attributesTwo.dischargeStatus(),
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.HONORABLE);
    Assertions.assertEquals(attributesTwo.separationReason(), "SUFFICIENT SERVICE FOR RETIREMENT");
    Assertions.assertEquals(0, attributesTwo.deployments().stream().count());
  }

  @Test
  public void happyPathDeploymentLocationIsNull() {
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
        TestUtils.createServiceHistoryResponse("emis/service-episodes/ascending.xml");
    PRPAIN201306UV02 mpiResponse = TestUtils.createMpiResponse("mpi/mpi_profile_response_body.xml");
    EMISdeploymentResponseType deployments =
        TestUtils.createDeploymentResponse("emis/deployments_response.xml");
    deployments
        .getDeployment()
        .get(0)
        .getDeploymentData()
        .getDeploymentLocation()
        .get(0)
        .setDeploymentISOAlpha3Country(null);
    deployments
        .getDeployment()
        .get(1)
        .getDeploymentData()
        .getDeploymentLocation()
        .get(0)
        .setDeploymentISOAlpha3Country(null);
    deployments
        .getDeployment()
        .get(2)
        .getDeploymentData()
        .getDeploymentLocation()
        .get(0)
        .setDeploymentISOAlpha3Country(null);
    deployments
        .getDeployment()
        .get(3)
        .getDeploymentData()
        .getDeploymentLocation()
        .get(0)
        .setDeploymentISOAlpha3Country(null);
    deployments
        .getDeployment()
        .get(4)
        .getDeploymentData()
        .getDeploymentLocation()
        .get(0)
        .setDeploymentISOAlpha3Country(null);
    VeteranServiceHistoryTransformer transformer =
        VeteranServiceHistoryTransformer.builder()
            .icn("uuid")
            .deploymentResponse(deployments)
            .serviceEpisodeResponseType(serviceEpisodeResponse)
            .mpiResponse(mpiResponse)
            .build();
    ServiceHistoryResponse response = transformer.serviceHistoryTransformer();
    Assertions.assertEquals(response.data().size(), 2);
    ServiceHistoryResponse.ServiceHistoryEpisode episodeOne = response.data().get(0);
    ServiceHistoryResponse.ServiceHistoryEpisode episodeTwo = response.data().get(1);
    Assertions.assertEquals(episodeOne.id(), "90a16974-079a-563e-94dd-365a68b209d9");
    Assertions.assertEquals(episodeOne.type(), "service-history-episodes");
    Assertions.assertEquals(episodeTwo.id(), "eee59014-6bc4-5c7a-ab24-4bd64c090948");
    Assertions.assertEquals(episodeTwo.type(), "service-history-episodes");
    ServiceHistoryResponse.ServiceHistoryAttributes attributesOne = episodeOne.attributes();
    ServiceHistoryResponse.ServiceHistoryAttributes attributesTwo = episodeTwo.attributes();
    Assertions.assertEquals(attributesOne.firstName(), "Alfredo");
    Assertions.assertEquals(attributesOne.lastName(), "Armstrong");
    Assertions.assertEquals(attributesOne.branchOfService().toString(), "Army");
    Assertions.assertEquals(attributesOne.startDate().toString(), "2000-01-01");
    Assertions.assertEquals(attributesOne.endDate().toString(), "2001-02-01");
    Assertions.assertEquals(attributesOne.payGrade().toString(), "E05");
    Assertions.assertEquals(
        attributesOne.dischargeStatus(),
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.HONORABLE);
    Assertions.assertEquals(attributesOne.separationReason(), "SUFFICIENT SERVICE FOR RETIREMENT");
    Assertions.assertEquals(attributesOne.deployments().stream().count(), 1);
    Assertions.assertEquals(
        attributesOne.deployments().get(0).startDate().toString(), "2000-02-01");
    Assertions.assertEquals(attributesOne.deployments().get(0).endDate().toString(), "2001-01-01");
    Assertions.assertEquals(attributesOne.deployments().get(0).location(), null);
    Assertions.assertEquals(attributesTwo.firstName(), "Alfredo");
    Assertions.assertEquals(attributesTwo.lastName(), "Armstrong");
    Assertions.assertEquals(attributesTwo.branchOfService().toString(), "Air Force");
    Assertions.assertEquals(attributesTwo.startDate().toString(), "2002-01-01");
    Assertions.assertEquals(attributesTwo.endDate().toString(), "2003-02-01");
    Assertions.assertEquals(attributesTwo.payGrade().toString(), "E05");
    Assertions.assertEquals(
        attributesTwo.dischargeStatus(),
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.HONORABLE);
    Assertions.assertEquals(attributesTwo.separationReason(), "SUFFICIENT SERVICE FOR RETIREMENT");
    Assertions.assertEquals(attributesTwo.deployments().stream().count(), 1);
    Assertions.assertEquals(
        attributesTwo.deployments().get(0).startDate().toString(), "2002-02-01");
    Assertions.assertEquals(attributesTwo.deployments().get(0).endDate().toString(), "2003-01-01");
    Assertions.assertEquals(attributesTwo.deployments().get(0).location(), null);
  }

  @Test
  public void happyPathFullData() {
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
        TestUtils.createServiceHistoryResponse("emis/service-episodes/ascending.xml");
    PRPAIN201306UV02 mpiResponse = TestUtils.createMpiResponse("mpi/mpi_profile_response_body.xml");
    EMISdeploymentResponseType deployments =
        TestUtils.createDeploymentResponse("emis/deployments_response.xml");
    VeteranServiceHistoryTransformer transformer =
        VeteranServiceHistoryTransformer.builder()
            .icn("uuid")
            .deploymentResponse(deployments)
            .serviceEpisodeResponseType(serviceEpisodeResponse)
            .mpiResponse(mpiResponse)
            .build();
    ServiceHistoryResponse response = transformer.serviceHistoryTransformer();
    Assertions.assertEquals(response.data().size(), 2);
    ServiceHistoryResponse.ServiceHistoryEpisode episodeOne = response.data().get(0);
    ServiceHistoryResponse.ServiceHistoryEpisode episodeTwo = response.data().get(1);
    Assertions.assertEquals(episodeOne.id(), "90a16974-079a-563e-94dd-365a68b209d9");
    Assertions.assertEquals(episodeOne.type(), "service-history-episodes");
    Assertions.assertEquals(episodeTwo.id(), "eee59014-6bc4-5c7a-ab24-4bd64c090948");
    Assertions.assertEquals(episodeTwo.type(), "service-history-episodes");
    ServiceHistoryResponse.ServiceHistoryAttributes attributesOne = episodeOne.attributes();
    ServiceHistoryResponse.ServiceHistoryAttributes attributesTwo = episodeTwo.attributes();
    Assertions.assertEquals(attributesOne.firstName(), "Alfredo");
    Assertions.assertEquals(attributesOne.lastName(), "Armstrong");
    Assertions.assertEquals(attributesOne.branchOfService().toString(), "Army");
    Assertions.assertEquals(attributesOne.startDate().toString(), "2000-01-01");
    Assertions.assertEquals(attributesOne.endDate().toString(), "2001-02-01");
    Assertions.assertEquals(attributesOne.payGrade().toString(), "E05");
    Assertions.assertEquals(
        attributesOne.dischargeStatus(),
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.HONORABLE);
    Assertions.assertEquals(attributesOne.separationReason(), "SUFFICIENT SERVICE FOR RETIREMENT");
    Assertions.assertEquals(attributesOne.deployments().stream().count(), 1);
    Assertions.assertEquals(
        attributesOne.deployments().get(0).startDate().toString(), "2000-02-01");
    Assertions.assertEquals(attributesOne.deployments().get(0).endDate().toString(), "2001-01-01");
    Assertions.assertEquals(attributesOne.deployments().get(0).location(), "QAT");
    Assertions.assertEquals(attributesTwo.firstName(), "Alfredo");
    Assertions.assertEquals(attributesTwo.lastName(), "Armstrong");
    Assertions.assertEquals(attributesTwo.branchOfService().toString(), "Air Force");
    Assertions.assertEquals(attributesTwo.startDate().toString(), "2002-01-01");
    Assertions.assertEquals(attributesTwo.endDate().toString(), "2003-02-01");
    Assertions.assertEquals(attributesTwo.payGrade().toString(), "E05");
    Assertions.assertEquals(
        attributesTwo.dischargeStatus(),
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.HONORABLE);
    Assertions.assertEquals(attributesTwo.separationReason(), "SUFFICIENT SERVICE FOR RETIREMENT");
    Assertions.assertEquals(attributesTwo.deployments().stream().count(), 1);
    Assertions.assertEquals(
        attributesTwo.deployments().get(0).startDate().toString(), "2002-02-01");
    Assertions.assertEquals(attributesTwo.deployments().get(0).endDate().toString(), "2003-01-01");
    Assertions.assertEquals(attributesTwo.deployments().get(0).location(), "QAT");
  }

  @Test
  public void happyPathNoNullsReverseEpisodes() {
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
        TestUtils.createServiceHistoryResponse("emis/service-episodes/descending.xml");
    PRPAIN201306UV02 mpiResponse = TestUtils.createMpiResponse("mpi/mpi_profile_response_body.xml");
    EMISdeploymentResponseType deployments =
        TestUtils.createDeploymentResponse("emis/deployments_response.xml");
    VeteranServiceHistoryTransformer transformer =
        VeteranServiceHistoryTransformer.builder()
            .icn("uuid")
            .deploymentResponse(deployments)
            .serviceEpisodeResponseType(serviceEpisodeResponse)
            .mpiResponse(mpiResponse)
            .build();
    ServiceHistoryResponse response = transformer.serviceHistoryTransformer();
    Assertions.assertEquals(response.data().size(), 2);
    ServiceHistoryResponse.ServiceHistoryEpisode episodeOne = response.data().get(0);
    ServiceHistoryResponse.ServiceHistoryEpisode episodeTwo = response.data().get(1);
    Assertions.assertEquals(episodeOne.id(), "90a16974-079a-563e-94dd-365a68b209d9");
    Assertions.assertEquals(episodeOne.type(), "service-history-episodes");
    Assertions.assertEquals(episodeTwo.id(), "eee59014-6bc4-5c7a-ab24-4bd64c090948");
    Assertions.assertEquals(episodeTwo.type(), "service-history-episodes");
    ServiceHistoryResponse.ServiceHistoryAttributes attributesOne = episodeOne.attributes();
    ServiceHistoryResponse.ServiceHistoryAttributes attributesTwo = episodeTwo.attributes();
    Assertions.assertEquals(attributesOne.firstName(), "Alfredo");
    Assertions.assertEquals(attributesOne.lastName(), "Armstrong");
    Assertions.assertEquals(attributesOne.branchOfService().toString(), "Army");
    Assertions.assertEquals(attributesOne.startDate().toString(), "2000-01-01");
    Assertions.assertEquals(attributesOne.endDate().toString(), "2001-02-01");
    Assertions.assertEquals(attributesOne.payGrade().toString(), "E05");
    Assertions.assertEquals(
        attributesOne.dischargeStatus(),
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.HONORABLE);
    Assertions.assertEquals(attributesOne.separationReason(), "SUFFICIENT SERVICE FOR RETIREMENT");
    Assertions.assertEquals(attributesOne.deployments().stream().count(), 1);
    Assertions.assertEquals(
        attributesOne.deployments().get(0).startDate().toString(), "2000-02-01");
    Assertions.assertEquals(attributesOne.deployments().get(0).endDate().toString(), "2001-01-01");
    Assertions.assertEquals(attributesOne.deployments().get(0).location(), "QAT");
    Assertions.assertEquals(attributesTwo.firstName(), "Alfredo");
    Assertions.assertEquals(attributesTwo.lastName(), "Armstrong");
    Assertions.assertEquals(attributesTwo.branchOfService().toString(), "Air Force");
    Assertions.assertEquals(attributesTwo.startDate().toString(), "2002-01-01");
    Assertions.assertEquals(attributesTwo.endDate().toString(), "2003-02-01");
    Assertions.assertEquals(attributesTwo.payGrade().toString(), "E05");
    Assertions.assertEquals(
        attributesTwo.dischargeStatus(),
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.HONORABLE);
    Assertions.assertEquals(attributesTwo.separationReason(), "SUFFICIENT SERVICE FOR RETIREMENT");
    Assertions.assertEquals(attributesTwo.deployments().stream().count(), 1);
    Assertions.assertEquals(
        attributesTwo.deployments().get(0).startDate().toString(), "2002-02-01");
    Assertions.assertEquals(attributesTwo.deployments().get(0).endDate().toString(), "2003-01-01");
    Assertions.assertEquals(attributesTwo.deployments().get(0).location(), "QAT");
  }

  @Test
  public void happyPathNullEndDate() {
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
        TestUtils.createServiceHistoryResponse("emis/service-episodes/ascending.xml");
    PRPAIN201306UV02 mpiResponse = TestUtils.createMpiResponse("mpi/mpi_profile_response_body.xml");
    EMISdeploymentResponseType deployments =
        TestUtils.createDeploymentResponse("emis/deployments_response.xml");
    serviceEpisodeResponse
        .getMilitaryServiceEpisode()
        .get(0)
        .getMilitaryServiceEpisodeData()
        .setServiceEpisodeEndDate(null);
    VeteranServiceHistoryTransformer transformer =
        VeteranServiceHistoryTransformer.builder()
            .icn("uuid")
            .deploymentResponse(deployments)
            .serviceEpisodeResponseType(serviceEpisodeResponse)
            .mpiResponse(mpiResponse)
            .build();
    ServiceHistoryResponse response = transformer.serviceHistoryTransformer();
    Assertions.assertEquals(response.data().size(), 2);
    ServiceHistoryResponse.ServiceHistoryEpisode episodeOne = response.data().get(0);
    ServiceHistoryResponse.ServiceHistoryEpisode episodeTwo = response.data().get(1);
    Assertions.assertEquals(episodeOne.id(), "639dbf29-f2d0-54a7-a1d0-b26a26e13f8a");
    Assertions.assertEquals(episodeOne.type(), "service-history-episodes");
    Assertions.assertEquals(episodeTwo.id(), "eee59014-6bc4-5c7a-ab24-4bd64c090948");
    Assertions.assertEquals(episodeTwo.type(), "service-history-episodes");
    ServiceHistoryResponse.ServiceHistoryAttributes attributesOne = episodeOne.attributes();
    ServiceHistoryResponse.ServiceHistoryAttributes attributesTwo = episodeTwo.attributes();
    Assertions.assertEquals(attributesOne.firstName(), "Alfredo");
    Assertions.assertEquals(attributesOne.lastName(), "Armstrong");
    Assertions.assertEquals(attributesOne.branchOfService().toString(), "Army");
    Assertions.assertEquals(attributesOne.startDate().toString(), "2000-01-01");
    Assertions.assertNull(attributesOne.endDate());
    Assertions.assertEquals(attributesOne.payGrade().toString(), "E05");
    Assertions.assertEquals(
        attributesOne.dischargeStatus(),
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.HONORABLE);
    Assertions.assertEquals(attributesOne.separationReason(), "SUFFICIENT SERVICE FOR RETIREMENT");
    Assertions.assertEquals(attributesOne.deployments().stream().count(), 4);
    Assertions.assertEquals(
        attributesOne.deployments().get(0).startDate().toString(), "2000-02-01");
    Assertions.assertEquals(attributesOne.deployments().get(0).endDate().toString(), "2001-01-01");
    Assertions.assertEquals(attributesOne.deployments().get(0).location(), "QAT");
    Assertions.assertEquals(
        attributesOne.deployments().get(1).startDate().toString(), "2000-03-01");
    Assertions.assertEquals(attributesOne.deployments().get(1).endDate().toString(), "2003-01-01");
    Assertions.assertEquals(attributesOne.deployments().get(1).location(), "QAT");
    Assertions.assertEquals(
        attributesOne.deployments().get(1).startDate().toString(), "2000-03-01");
    Assertions.assertEquals(attributesOne.deployments().get(1).endDate().toString(), "2003-01-01");
    Assertions.assertEquals(attributesOne.deployments().get(1).location(), "QAT");
    Assertions.assertEquals(
        attributesOne.deployments().get(2).startDate().toString(), "2004-02-01");
    Assertions.assertEquals(attributesOne.deployments().get(2).endDate().toString(), "2005-01-01");
    Assertions.assertEquals(attributesOne.deployments().get(2).location(), "AX1");
    Assertions.assertEquals(
        attributesOne.deployments().get(3).startDate().toString(), "2006-02-01");
    Assertions.assertEquals(attributesOne.deployments().get(3).endDate().toString(), "2007-01-01");
    Assertions.assertEquals(attributesOne.deployments().get(3).location(), "AX1");
    Assertions.assertEquals(attributesTwo.firstName(), "Alfredo");
    Assertions.assertEquals(attributesTwo.lastName(), "Armstrong");
    Assertions.assertEquals(attributesTwo.branchOfService().toString(), "Air Force");
    Assertions.assertEquals(attributesTwo.startDate().toString(), "2002-01-01");
    Assertions.assertEquals(attributesTwo.endDate().toString(), "2003-02-01");
    Assertions.assertEquals(attributesTwo.payGrade().toString(), "E05");
    Assertions.assertEquals(
        attributesTwo.dischargeStatus(),
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.HONORABLE);
    Assertions.assertEquals(attributesTwo.separationReason(), "SUFFICIENT SERVICE FOR RETIREMENT");
    Assertions.assertEquals(attributesTwo.deployments().stream().count(), 1);
    Assertions.assertEquals(
        attributesTwo.deployments().get(0).startDate().toString(), "2002-02-01");
    Assertions.assertEquals(attributesTwo.deployments().get(0).endDate().toString(), "2003-01-01");
    Assertions.assertEquals(attributesTwo.deployments().get(0).location(), "QAT");
  }

  @Test
  public void happyPathNullEndDateEpisodesReverse() {
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
        TestUtils.createServiceHistoryResponse("emis/service-episodes/descending.xml");
    PRPAIN201306UV02 mpiResponse = TestUtils.createMpiResponse("mpi/mpi_profile_response_body.xml");
    EMISdeploymentResponseType deployments =
        TestUtils.createDeploymentResponse("emis/deployments_response.xml");
    serviceEpisodeResponse
        .getMilitaryServiceEpisode()
        .get(1)
        .getMilitaryServiceEpisodeData()
        .setServiceEpisodeEndDate(null);
    VeteranServiceHistoryTransformer transformer =
        VeteranServiceHistoryTransformer.builder()
            .icn("uuid")
            .deploymentResponse(deployments)
            .serviceEpisodeResponseType(serviceEpisodeResponse)
            .mpiResponse(mpiResponse)
            .build();
    ServiceHistoryResponse response = transformer.serviceHistoryTransformer();
    Assertions.assertEquals(response.data().size(), 2);
    ServiceHistoryResponse.ServiceHistoryEpisode episodeOne = response.data().get(0);
    ServiceHistoryResponse.ServiceHistoryEpisode episodeTwo = response.data().get(1);
    Assertions.assertEquals(episodeOne.id(), "639dbf29-f2d0-54a7-a1d0-b26a26e13f8a");
    Assertions.assertEquals(episodeOne.type(), "service-history-episodes");
    Assertions.assertEquals(episodeTwo.id(), "eee59014-6bc4-5c7a-ab24-4bd64c090948");
    Assertions.assertEquals(episodeTwo.type(), "service-history-episodes");
    ServiceHistoryResponse.ServiceHistoryAttributes attributesOne = episodeOne.attributes();
    ServiceHistoryResponse.ServiceHistoryAttributes attributesTwo = episodeTwo.attributes();
    Assertions.assertEquals(attributesOne.firstName(), "Alfredo");
    Assertions.assertEquals(attributesOne.lastName(), "Armstrong");
    Assertions.assertEquals(attributesOne.branchOfService().toString(), "Army");
    Assertions.assertEquals(attributesOne.startDate().toString(), "2000-01-01");
    Assertions.assertNull(attributesOne.endDate());
    Assertions.assertEquals(attributesOne.payGrade().toString(), "E05");
    Assertions.assertEquals(
        attributesOne.dischargeStatus(),
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.HONORABLE);
    Assertions.assertEquals(attributesOne.separationReason(), "SUFFICIENT SERVICE FOR RETIREMENT");
    Assertions.assertEquals(attributesOne.deployments().stream().count(), 4);
    Assertions.assertEquals(
        attributesOne.deployments().get(0).startDate().toString(), "2000-02-01");
    Assertions.assertEquals(attributesOne.deployments().get(0).endDate().toString(), "2001-01-01");
    Assertions.assertEquals(attributesOne.deployments().get(0).location(), "QAT");
    Assertions.assertEquals(
        attributesOne.deployments().get(1).startDate().toString(), "2000-03-01");
    Assertions.assertEquals(attributesOne.deployments().get(1).endDate().toString(), "2003-01-01");
    Assertions.assertEquals(attributesOne.deployments().get(1).location(), "QAT");
    Assertions.assertEquals(
        attributesOne.deployments().get(2).startDate().toString(), "2004-02-01");
    Assertions.assertEquals(attributesOne.deployments().get(2).endDate().toString(), "2005-01-01");
    Assertions.assertEquals(attributesOne.deployments().get(2).location(), "AX1");
    Assertions.assertEquals(
        attributesOne.deployments().get(3).startDate().toString(), "2006-02-01");
    Assertions.assertEquals(attributesOne.deployments().get(3).endDate().toString(), "2007-01-01");
    Assertions.assertEquals(attributesOne.deployments().get(3).location(), "AX1");
    Assertions.assertEquals(attributesTwo.firstName(), "Alfredo");
    Assertions.assertEquals(attributesTwo.lastName(), "Armstrong");
    Assertions.assertEquals(attributesTwo.branchOfService().toString(), "Air Force");
    Assertions.assertEquals(attributesTwo.startDate().toString(), "2002-01-01");
    Assertions.assertEquals(attributesTwo.endDate().toString(), "2003-02-01");
    Assertions.assertEquals(attributesTwo.payGrade().toString(), "E05");
    Assertions.assertEquals(
        attributesTwo.dischargeStatus(),
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.HONORABLE);
    Assertions.assertEquals(attributesTwo.separationReason(), "SUFFICIENT SERVICE FOR RETIREMENT");
    Assertions.assertEquals(attributesTwo.deployments().stream().count(), 1);
    Assertions.assertEquals(
        attributesTwo.deployments().get(0).startDate().toString(), "2002-02-01");
    Assertions.assertEquals(attributesTwo.deployments().get(0).endDate().toString(), "2003-01-01");
    Assertions.assertEquals(attributesTwo.deployments().get(0).location(), "QAT");
  }

  @Test
  public void nullDeployments() {
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
        TestUtils.createServiceHistoryResponse("emis/service-episodes/ascending.xml");
    PRPAIN201306UV02 mpiResponse = TestUtils.createMpiResponse("mpi/mpi_profile_response_body.xml");
    EMISdeploymentResponseType deployments =
        TestUtils.createDeploymentResponse("emis/deployments_response.xml");
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          VeteranServiceHistoryTransformer.builder()
              .icn("uuid")
              .deploymentResponse(null)
              .serviceEpisodeResponseType(serviceEpisodeResponse)
              .mpiResponse(mpiResponse)
              .build();
        });
  }

  @Test
  public void nullIcn() {
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
        TestUtils.createServiceHistoryResponse("emis/service-episodes/ascending.xml");
    PRPAIN201306UV02 mpiResponse = TestUtils.createMpiResponse("mpi/mpi_profile_response_body.xml");
    EMISdeploymentResponseType deployments =
        TestUtils.createDeploymentResponse("emis/deployments_response.xml");
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          VeteranServiceHistoryTransformer.builder()
              .icn(null)
              .deploymentResponse(deployments)
              .serviceEpisodeResponseType(serviceEpisodeResponse)
              .mpiResponse(mpiResponse)
              .build();
        });
  }

  @Test
  public void nullMpiResponse() {
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
        TestUtils.createServiceHistoryResponse("emis/service-episodes/ascending.xml");
    PRPAIN201306UV02 mpiResponse = TestUtils.createMpiResponse("mpi/mpi_profile_response_body.xml");
    EMISdeploymentResponseType deployments =
        TestUtils.createDeploymentResponse("emis/deployments_response.xml");
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          VeteranServiceHistoryTransformer.builder()
              .icn("uuid")
              .deploymentResponse(deployments)
              .serviceEpisodeResponseType(serviceEpisodeResponse)
              .mpiResponse(null)
              .build();
        });
  }

  @Test
  public void nullServiceEpisodeResponse() {
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
        TestUtils.createServiceHistoryResponse("emis/service-episodes/ascending.xml");
    PRPAIN201306UV02 mpiResponse = TestUtils.createMpiResponse("mpi/mpi_profile_response_body.xml");
    EMISdeploymentResponseType deployments =
        TestUtils.createDeploymentResponse("emis/deployments_response.xml");
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          VeteranServiceHistoryTransformer.builder()
              .icn("uuid")
              .deploymentResponse(deployments)
              .serviceEpisodeResponseType(null)
              .mpiResponse(mpiResponse)
              .build();
        });
  }

  @Test
  public void payGradeTests() {
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
        TestUtils.createServiceHistoryResponse(
            "emis/service-episodes/pay_grade_and_branch_of_service_scenarios.xml");
    PRPAIN201306UV02 mpiResponse = TestUtils.createMpiResponse("mpi/mpi_profile_response_body.xml");
    EMISdeploymentResponseType deployments =
        TestUtils.createDeploymentResponse("emis/deployments_response.xml");
    VeteranServiceHistoryTransformer transformer =
        VeteranServiceHistoryTransformer.builder()
            .icn("uuid")
            .deploymentResponse(deployments)
            .serviceEpisodeResponseType(serviceEpisodeResponse)
            .mpiResponse(mpiResponse)
            .build();
    ServiceHistoryResponse response = transformer.serviceHistoryTransformer();
    // Happy Path
    String happyPathExpected = "E05";
    String happyPathActual = response.data().get(0).attributes().payGrade();
    Assertions.assertEquals(happyPathExpected, happyPathActual);
    // Happy Path Extra Space
    String happyPathExtraSpaceExpected = "E05";
    String happyPathExtraSpaceActual = response.data().get(1).attributes().payGrade();
    Assertions.assertEquals(happyPathExtraSpaceExpected, happyPathExtraSpaceActual);
    // Pay Grade Code is Empty
    String payGradeCodeIsEmptyExpected = "unknown";
    String payGradeCodeIsEmptyActual = response.data().get(2).attributes().payGrade();
    Assertions.assertEquals(payGradeCodeIsEmptyExpected, payGradeCodeIsEmptyActual);
    // Pay Grade Code Is Null
    String payGradeCodeIsNullExpected = "unknown";
    String payGradeCodeIsNullActual = response.data().get(3).attributes().payGrade();
    Assertions.assertEquals(payGradeCodeIsNullExpected, payGradeCodeIsNullActual);
    // Pay Plan Code Is Empty
    String payPlanCodeIsEmptyExpected = "unknown";
    String payPlanCodeIsEmptyActual = response.data().get(4).attributes().payGrade();
    Assertions.assertEquals(payPlanCodeIsEmptyExpected, payPlanCodeIsEmptyActual);
    // Pay Plan Code Is Null
    String payPlanCodeIsNullExpected = "unknown";
    String payPlanCodeIsNullActual = response.data().get(5).attributes().payGrade();
    Assertions.assertEquals(payPlanCodeIsNullExpected, payPlanCodeIsNullActual);
  }
}
