package de.objectcode.time4u.server.utils;

/**
 * @author Bodo Junglas
 * @created 7. March 2002
 * @version $Id: Base64.java,v 1.1 2003/03/14 23:37:20 junglas Exp $
 */
public class Base64
{
  private static final char g_alphabet[] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="
      .toCharArray();

  private static final byte g_codes[] = new byte[256];

  private static final char g_altAlphabet[] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_."
      .toCharArray();

  private static final byte g_altCodes[] = new byte[256];

  /**
   * Description of the Method
   * 
   * @param data
   *          Description of the Parameter
   * @return Description of the Return Value
   */
  public static byte[] decode(final char data[])
  {
    int tempLen = data.length;
    for (int ix = 0; ix < data.length; ix++) {
      final int value = g_codes[data[ix] & 0xff];
      if (value < 0 && data[ix] != '=') {
        tempLen--;
      }
    }

    int len = (tempLen + 3) / 4 * 3;
    if (tempLen > 0 && data[tempLen - 1] == '=') {
      len--;
    }
    if (tempLen > 1 && data[tempLen - 2] == '=') {
      len--;
    }
    final byte out[] = new byte[len];
    int shift = 0;
    int accum = 0;
    int index = 0;
    for (int ix = 0; ix < data.length; ix++) {
      final int value = g_codes[data[ix] & 0xff];
      if (value >= 0) {
        accum <<= 6;
        shift += 6;
        accum |= value;
        if (shift >= 8) {
          shift -= 8;
          out[index++] = (byte) (accum >> shift & 0xff);
        }
      }
    }

    if (index != out.length) {
      throw new IllegalStateException("Invalid length");
    }

    return out;
  }

  /**
   * Description of the Method
   * 
   * @param data
   *          Description of the Parameter
   * @return Description of the Return Value
   */
  public static byte[] altDecode(final char data[])
  {
    int tempLen = data.length;
    for (int ix = 0; ix < data.length; ix++) {
      final int value = g_altCodes[data[ix] & 0xff];
      if (value < 0 && data[ix] != '.') {
        tempLen--;
      }
    }

    int len = (tempLen + 3) / 4 * 3;
    if (tempLen > 0 && data[tempLen - 1] == '.') {
      len--;
    }
    if (tempLen > 1 && data[tempLen - 2] == '.') {
      len--;
    }
    final byte out[] = new byte[len];
    int shift = 0;
    int accum = 0;
    int index = 0;
    for (int ix = 0; ix < data.length; ix++) {
      final int value = g_altCodes[data[ix] & 0xff];
      if (value >= 0) {
        accum <<= 6;
        shift += 6;
        accum |= value;
        if (shift >= 8) {
          shift -= 8;
          out[index++] = (byte) (accum >> shift & 0xff);
        }
      }
    }

    if (index != out.length) {
      throw new IllegalStateException("Invalid length");
    }

    return out;
  }

  /**
   * Description of the Method
   * 
   * @param data
   *          Description of the Parameter
   * @return Description of the Return Value
   */
  public static char[] encode(final byte data[])
  {
    final char out[] = new char[(data.length + 2) / 3 * 4];
    int i = 0;
    for (int index = 0; i < data.length; index += 4) {
      boolean quad = false;
      boolean trip = false;
      int val = 0xff & data[i];
      val <<= 8;
      if (i + 1 < data.length) {
        val |= 0xff & data[i + 1];
        trip = true;
      }
      val <<= 8;
      if (i + 2 < data.length) {
        val |= 0xff & data[i + 2];
        quad = true;
      }
      out[index + 3] = g_alphabet[quad ? val & 0x3f : 64];
      val >>= 6;
      out[index + 2] = g_alphabet[trip ? val & 0x3f : 64];
      val >>= 6;
      out[index + 1] = g_alphabet[val & 0x3f];
      val >>= 6;
      out[index] = g_alphabet[val & 0x3f];
      i += 3;
    }

    return out;
  }

  /**
   * Description of the Method
   * 
   * @param data
   *          Description of the Parameter
   * @return Description of the Return Value
   */
  public static char[] altEncode(final byte data[])
  {
    final char out[] = new char[(data.length + 2) / 3 * 4];
    int i = 0;
    for (int index = 0; i < data.length; index += 4) {
      boolean quad = false;
      boolean trip = false;
      int val = 0xff & data[i];
      val <<= 8;
      if (i + 1 < data.length) {
        val |= 0xff & data[i + 1];
        trip = true;
      }
      val <<= 8;
      if (i + 2 < data.length) {
        val |= 0xff & data[i + 2];
        quad = true;
      }
      out[index + 3] = g_altAlphabet[quad ? val & 0x3f : 64];
      val >>= 6;
      out[index + 2] = g_altAlphabet[trip ? val & 0x3f : 64];
      val >>= 6;
      out[index + 1] = g_altAlphabet[val & 0x3f];
      val >>= 6;
      out[index] = g_altAlphabet[val & 0x3f];
      i += 3;
    }

    return out;
  }

  static {
    for (int i = 0; i < 256; i++) {
      g_codes[i] = -1;
    }

    for (int i = 65; i <= 90; i++) {
      g_codes[i] = (byte) (i - 65);
    }

    for (int i = 97; i <= 122; i++) {
      g_codes[i] = (byte) (26 + i - 97);
    }

    for (int i = 48; i <= 57; i++) {
      g_codes[i] = (byte) (52 + i - 48);
    }

    g_codes[43] = 62;
    g_codes[47] = 63;

    for (int i = 0; i < 256; i++) {
      g_altCodes[i] = -1;
    }

    for (int i = 65; i <= 90; i++) {
      g_altCodes[i] = (byte) (i - 65);
    }

    for (int i = 97; i <= 122; i++) {
      g_altCodes[i] = (byte) (26 + i - 97);
    }

    for (int i = 48; i <= 57; i++) {
      g_altCodes[i] = (byte) (52 + i - 48);
    }

    g_altCodes[45] = 62;
    g_altCodes[95] = 63;
  }
}
