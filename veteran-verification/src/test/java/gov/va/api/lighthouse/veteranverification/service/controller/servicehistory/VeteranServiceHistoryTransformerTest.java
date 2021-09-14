package gov.va.api.lighthouse.veteranverification.service.controller.servicehistory;

import gov.va.api.lighthouse.veteranverification.api.v0.ServiceHistoryResponse;
import gov.va.api.lighthouse.veteranverification.service.TestUtils;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISdeploymentResponseType;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISguardReserveServicePeriodsResponseType;
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
    EMISguardReserveServicePeriodsResponseType grasResponse =
        TestUtils.createGrasResponse("emis/gras/single_title_training_period.xml");
    PRPAIN201306UV02 mpiResponse = TestUtils.createMpiResponse("mpi/mpi_profile_response_body.xml");
    EMISdeploymentResponseType deployments =
        TestUtils.createDeploymentResponse("emis/deployments_response.xml");
    VeteranServiceHistoryTransformer transformer =
        VeteranServiceHistoryTransformer.builder()
            .icn("uuid")
            .deploymentResponse(deployments)
            .serviceEpisodeResponseType(serviceEpisodeResponse)
            .mpiResponse(mpiResponse)
            .grasResponse(grasResponse)
            .build();
    ServiceHistoryResponse response = transformer.serviceHistoryTransformer();

    // Happy Path Air Force No Category Expected
    // BranchOfServiceCode: F; PersonnelCategoryCode: A
    String happyPathAirForceNoCategoryExpected = "Air Force";
    String happyPathAirForceNoCategoryActual =
        response.data().get(0).attributes().branchOfService();
    Assertions.assertEquals(happyPathAirForceNoCategoryExpected, happyPathAirForceNoCategoryActual);
    // Happy Path Army No Category Expected
    // BranchOfServiceCode: A; PersonnelCategoryCode: A
    String happyPathArmyNoCategoryExpected = "Army";
    String happyPathArmyNoCategoryActual = response.data().get(1).attributes().branchOfService();
    Assertions.assertEquals(happyPathArmyNoCategoryExpected, happyPathArmyNoCategoryActual);
    // Happy Path Coast Guard No Category
    // BranchOfServiceCode: A; PersonnelCategoryCode: A
    String happyPathCoastGuardNoCategoryExpected = "Coast Guard";
    String happyPathCoastGuardNoCategoryActual =
        response.data().get(2).attributes().branchOfService();
    Assertions.assertEquals(
        happyPathCoastGuardNoCategoryExpected, happyPathCoastGuardNoCategoryActual);
    // Happy Path Marine No Category
    // BranchOfServiceCode: M; PersonnelCategoryCode: A
    String happyPathMarineNoCategoryExpected = "Marine Corps";
    String happyPathMarineNoCategoryActual = response.data().get(3).attributes().branchOfService();
    Assertions.assertEquals(happyPathMarineNoCategoryExpected, happyPathMarineNoCategoryActual);
    // Happy Path Noaa No Category
    // BranchOfServiceCode: O; PersonnelCategoryCode: A
    String happyPathNoaaNoCategoryExpected = "NOAA";
    String happyPathNoaaNoCategoryActual = response.data().get(4).attributes().branchOfService();
    Assertions.assertEquals(happyPathNoaaNoCategoryExpected, happyPathNoaaNoCategoryActual);
    // Happy Path Usphs No Category
    // BranchOfServiceCode: H; PersonnelCategoryCode: A
    String happyPathUsphsNoCategoryExpected = "Public Health Service";
    String happyPathUsphsNoCategoryActual = response.data().get(5).attributes().branchOfService();
    Assertions.assertEquals(happyPathUsphsNoCategoryExpected, happyPathUsphsNoCategoryActual);
    // Other Branch Of Service
    String otherBranchOfServiceExpected = "Unknown";
    String otherBranchOfServiceActual = response.data().get(6).attributes().branchOfService();
    Assertions.assertEquals(otherBranchOfServiceExpected, otherBranchOfServiceActual);
  }

  @Test
  public void happyPathCheckForTitle32GrasResponsesAscending() {
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
        TestUtils.createServiceHistoryResponse("emis/service-episodes/single_inactive.xml");
    EMISguardReserveServicePeriodsResponseType grasResponse =
        TestUtils.createGrasResponse("emis/gras/ascending.xml");
    PRPAIN201306UV02 mpiResponse = TestUtils.createMpiResponse("mpi/mpi_profile_response_body.xml");
    EMISdeploymentResponseType deployments =
        TestUtils.createDeploymentResponse("emis/deployments_response.xml");
    VeteranServiceHistoryTransformer transformer =
        VeteranServiceHistoryTransformer.builder()
            .icn("uuid")
            .deploymentResponse(deployments)
            .serviceEpisodeResponseType(serviceEpisodeResponse)
            .mpiResponse(mpiResponse)
            .grasResponse(grasResponse)
            .build();
    ServiceHistoryResponse response = transformer.serviceHistoryTransformer();
    Assertions.assertEquals(4, response.data().size());
    ServiceHistoryResponse.ServiceHistoryEpisode episodeOne = response.data().get(0);
    Assertions.assertEquals("74f6658f-d17c-5182-917b-1a409fcb77b0", episodeOne.id());
    Assertions.assertEquals("service-history-episodes", episodeOne.type());
    ServiceHistoryResponse.ServiceHistoryAttributes attributesOne = episodeOne.attributes();
    Assertions.assertEquals("Alfredo", attributesOne.firstName());
    Assertions.assertEquals("Armstrong", attributesOne.lastName());
    // branchOfService is N
    Assertions.assertEquals("National Guard", attributesOne.branchOfService());
    Assertions.assertEquals("2012-01-01", attributesOne.startDate().toString());
    Assertions.assertEquals("2013-02-01", attributesOne.endDate().toString());
    Assertions.assertNull(attributesOne.payGrade());
    Assertions.assertEquals(
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus
            .HONORABLE_ABSENCE_OF_NEGATIVE_REPORT,
        attributesOne.dischargeStatus());
    Assertions.assertEquals("SUFFICIENT SERVICE FOR RETIREMENT", attributesOne.separationReason());
    Assertions.assertEquals(attributesOne.deployments().stream().count(), 0);

    ServiceHistoryResponse.ServiceHistoryEpisode episodeTwo = response.data().get(1);
    Assertions.assertEquals("a0682550-f285-50d9-98ce-7a83982efe37", episodeTwo.id());
    Assertions.assertEquals("service-history-episodes", episodeTwo.type());
    ServiceHistoryResponse.ServiceHistoryAttributes attributesTwo = episodeTwo.attributes();
    Assertions.assertEquals("Alfredo", attributesTwo.firstName());
    Assertions.assertEquals("Armstrong", attributesTwo.lastName());
    // branchOfService is V
    Assertions.assertEquals("Reserve", attributesTwo.branchOfService());
    Assertions.assertEquals("2014-01-01", attributesTwo.startDate().toString());
    Assertions.assertEquals("2015-02-01", attributesTwo.endDate().toString());
    Assertions.assertNull(attributesTwo.payGrade());
    Assertions.assertEquals(
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus
            .HONORABLE_ABSENCE_OF_NEGATIVE_REPORT,
        attributesTwo.dischargeStatus());
    Assertions.assertEquals("SUFFICIENT SERVICE FOR RETIREMENT", attributesTwo.separationReason());
    Assertions.assertEquals(attributesTwo.deployments().stream().count(), 0);

    ServiceHistoryResponse.ServiceHistoryEpisode episodeThree = response.data().get(2);
    Assertions.assertEquals("b687a52c-11a5-5095-9d01-b09b55dbb1b2", episodeThree.id());
    Assertions.assertEquals("service-history-episodes", episodeThree.type());
    ServiceHistoryResponse.ServiceHistoryAttributes attributesThree = episodeThree.attributes();
    Assertions.assertEquals("Alfredo", attributesThree.firstName());
    Assertions.assertEquals("Armstrong", attributesThree.lastName());
    // branchOfService is Q
    Assertions.assertEquals("Reserve", attributesThree.branchOfService());
    Assertions.assertEquals("2016-01-01", attributesThree.startDate().toString());
    Assertions.assertEquals("2017-02-01", attributesThree.endDate().toString());
    Assertions.assertNull(attributesThree.payGrade());
    Assertions.assertEquals(
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus
            .HONORABLE_ABSENCE_OF_NEGATIVE_REPORT,
        attributesThree.dischargeStatus());
    Assertions.assertEquals(
        "SUFFICIENT SERVICE FOR RETIREMENT", attributesThree.separationReason());
    Assertions.assertEquals(attributesThree.deployments().stream().count(), 0);

    ServiceHistoryResponse.ServiceHistoryEpisode episodeFour = response.data().get(3);
    Assertions.assertEquals("efc43e85-f327-5bbf-9d02-3b861e1c5a9a", episodeFour.id());
    Assertions.assertEquals("service-history-episodes", episodeFour.type());
    ServiceHistoryResponse.ServiceHistoryAttributes attributesFour = episodeFour.attributes();
    Assertions.assertEquals("Alfredo", attributesFour.firstName());
    Assertions.assertEquals("Armstrong", attributesFour.lastName());
    // branchOfService is Other
    Assertions.assertNull(attributesFour.branchOfService());
    Assertions.assertEquals("2018-01-01", attributesFour.startDate().toString());
    Assertions.assertEquals("2019-02-01", attributesFour.endDate().toString());
    Assertions.assertNull(attributesFour.payGrade());
    Assertions.assertEquals(
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus
            .HONORABLE_ABSENCE_OF_NEGATIVE_REPORT,
        attributesFour.dischargeStatus());
    Assertions.assertEquals("SUFFICIENT SERVICE FOR RETIREMENT", attributesFour.separationReason());
    Assertions.assertEquals(attributesFour.deployments().stream().count(), 0);
  }

  @Test
  public void happyPathCheckForTitle32GrasResponsesDescending() {
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
        TestUtils.createServiceHistoryResponse("emis/service-episodes/single_inactive.xml");
    EMISguardReserveServicePeriodsResponseType grasResponse =
        TestUtils.createGrasResponse("emis/gras/descending.xml");
    PRPAIN201306UV02 mpiResponse = TestUtils.createMpiResponse("mpi/mpi_profile_response_body.xml");
    EMISdeploymentResponseType deployments =
        TestUtils.createDeploymentResponse("emis/deployments_response.xml");
    VeteranServiceHistoryTransformer transformer =
        VeteranServiceHistoryTransformer.builder()
            .icn("uuid")
            .deploymentResponse(deployments)
            .serviceEpisodeResponseType(serviceEpisodeResponse)
            .mpiResponse(mpiResponse)
            .grasResponse(grasResponse)
            .build();
    ServiceHistoryResponse response = transformer.serviceHistoryTransformer();
    Assertions.assertEquals(4, response.data().size());
    ServiceHistoryResponse.ServiceHistoryEpisode episodeOne = response.data().get(0);
    Assertions.assertEquals("74f6658f-d17c-5182-917b-1a409fcb77b0", episodeOne.id());
    Assertions.assertEquals("service-history-episodes", episodeOne.type());
    ServiceHistoryResponse.ServiceHistoryAttributes attributesOne = episodeOne.attributes();
    Assertions.assertEquals("Alfredo", attributesOne.firstName());
    Assertions.assertEquals("Armstrong", attributesOne.lastName());
    // branchOfService is N
    Assertions.assertEquals("National Guard", attributesOne.branchOfService());
    Assertions.assertEquals("2012-01-01", attributesOne.startDate().toString());
    Assertions.assertEquals("2013-02-01", attributesOne.endDate().toString());
    Assertions.assertNull(attributesOne.payGrade());
    Assertions.assertEquals(
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus
            .HONORABLE_ABSENCE_OF_NEGATIVE_REPORT,
        attributesOne.dischargeStatus());
    Assertions.assertEquals("SUFFICIENT SERVICE FOR RETIREMENT", attributesOne.separationReason());
    Assertions.assertEquals(attributesOne.deployments().stream().count(), 0);

    ServiceHistoryResponse.ServiceHistoryEpisode episodeTwo = response.data().get(1);
    Assertions.assertEquals("a0682550-f285-50d9-98ce-7a83982efe37", episodeTwo.id());
    Assertions.assertEquals("service-history-episodes", episodeTwo.type());
    ServiceHistoryResponse.ServiceHistoryAttributes attributesTwo = episodeTwo.attributes();
    Assertions.assertEquals("Alfredo", attributesTwo.firstName());
    Assertions.assertEquals("Armstrong", attributesTwo.lastName());
    // branchOfService is V
    Assertions.assertEquals("Reserve", attributesTwo.branchOfService());
    Assertions.assertEquals("2014-01-01", attributesTwo.startDate().toString());
    Assertions.assertEquals("2015-02-01", attributesTwo.endDate().toString());
    Assertions.assertNull(attributesTwo.payGrade());
    Assertions.assertEquals(
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus
            .HONORABLE_ABSENCE_OF_NEGATIVE_REPORT,
        attributesTwo.dischargeStatus());
    Assertions.assertEquals("SUFFICIENT SERVICE FOR RETIREMENT", attributesTwo.separationReason());
    Assertions.assertEquals(attributesTwo.deployments().stream().count(), 0);

    ServiceHistoryResponse.ServiceHistoryEpisode episodeThree = response.data().get(2);
    Assertions.assertEquals("b687a52c-11a5-5095-9d01-b09b55dbb1b2", episodeThree.id());
    Assertions.assertEquals("service-history-episodes", episodeThree.type());
    ServiceHistoryResponse.ServiceHistoryAttributes attributesThree = episodeThree.attributes();
    Assertions.assertEquals("Alfredo", attributesThree.firstName());
    Assertions.assertEquals("Armstrong", attributesThree.lastName());
    // branchOfService is Q
    Assertions.assertEquals("Reserve", attributesThree.branchOfService());
    Assertions.assertEquals("2016-01-01", attributesThree.startDate().toString());
    Assertions.assertEquals("2017-02-01", attributesThree.endDate().toString());
    Assertions.assertNull(attributesThree.payGrade());
    Assertions.assertEquals(
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus
            .HONORABLE_ABSENCE_OF_NEGATIVE_REPORT,
        attributesThree.dischargeStatus());
    Assertions.assertEquals(
        "SUFFICIENT SERVICE FOR RETIREMENT", attributesThree.separationReason());
    Assertions.assertEquals(attributesThree.deployments().stream().count(), 0);

    ServiceHistoryResponse.ServiceHistoryEpisode episodeFour = response.data().get(3);
    Assertions.assertEquals("efc43e85-f327-5bbf-9d02-3b861e1c5a9a", episodeFour.id());
    Assertions.assertEquals("service-history-episodes", episodeFour.type());
    ServiceHistoryResponse.ServiceHistoryAttributes attributesFour = episodeFour.attributes();
    Assertions.assertEquals("Alfredo", attributesFour.firstName());
    Assertions.assertEquals("Armstrong", attributesFour.lastName());
    // branchOfService is Other
    Assertions.assertNull(attributesFour.branchOfService());
    Assertions.assertEquals("2018-01-01", attributesFour.startDate().toString());
    Assertions.assertEquals("2019-02-01", attributesFour.endDate().toString());
    Assertions.assertNull(attributesFour.payGrade());
    Assertions.assertEquals(
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus
            .HONORABLE_ABSENCE_OF_NEGATIVE_REPORT,
        attributesFour.dischargeStatus());
    Assertions.assertEquals("SUFFICIENT SERVICE FOR RETIREMENT", attributesFour.separationReason());
    Assertions.assertEquals(attributesFour.deployments().stream().count(), 0);
  }

  @Test
  public void happyPathDeploymentDataIsNull() {
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
        TestUtils.createServiceHistoryResponse("emis/service-episodes/ascending.xml");
    EMISguardReserveServicePeriodsResponseType grasResponse =
        TestUtils.createGrasResponse("emis/gras/single_title_training_period.xml");
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
            .grasResponse(grasResponse)
            .build();
    ServiceHistoryResponse response = transformer.serviceHistoryTransformer();
    // This checks that non active responses are filtered out
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
    EMISguardReserveServicePeriodsResponseType grasResponse =
        TestUtils.createGrasResponse("emis/gras/single_title_training_period.xml");
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
            .grasResponse(grasResponse)
            .build();
    ServiceHistoryResponse response = transformer.serviceHistoryTransformer();
    // This checks that non active responses are filtered out
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
    EMISguardReserveServicePeriodsResponseType grasResponse =
        TestUtils.createGrasResponse("emis/gras/single_title_training_period.xml");
    PRPAIN201306UV02 mpiResponse = TestUtils.createMpiResponse("mpi/mpi_profile_response_body.xml");
    EMISdeploymentResponseType deployments =
        TestUtils.createDeploymentResponse("emis/deployments_response.xml");
    VeteranServiceHistoryTransformer transformer =
        VeteranServiceHistoryTransformer.builder()
            .icn("uuid")
            .deploymentResponse(deployments)
            .serviceEpisodeResponseType(serviceEpisodeResponse)
            .mpiResponse(mpiResponse)
            .grasResponse(grasResponse)
            .build();
    ServiceHistoryResponse response = transformer.serviceHistoryTransformer();
    // This checks that non active responses are filtered out
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
    EMISguardReserveServicePeriodsResponseType grasResponse =
        TestUtils.createGrasResponse("emis/gras/single_title_training_period.xml");
    PRPAIN201306UV02 mpiResponse = TestUtils.createMpiResponse("mpi/mpi_profile_response_body.xml");
    EMISdeploymentResponseType deployments =
        TestUtils.createDeploymentResponse("emis/deployments_response.xml");
    VeteranServiceHistoryTransformer transformer =
        VeteranServiceHistoryTransformer.builder()
            .icn("uuid")
            .deploymentResponse(deployments)
            .serviceEpisodeResponseType(serviceEpisodeResponse)
            .mpiResponse(mpiResponse)
            .grasResponse(grasResponse)
            .build();
    ServiceHistoryResponse response = transformer.serviceHistoryTransformer();
    // This checks that non active responses are filtered out
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
    EMISguardReserveServicePeriodsResponseType grasResponse =
        TestUtils.createGrasResponse("emis/gras/single_title_training_period.xml");
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
            .grasResponse(grasResponse)
            .build();
    ServiceHistoryResponse response = transformer.serviceHistoryTransformer();
    // This checks that non active responses are filtered out
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
    EMISguardReserveServicePeriodsResponseType grasResponse =
        TestUtils.createGrasResponse("emis/gras/single_title_training_period.xml");
    PRPAIN201306UV02 mpiResponse = TestUtils.createMpiResponse("mpi/mpi_profile_response_body.xml");
    EMISdeploymentResponseType deployments =
        TestUtils.createDeploymentResponse("emis/deployments_response.xml");
    serviceEpisodeResponse
        .getMilitaryServiceEpisode()
        .get(2)
        .getMilitaryServiceEpisodeData()
        .setServiceEpisodeEndDate(null);
    VeteranServiceHistoryTransformer transformer =
        VeteranServiceHistoryTransformer.builder()
            .icn("uuid")
            .deploymentResponse(deployments)
            .serviceEpisodeResponseType(serviceEpisodeResponse)
            .mpiResponse(mpiResponse)
            .grasResponse(grasResponse)
            .build();
    ServiceHistoryResponse response = transformer.serviceHistoryTransformer();
    // This checks that non active responses are filtered out
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
    EMISguardReserveServicePeriodsResponseType grasResponse =
        TestUtils.createGrasResponse("emis/gras/single_title_training_period.xml");
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
              .grasResponse(grasResponse)
              .build();
        });
  }

  @Test
  public void nullGrasResponse() {
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
        TestUtils.createServiceHistoryResponse("emis/service-episodes/single_inactive.xml");
    PRPAIN201306UV02 mpiResponse = TestUtils.createMpiResponse("mpi/mpi_profile_response_body.xml");
    EMISdeploymentResponseType deployments =
        TestUtils.createDeploymentResponse("emis/deployments_response.xml");
    VeteranServiceHistoryTransformer transformer =
        VeteranServiceHistoryTransformer.builder()
            .icn("uuid")
            .deploymentResponse(deployments)
            .serviceEpisodeResponseType(serviceEpisodeResponse)
            .mpiResponse(mpiResponse)
            .grasResponse(null)
            .build();
    ServiceHistoryResponse response = transformer.serviceHistoryTransformer();
    // No error should be thrown
    Assertions.assertEquals(response.data().size(), 0);
  }

  @Test
  public void nullIcn() {
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
        TestUtils.createServiceHistoryResponse("emis/service-episodes/ascending.xml");
    EMISguardReserveServicePeriodsResponseType grasResponse =
        TestUtils.createGrasResponse("emis/gras/single_title_training_period.xml");
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
              .grasResponse(grasResponse)
              .build();
        });
  }

  @Test
  public void nullMpiResponse() {
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
        TestUtils.createServiceHistoryResponse("emis/service-episodes/ascending.xml");
    EMISguardReserveServicePeriodsResponseType grasResponse =
        TestUtils.createGrasResponse("emis/gras/single_title_training_period.xml");
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
              .grasResponse(grasResponse)
              .build();
        });
  }

  @Test
  public void nullServiceEpisodeResponse() {
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
        TestUtils.createServiceHistoryResponse("emis/service-episodes/ascending.xml");
    EMISguardReserveServicePeriodsResponseType grasResponse =
        TestUtils.createGrasResponse("emis/gras/single_title_training_period.xml");
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
              .grasResponse(grasResponse)
              .build();
        });
  }

  @Test
  public void payGradeTests() {
    EMISserviceEpisodeResponseType serviceEpisodeResponse =
        TestUtils.createServiceHistoryResponse(
            "emis/service-episodes/pay_grade_and_branch_of_service_scenarios.xml");
    EMISguardReserveServicePeriodsResponseType grasResponse =
        TestUtils.createGrasResponse("emis/gras/single_title_training_period.xml");
    PRPAIN201306UV02 mpiResponse = TestUtils.createMpiResponse("mpi/mpi_profile_response_body.xml");
    EMISdeploymentResponseType deployments =
        TestUtils.createDeploymentResponse("emis/deployments_response.xml");
    VeteranServiceHistoryTransformer transformer =
        VeteranServiceHistoryTransformer.builder()
            .icn("uuid")
            .deploymentResponse(deployments)
            .serviceEpisodeResponseType(serviceEpisodeResponse)
            .mpiResponse(mpiResponse)
            .grasResponse(grasResponse)
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
