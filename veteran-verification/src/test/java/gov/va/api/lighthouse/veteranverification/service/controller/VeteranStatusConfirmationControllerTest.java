package gov.va.api.lighthouse.veteranverification.service.controller;

import static org.apache.tomcat.util.http.fileupload.util.Streams.asString;
import static org.assertj.core.api.Assertions.assertThat;

import gov.va.api.lighthouse.emis.EmisConfigV1;
import gov.va.api.lighthouse.mpi.Mpi1305RequestAttributes;
import gov.va.api.lighthouse.mpi.MpiConfig;
import gov.va.api.lighthouse.veteranverification.api.VeteranStatusConfirmation;
import gov.va.api.lighthouse.veteranverification.api.VeteranStatusRequest;
import gov.va.viers.cdi.emis.requestresponse.v1.EMISveteranStatusResponseType;
import gov.va.viers.cdi.emis.requestresponse.v1.InputEdiPiOrIcn;
import java.util.function.Function;

import lombok.SneakyThrows;
import org.hl7.v3.PRPAIN201306UV02;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.HttpStatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

public class VeteranStatusConfirmationControllerTest {
  final VeteranStatusRequest attributes =
      VeteranStatusRequest.builder()
          .birthDate("1111-11-11")
          .firstName("test")
          .lastName("test")
          .ssn("111111111")
          .gender("M")
          .middleName("test")
          .build();

  MpiConfig mpiConfig;

  @Mock PRPAIN201306UV02 mpiMockResponse;

  @Mock EmisConfigV1 emisConfig;

  @Mock EMISveteranStatusResponseType emisMockResponse;

  ClientAndServer mpiClientServer;
  @BeforeEach
  void _init() {
    MockitoAnnotations.initMocks(this);
    mpiConfig = makeMpiConfig();
  }
  @BeforeEach
  void startMPIServer() {
    startMPI();
  }

  @Test
  void emisVeteranStatusRequestTest() {
    var controller = new VeteranStatusConfirmationController(mpiConfig, emisConfig);
    assertThat(controller.emisVeteranStatusRequest()).isInstanceOf(Function.class);
    Function<InputEdiPiOrIcn, EMISveteranStatusResponseType> emisMockFunction =
        (m) -> emisMockResponse;
    var controllerFunctionOverride =
        new VeteranStatusConfirmationController(mpiConfig, emisConfig);
    controllerFunctionOverride.emisVeteranStatusRequest(emisMockFunction);
    assertThat(controllerFunctionOverride.emisVeteranStatusRequest()).isEqualTo(emisMockFunction);
  }

  @Test
  void mpi1305RequestTest() {
    var controller = new VeteranStatusConfirmationController(mpiConfig, emisConfig);
    assertThat(controller.mpi1305Request()).isInstanceOf(Function.class);
    Function<Mpi1305RequestAttributes, PRPAIN201306UV02> mpiMockFunction = (s) -> mpiMockResponse;
    var controllerFunctionOverride =
        new VeteranStatusConfirmationController(mpiConfig, emisConfig);
    controllerFunctionOverride.mpi1305Request(mpiMockFunction);
    assertThat(controllerFunctionOverride.mpi1305Request()).isEqualTo(mpiMockFunction);
    assertThat(controllerFunctionOverride.veteranStatusConfirmationResponse(attributes))
        .isInstanceOf(VeteranStatusConfirmation.class);
  }
  @Test @SneakyThrows
  void harness() {
    mpiClientServer.when(new HttpRequest().withMethod("POST"))
            .respond(new HttpResponse().withStatusCode(HttpStatusCode.OK_200.code())
                    .withBody(asString(getClass().getClassLoader().getResourceAsStream("mpi_profile_response.xml"))));
    var controller = new VeteranStatusConfirmationController(mpiConfig, emisConfig);
    try {
      controller.veteranStatusConfirmationResponse(attributes);
    }catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  void happyPathRetrieveByEDIPITest() {
    var controller = new VeteranStatusConfirmationController(mpiConfig, emisConfig);
    throw new UnsupportedOperationException();
  }

  @Test
  void happyPathRetrieveByICNTest() {
    var controller = new VeteranStatusConfirmationController(mpiConfig, emisConfig);
    throw new UnsupportedOperationException();
  }

  @Test
  void NoMpiResponse() {
    var controller = new VeteranStatusConfirmationController(mpiConfig, emisConfig);
    throw new UnsupportedOperationException();
  }

  @Test
  void MpiResponseError() {
    var controller = new VeteranStatusConfirmationController(mpiConfig, emisConfig);
    throw new UnsupportedOperationException();
  }

  @Test
  void EmisResponseError() {
    var controller = new VeteranStatusConfirmationController(mpiConfig, emisConfig);
    throw new UnsupportedOperationException();
  }

  @SneakyThrows
  private void startMPI() {
    mpiClientServer = ClientAndServer.startClientAndServer(2018);
    String xml = asString(new ClassPathResource("META-INF/wsdl/IdMHL7v3.WSDL").getInputStream());
    mpiClientServer.when(new HttpRequest().withMethod("GET"))
            .respond(new HttpResponse().withStatusCode(HttpStatusCode.OK_200.code())
                    .withBody(xml));

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
}
