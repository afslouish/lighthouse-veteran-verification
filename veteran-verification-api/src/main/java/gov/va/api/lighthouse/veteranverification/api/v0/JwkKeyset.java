package gov.va.api.lighthouse.veteranverification.api.v0;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(fluent = false)
@AllArgsConstructor
@NoArgsConstructor
@Schema(
    name = "JWKKeyset",
    description = "A JWK Keyset compliant with RFC 7517",
    requiredProperties = {"keys"})
public class JwkKeyset {
  @NonNull @Builder.Default List<Jwk> keys = new ArrayList<>();

  @Data
  @Builder
  @Accessors(fluent = false)
  @AllArgsConstructor
  @NoArgsConstructor
  @Schema(
      description = "JSON Web Key compliant with RFC 7517",
      requiredProperties = {"kty", "alg", "kid", "pem", "e", "n"})
  public static class Jwk {
    @NonNull
    @Schema(
        description =
            "The \"kty\" (key type) parameter identifies the cryptographic algorithm family used "
                + "with the key, such as \"RSA\" or \"EC\".",
        example = "RSA")
    String kty;

    @NonNull
    @Schema(
        description =
            "The \"alg\" (algorithm) parameter identifies the algorithm intended for use with the "
                + "key.",
        example = "RSA256")
    String alg;

    @NonNull
    @Schema(
        description = "The \"kid\" (key ID) parameter is used to match a specific key.",
        example = "088d24232ff6faa4cd4cfec126ad0431dff1ea028afdb1c86b3718d70171aed6")
    String kid;

    @NonNull
    @Schema(
        name = "e",
        description =
            "The \"e\" (exponent) parameter contains the exponent value for the RSA public key. "
                + "It is represented as a Base64urlUInt-encoded value.",
        example = "AQAB")
    @JsonProperty("e")
    String exponent;

    @NonNull
    @Schema(
        name = "n",
        description =
            "The \"n\" (modulus) parameter contains the modulus value for the RSA public key. "
                + "It is represented as a Base64urlUInt-encoded value.",
        example =
            "3wtLybygT4UvigPmndR5KV6yPCaGsLas1cGIAhq5SIGIJ0cyg70RTG7JGBU3zwe2vJE9IAJMhRr45caeBEOpq"
                + "VRoQAzVVgVQK0JXJzDGzbCkZm9o0i0s41-PQtup2uplIvV_sXuokvey9MsiVgQPdWKt3AtJCfCfJcD6"
                + "CvU716s=")
    @JsonProperty("n")
    String modulus;
  }
}
