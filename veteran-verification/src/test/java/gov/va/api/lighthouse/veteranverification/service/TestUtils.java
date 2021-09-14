package gov.va.api.lighthouse.veteranverification.service;

import static org.apache.tomcat.util.http.fileupload.util.Streams.asString;
import static org.mockito.BDDMockito.given;

import gov.va.api.lighthouse.bgs.BenefitsGatewayServicesClient;
import gov.va.api.lighthouse.bgs.BgsConfig;
import gov.va.api.lighthouse.emis.EmisConfigV1;
import gov.va.api.lighthouse.emis.EmisConfigV2;
import gov.va.api.lighthouse.emis.EmisMilitaryInformationServiceClient;
import gov.va.api.lighthouse.emis.EmisVeteranStatusServiceClient;
import gov.va.api.lighthouse.mpi.MasterPatientIndexClient;
import gov.va.api.lighthouse.mpi.MpiConfig;
import gov.va.api.lighthouse.veteranverification.api.v0.Deployment;
import gov.va.api.lighthouse.veteranverification.api.v0.ServiceHistoryResponse;
import gov.va.vba.benefits.share.services.FindRatingDataResponse;
import gov.va.vba.benefits.share.services.RatingRecord;
import gov.va.viers.cdi.emis.commonservice.v1.VeteranStatus;
import gov.va.viers.cdi.emis.requestresponse.v1.EMISveteranStatusResponseType;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISdeploymentResponseType;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISguardReserveServicePeriodsResponseType;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISserviceEpisodeResponseType;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.Arrays;
import javax.xml.bind.JAXBContext;
import javax.xml.transform.stream.StreamSource;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.hl7.v3.PRPAIN201306UV02;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;

@UtilityClass
public class TestUtils {
  public Deployment[] deploymentArray = {
    Deployment.builder()
        .location("AFG")
        .startDate(LocalDate.of(2000, 2, 1))
        .endDate(LocalDate.of(2001, 1, 1))
        .build(),
    Deployment.builder()
        .location("AFG")
        .startDate(LocalDate.of(2002, 2, 1))
        .endDate(LocalDate.of(2003, 1, 1))
        .build(),
    Deployment.builder()
        .location("AFG")
        .startDate(LocalDate.of(2004, 2, 1))
        .endDate(LocalDate.of(2005, 1, 1))
        .build(),
    Deployment.builder()
        .location("AFG")
        .startDate(LocalDate.of(2006, 2, 1))
        .endDate(LocalDate.of(2007, 1, 1))
        .build()
  };

  private FindRatingDataResponse createBgsResponse(RatingRecord ratingRecord) {
    return FindRatingDataResponse.builder()._return(ratingRecord).build();
  }

  @SneakyThrows
  public EMISdeploymentResponseType createDeploymentResponse(String filename) {
    String profile = asString(TestUtils.class.getClassLoader().getResourceAsStream(filename));
    return JAXBContext.newInstance(EMISdeploymentResponseType.class)
        .createUnmarshaller()
        .unmarshal(new StreamSource(new StringReader(profile)), EMISdeploymentResponseType.class)
        .getValue();
  }

  private EMISveteranStatusResponseType createEmisResponse(VeteranStatus veteranStatus) {
    return EMISveteranStatusResponseType.builder().veteranStatus(veteranStatus).build();
  }

  @SneakyThrows
  public EMISguardReserveServicePeriodsResponseType createGrasResponse(String filename) {
    String profile = asString(TestUtils.class.getClassLoader().getResourceAsStream(filename));
    return JAXBContext.newInstance(EMISguardReserveServicePeriodsResponseType.class)
        .createUnmarshaller()
        .unmarshal(
            new StreamSource(new StringReader(profile)),
            EMISguardReserveServicePeriodsResponseType.class)
        .getValue();
  }

  @SneakyThrows
  public PRPAIN201306UV02 createMpiResponse(String filename) {
    String profile = asString(TestUtils.class.getClassLoader().getResourceAsStream(filename));
    return JAXBContext.newInstance(PRPAIN201306UV02.class)
        .createUnmarshaller()
        .unmarshal(new StreamSource(new StringReader(profile)), PRPAIN201306UV02.class)
        .getValue();
  }

  @SneakyThrows
  public EMISserviceEpisodeResponseType createServiceHistoryResponse(String filename) {
    String profile = asString(TestUtils.class.getClassLoader().getResourceAsStream(filename));
    return JAXBContext.newInstance(EMISserviceEpisodeResponseType.class)
        .createUnmarshaller()
        .unmarshal(
            new StreamSource(new StringReader(profile)), EMISserviceEpisodeResponseType.class)
        .getValue();
  }

  public BgsConfig makeBgsConfig() {
    return BgsConfig.builder()
        .keyAlias("fake")
        .url("http://localhost:2021")
        .wsdlLocation("http://localhost:2021")
        .keystorePath("src/test/resources/fakekeystore.jks")
        .keystorePassword("secret")
        .truststorePath("src/test/resources/faketruststore.jks")
        .truststorePassword("secret")
        .build();
  }

  public EmisConfigV1 makeEmisConfig() {
    return EmisConfigV1.builder()
        .keyAlias("fake")
        .keystorePath("src/test/resources/fakekeystore.jks")
        .keystorePassword("secret")
        .truststorePath("src/test/resources/faketruststore.jks")
        .truststorePassword("secret")
        .url("http://localhost:2020")
        .wsdlLocation("http://localhost:2020")
        .build();
  }

