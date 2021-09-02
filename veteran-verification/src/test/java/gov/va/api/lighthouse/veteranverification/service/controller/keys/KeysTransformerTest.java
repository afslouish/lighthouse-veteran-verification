package gov.va.api.lighthouse.veteranverification.service.controller.keys;

import gov.va.api.lighthouse.veteranverification.api.v0.JwkKeyset;
import gov.va.api.lighthouse.veteranverification.service.utils.JwksProperties;
import java.io.FileInputStream;
import java.security.KeyStore;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class KeysTransformerTest {
  @Test
  @SneakyThrows
  public void happyPath() {
    KeyStore ks = KeyStore.getInstance("jks");
    ks.load(new FileInputStream("src/test/resources/fakekeystore.jks"), "secret".toCharArray());
    KeysTransformer transformer =
        KeysTransformer.builder().jwksProperties(new JwksProperties(ks, "fake", "secret")).build();
    JwkKeyset keySet = transformer.keysTransformer();
    Assertions.assertEquals(1, keySet.getKeys().size());
    Assertions.assertEquals("RS256", keySet.getKeys().get(0).getAlg());
    Assertions.assertEquals("fake", keySet.getKeys().get(0).getKid());
    Assertions.assertEquals("AQAB", keySet.getKeys().get(0).getExponent());
    Assertions.assertEquals(
        "-----BEGIN PUBLIC KEY-----\n"
            + "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAl0HEtNs2NuvF20jd9cmw\n"
            + "A1gtIUv+mpPpHkQcMU5Mx/EkLxWwwu1kZUMfrEGungdaf/SkN6/1/6Q2/4zxy1eJ\n"
            + "qYz9xabxGC3QuEsUs1pzoVyS3OwPtnoWa1qpToUs0hxDSWJCdJ9NpGO4CZ//KtTE\n"
            + "Lxq9Ym9ceho+YIUUO0RrLXl/3x1sAjay8ejSDxM3ld8LGsAskLYcfzlhPhKA36p9\n"
            + "UFjPzijhQtuMbrczIr1bv+jrVgFOxOj5tiRVARHzhB1ptvS0mD6o5XV161M3TyXa\n"
            + "egYk7cUfDrhHCxsezFNaSROs7pHm6Rfj1YXA2DiaD6HycXzD2M+wcsUhfPtyIoWb\n"
            + "kQIDAQAB\n"
            + "-----END PUBLIC KEY-----",
        keySet.getKeys().get(0).getPem());
    Assertions.assertEquals(
        "l0HEtNs2NuvF20jd9cmwA1gtIUv-mpPpHkQcMU5Mx_EkLxWwwu1kZUMfrEGungdaf_SkN6_1_6Q2_4zxy1eJqYz9x"
            + "abxGC3QuEsUs1pzoVyS3OwPtnoWa1qpToUs0hxDSWJCdJ9NpGO4CZ__KtTELxq9Ym9ceho-YIUUO0RrLXl_3x1sAja"
            + "y8ejSDxM3ld8LGsAskLYcfzlhPhKA36p9UFjPzijhQtuMbrczIr1bv-jrVgFOxOj5tiRVARHzhB1ptvS0mD6o5XV16"
            + "1M3TyXaegYk7cUfDrhHCxsezFNaSROs7pHm6Rfj1YXA2DiaD6HycXzD2M-wcsUhfPtyIoWbkQ",
        keySet.getKeys().get(0).getModulus());
  }

  @Test
  public void jwksPropertiesIsNonNull() {
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          KeysTransformer.builder().jwksProperties(null).build();
        });
  }
}
