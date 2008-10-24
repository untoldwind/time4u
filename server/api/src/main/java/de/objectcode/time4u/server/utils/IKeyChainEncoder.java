package de.objectcode.time4u.server.utils;

import java.util.Map;

/**
 * Interface of a key chain encoder.
 * 
 * Contrary to a password encode a key chain encoder has to be able to decrypt the encoded values. This can either be
 * achieved with a symmetric cipher or a public/private key cipher.
 * 
 * @author junglas
 */
public interface IKeyChainEncoder
{
  /**
   * Generate a new master key.
   * 
   * @return The key data
   */
  byte[] generateKeyData();

  /**
   * Initialize the encoder.
   * 
   * @param keyData
   *          Date raw data of the secret key used for encoding and decoding
   */
  void init(byte[] keyData);

  /**
   * Encrypt a key chain.
   * 
   * @param keyChain
   *          The data of the key chain
   * @return The encrypted data
   */
  String encrypt(Map<String, String> keyChain);

  /**
   * Decrypt a key chain.
   * 
   * @param encoded
   *          The encrypted data
   * @return The data of the key chain
   */
  Map<String, String> decrypt(String encoded);
}
