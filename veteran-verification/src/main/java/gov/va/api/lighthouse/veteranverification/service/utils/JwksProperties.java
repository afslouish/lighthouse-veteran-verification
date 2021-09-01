package gov.va.api.lighthouse.veteranverification.service.utils;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import java.io.StringWriter;
import java.security.KeyStore;
import javax.xml.bind.DatatypeConverter;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;

public class JwksProperties {
  public static final String BEGIN_CERT = "-----BEGIN PUBLIC KEY-----\n";

  public static final String END_CERT = "\n-----END PUBLIC KEY-----";

  @Getter private final JWKSet jwksPrivate;

  @Getter private final JWKSet jwksPublic;

  @Getter private final String jwksPublicJson;

  @Getter private String currentKeyId;

  /** Constructor. */
  @SneakyThrows
  public JwksProperties(
      @NonNull KeyStore keyStore,
      @NonNull @Value("${jwk-set.current-key-id}") String currentKeyId,
      @NonNull @Value("${jwk-set.current-password}") String currentKeyPassword) {
    this.jwksPrivate = JWKSet.load(keyStore, name -> currentKeyPassword.toCharArray());
    this.jwksPublic = jwksPrivate.toPublicJWKSet();
    this.jwksPublicJson = jwksPublic.toString();
    this.currentKeyId = currentKeyId;
  }

  /**
   * Returns signing algorithm.
   *
   * @return Encryption algorithm type.
   */
  public JWSAlgorithm algorithm() {
    return JWSAlgorithm.RS256;
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
  @SneakyThrows
  public String jwkPublicPem(String keyId) {
    StringWriter sw = new StringWriter();
    sw.write(BEGIN_CERT);
    sw.write(
        DatatypeConverter.printBase64Binary(
                jwksPublic().getKeyByKeyId(keyId).getParsedX509CertChain().get(0).getEncoded())
            .replaceAll("(.{64})", "$1\n"));
    sw.write(END_CERT);
    return sw.toString();
  }
}
