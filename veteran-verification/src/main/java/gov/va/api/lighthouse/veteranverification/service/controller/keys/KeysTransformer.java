package gov.va.api.lighthouse.veteranverification.service.controller.keys;

import gov.va.api.lighthouse.veteranverification.api.v0.JwkKeyset;
import gov.va.api.lighthouse.veteranverification.service.utils.JwksProperties;
import lombok.Builder;
import lombok.NonNull;
import lombok.SneakyThrows;

@Builder
public class KeysTransformer {
  @NonNull private final JwksProperties jwksProperties;

  /** Transformer for Keys response. */
  @SneakyThrows
  public JwkKeyset keysTransformer() {
    return JwkKeyset.builder()
        .keys(
            jwksProperties.jwksPublic().getKeys().stream()
                .map(
                    jwk ->
                        JwkKeyset.Jwk.builder()
                            .kty(jwk.getKeyType().toString())
                            .alg(jwksProperties.algorithm().toString())
                            .kid(jwk.getKeyID())
                            .pem(jwksProperties.jwkPublicPem(jwksProperties.currentKeyId()))
                            .exponent(jwk.toRSAKey().getPublicExponent().toString())
                            .modulus(jwk.toRSAKey().getModulus().toString())
                            .build())
                .toList())
        .build();
  }
}
