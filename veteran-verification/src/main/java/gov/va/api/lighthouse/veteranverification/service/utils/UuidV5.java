package gov.va.api.lighthouse.veteranverification.service.utils;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.UUID;

@SuppressFBWarnings({"WEAK_MESSAGE_DIGEST_SHA1"})
public class UuidV5 {
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

  private static UUID nameUuidFromNamespaceAndBytes(byte[] namespace, byte[] name) {
    MessageDigest md;
    try {
      md = MessageDigest.getInstance("SHA-1");
    } catch (NoSuchAlgorithmException nsae) {
      throw new InternalError("SHA-1 not supported");
    }
    md.update(Objects.requireNonNull(namespace, "namespace is null"));
    md.update(Objects.requireNonNull(name, "name is null"));
    byte[] sha1Bytes = md.digest();
    sha1Bytes[6] = (byte) (sha1Bytes[6] & 0x0f);
    /* clear version        */
    sha1Bytes[6] = (byte) (sha1Bytes[6] | 0x50);
    /* set to version 5     */
    sha1Bytes[8] = (byte) (sha1Bytes[8] & 0x3f);
    /* clear variant        */
    sha1Bytes[8] = (byte) (sha1Bytes[8] | 0x80);
    /* set to IETF variant  */
    return fromBytes(sha1Bytes);
  }

  /**
   * Build UUIDV5 from namespace and name.
   *
   * @param namespace UUID namespace.
   * @param name UUID name.
   * @return UUID V5 Object.
   */
  public static UUID nameUuidFromNamespaceAndString(String namespace, String name) {
    return nameUuidFromNamespaceAndBytes(namespace.getBytes(UTF8), name.getBytes(UTF8));
  }
}
