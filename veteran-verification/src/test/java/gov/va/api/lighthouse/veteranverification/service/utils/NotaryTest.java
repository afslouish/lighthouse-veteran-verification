package gov.va.api.lighthouse.veteranverification.service.utils;

import gov.va.api.lighthouse.veteranverification.api.v0.ServiceHistoryResponse;
import gov.va.api.lighthouse.veteranverification.service.TestUtils;
import java.io.File;
import java.util.Arrays;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NotaryTest {
  @Test
  public void constructorFileIsNonNull() {
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          new Notary(null);
        });
  }

  @Test
  @SneakyThrows
  public void objectToJwObjectIsNonNull() {
    Notary notary = new Notary(new File("src/test/resources/verification_test_private.pem"));
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          notary.objectToJwt(null);
        });
  }

  @Test
  @SneakyThrows
  public void objectToJwtHappyPath() {
    ServiceHistoryResponse.ServiceHistoryEpisode[] serviceHistoryEpisodes = {
      TestUtils.makeServiceHistoryResponse()
    };
    ServiceHistoryResponse serviceHistoryResponse =
        ServiceHistoryResponse.builder()
            .data(Arrays.stream(serviceHistoryEpisodes).toList())
            .build();
    Notary notary = new Notary(new File("src/test/resources/verification_test_private.pem"));
    String jwt = notary.objectToJwt(serviceHistoryResponse);
    Assertions.assertEquals(
        "eyJraWQiOiIwODhkMjQyMzJmZjZmYWE0Y2Q0Y2ZlYzEyNmFkMDQzMWRmZjFlYTAyOGFmZGIxYzg2YjM3MThkNzAxN"
            + "zFhZWQ2IiwidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYifQ.eyJkYXRhIjpbeyJpZCI6Im1vY2siLCJ0eXBlIjoic2Vydm"
            + "ljZS1oaXN0b3J5LWVwaXNvZGVzIiwiYXR0cmlidXRlcyI6eyJmaXJzdF9uYW1lIjoiSm9obiIsImxhc3RfbmFtZSI6Ik"
            + "RvZSIsImJyYW5jaF9vZl9zZXJ2aWNlIjoiQnJhbmNoT2ZTZXJ2aWNlIiwic3RhcnRfZGF0ZSI6IjIwMDAtMDEtMDEiLC"
            + "JlbmRfZGF0ZSI6IjIwMDEtMDEtMDEiLCJwYXlfZ3JhZGUiOiJQYXlHcmFkZSIsImRpc2NoYXJnZV9zdGF0dXMiOiJob2"
            + "5vcmFibGUiLCJzZXBhcmF0aW9uX3JlYXNvbiI6IlNlcGFyYXRpb25SZWFzb24iLCJkZXBsb3ltZW50cyI6W3sic3Rhcn"
            + "RfZGF0ZSI6IjIwMDAtMDItMDEiLCJlbmRfZGF0ZSI6IjIwMDEtMDEtMDEiLCJsb2NhdGlvbiI6IkFGRyJ9LHsic3Rhcn"
            + "RfZGF0ZSI6IjIwMDItMDItMDEiLCJlbmRfZGF0ZSI6IjIwMDMtMDEtMDEiLCJsb2NhdGlvbiI6IkFGRyJ9LHsic3Rhcn"
            + "RfZGF0ZSI6IjIwMDQtMDItMDEiLCJlbmRfZGF0ZSI6IjIwMDUtMDEtMDEiLCJsb2NhdGlvbiI6IkFGRyJ9LHsic3Rhcn"
            + "RfZGF0ZSI6IjIwMDYtMDItMDEiLCJlbmRfZGF0ZSI6IjIwMDctMDEtMDEiLCJsb2NhdGlvbiI6IkFGRyJ9XX19XX0.uM"
            + "bTDYFXSfdJovBJMOdlPwF9USHGOd6ksG-ZJrBnOa8vwFBQ-h6lDV6qg73W_gt64DwXj1wT4o-NUiS5rfs9czLwx-Ck9V"
            + "PHmatQZVyrnWkHkWPLlWve11kIs1nHOWEG1m28zug4s5SyafGfC6whmXb6VKlWtY_Ll6Gy0dMQqns",
        jwt);
  }
}
