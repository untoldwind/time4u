package de.objectcode.time4u.server.utils;

public interface IPasswordEncoder
{
  /**
   * Encrypt a password.
   * 
   * @param password
   *          The password to encrypt
   * @return The hashed/encrypted password
   */
  String encrypt(final char[] password);

  /**
   * Verify a password against an encoded password.
   * 
   * @param password
   *          The password to be verified
   * @param encryptedPassword
   *          The encrypted password
   * @return <tt>true</tt> if <tt>password</tt> matches <tt>encryptedPassword</tt>
   */
  boolean verify(final char[] password, final String encryptedPassword);
}
