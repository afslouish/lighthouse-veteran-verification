package gov.va.api.lighthouse.veteranverification.service.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jwt.JWTClaimsSet;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.Map;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.util.encoders.Hex;

public class Notary {
  private final JWK privateKey;

  /**
   * Builder for Notary.
   *
   * @param privateKey File that contains the private key.
   */
  @SneakyThrows
  public Notary(@NonNull File privateKey) {
    this.privateKey = JWK.parseFromPEMEncodedObjects(Files.readString(privateKey.toPath()));
  }

  @SneakyThrows
  private String kid() {
    ASN1EncodableVector vector = new ASN1EncodableVector();
    vector.add(new ASN1Integer(privateKey.toRSAKey().getModulus().decodeToBigInteger()));
    vector.add(new ASN1Integer(privateKey.toRSAKey().getPublicExponent().decodeToBigInteger()));
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ASN1OutputStream asnOs = new ASN1OutputStream(baos);
    asnOs.writeObject(new DERSequence(vector));
    asnOs.flush();
    ASN1Sequence sequence = (ASN1Sequence) ASN1Sequence.fromByteArray(baos.toByteArray());
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    byte[] hash = digest.digest(sequence.getEncoded());
    return new String(Hex.encode(hash), StandardCharsets.UTF_8);
  }

  @SuppressWarnings("unchecked")
  private JWTClaimsSet objectToClaimSet(Object object) {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    Map<String, Object> claimsMap =
        (Map<String, Object>) objectMapper.convertValue(object, Map.class);
    JWTClaimsSet.Builder jwtClaimsBuilder = new JWTClaimsSet.Builder();
    for (Map.Entry<String, Object> entry : claimsMap.entrySet()) {
      jwtClaimsBuilder.claim(entry.getKey(), entry.getValue());
    }
    return jwtClaimsBuilder.build();
  }

  /**
   * Converts Map into Jwt.
   *
   * @param object object to be converted into a jwt.
   * @return Jwt representation of the map.
   */
  @SuppressWarnings("deprecation")
  @SneakyThrows
  public String objectToJwt(@NonNull Object object) {
    JWSObject jwsObject =
        new JWSObject(
            new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(kid()).type(JOSEObjectType.JWT).build(),
            objectToClaimSet(object).toPayload());
    jwsObject.sign(new RSASSASigner(privateKey.toRSAKey(), true));
    return jwsObject.serialize();
  }
}
