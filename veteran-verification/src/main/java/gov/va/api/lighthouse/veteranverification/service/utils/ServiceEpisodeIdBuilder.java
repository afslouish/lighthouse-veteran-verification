package gov.va.api.lighthouse.veteranverification.service.utils;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
import lombok.experimental.UtilityClass;

@SuppressFBWarnings({"WEAK_MESSAGE_DIGEST_SHA1"})
@UtilityClass
public class ServiceEpisodeIdBuilder {
  /**
   * Builds uuid5 service episode id.
   *
   * @param uuid Unique user identity.
   * @param beginDate Service episode begin date.
   * @param endDate Service episode end date.
   * @return Service episode uuid5 id.
   */
  public String buildServiceEpisodeId(String uuid, LocalDate beginDate, LocalDate endDate) {
    String beginDateStr = beginDate.toString().trim();
    return uuidv5(
        "gov.vets.service-history-episodes",
        String.format("%s-%s-%s", uuid.trim(), beginDateStr, endDate.toString().trim()));
  }

  private String uuidv5(String nameSpace, String str) {
    String sha1 = org.apache.commons.codec.digest.DigestUtils.sha1Hex(nameSpace);
    return UuidType5.nameUuidFromNamespaceAndString(UUID.fromString(sha1), str).toString();
  }

  public static class UuidType5 {
    public static final UUID NAMESPACE_DNS =
        UUID.fromString("6ba7b810-9dad-11d1-80b4-00c04fd430c8");

    public static final UUID NAMESPACE_URL =
        UUID.fromString("6ba7b811-9dad-11d1-80b4-00c04fd430c8");

    public static final UUID NAMESPACE_OID =
        UUID.fromString("6ba7b812-9dad-11d1-80b4-00c04fd430c8");

    public static final UUID NAMESPACE_X500 =
        UUID.fromString("6ba7b814-9dad-11d1-80b4-00c04fd430c8");

    private static final Charset UTF8 = Charset.forName("UTF-8");

    private static UUID fromBytes(byte[] data) {
      // Based on the private UUID(bytes[]) constructor
      long msb = 0;
      long lsb = 0;
      assert data.length >= 16;
      for (int i = 0; i < 8; i++) {
        msb = (msb << 8) | (data[i] & 0xff);
      }
      for (int i = 8; i < 16; i++) {
        lsb = (lsb << 8) | (data[i] & 0xff);
      }
      return new UUID(msb, lsb);
    }

    private static UUID nameUuidFromNamespaceAndBytes(UUID namespace, byte[] name) {
      MessageDigest md;
      try {
        md = MessageDigest.getInstance("SHA-1");
      } catch (NoSuchAlgorithmException nsae) {
        throw new InternalError("SHA-1 not supported");
      }
      md.update(toBytes(Objects.requireNonNull(namespace, "namespace is null")));
      md.update(Objects.requireNonNull(name, "name is null"));
      byte[] sha1Bytes = md.digest();
      sha1Bytes[6] = (byte) (sha1Bytes[6] & 0x0f);
      /* clear version        */
      sha1Bytes[6] = (byte) (sha1Bytes[6] & 0x0f);
      /* set to version 5     */
      sha1Bytes[8] = (byte) (sha1Bytes[8] & 0x3f);
      /* clear variant        */
      sha1Bytes[8] = (byte) (sha1Bytes[8] | 0x80);
      /* set to IETF variant  */
      return fromBytes(sha1Bytes);
    }

    /**
     * Creates UUID V5 from namespace and name.
     *
     * @param namespace Namespace for uuid.
     * @param name Value being converted into uuid.
     * @return A uuid created with a namespace and name.
     */
    public static UUID nameUuidFromNamespaceAndString(UUID namespace, String name) {
      return nameUuidFromNamespaceAndBytes(
          namespace, Objects.requireNonNull(name, "name == null").getBytes(UTF8));
    }

    private static byte[] toBytes(UUID uuid) {
      // inverted logic of fromBytes()
      byte[] out = new byte[16];
      long msb = uuid.getMostSignificantBits();
      long lsb = uuid.getLeastSignificantBits();
      for (int i = 0; i < 8; i++) {
        out[i] = (byte) ((msb >> ((7 - i) * 8)) & 0xff);
      }
      for (int i = 8; i < 16; i++) {
        out[i] = (byte) ((lsb >> ((15 - i) * 8)) & 0xff);
      }
      return out;
    }
  }
}
