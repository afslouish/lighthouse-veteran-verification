package gov.va.api.lighthouse.veteranverification.api;

import static gov.va.api.lighthouse.veteranverification.api.RoundTrip.assertRoundTrip;

import gov.va.api.lighthouse.veteranverification.api.v0.JwkKeyset;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JwkTest {
  @Test
  public void algIsNonNull() {
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          JwkKeyset.Jwk.builder()
              .alg(null)
              .kid("kid")
              .kty("kty")
              .exponent("e")
              .modulus("n")
              .build();
        });
  }

  @Test
  public void exponentIsNonNull() {
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          JwkKeyset.Jwk.builder()
              .alg("alg")
              .kid("kid")
              .kty("kty")
              .exponent(null)
              .modulus("n")
              .build();
        });
  }

  @Test
  public void happyPath() {
    JwkKeyset.Jwk jwk =
        JwkKeyset.Jwk.builder().alg("alg").kid("kid").kty("kty").exponent("e").modulus("n").build();
    assertRoundTrip(jwk);
  }

  @Test
  public void kidIsNonNull() {
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          JwkKeyset.Jwk.builder()
              .alg("alg")
              .kid(null)
              .kty("kty")
              .exponent("e")
              .modulus("n")
              .build();
        });
  }

  @Test
  public void ktyIsNonNull() {
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          JwkKeyset.Jwk.builder()
              .alg("alg")
              .kid("kid")
              .kty(null)
              .exponent("e")
              .modulus("n")
              .build();
        });
  }

  @Test
  public void modulusIsNonNull() {
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          JwkKeyset.Jwk.builder()
              .alg("alg")
              .kid("kid")
              .kty("kty")
              .exponent("e")
              .modulus(null)
              .build();
        });
  }
}
