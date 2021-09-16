package gov.va.api.lighthouse.veteranverification.api;

import static gov.va.api.lighthouse.veteranverification.api.RoundTrip.assertRoundTrip;

import gov.va.api.lighthouse.veteranverification.api.v1.JwkKeyset;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

public class JwkKeysetTest {
  @Test
  public void happyPath() {
    JwkKeyset keyset =
        JwkKeyset.builder()
            .keys(
                Lists.newArrayList(
                    new JwkKeyset.Jwk[] {
                      JwkKeyset.Jwk.builder()
                          .alg("alg")
                          .kid("kid")
                          .kty("kty")
                          .exponent("e")
                          .modulus("n")
                          .pem("pem")
                          .build()
                    }))
            .build();
    assertRoundTrip(keyset);
  }

  @Test
  public void happyPathDefaultEmptyJwksList() {
    JwkKeyset keyset = JwkKeyset.builder().build();
    assertRoundTrip(keyset);
  }
}
