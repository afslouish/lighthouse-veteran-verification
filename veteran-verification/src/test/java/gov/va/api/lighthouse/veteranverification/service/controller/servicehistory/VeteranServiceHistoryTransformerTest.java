package gov.va.api.lighthouse.veteranverification.service.controller.servicehistory;

import gov.va.api.lighthouse.veteranverification.api.v1.ServiceHistoryResponse;
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
    Assertions.assertEquals("f15059f7-963a-5c02-b97b-18888d0496c1", episodeOne.id());
    Assertions.assertEquals("service-history-episodes", episodeOne.type());
    ServiceHistoryResponse.ServiceHistoryAttributes attributesOne = episodeOne.attributes();
    Assertions.assertEquals("Alfredo", attributesOne.firstName());
    Assertions.assertEquals("Armstrong", attributesOne.lastName());
    // personnelCategoryTypeCode is N
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
    Assertions.assertEquals("c40dc18f-52b3-5c2a-97d3-78fa62a836fe", episodeTwo.id());
    Assertions.assertEquals("service-history-episodes", episodeTwo.type());
    ServiceHistoryResponse.ServiceHistoryAttributes attributesTwo = episodeTwo.attributes();
    Assertions.assertEquals("Alfredo", attributesTwo.firstName());
    Assertions.assertEquals("Armstrong", attributesTwo.lastName());
    // personnelCategoryTypeCode is V
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
    Assertions.assertEquals("0564c93f-6409-58cb-a868-0bcf201b7dac", episodeThree.id());
    Assertions.assertEquals("service-history-episodes", episodeThree.type());
    ServiceHistoryResponse.ServiceHistoryAttributes attributesThree = episodeThree.attributes();
    Assertions.assertEquals("Alfredo", attributesThree.firstName());
    Assertions.assertEquals("Armstrong", attributesThree.lastName());
    // personnelCategoryTypeCode is Q
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
    Assertions.assertEquals("178f9eb0-9552-5751-92ae-83edfb13ac2d", episodeFour.id());
    Assertions.assertEquals("service-history-episodes", episodeFour.type());
    ServiceHistoryResponse.ServiceHistoryAttributes attributesFour = episodeFour.attributes();
    Assertions.assertEquals("Alfredo", attributesFour.firstName());
    Assertions.assertEquals("Armstrong", attributesFour.lastName());
    // personnelCategoryTypeCode is Other
    Assertions.assertEquals("Unknown", attributesFour.branchOfService());
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
    Assertions.assertEquals("f15059f7-963a-5c02-b97b-18888d0496c1", episodeOne.id());
    Assertions.assertEquals("service-history-episodes", episodeOne.type());
    ServiceHistoryResponse.ServiceHistoryAttributes attributesOne = episodeOne.attributes();
    Assertions.assertEquals("Alfredo", attributesOne.firstName());
    Assertions.assertEquals("Armstrong", attributesOne.lastName());
    // personnelCategoryTypeCode is N
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
    Assertions.assertEquals("c40dc18f-52b3-5c2a-97d3-78fa62a836fe", episodeTwo.id());
    Assertions.assertEquals("service-history-episodes", episodeTwo.type());
    ServiceHistoryResponse.ServiceHistoryAttributes attributesTwo = episodeTwo.attributes();
    Assertions.assertEquals("Alfredo", attributesTwo.firstName());
    Assertions.assertEquals("Armstrong", attributesTwo.lastName());
    // personnelCategoryTypeCode is V
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
    Assertions.assertEquals("0564c93f-6409-58cb-a868-0bcf201b7dac", episodeThree.id());
    Assertions.assertEquals("service-history-episodes", episodeThree.type());
    ServiceHistoryResponse.ServiceHistoryAttributes attributesThree = episodeThree.attributes();
    Assertions.assertEquals("Alfredo", attributesThree.firstName());
    Assertions.assertEquals("Armstrong", attributesThree.lastName());
    // personnelCategoryTypeCode is Q
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
    Assertions.assertEquals("178f9eb0-9552-5751-92ae-83edfb13ac2d", episodeFour.id());
    Assertions.assertEquals("service-history-episodes", episodeFour.type());
    ServiceHistoryResponse.ServiceHistoryAttributes attributesFour = episodeFour.attributes();
    Assertions.assertEquals("Alfredo", attributesFour.firstName());
    Assertions.assertEquals("Armstrong", attributesFour.lastName());
    // personnelCategoryTypeCode is Other
    Assertions.assertEquals("Unknown", attributesFour.branchOfService());
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
    Assertions.assertEquals("fb8d1335-6ad4-5155-a2e1-be9f6c2af42f", episodeOne.id());
    Assertions.assertEquals("service-history-episodes", episodeOne.type());
    Assertions.assertEquals("3d72b5cc-afca-5190-9dbf-0f5c97f80b6c", episodeTwo.id());
    Assertions.assertEquals("service-history-episodes", episodeTwo.type());
    ServiceHistoryResponse.ServiceHistoryAttributes attributesOne = episodeOne.attributes();
    ServiceHistoryResponse.ServiceHistoryAttributes attributesTwo = episodeTwo.attributes();
    Assertions.assertEquals("Alfredo", attributesOne.firstName());
    Assertions.assertEquals("Armstrong", attributesOne.lastName());
    Assertions.assertEquals("Army", attributesOne.branchOfService().toString());
    Assertions.assertEquals("2000-01-01", attributesOne.startDate().toString());
    Assertions.assertEquals(attributesOne.endDate().toString(), "2001-02-01");
    Assertions.assertEquals("E05", attributesOne.payGrade().toString());
    Assertions.assertEquals(
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.HONORABLE,
        attributesOne.dischargeStatus());
    Assertions.assertEquals("SUFFICIENT SERVICE FOR RETIREMENT", attributesOne.separationReason());
    Assertions.assertEquals(0, attributesOne.deployments().stream().count());
    Assertions.assertEquals("Alfredo", attributesTwo.firstName());
    Assertions.assertEquals("Armstrong", attributesTwo.lastName());
    Assertions.assertEquals("Air Force", attributesTwo.branchOfService().toString());
    Assertions.assertEquals("2002-01-01", attributesTwo.startDate().toString());
    Assertions.assertEquals("2003-02-01", attributesTwo.endDate().toString());
    Assertions.assertEquals("E05", attributesTwo.payGrade().toString());
    Assertions.assertEquals(
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.HONORABLE,
        attributesTwo.dischargeStatus());
    Assertions.assertEquals("SUFFICIENT SERVICE FOR RETIREMENT", attributesTwo.separationReason());
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
    Assertions.assertEquals(episodeOne.id(), "fb8d1335-6ad4-5155-a2e1-be9f6c2af42f");
    Assertions.assertEquals("service-history-episodes", episodeOne.type());
    Assertions.assertEquals(episodeTwo.id(), "3d72b5cc-afca-5190-9dbf-0f5c97f80b6c");
    Assertions.assertEquals("service-history-episodes", episodeTwo.type());
    ServiceHistoryResponse.ServiceHistoryAttributes attributesOne = episodeOne.attributes();
    ServiceHistoryResponse.ServiceHistoryAttributes attributesTwo = episodeTwo.attributes();
    Assertions.assertEquals("Alfredo", attributesOne.firstName());
    Assertions.assertEquals("Armstrong", attributesOne.lastName());
    Assertions.assertEquals("Army", attributesOne.branchOfService().toString());
    Assertions.assertEquals("2000-01-01", attributesOne.startDate().toString());
    Assertions.assertEquals(attributesOne.endDate().toString(), "2001-02-01");
    Assertions.assertEquals("E05", attributesOne.payGrade().toString());
    Assertions.assertEquals(
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.HONORABLE,
        attributesOne.dischargeStatus());
    Assertions.assertEquals("SUFFICIENT SERVICE FOR RETIREMENT", attributesOne.separationReason());
    Assertions.assertEquals(1, attributesOne.deployments().stream().count());
    Assertions.assertEquals(
        "2000-02-01", attributesOne.deployments().get(0).startDate().toString());
    Assertions.assertEquals("2001-01-01", attributesOne.deployments().get(0).endDate().toString());
    Assertions.assertEquals(attributesOne.deployments().get(0).location(), null);
    Assertions.assertEquals("Alfredo", attributesTwo.firstName());
    Assertions.assertEquals("Armstrong", attributesTwo.lastName());
    Assertions.assertEquals("Air Force", attributesTwo.branchOfService().toString());
    Assertions.assertEquals("2002-01-01", attributesTwo.startDate().toString());
    Assertions.assertEquals("2003-02-01", attributesTwo.endDate().toString());
    Assertions.assertEquals("E05", attributesTwo.payGrade().toString());
    Assertions.assertEquals(
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.HONORABLE,
        attributesTwo.dischargeStatus());
    Assertions.assertEquals("SUFFICIENT SERVICE FOR RETIREMENT", attributesTwo.separationReason());
    Assertions.assertEquals(1, attributesTwo.deployments().stream().count());
    Assertions.assertEquals(
        "2002-02-01", attributesTwo.deployments().get(0).startDate().toString());
    Assertions.assertEquals("2003-01-01", attributesTwo.deployments().get(0).endDate().toString());
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
    Assertions.assertEquals("fb8d1335-6ad4-5155-a2e1-be9f6c2af42f", episodeOne.id());
    Assertions.assertEquals("service-history-episodes", episodeOne.type());
    Assertions.assertEquals("3d72b5cc-afca-5190-9dbf-0f5c97f80b6c", episodeTwo.id());
    Assertions.assertEquals("service-history-episodes", episodeTwo.type());
    ServiceHistoryResponse.ServiceHistoryAttributes attributesOne = episodeOne.attributes();
    ServiceHistoryResponse.ServiceHistoryAttributes attributesTwo = episodeTwo.attributes();
    Assertions.assertEquals("Alfredo", attributesOne.firstName());
    Assertions.assertEquals("Armstrong", attributesOne.lastName());
    Assertions.assertEquals("Army", attributesOne.branchOfService().toString());
    Assertions.assertEquals("2000-01-01", attributesOne.startDate().toString());
    Assertions.assertEquals(attributesOne.endDate().toString(), "2001-02-01");
    Assertions.assertEquals("E05", attributesOne.payGrade().toString());
    Assertions.assertEquals(
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.HONORABLE,
        attributesOne.dischargeStatus());
    Assertions.assertEquals("SUFFICIENT SERVICE FOR RETIREMENT", attributesOne.separationReason());
    Assertions.assertEquals(1, attributesOne.deployments().stream().count());
    Assertions.assertEquals(
        "2000-02-01", attributesOne.deployments().get(0).startDate().toString());
    Assertions.assertEquals("2001-01-01", attributesOne.deployments().get(0).endDate().toString());
    Assertions.assertEquals("QAT", attributesOne.deployments().get(0).location());
    Assertions.assertEquals("Alfredo", attributesTwo.firstName());
    Assertions.assertEquals("Armstrong", attributesTwo.lastName());
    Assertions.assertEquals("Air Force", attributesTwo.branchOfService().toString());
    Assertions.assertEquals("2002-01-01", attributesTwo.startDate().toString());
    Assertions.assertEquals("2003-02-01", attributesTwo.endDate().toString());
    Assertions.assertEquals("E05", attributesTwo.payGrade().toString());
    Assertions.assertEquals(
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.HONORABLE,
        attributesTwo.dischargeStatus());
    Assertions.assertEquals("SUFFICIENT SERVICE FOR RETIREMENT", attributesTwo.separationReason());
    Assertions.assertEquals(1, attributesTwo.deployments().stream().count());
    Assertions.assertEquals(
        "2002-02-01", attributesTwo.deployments().get(0).startDate().toString());
    Assertions.assertEquals("2003-01-01", attributesTwo.deployments().get(0).endDate().toString());
    Assertions.assertEquals("QAT", attributesTwo.deployments().get(0).location());
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
    Assertions.assertEquals(episodeOne.id(), "fb8d1335-6ad4-5155-a2e1-be9f6c2af42f");
    Assertions.assertEquals("service-history-episodes", episodeOne.type());
    Assertions.assertEquals(episodeTwo.id(), "3d72b5cc-afca-5190-9dbf-0f5c97f80b6c");
    Assertions.assertEquals("service-history-episodes", episodeTwo.type());
    ServiceHistoryResponse.ServiceHistoryAttributes attributesOne = episodeOne.attributes();
    ServiceHistoryResponse.ServiceHistoryAttributes attributesTwo = episodeTwo.attributes();
    Assertions.assertEquals("Alfredo", attributesOne.firstName());
    Assertions.assertEquals("Armstrong", attributesOne.lastName());
    Assertions.assertEquals("Army", attributesOne.branchOfService().toString());
    Assertions.assertEquals("2000-01-01", attributesOne.startDate().toString());
    Assertions.assertEquals(attributesOne.endDate().toString(), "2001-02-01");
    Assertions.assertEquals("E05", attributesOne.payGrade().toString());
    Assertions.assertEquals(
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.HONORABLE,
        attributesOne.dischargeStatus());
    Assertions.assertEquals("SUFFICIENT SERVICE FOR RETIREMENT", attributesOne.separationReason());
    Assertions.assertEquals(1, attributesOne.deployments().stream().count());
    Assertions.assertEquals(
        "2000-02-01", attributesOne.deployments().get(0).startDate().toString());
    Assertions.assertEquals("2001-01-01", attributesOne.deployments().get(0).endDate().toString());
    Assertions.assertEquals("QAT", attributesOne.deployments().get(0).location());
    Assertions.assertEquals("Alfredo", attributesTwo.firstName());
    Assertions.assertEquals("Armstrong", attributesTwo.lastName());
    Assertions.assertEquals("Air Force", attributesTwo.branchOfService().toString());
    Assertions.assertEquals("2002-01-01", attributesTwo.startDate().toString());
    Assertions.assertEquals("2003-02-01", attributesTwo.endDate().toString());
    Assertions.assertEquals("E05", attributesTwo.payGrade().toString());
    Assertions.assertEquals(
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.HONORABLE,
        attributesTwo.dischargeStatus());
    Assertions.assertEquals("SUFFICIENT SERVICE FOR RETIREMENT", attributesTwo.separationReason());
    Assertions.assertEquals(1, attributesTwo.deployments().stream().count());
    Assertions.assertEquals(
        "2002-02-01", attributesTwo.deployments().get(0).startDate().toString());
    Assertions.assertEquals("2003-01-01", attributesTwo.deployments().get(0).endDate().toString());
    Assertions.assertEquals("QAT", attributesTwo.deployments().get(0).location());
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
    Assertions.assertEquals("09b4ef73-68af-5a53-900e-012716f45fd1", episodeOne.id());
    Assertions.assertEquals("service-history-episodes", episodeOne.type());
    Assertions.assertEquals("3d72b5cc-afca-5190-9dbf-0f5c97f80b6c", episodeTwo.id());
    Assertions.assertEquals("service-history-episodes", episodeTwo.type());
    ServiceHistoryResponse.ServiceHistoryAttributes attributesOne = episodeOne.attributes();
    ServiceHistoryResponse.ServiceHistoryAttributes attributesTwo = episodeTwo.attributes();
    Assertions.assertEquals("Alfredo", attributesOne.firstName());
    Assertions.assertEquals("Armstrong", attributesOne.lastName());
    Assertions.assertEquals("Army", attributesOne.branchOfService().toString());
    Assertions.assertEquals("2000-01-01", attributesOne.startDate().toString());
    Assertions.assertNull(attributesOne.endDate());
    Assertions.assertEquals("E05", attributesOne.payGrade().toString());
    Assertions.assertEquals(
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.HONORABLE,
        attributesOne.dischargeStatus());
    Assertions.assertEquals("SUFFICIENT SERVICE FOR RETIREMENT", attributesOne.separationReason());
    Assertions.assertEquals(4, attributesOne.deployments().stream().count());
    Assertions.assertEquals(
        "2000-02-01", attributesOne.deployments().get(0).startDate().toString());
    Assertions.assertEquals("2001-01-01", attributesOne.deployments().get(0).endDate().toString());
    Assertions.assertEquals("QAT", attributesOne.deployments().get(0).location());
    Assertions.assertEquals(
        attributesOne.deployments().get(1).startDate().toString(), "2000-03-01");
    Assertions.assertEquals("2003-01-01", attributesOne.deployments().get(1).endDate().toString());
    Assertions.assertEquals("QAT", attributesOne.deployments().get(1).location());
    Assertions.assertEquals(
        "2000-03-01", attributesOne.deployments().get(1).startDate().toString());
    Assertions.assertEquals("2003-01-01", attributesOne.deployments().get(1).endDate().toString());
    Assertions.assertEquals("QAT", attributesOne.deployments().get(1).location());
    Assertions.assertEquals(
        "2004-02-01", attributesOne.deployments().get(2).startDate().toString());
    Assertions.assertEquals("2005-01-01", attributesOne.deployments().get(2).endDate().toString());
    Assertions.assertEquals("AX1", attributesOne.deployments().get(2).location());
    Assertions.assertEquals(
        "2006-02-01", attributesOne.deployments().get(3).startDate().toString());
    Assertions.assertEquals("2007-01-01", attributesOne.deployments().get(3).endDate().toString());
    Assertions.assertEquals("AX1", attributesOne.deployments().get(3).location());
    Assertions.assertEquals("Alfredo", attributesTwo.firstName());
    Assertions.assertEquals("Armstrong", attributesTwo.lastName());
    Assertions.assertEquals("Air Force", attributesTwo.branchOfService().toString());
    Assertions.assertEquals("2002-01-01", attributesTwo.startDate().toString());
    Assertions.assertEquals("2003-02-01", attributesTwo.endDate().toString());
    Assertions.assertEquals("E05", attributesTwo.payGrade().toString());
    Assertions.assertEquals(
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.HONORABLE,
        attributesTwo.dischargeStatus());
    Assertions.assertEquals("SUFFICIENT SERVICE FOR RETIREMENT", attributesTwo.separationReason());
    Assertions.assertEquals(1, attributesTwo.deployments().stream().count());
    Assertions.assertEquals(
        "2002-02-01", attributesTwo.deployments().get(0).startDate().toString());
    Assertions.assertEquals("2003-01-01", attributesTwo.deployments().get(0).endDate().toString());
    Assertions.assertEquals("QAT", attributesTwo.deployments().get(0).location());
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
    Assertions.assertEquals("09b4ef73-68af-5a53-900e-012716f45fd1", episodeOne.id());
    Assertions.assertEquals("service-history-episodes", episodeOne.type());
    Assertions.assertEquals("3d72b5cc-afca-5190-9dbf-0f5c97f80b6c", episodeTwo.id());
    Assertions.assertEquals("service-history-episodes", episodeTwo.type());
    ServiceHistoryResponse.ServiceHistoryAttributes attributesOne = episodeOne.attributes();
    ServiceHistoryResponse.ServiceHistoryAttributes attributesTwo = episodeTwo.attributes();
    Assertions.assertEquals("Alfredo", attributesOne.firstName());
    Assertions.assertEquals("Armstrong", attributesOne.lastName());
    Assertions.assertEquals("Army", attributesOne.branchOfService().toString());
    Assertions.assertEquals("2000-01-01", attributesOne.startDate().toString());
    Assertions.assertNull(attributesOne.endDate());
    Assertions.assertEquals("E05", attributesOne.payGrade().toString());
    Assertions.assertEquals(
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.HONORABLE,
        attributesOne.dischargeStatus());
    Assertions.assertEquals("SUFFICIENT SERVICE FOR RETIREMENT", attributesOne.separationReason());
    Assertions.assertEquals(4, attributesOne.deployments().stream().count());
    Assertions.assertEquals(
        "2000-02-01", attributesOne.deployments().get(0).startDate().toString());
    Assertions.assertEquals("2001-01-01", attributesOne.deployments().get(0).endDate().toString());
    Assertions.assertEquals("QAT", attributesOne.deployments().get(0).location());
    Assertions.assertEquals(
        attributesOne.deployments().get(1).startDate().toString(), "2000-03-01");
    Assertions.assertEquals("2003-01-01", attributesOne.deployments().get(1).endDate().toString());
    Assertions.assertEquals("QAT", attributesOne.deployments().get(1).location());
    Assertions.assertEquals(
        "2004-02-01", attributesOne.deployments().get(2).startDate().toString());
    Assertions.assertEquals("2005-01-01", attributesOne.deployments().get(2).endDate().toString());
    Assertions.assertEquals("AX1", attributesOne.deployments().get(2).location());
    Assertions.assertEquals(
        "2006-02-01", attributesOne.deployments().get(3).startDate().toString());
    Assertions.assertEquals("2007-01-01", attributesOne.deployments().get(3).endDate().toString());
    Assertions.assertEquals("AX1", attributesOne.deployments().get(3).location());
    Assertions.assertEquals("Alfredo", attributesTwo.firstName());
    Assertions.assertEquals("Armstrong", attributesTwo.lastName());
    Assertions.assertEquals("Air Force", attributesTwo.branchOfService().toString());
    Assertions.assertEquals("2002-01-01", attributesTwo.startDate().toString());
    Assertions.assertEquals("2003-02-01", attributesTwo.endDate().toString());
    Assertions.assertEquals("E05", attributesTwo.payGrade().toString());
    Assertions.assertEquals(
        ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.HONORABLE,
        attributesTwo.dischargeStatus());
    Assertions.assertEquals("SUFFICIENT SERVICE FOR RETIREMENT", attributesTwo.separationReason());
    Assertions.assertEquals(1, attributesTwo.deployments().stream().count());
    Assertions.assertEquals(
        "2002-02-01", attributesTwo.deployments().get(0).startDate().toString());
    Assertions.assertEquals("2003-01-01", attributesTwo.deployments().get(0).endDate().toString());
    Assertions.assertEquals("QAT", attributesTwo.deployments().get(0).location());
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

    VeteranServiceHistoryTransformer transformer =
        VeteranServiceHistoryTransformer.builder()
            .icn("uuid")
            .deploymentResponse(deployments)
            .serviceEpisodeResponseType(null)
            .mpiResponse(mpiResponse)
            .grasResponse(grasResponse)
            .build();

    ServiceHistoryResponse response = transformer.serviceHistoryTransformer();
    // No error should be thrown
    Assertions.assertEquals(response.data().size(), 0);
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
