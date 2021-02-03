package gov.va.api.lighthouse.veteranverification.service.controller;

import static org.apache.tomcat.util.http.fileupload.util.Streams.asString;
import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.lighthouse.emis.EmisConfigV1;
import gov.va.api.lighthouse.emis.EmisVeteranStatusServiceClient;
import gov.va.api.lighthouse.emis.SoapEmisVeteranStatusServiceClient;
import gov.va.api.lighthouse.mpi.MasterPatientIndexClient;
import gov.va.api.lighthouse.mpi.MpiConfig;
import gov.va.api.lighthouse.mpi.SoapMasterPatientIndexClient;
import gov.va.api.lighthouse.veteranverification.api.VeteranStatusConfirmation;
import gov.va.api.lighthouse.veteranverification.api.VeteranStatusRequest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.HttpStatusCode;
import org.springframework.core.io.ClassPathResource;

public class VeteranStatusConfirmationControllerIntegrationTest {
  private final VeteranStatusRequest attributes =
      VeteranStatusRequest.builder()
          .birthDate("1111-11-11")
          .firstName("test")
          .lastName("test")
          .ssn("111111111")
          .gender("M")
          .middleName("test")
          .build();

  private ClientAndServer mpiClientServer;

  private ClientAndServer emisClientServer;

  private EmisVeteranStatusServiceClient emisClient;

  private MasterPatientIndexClient mpiClient;

  @Test
  @SneakyThrows
  void HappyPath() {
    mpiClientServer
        .when(new HttpRequest().withMethod("POST"))
        .respond(
            new HttpResponse()
                .withStatusCode(HttpStatusCode.OK_200.code())
                .withBody(
                    asString(
                        getClass()
                            .getClassLoader()
                            .getResourceAsStream("mpi_profile_response.xml"))));
    emisClientServer
        .when(new HttpRequest().withMethod("POST"))
        .respond(
            new HttpResponse()
                .withStatusCode(HttpStatusCode.OK_200.code())
                .withBody(
                    asString(
                        getClass()
                            .getClassLoader()
                            .getResourceAsStream("emis_edipi_response.xml"))));
    var controller = new VeteranStatusConfirmationController(mpiClient, emisClient);
    VeteranStatusConfirmation veteranStatusConfirmation =
        controller.veteranStatusConfirmationResponse(attributes);
    assertThat(veteranStatusConfirmation.getVeteranStatus()).isEqualTo("confirmed");
  }

  @BeforeEach
  void _init() {
    MockitoAnnotations.initMocks(this);
    mpiClient = SoapMasterPatientIndexClient.of(makeMpiConfig());
    emisClient = SoapEmisVeteranStatusServiceClient.of(makeEmisConfig());
  }

  private EmisConfigV1 makeEmisConfig() {
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

  private MpiConfig makeMpiConfig() {
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

  @SneakyThrows
  private void startEMIS() {
    emisClientServer = ClientAndServer.startClientAndServer(2020);
    String xml =
        asString(
            new ClassPathResource("META-INF/wsdl/v1/eMISVeteranStatusService.wsdl")
                .getInputStream());
    emisClientServer
        .when(new HttpRequest().withMethod("GET"))
        .respond(new HttpResponse().withStatusCode(HttpStatusCode.OK_200.code()).withBody(xml));
  }

  @BeforeEach
  void startEMISServer() {
    startEMIS();
  }

  @SneakyThrows
  private void startMPI() {
    mpiClientServer = ClientAndServer.startClientAndServer(2018);
    String xml = asString(new ClassPathResource("META-INF/wsdl/IdMHL7v3.WSDL").getInputStream());
    mpiClientServer
        .when(new HttpRequest().withMethod("GET"))
        .respond(new HttpResponse().withStatusCode(HttpStatusCode.OK_200.code()).withBody(xml));
  }

  @BeforeEach
  void startMPIServer() {
    startMPI();
  }
}
