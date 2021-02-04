package gov.va.api.lighthouse.veteranverification.service.controller;

import static org.apache.tomcat.util.http.fileupload.util.Streams.asString;
import static org.mockito.BDDMockito.given;

import gov.va.api.lighthouse.emis.EmisConfigV1;
import gov.va.api.lighthouse.emis.EmisVeteranStatusServiceClient;
import gov.va.api.lighthouse.mpi.MasterPatientIndexClient;
import gov.va.api.lighthouse.mpi.MpiConfig;
import gov.va.viers.cdi.emis.commonservice.v1.VeteranStatus;
import gov.va.viers.cdi.emis.requestresponse.v1.EMISveteranStatusResponseType;
import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.transform.stream.StreamSource;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.hl7.v3.PRPAIN201306UV02;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;

@UtilityClass
public class VeteranStatusConfirmationControllerTestUtils {
  private EMISveteranStatusResponseType createEmisResponse(VeteranStatus veteranStatus) {
    return EMISveteranStatusResponseType.builder().veteranStatus(veteranStatus).build();
  }

  @SneakyThrows
  private PRPAIN201306UV02 createMpiResponse(String filename) {
    String profile =
        asString(
            VeteranStatusConfirmationControllerTestUtils.class
                .getClassLoader()
                .getResourceAsStream(filename));
    return JAXBContext.newInstance(PRPAIN201306UV02.class)
        .createUnmarshaller()
        .unmarshal(new StreamSource(new StringReader(profile)), PRPAIN201306UV02.class)
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

  void setEmisMockResponse(@Mock EmisVeteranStatusServiceClient emisClient, VeteranStatus status) {
    EMISveteranStatusResponseType response = createEmisResponse(status);
    Mockito.when(emisClient.veteranStatusRequest(ArgumentMatchers.any())).thenReturn(response);
  }

  void setEmisResponseException(@Mock EmisVeteranStatusServiceClient emisClient, Exception e) {
    given(emisClient.veteranStatusRequest(ArgumentMatchers.any()))
        .willAnswer(
            invocation -> {
              throw e;
            });
  }

  @SneakyThrows
  void setMpiMockResponse(@Mock MasterPatientIndexClient mpiClient, String filename) {
    PRPAIN201306UV02 response = createMpiResponse(filename);
    Mockito.when(mpiClient.request1305ByAttributes(ArgumentMatchers.any())).thenReturn(response);
  }

  void setMpiResponseException(@Mock MasterPatientIndexClient mpiClient, Exception e) {
    given(mpiClient.request1305ByAttributes(ArgumentMatchers.any()))
        .willAnswer(
            invocation -> {
              throw e;
            });
  }

  @SneakyThrows
  void setNullMpiMockResponse(@Mock MasterPatientIndexClient mpiClient) {
    Mockito.when(mpiClient.request1305ByAttributes(ArgumentMatchers.any())).thenReturn(null);
  }
}
