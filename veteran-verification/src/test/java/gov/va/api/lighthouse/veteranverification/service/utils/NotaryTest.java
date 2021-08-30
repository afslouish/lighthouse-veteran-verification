package gov.va.api.lighthouse.veteranverification.service.utils;

import gov.va.api.lighthouse.veteranverification.api.v0.ServiceHistoryResponse;
import gov.va.api.lighthouse.veteranverification.service.TestUtils;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.Arrays;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NotaryTest {
  @Test
  public void jwksPropertiesIsNonNull() {
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          new Notary(null);
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
    KeyStore ks = KeyStore.getInstance("jks");
    ks.load(new FileInputStream("src/test/resources/fakekeystore.jks"), "secret".toCharArray());
    JwksProperties jwksProperties = new JwksProperties(ks, "fake", "secret");
    Notary notary = new Notary(jwksProperties);
    String jwt = notary.objectToJwt(serviceHistoryResponse);
    Assertions.assertEquals(
        "eyJraWQiOiJmYWtlIiwidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYifQ.eyJkYXRhIjpbeyJpZCI6Im1vY2siLCJ0eX"
            + "BlIjoic2VydmljZS1oaXN0b3J5LWVwaXNvZGVzIiwiYXR0cmlidXRlcyI6eyJmaXJzdF9uYW1lIjoiSm9obiIsImxhc3R"
            + "fbmFtZSI6IkRvZSIsImJyYW5jaF9vZl9zZXJ2aWNlIjoiQnJhbmNoT2ZTZXJ2aWNlIiwic3RhcnRfZGF0ZSI6IjIwMDAt"
            + "MDEtMDEiLCJlbmRfZGF0ZSI6IjIwMDEtMDEtMDEiLCJwYXlfZ3JhZGUiOiJQYXlHcmFkZSIsImRpc2NoYXJnZV9zdGF0d"
            + "XMiOiJob25vcmFibGUiLCJzZXBhcmF0aW9uX3JlYXNvbiI6IlNlcGFyYXRpb25SZWFzb24iLCJkZXBsb3ltZW50cyI6W3"
            + "sic3RhcnRfZGF0ZSI6IjIwMDAtMDItMDEiLCJlbmRfZGF0ZSI6IjIwMDEtMDEtMDEiLCJsb2NhdGlvbiI6IkFGRyJ9LHs"
            + "ic3RhcnRfZGF0ZSI6IjIwMDItMDItMDEiLCJlbmRfZGF0ZSI6IjIwMDMtMDEtMDEiLCJsb2NhdGlvbiI6IkFGRyJ9LHsi"
            + "c3RhcnRfZGF0ZSI6IjIwMDQtMDItMDEiLCJlbmRfZGF0ZSI6IjIwMDUtMDEtMDEiLCJsb2NhdGlvbiI6IkFGRyJ9LHsic"
            + "3RhcnRfZGF0ZSI6IjIwMDYtMDItMDEiLCJlbmRfZGF0ZSI6IjIwMDctMDEtMDEiLCJsb2NhdGlvbiI6IkFGRyJ9XX19XX"
            + "0.dmBxYrcELzUq0ntBHY2ZD2PvrCV6rNJBTuvXUr3VxXirzoY6vB4JPmTAznbviRjvS44D4z9uJat0UUhko9PoMRIfYCr"
            + "MqY-9crbGVmu5P1RJoiHLxL_HAykygov3wMKC_yzeIS_IJ2UIw_NIzuaLZDuk9HE6WJCVcs0A_Yjiv6thRgVjUD-08gbT"
            + "Q0Oz38UH3xdDSyNozUXH5AHFpkkC8DWl4MDg774oXiMBCAzjfw7uR4FFHXXAqZJZR7uPKVbASxCcmqHKH5JInnZjQ_1Fe"
            + "QSxKF0FXQx-_XzjoIvKshS43_f3277ok_eT9zZlStzNrtLgZmuSLb3eSe8U6UFLkg",
        jwt);
  }

  @Test
  @SneakyThrows
  public void objectToJwtObjectIsNonNull() {
    KeyStore ks = KeyStore.getInstance("jks");
    ks.load(new FileInputStream("src/test/resources/fakekeystore.jks"), "secret".toCharArray());
    JwksProperties jwksProperties = new JwksProperties(ks, "fake", "fake");
    Notary notary = new Notary(jwksProperties);
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          notary.objectToJwt(null);
        });
  }
}
