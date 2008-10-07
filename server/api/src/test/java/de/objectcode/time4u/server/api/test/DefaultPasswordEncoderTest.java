package de.objectcode.time4u.server.api.test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import de.objectcode.time4u.server.utils.DefaultPasswordEncoder;

@Test(groups = "default-passowrdencoder")
public class DefaultPasswordEncoderTest
{
  DefaultPasswordEncoder passwordEncoder = new DefaultPasswordEncoder();

  @Test(dataProvider = "algorithms")
  public void testDirect(final String algorithm) throws Exception
  {
    final List<String> passwords = generatePasswords();
    final List<String> crypteds = new ArrayList<String>();

    final Random random = new Random(1234);
    final byte[] salt = new byte[16];
    for (final String password : passwords) {
      random.nextBytes(salt);
      final String crypted = passwordEncoder.encrypt(password.toCharArray(), algorithm, salt, (short) 100);
      assertNotNull(crypted);
      crypteds.add(crypted);
    }

    for (int i = 0; i < passwords.size(); i++) {
      final String password = passwords.get(i);

      for (int j = 0; j < crypteds.size(); j++) {
        final String crypted = crypteds.get(j);

        if (i == j) {
          assertTrue(passwordEncoder.verify(password.toCharArray(), crypted));
        } else {
          assertFalse(passwordEncoder.verify(password.toCharArray(), crypted));
        }
      }
    }
  }

  public void testDefault() throws Exception
  {
    final List<String> passwords = generatePasswords();
    final List<String> crypteds = new ArrayList<String>();

    final Random random = new Random(1234);
    final byte[] salt = new byte[16];
    for (final String password : passwords) {
      random.nextBytes(salt);
      final String crypted = passwordEncoder.encrypt(password.toCharArray());
      assertNotNull(crypted);
      crypteds.add(crypted);
    }

    for (int i = 0; i < passwords.size(); i++) {
      final String password = passwords.get(i);

      for (int j = 0; j < crypteds.size(); j++) {
        final String crypted = crypteds.get(j);

        if (i == j) {
          assertTrue(passwordEncoder.verify(password.toCharArray(), crypted));
        } else {
          assertFalse(passwordEncoder.verify(password.toCharArray(), crypted));
        }
      }
    }
  }

  @DataProvider(name = "algorithms")
  public Object[][] getAlgorithms()
  {
    return new Object[][] { { "SHA" }, { "SSHA" }, { "MD5" }, { "SMD5" } };
  }

  private List<String> generatePasswords()
  {
    final List<String> result = new ArrayList<String>();
    final Random random = new Random(1234);

    for (int i = 0; i < 100; i++) {
      final StringBuffer out = new StringBuffer();

      for (int j = 0; j < 8 + i % 5; j++) {
        out.append(Integer.toString(random.nextInt(36), 36));
      }

      result.add(out.toString());
    }
    return result;
  }

}
