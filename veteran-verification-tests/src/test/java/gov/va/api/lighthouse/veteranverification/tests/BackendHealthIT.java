package gov.va.api.lighthouse.veteranverification.tests;

import static gov.va.api.lighthouse.veteranverification.tests.Requestor.veteranVerificationGetRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BackendHealthIT {
  ObjectMapper mapper = new ObjectMapper();

  @Test
  public void HappyPath() throws JsonProcessingException {
    Response response =
        veteranVerificationGetRequest("backend/health", "application/json", 200).response();

    Assertions.assertEquals(
        "Veteran-Verification Backend Health Check", response.jsonPath().get("description"));
    Assertions.assertEquals("UP", response.jsonPath().get("status"));
    Assertions.assertNotNull(response.jsonPath().get("details.time"));

    List<Map<String, String>> backendServices =
        response.jsonPath().getList("details.backendServices");
    Assertions.assertEquals(2, backendServices.size());

    Map<String, String> emisService =
        backendServices.stream()
            .filter(obj -> obj.get("description").equals("EMIS"))
            .findFirst()
            .get();
    Assertions.assertNotNull(emisService);
    Assertions.assertEquals("EMIS", emisService.get("description"));
    Assertions.assertEquals("UP", emisService.get("status"));
    Map<String, String> emisDetails =
        mapper.readValue(mapper.writeValueAsString(emisService.get("details")), Map.class);
    Assertions.assertNotNull(emisDetails);
    Assertions.assertEquals("EMIS", emisDetails.get("name"));
    Assertions.assertEquals(200, emisDetails.get("httpCode"));
    Assertions.assertEquals("OK", emisDetails.get("status"));
    Assertions.assertNotNull(emisDetails.get("time"));

    Map<String, String> bgsService =
        backendServices.stream()
            .filter(obj -> obj.get("description").equals("BGS"))
            .findFirst()
            .get();
    Assertions.assertNotNull(bgsService);
    Assertions.assertEquals("BGS", bgsService.get("description"));
    Assertions.assertEquals("UP", bgsService.get("status"));
    Map<String, String> bgsDetails =
        mapper.readValue(mapper.writeValueAsString(bgsService.get("details")), Map.class);
    Assertions.assertNotNull(emisDetails);
    Assertions.assertEquals("BGS", bgsDetails.get("name"));
    Assertions.assertEquals(200, bgsDetails.get("httpCode"));
    Assertions.assertEquals("OK", bgsDetails.get("status"));
    Assertions.assertNotNull(emisDetails.get("time"));
  }
}
