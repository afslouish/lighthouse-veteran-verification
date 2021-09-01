package gov.va.api.lighthouse.veteranverification.service.utils;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import java.security.KeyStore;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;

@Getter
public class JwksProperties {
  private final JWKSet jwksPrivate;

  private final JWKSet jwksPublic;

  private final String jwksPublicJson;

  private String currentKeyId;

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
}
