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

    return encrypt(password, "SSHA", salt);
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
  public String encrypt(final char[] password, final String algorithm, final byte[] salt)
  {
    final StringBuffer buffer = new StringBuffer();

    MessageDigest digest = null;

    int size = 0;

    if ("SHA".equalsIgnoreCase(algorithm) || "SSHA".equalsIgnoreCase(algorithm)) {
      size = 20;
      if (salt != null && salt.length > 0) {
        buffer.append("{SSHA}");
      } else {
        buffer.append("{SHA}");
      }

      try {
        digest = MessageDigest.getInstance("SHA-1");
      } catch (final NoSuchAlgorithmException e) {
        throw new IllegalStateException("Invalid algorithm");
      }
    } else if ("MD5".equalsIgnoreCase(algorithm) || "SMD5".equalsIgnoreCase(algorithm)) {
      size = 16;
      if (salt != null && salt.length > 0) {
        buffer.append("{SMD5}");
      } else {
        buffer.append("{MD5}");
      }

      try {
        digest = MessageDigest.getInstance("MD5");
      } catch (final NoSuchAlgorithmException e) {
        throw new IllegalStateException("Invalid algorithm");
      }
    } else {
      throw new UnsupportedOperationException("Not implemented");
    }

    int outSize = size;

    digest.reset();

    try {
      digest.update(new String(password).getBytes("UTF-8"));
    } catch (final UnsupportedEncodingException e) {
      throw new IllegalStateException("UTF-8 Unsupported");
    }

    if (salt != null && salt.length > 0) {
      digest.update(salt);
      outSize += salt.length;
    }

    final byte[] out = new byte[outSize];

    System.arraycopy(digest.digest(), 0, out, 0, size);

    if (salt != null && salt.length > 0) {
      System.arraycopy(salt, 0, out, size, salt.length);
    }

    buffer.append(new String(Base64.encode(out)));

    return buffer.toString();

  }

  public boolean verify(final char[] password, final String encryptedPassword)
  {
    MessageDigest digest = null;

    int size = 0;

    String base64 = null;

    if (encryptedPassword.regionMatches(true, 0, "{SHA}", 0, 5)) {
      size = 20;
      base64 = encryptedPassword.substring(5);

      try {
        digest = MessageDigest.getInstance("SHA-1");
      } catch (final NoSuchAlgorithmException e) {
        throw new IllegalStateException("Invalid algorithm");
      }
    } else if (encryptedPassword.regionMatches(true, 0, "{SSHA}", 0, 6)) {
      size = 20;
      base64 = encryptedPassword.substring(6);

      try {
        digest = MessageDigest.getInstance("SHA-1");
      } catch (final NoSuchAlgorithmException e) {
        throw new IllegalStateException("Invalid algorithm");
      }
    } else if (encryptedPassword.regionMatches(true, 0, "{MD5}", 0, 5)) {
      size = 16;
      base64 = encryptedPassword.substring(5);

      try {
        digest = MessageDigest.getInstance("MD5");
      } catch (final NoSuchAlgorithmException e) {
        throw new IllegalStateException("Invalid algorithm");
      }
    } else if (encryptedPassword.regionMatches(true, 0, "{SMD5}", 0, 6)) {
      size = 16;
      base64 = encryptedPassword.substring(6);

      try {
        digest = MessageDigest.getInstance("MD5");
      } catch (final NoSuchAlgorithmException e) {
        throw new IllegalStateException("Invalid algorithm");
      }
    } else {
      return false;
    }

    try {
      final byte[] data = Base64.decode(base64.toCharArray());
      final byte[] orig = new byte[size];

      System.arraycopy(data, 0, orig, 0, size);

      digest.reset();
      digest.update(new String(password).getBytes("UTF-8"));

      if (data.length > size) {
        digest.update(data, size, data.length - size);
      }

      return MessageDigest.isEqual(digest.digest(), orig);
    } catch (final UnsupportedEncodingException e) {
      throw new IllegalStateException("UTF-8 Unsupported");
    }
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
