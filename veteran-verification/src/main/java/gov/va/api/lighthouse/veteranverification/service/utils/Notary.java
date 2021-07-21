package gov.va.api.lighthouse.veteranverification.service.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1OutputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.util.encoders.Hex;

public class Notary {
    private RSAPrivateKey privateKey;

    private RSAPublicKey publicKey;

    /**
     * Builder for Notary.
     *
     * @param privateKey File that contains the private key.
     */
    @SneakyThrows
    public Notary(@NonNull File privateKey) {
        KeyPair kp = readKeyPair(privateKey);
        this.publicKey = (RSAPublicKey) kp.getPublic();
        this.privateKey = (RSAPrivateKey) kp.getPrivate();
    }

    @SneakyThrows
    private String kid() {
        BigInteger e = publicKey.getPublicExponent();
        BigInteger n = publicKey.getModulus();
        ASN1EncodableVector vector = new ASN1EncodableVector();
        vector.add(new ASN1Integer(n));
        vector.add(new ASN1Integer(e));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ASN1OutputStream asnOs = new ASN1OutputStream(baos);

        asnOs.writeObject(new DERSequence(vector));
        asnOs.flush();

        ASN1Sequence sequence = (ASN1Sequence) ASN1Sequence.fromByteArray(baos.toByteArray());

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(sequence.getEncoded());
        return new String(Hex.encode(hash), StandardCharsets.UTF_8);
    }

    /**
     * Converts Map into Jwt.
     *
     * @param object object to be converted into a jwt.
     * @return Jwt representation of the map.
     */
    public String objectToJwt(Object object) {
        Map<String, Object> map = objectToMap(object);
        Algorithm algorithmRs = Algorithm.RSA256(publicKey, privateKey);
        return JWT.create().withKeyId(kid()).withPayload(map).sign(algorithmRs);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> objectToMap(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return (Map<String, Object>) objectMapper.convertValue(object, Map.class);
    }

    private KeyPair readKeyPair(File file) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        try (PEMParser pemParser =
                     new PEMParser(Files.newBufferedReader(file.toPath(), Charset.defaultCharset())); ) {
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
            Object object = pemParser.readObject();
            return converter.getKeyPair((PEMKeyPair) object);
        }
    }
}
