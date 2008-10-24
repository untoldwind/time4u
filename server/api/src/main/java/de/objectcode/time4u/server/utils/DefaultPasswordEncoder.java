package de.objectcode.time4u.server.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Oneway encryption of passwords using message digests.
 * 
 * @author junglas
 */
public class DefaultPasswordEncoder implements IPasswordEncoder
{
  private static SecureRandom RANDOM;

  public String encrypt(final char[] password)
  {
    final byte[] salt = new byte[16];

    RANDOM.nextBytes(salt);

    return encrypt(password, "SSHA", salt, (short) 100);
  }

  /**
   * Encrypt a password with a specific algorithm and salt.
   * 
   * @param password
   *          The password to encrypt
   * @param algorithm
   *          The name of the algorithm
   * @param salt
   *          The (random) salt
   * @return The hashed/encrypted password
   */
  public String encrypt(final char[] password, final String algorithm, final byte[] salt, final short iterations)
  {
    byte passwordData[];

    try {
      passwordData = new String(password).getBytes("UTF-8");
    } catch (final UnsupportedEncodingException e) {
      throw new IllegalStateException("UTF-8 Unsupported");
    }

    final StringBuffer buffer = new StringBuffer();

    MessageDigest messageDigest = null;

    int size = 0;

    if ("SHA".equalsIgnoreCase(algorithm) || "SSHA".equalsIgnoreCase(algorithm)) {
      if (salt != null && salt.length > 0) {
        buffer.append("{SSHA}");
      } else {
        buffer.append("{SHA}");
      }

      try {
        messageDigest = MessageDigest.getInstance("SHA-1");
        size = messageDigest.getDigestLength();
      } catch (final NoSuchAlgorithmException e) {
        throw new IllegalStateException("Invalid algorithm");
      }
    } else if ("MD5".equalsIgnoreCase(algorithm) || "SMD5".equalsIgnoreCase(algorithm)) {
      if (salt != null && salt.length > 0) {
        buffer.append("{SMD5}");
      } else {
        buffer.append("{MD5}");
      }

      try {
        messageDigest = MessageDigest.getInstance("MD5");
        size = messageDigest.getDigestLength();
      } catch (final NoSuchAlgorithmException e) {
        throw new IllegalStateException("Invalid algorithm");
      }
    } else {
      throw new UnsupportedOperationException("Not implemented");
    }

    final byte[] digest = new byte[size];

    for (int i = 0; i < iterations; i++) {
      messageDigest.reset();
      if (i > 0) {
        messageDigest.update(digest);
      }
      messageDigest.update(passwordData);

      if (salt != null && salt.length > 0) {
        messageDigest.update(salt);
      }

      System.arraycopy(messageDigest.digest(), 0, digest, 0, size);
    }

    int outSize = size + 2;
    if (salt != null && salt.length > 0) {
      outSize += salt.length;
    }

    final byte[] out = new byte[outSize];

    out[0] = (byte) (iterations >>> 8 & 0xff);
    out[1] = (byte) (iterations & 0xff);

    System.arraycopy(digest, 0, out, 2, size);

    if (salt != null && salt.length > 0) {
      System.arraycopy(salt, 0, out, size + 2, salt.length);
    }

    buffer.append(new String(Base64.encode(out)));

    return buffer.toString();
  }

  public boolean verify(final char[] password, final String encryptedPassword)
  {
    byte passwordData[];

    try {
      passwordData = new String(password).getBytes("UTF-8");
    } catch (final UnsupportedEncodingException e) {
      throw new IllegalStateException("UTF-8 Unsupported");
    }

    MessageDigest messageDigest = null;

    int size = 0;

    String base64 = null;

    if (encryptedPassword.regionMatches(true, 0, "{SHA}", 0, 5)) {
      base64 = encryptedPassword.substring(5);

      try {
        messageDigest = MessageDigest.getInstance("SHA-1");
        size = messageDigest.getDigestLength();
      } catch (final NoSuchAlgorithmException e) {
        throw new IllegalStateException("Invalid algorithm");
      }
    } else if (encryptedPassword.regionMatches(true, 0, "{SSHA}", 0, 6)) {
      base64 = encryptedPassword.substring(6);

      try {
        messageDigest = MessageDigest.getInstance("SHA-1");
        size = messageDigest.getDigestLength();
      } catch (final NoSuchAlgorithmException e) {
        throw new IllegalStateException("Invalid algorithm");
      }
    } else if (encryptedPassword.regionMatches(true, 0, "{MD5}", 0, 5)) {
      base64 = encryptedPassword.substring(5);

      try {
        messageDigest = MessageDigest.getInstance("MD5");
        size = messageDigest.getDigestLength();
      } catch (final NoSuchAlgorithmException e) {
        throw new IllegalStateException("Invalid algorithm");
      }
    } else if (encryptedPassword.regionMatches(true, 0, "{SMD5}", 0, 6)) {
      base64 = encryptedPassword.substring(6);

      try {
        messageDigest = MessageDigest.getInstance("MD5");
        size = messageDigest.getDigestLength();
      } catch (final NoSuchAlgorithmException e) {
        throw new IllegalStateException("Invalid algorithm");
      }
    } else {
      return false;
    }

    final byte[] data = Base64.decode(base64.toCharArray());
    final byte[] orig = new byte[size];

    final short iterations = (short) ((data[0] & 0xff) << 8 | data[1] & 0xff);

    System.arraycopy(data, 2, orig, 0, size);
    byte[] salt = null;

    if (data.length > size + 2) {
      salt = new byte[data.length - size - 2];
      System.arraycopy(data, size + 2, salt, 0, data.length - size - 2);
    }

    final byte[] digest = new byte[size];

    for (int i = 0; i < iterations; i++) {
      messageDigest.reset();
      if (i > 0) {
        messageDigest.update(digest);
      }

      messageDigest.update(passwordData);
      if (salt != null && salt.length > 0) {
        messageDigest.update(salt);
      }

      System.arraycopy(messageDigest.digest(), 0, digest, 0, size);
    }

    return MessageDigest.isEqual(digest, orig);
  }

  static {
    try {
      RANDOM = SecureRandom.getInstance("SHA1PRNG");

      if (new File("/dev/urandom").exists()) {
        final byte[] salt = new byte[8192];
        new FileInputStream("/dev/urandom").read(salt);
        RANDOM.setSeed(salt);
      }
    } catch (final Exception e) {
      RANDOM = new SecureRandom();
    }
  }
}
