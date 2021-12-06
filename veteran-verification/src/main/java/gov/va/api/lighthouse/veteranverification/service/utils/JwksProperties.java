package gov.va.api.lighthouse.veteranverification.service.utils;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.KeyStore;
import javax.xml.bind.DatatypeConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;

@Slf4j
public class JwksProperties {
  public static final String BEGIN_CERT = "-----BEGIN PUBLIC KEY-----\n";

  public static final String END_CERT = "\n-----END PUBLIC KEY-----";

  @Getter private final JWSAlgorithm algorithm = JWSAlgorithm.RS256;

  @Getter private final JWKSet jwksPrivate;

  @Getter private final JWKSet jwksPublic;

  @Getter private final String jwksPublicJson;

  @Getter private String currentKeyId;

  /** Constructor. */
  private JwksProperties(
      @NonNull JWKSet jwksPrivate, JWKSet jwksPublic, String jwksPublicJson, String currentKeyId) {
    this.jwksPrivate = jwksPrivate;
    this.jwksPublic = jwksPublic;
    this.jwksPublicJson = jwksPublicJson;
    this.currentKeyId = currentKeyId;
  }

  /** return builder. */
  public static JwksPropertiesBuilder builder() {
    return new JwksPropertiesBuilder();
  }

  /**
   * Gets Private JWK by the keyId specified in the constructor.
   *
   * @return Private JWK.
   */
  public JWK currentPrivateJwk() {
    return jwksPrivate.getKeyByKeyId(currentKeyId);
  }

  /**
   * Gets Public JWK by the keyId specified in the constructor.
   *
   * @return Public JWK.
   */
  public JWK currentPublicJwk() {
    return jwksPublic.getKeyByKeyId(currentKeyId);
  }

  /**
   * Creates pub string from keyId.
   *
   * @param keyId kid.
   * @return pub key string.
   */
  public String jwkPublicPem(String keyId) {
    StringWriter sw = new StringWriter();
    sw.write(BEGIN_CERT);
    sw.write(
        DatatypeConverter.printBase64Binary(
                jwksPublic()
                    .getKeyByKeyId(keyId)
                    .getParsedX509CertChain()
                    .get(0)
                    .getPublicKey()
                    .getEncoded())
            .replaceAll("(.{64})", "$1\n"));
    sw.write(END_CERT);
    return sw.toString();
  }

  /** Builder class for JwksProperties. */
  @Accessors(fluent = true)
  @Setter
  @NoArgsConstructor
  public static class JwksPropertiesBuilder {
    @NonNull String currentKeyId;

    @NonNull String currentKeyPassword;

    @NonNull String keyStorePassword;

    @NonNull String keyStorePath;

    /** Builder method. */
    @SneakyThrows
    public JwksProperties build() {
      KeyStore keystore = KeyStore.getInstance("JKS");
      try (InputStream keystoreInputStream = ResourceUtils.getURL(keyStorePath).openStream()) {
        keystore.load(keystoreInputStream, keyStorePassword.toCharArray());
      } catch (IOException e) {
        log.error("Failed to create keystore");
        throw e;
      }
      JWKSet jwksPrivate = JWKSet.load(keystore, name -> currentKeyPassword.toCharArray());
      return new JwksProperties(
          jwksPrivate,
          jwksPrivate.toPublicJWKSet(),
          jwksPrivate.toPublicJWKSet().toString(),
          currentKeyId);
    }
  }
}