  public EmisConfigV2 makeEmisConfigV2() {
    return EmisConfigV2.builder()
        .keyAlias("fake")
        .keystorePath("src/test/resources/fakekeystore.jks")
        .keystorePassword("secret")
        .truststorePath("src/test/resources/faketruststore.jks")
        .truststorePassword("secret")
        .url("http://localhost:2020")
        .wsdlLocation("http://localhost:2020")
        .build();
  }

  public EmisConfigV2 makeEmisV2Config() {
    return EmisConfigV2.builder()
        .keyAlias("fake")
        .keystorePath("src/test/resources/fakekeystore.jks")
        .keystorePassword("secret")
        .truststorePath("src/test/resources/faketruststore.jks")
        .truststorePassword("secret")
        .url("http://localhost:2020")
        .wsdlLocation("http://localhost:2020")
        .build();
  }

  public MpiConfig makeMpiConfig() {
    return MpiConfig.builder()
        .userId("ID")
        .integrationProcessId("ID")
        .asAgentId("ID")
        .keyAlias("fake")
        .url("http://localhost:2018")
        .wsdlLocation("http://localhost:2018")
        .keystorePath("src/test/resources/fakekeystore.jks")
        .keystorePassword("secret")
        .truststorePath("src/test/resources/faketruststore.jks")
        .truststorePassword("secret")
        .build();
  }

  public ServiceHistoryResponse.ServiceHistoryAttributes makeServiceHistoryAttributes() {
    return ServiceHistoryResponse.ServiceHistoryAttributes.builder()
        .firstName("John")
        .lastName("Doe")
        .branchOfService("BranchOfService")
        .startDate(LocalDate.of(2000, 1, 1))
        .endDate(LocalDate.of(2001, 1, 1))
        .payGrade("PayGrade")
        .dischargeStatus(ServiceHistoryResponse.ServiceHistoryAttributes.DischargeStatus.HONORABLE)
        .separationReason("SeparationReason")
        .deployments(Arrays.stream(deploymentArray).toList())
        .build();
  }

  public ServiceHistoryResponse.ServiceHistoryEpisode makeServiceHistoryResponse() {
    return ServiceHistoryResponse.ServiceHistoryEpisode.builder()
        .id("mock")
        .attributes(makeServiceHistoryAttributes())
        .build();
  }

  public void setBgsMockResponse(
      @Mock BenefitsGatewayServicesClient bgsClient, RatingRecord ratingRecord) {
    FindRatingDataResponse response = createBgsResponse(ratingRecord);
    Mockito.when(bgsClient.ratingServiceRequest(ArgumentMatchers.any())).thenReturn(response);
  }

  public void setDeploymentsMockResponse(
      @Mock EmisMilitaryInformationServiceClient emisClient, EMISdeploymentResponseType response) {
    Mockito.when(emisClient.deploymentRequest(ArgumentMatchers.any())).thenReturn(response);
  }

  public void setDeploymentsResponseException(
      @Mock EmisMilitaryInformationServiceClient emisClient, Exception e) {
    given(emisClient.serviceEpisodesRequest(ArgumentMatchers.any()))
        .willAnswer(
            invocation -> {
              throw e;
            });
  }

  public void setEmisMockResponse(
      @Mock EmisVeteranStatusServiceClient emisClient, VeteranStatus status) {
    EMISveteranStatusResponseType response = createEmisResponse(status);
    Mockito.when(emisClient.veteranStatusRequest(ArgumentMatchers.any())).thenReturn(response);
  }

  public void setEmisResponseException(
      @Mock EmisVeteranStatusServiceClient emisClient, Exception e) {
    given(emisClient.veteranStatusRequest(ArgumentMatchers.any()))
        .willAnswer(
            invocation -> {
              throw e;
            });
  }

  public void setEpisodesResponseException(
      @Mock EmisMilitaryInformationServiceClient emisClient, Exception e) {
    given(emisClient.serviceEpisodesRequest(ArgumentMatchers.any()))
        .willAnswer(
            invocation -> {
              throw e;
            });
  }

  @SneakyThrows
  public void setMpiMockResponse(@Mock MasterPatientIndexClient mpiClient, String filename) {
    PRPAIN201306UV02 response = createMpiResponse(filename);
    Mockito.when(mpiClient.request1305ByAttributes(ArgumentMatchers.any())).thenReturn(response);
    Mockito.when(mpiClient.request1305ByIcn(ArgumentMatchers.any())).thenReturn(response);
  }

  public void setMpiResponseException(@Mock MasterPatientIndexClient mpiClient, Exception e) {
    given(mpiClient.request1305ByAttributes(ArgumentMatchers.any()))
        .willAnswer(
            invocation -> {
              throw e;
            });
    given(mpiClient.request1305ByIcn(ArgumentMatchers.any()))
        .willAnswer(
            invocation -> {
              throw e;
            });
  }

  @SneakyThrows
  public void setNullMpiMockResponse(@Mock MasterPatientIndexClient mpiClient) {
    Mockito.when(mpiClient.request1305ByAttributes(ArgumentMatchers.any())).thenReturn(null);
    Mockito.when(mpiClient.request1305ByIcn(ArgumentMatchers.any())).thenReturn(null);
  }

  public void setServiceHistoryMockResponse(
      @Mock EmisMilitaryInformationServiceClient emisClient,
      EMISserviceEpisodeResponseType response) {
    Mockito.when(emisClient.serviceEpisodesRequest(ArgumentMatchers.any())).thenReturn(response);
  }
}
