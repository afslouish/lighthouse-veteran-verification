package gov.va.api.lighthouse.veteranverification.service.utils;

import com.nimbusds.jose.jwk.JWK;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.KeyStore;
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
    KeyStore ks = KeyStore.getInstance("jks");
    ks.load(new FileInputStream("src/test/resources/fakekeystore.jks"), "secret".toCharArray());
    JwksProperties jwksProperties = new JwksProperties(ks, "fake", "secret");
    Assertions.assertEquals("fake", jwksProperties.currentKeyId());
  }

  @Test
  @SneakyThrows
  public void currentKeyIdIsNonNull() {
    KeyStore ks = KeyStore.getInstance("jks");
    ks.load(new FileInputStream("src/test/resources/fakekeystore.jks"), "secret".toCharArray());
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          new JwksProperties(ks, null, "secret");
        });
  }

  @Test
  @SneakyThrows
  public void currentKeyPasswordIsNonNull() {
    KeyStore ks = KeyStore.getInstance("jks");
    ks.load(new FileInputStream("src/test/resources/fakekeystore.jks"), "secret".toCharArray());
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          new JwksProperties(ks, "fake", null);
        });
  }

  @Test
  @SneakyThrows
  public void currentPrivateJwkHappyPath() {
    KeyStore ks = KeyStore.getInstance("jks");
    ks.load(new FileInputStream("src/test/resources/fakekeystore.jks"), "secret".toCharArray());
    JwksProperties jwksProperties = new JwksProperties(ks, "fake", "secret");
    Assertions.assertNotNull(jwksProperties.currentPrivateJwk());
  }

  @Test
  @SneakyThrows
  public void currentPublicJwkHappyPath() {
    KeyStore ks = KeyStore.getInstance("jks");
    ks.load(new FileInputStream("src/test/resources/fakekeystore.jks"), "secret".toCharArray());
    JwksProperties jwksProperties = new JwksProperties(ks, "fake", "secret");
    Assertions.assertNotNull(jwksProperties.currentPublicJwk());
  }

  @Test
  @SneakyThrows
  public void jwkPublicPemHappy() {
    KeyStore ks = KeyStore.getInstance("jks");
    ks.load(new FileInputStream("src/test/resources/fakekeystore.jks"), "secret".toCharArray());
    JwksProperties jwksProperties = new JwksProperties(ks, "fake", "secret");
    String publicKeyPEM =
        jwksProperties
            .jwkPublicPem("fake")
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replaceAll(System.lineSeparator(), "")
            .replace("-----END PUBLIC KEY-----", "");
    byte[] encoded = Base64.decodeBase64(publicKeyPEM);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
    // This would fail if a non public key was being return
    RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(keySpec);
    Assertions.assertNotNull(key);
  }

  @Test
  @SneakyThrows
  public void jwksPrivateHappyPath() {
    KeyStore ks = KeyStore.getInstance("jks");
    ks.load(new FileInputStream("src/test/resources/fakekeystore.jks"), "secret".toCharArray());
    List<JWK> keys = new JwksProperties(ks, "fake", "secret").jwksPrivate().getKeys();
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
  public void jwksPublicHappyPath() {
    KeyStore ks = KeyStore.getInstance("jks");
    ks.load(new FileInputStream("src/test/resources/fakekeystore.jks"), "secret".toCharArray());
    List<JWK> keys = new JwksProperties(ks, "fake", "secret").jwksPublic().getKeys();
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
    KeyStore ks = KeyStore.getInstance("jks");
    ks.load(new FileInputStream("src/test/resources/fakekeystore.jks"), "secret".toCharArray());
    JwksProperties jwksProperties = new JwksProperties(ks, "fake", "secret");
    Assertions.assertNotNull(jwksProperties.jwksPublicJson());
  }

  @Test
  public void keyStoreIsNonNull() {
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          new JwksProperties(null, "fake", "secret");
        });
  }
}
