package gov.va.api.lighthouse.veteranverification.service;

import static org.apache.tomcat.util.http.fileupload.util.Streams.asString;
import static org.mockito.BDDMockito.given;

import gov.va.api.lighthouse.emis.EmisConfigV1;
import gov.va.api.lighthouse.emis.EmisConfigV2;
import gov.va.api.lighthouse.emis.EmisMilitaryInformationServiceClient;
import gov.va.api.lighthouse.emis.EmisVeteranStatusServiceClient;
import gov.va.api.lighthouse.mpi.MasterPatientIndexClient;
import gov.va.api.lighthouse.mpi.MpiConfig;
import gov.va.api.lighthouse.veteranverification.api.v0.Deployment;
import gov.va.viers.cdi.emis.commonservice.v1.VeteranStatus;
import gov.va.viers.cdi.emis.requestresponse.v1.EMISveteranStatusResponseType;
import gov.va.viers.cdi.emis.requestresponse.v2.EMISserviceEpisodeResponseType;
import java.io.StringReader;
import java.time.LocalDate;
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
        .startDate(LocalDate.of(2000, 1, 1))
        .endDate(LocalDate.of(2001, 1, 1))
        .build(),
    Deployment.builder()
        .location("AFG")
        .startDate(LocalDate.of(2002, 1, 1))
        .endDate(LocalDate.of(2003, 1, 1))
        .build(),
    Deployment.builder()
        .location("AFG")
        .startDate(LocalDate.of(2004, 1, 1))
        .endDate(LocalDate.of(2005, 1, 1))
        .build(),
    Deployment.builder()
        .location("AFG")
        .startDate(LocalDate.of(2006, 1, 1))
        .endDate(LocalDate.of(2007, 1, 1))
        .build()
  };

  private EMISveteranStatusResponseType createEmisResponse(VeteranStatus veteranStatus) {
    return EMISveteranStatusResponseType.builder().veteranStatus(veteranStatus).build();
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

  public void setMockServiceHistoryResponse(
      @Mock EmisMilitaryInformationServiceClient emisClient, String filename) {
    EMISserviceEpisodeResponseType response = createServiceHistoryResponse(filename);
    Mockito.when(emisClient.serviceEpisodesRequest(ArgumentMatchers.any())).thenReturn(response);
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
}
