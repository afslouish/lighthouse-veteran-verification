package gov.va.api.lighthouse.veteranverification.service.utils;

import com.nimbusds.jose.jwk.JWK;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JwksPropertiesTest {
  @Test
  @SneakyThrows
  public void currentKeyIdHappyPath() {
    JwksProperties jwksProperties =
        JwksProperties.builder()
            .keyStorePath("src/test/resources/fakekeystore.jks")
            .keyStorePassword("secret")
            .currentKeyId("fake")
            .currentKeyPassword("secret")
            .build();
    Assertions.assertEquals("fake", jwksProperties.currentKeyId());
  }

  @Test
  @SneakyThrows
  public void currentPrivateJwkHappyPath() {
    JwksProperties jwksProperties =
        JwksProperties.builder()
            .keyStorePath("src/test/resources/fakekeystore.jks")
            .keyStorePassword("secret")
            .currentKeyId("fake")
            .currentKeyPassword("secret")
            .build();
    Assertions.assertNotNull(jwksProperties.currentPrivateJwk());
  }

  @Test
  @SneakyThrows
  public void currentPublicJwkHappyPath() {
    JwksProperties jwksProperties =
        JwksProperties.builder()
            .keyStorePath("src/test/resources/fakekeystore.jks")
            .keyStorePassword("secret")
            .currentKeyId("fake")
            .currentKeyPassword("secret")
            .build();
    Assertions.assertNotNull(jwksProperties.currentPublicJwk());
  }

  @Test
  @SneakyThrows
  public void jwkPublicPemHappy() {
    JwksProperties jwksProperties =
        JwksProperties.builder()
            .keyStorePath("src/test/resources/fakekeystore.jks")
            .keyStorePassword("secret")
            .currentKeyId("fake")
            .currentKeyPassword("secret")
            .build();
    String publicKeyPEM =
        jwksProperties
            .jwkPublicPem("fake")
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replaceAll(System.lineSeparator(), "")
            .replace("-----END PUBLIC KEY-----", "");
    byte[] encoded = Base64.decodeBase64(publicKeyPEM);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
    // This would fail if a non public key was returned
    RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(keySpec);
    Assertions.assertNotNull(key);
  }

  @Test
  @SneakyThrows
  public void jwksPrivateHappyPath() {
    List<JWK> keys =
        JwksProperties.builder()
            .keyStorePath("src/test/resources/fakekeystore.jks")
            .keyStorePassword("secret")
            .currentKeyId("fake")
            .currentKeyPassword("secret")
            .build()
            .jwksPrivate()
            .getKeys();
    Assertions.assertEquals(1, keys.size());
    Assertions.assertEquals("RSA", keys.get(0).getKeyType().toString());
    Assertions.assertEquals(
        "9nySB6kHPzMIg__xHs0erFz5mZyaGzdI_u3omSR1_qk",
        keys.get(0).getX509CertSHA256Thumbprint().toString());
    Assertions.assertEquals("fake", keys.get(0).getKeyID());
    Assertions.assertEquals(1, keys.get(0).getParsedX509CertChain().size());
    Assertions.assertTrue(keys.get(0).isPrivate());
  }

  @Test
  @SneakyThrows
  public void jwksPropertiesBuilderBadKeyStorePathThrowsException() {
    Assertions.assertThrows(
        IOException.class,
        () -> {
          JwksProperties.builder()
              .keyStorePath("bad")
              .keyStorePassword("secret")
              .currentKeyId("fake")
              .currentKeyPassword("secret")
              .build();
        });
  }

  @Test
  @SneakyThrows
  public void jwksPropertiesBuilderCurrentIdIsNonNull() {
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          JwksProperties.builder()
              .keyStorePath("src/test/resources/fakekeystore.jks")
              .keyStorePassword("secret")
              .currentKeyId(null)
              .currentKeyPassword("secret")
              .build();
        });
  }

  @Test
  @SneakyThrows
  public void jwksPropertiesBuilderCurrentKeyPasswordIsNonNull() {
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          JwksProperties.builder()
              .keyStorePath("src/test/resources/fakekeystore.jks")
              .keyStorePassword("secret")
              .currentKeyId("fake")
              .currentKeyPassword(null)
              .build();
        });
  }

  @Test
  @SneakyThrows
  public void jwksPropertiesBuilderKeyStorePasswordIsNonNull() {
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          JwksProperties.builder()
              .keyStorePath("src/test/resources/fakekeystore.jks")
              .keyStorePassword(null)
              .currentKeyId("fake")
              .currentKeyPassword("secret")
              .build();
        });
  }

  @Test
  @SneakyThrows
  public void jwksPropertiesBuilderKeyStorePathIsNonNull() {
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          JwksProperties.builder()
              .keyStorePath(null)
              .keyStorePassword("secret")
              .currentKeyId("fake")
              .currentKeyPassword("secret")
              .build();
        });
  }

  @Test
  @SneakyThrows
  public void jwksPublicHappyPath() {
    List<JWK> keys =
        JwksProperties.builder()
            .keyStorePath("src/test/resources/fakekeystore.jks")
            .keyStorePassword("secret")
            .currentKeyId("fake")
            .currentKeyPassword("secret")
            .build()
            .jwksPublic()
            .getKeys();
    Assertions.assertEquals(1, keys.size());
    Assertions.assertEquals("RSA", keys.get(0).getKeyType().toString());
    Assertions.assertEquals(
        "9nySB6kHPzMIg__xHs0erFz5mZyaGzdI_u3omSR1_qk",
        keys.get(0).getX509CertSHA256Thumbprint().toString());
    Assertions.assertEquals("fake", keys.get(0).getKeyID());
    Assertions.assertEquals(1, keys.get(0).getParsedX509CertChain().size());
    Assertions.assertFalse(keys.get(0).isPrivate());
  }

  @Test
  @SneakyThrows
  public void jwksPublicJsonHappyPath() {
    JwksProperties jwksProperties =
        JwksProperties.builder()
            .keyStorePath("src/test/resources/fakekeystore.jks")
            .keyStorePassword("secret")
            .currentKeyId("fake")
            .currentKeyPassword("secret")
            .build();
    Assertions.assertNotNull(jwksProperties.jwksPublicJson());
  }
}
