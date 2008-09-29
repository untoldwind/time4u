package de.objectcode.time4u.server.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class DefaultKeyChainEncoder implements IKeyChainEncoder
{
  private SecretKey m_secretKey;

  public byte[] generateKeyData()
  {
    try {
      final KeyGenerator generator = KeyGenerator.getInstance("AES");

      m_secretKey = generator.generateKey();
    } catch (final Exception e) {
      throw new RuntimeException("Key generation exception: ", e);
    }

    return m_secretKey.getEncoded();
  }

  public void init(final byte[] keyData)
  {
    try {
      m_secretKey = new SecretKeySpec(keyData, "AES");

    } catch (final Exception e) {
      throw new RuntimeException("Key generation exception: ", e);
    }

  }

  @SuppressWarnings("unchecked")
  public Map<String, String> decrypt(final String encoded)
  {
    try {
      final byte[] input = Base64.decode(encoded.toCharArray());
      final Cipher cipher = Cipher.getInstance("AES");

      cipher.init(Cipher.DECRYPT_MODE, m_secretKey);

      final int length = cipher.getOutputSize(input.length);
      final byte[] output = new byte[length];
      cipher.doFinal(input, 0, input.length, output, 0);

      final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(output));

      return (Map<String, String>) ois.readObject();
    } catch (final Exception e) {
      throw new RuntimeException("Decrypt exception: ", e);
    }
  }

  public String encrypt(final Map<String, String> keyChain)
  {
    try {
      final ByteArrayOutputStream bos = new ByteArrayOutputStream();
      final ObjectOutputStream oos = new ObjectOutputStream(bos);
      oos.writeObject(keyChain);
      oos.close();

      final byte[] serialized = bos.toByteArray();
      final Cipher cipher = Cipher.getInstance("AES");

      cipher.init(Cipher.ENCRYPT_MODE, m_secretKey);

      final int length = cipher.getOutputSize(serialized.length);
      final byte[] output = new byte[length];
      cipher.doFinal(serialized, 0, serialized.length, output, 0);

      return new String(Base64.encode(output));
    } catch (final Exception e) {
      throw new RuntimeException("Encrypt exception: ", e);
    }
  }

}
