package gov.va.api.lighthouse.veteranverification.service.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

@AllArgsConstructor(onConstructor_ = @Autowired)
public class Notary {
  @NonNull private final JwksProperties jwksProperties;

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
  @SneakyThrows
  public String objectToJwt(@NonNull Object object) {
    JWSObject jwsObject =
        new JWSObject(
            new JWSHeader.Builder(JWSAlgorithm.RS256)
                .keyID(jwksProperties.currentKeyId())
                .type(JOSEObjectType.JWT)
                .build(),
            objectToClaimSet(object).toPayload());
    jwsObject.sign(new RSASSASigner(jwksProperties.currentPrivateJwk().toRSAKey()));
    return jwsObject.serialize();
  }
}
