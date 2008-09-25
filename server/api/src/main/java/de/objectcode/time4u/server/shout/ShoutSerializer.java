package de.objectcode.time4u.server.shout;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Arrays;

public class ShoutSerializer
{
  private final static String VERSION_MAGIC = "Time4UScoutV1";

  private final static String DIGEST_ALOGRITHM = "MD5";

  public static byte[] serialize(final ShoutData shoutData)
  {
    // For security reasons we do not use java serialization here
    try {
      final ByteArrayOutputStream bos = new ByteArrayOutputStream();
      final DataOutputStream out = new DataOutputStream(bos);

      out.write(VERSION_MAGIC.getBytes("UTF-8"));
      out.writeUTF(shoutData.getWebserviceBaseURL().toString());
      out.flush();

      // May add real signature later
      final MessageDigest messageDigest = MessageDigest.getInstance(DIGEST_ALOGRITHM);

      out.write(messageDigest.digest(bos.toByteArray()));

      out.flush();
      out.close();

      return bos.toByteArray();
    } catch (final Exception e) {
    }

    return null;
  }

  public static ShoutData deserialize(final byte[] data)
  {
    try {
      final ByteArrayInputStream bis = new ByteArrayInputStream(data);
      final DataInputStream in = new DataInputStream(bis);

      final byte[] realMagic = VERSION_MAGIC.getBytes("UTF-8");
      final byte[] magic = new byte[realMagic.length];

      in.readFully(magic);

      if (!Arrays.equals(realMagic, magic)) {
        return null;
      }

      final MessageDigest messageDigest = MessageDigest.getInstance(DIGEST_ALOGRITHM);

      if (data.length <= messageDigest.getDigestLength()) {
        return null;
      }
      final byte[] digest = new byte[messageDigest.getDigestLength()];
      System.arraycopy(data, data.length - digest.length, digest, 0, digest.length);

      messageDigest.update(data, 0, data.length - digest.length);
      final byte[] realDigest = messageDigest.digest();

      if (!MessageDigest.isEqual(digest, realDigest)) {
        return null;
      }

      // Its save to use DataInputStream, all that might happen is that we create an unusable 64k string
      final String webserviceBaseURL = in.readUTF();

      return new ShoutData(new URL(webserviceBaseURL));
    } catch (final Exception e) {
    }

    return null;
  }
}
