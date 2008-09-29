package de.objectcode.time4u.server.api.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.testng.annotations.Test;

import de.objectcode.time4u.server.utils.DefaultKeyChainEncoder;
import de.objectcode.time4u.server.utils.IKeyChainEncoder;

@Test(groups = "default-keychainencoder")
public class DefaultKeyChainEncoderTest
{
  byte[] m_keyData;
  List<Map<String, String>> m_datas;
  List<String> m_crypteds;

  @Test
  public void test1() throws Exception
  {
    final IKeyChainEncoder keyChainEncoder = new DefaultKeyChainEncoder();

    m_keyData = keyChainEncoder.generateKeyData();

    assertNotNull(m_keyData);
    assertEquals(m_keyData.length, 16);

    m_datas = generateKeyChainData();
    m_crypteds = new ArrayList<String>();

    for (final Map<String, String> data : m_datas) {
      final String crypted = keyChainEncoder.encrypt(data);

      assertNotNull(crypted);
      m_crypteds.add(crypted);
    }

    for (int i = 0; i < m_datas.size(); i++) {
      final Map<String, String> origData = m_datas.get(i);
      final String crypted = m_crypteds.get(i);

      final Map<String, String> data = keyChainEncoder.decrypt(crypted);
      assertEquals(data, origData);
    }
  }

  @Test(dependsOnMethods = "test1")
  public void test2() throws Exception
  {
    final IKeyChainEncoder keyChainEncoder = new DefaultKeyChainEncoder();

    keyChainEncoder.init(m_keyData);

    for (int i = 0; i < m_datas.size(); i++) {
      final Map<String, String> origData = m_datas.get(i);
      final String crypted = m_crypteds.get(i);

      final Map<String, String> data = keyChainEncoder.decrypt(crypted);
      assertEquals(data, origData);
    }
  }

  private List<Map<String, String>> generateKeyChainData()
  {
    final List<Map<String, String>> result = new ArrayList<Map<String, String>>();
    final Random random = new Random(1234);

    for (int i = 0; i < 100; i++) {
      final Map<String, String> data = new HashMap<String, String>();

      for (int k = 0; k < 3 + i % 4; k++) {
        final StringBuffer out1 = new StringBuffer();
        final StringBuffer out2 = new StringBuffer();

        for (int j = 0; j < 8 + i % 5; j++) {
          out1.append(Integer.toString(random.nextInt(36), 36));
          out2.append(Integer.toString(random.nextInt(36), 36));
        }

        data.put(out1.toString(), out2.toString());
      }

      result.add(data);
    }
    return result;
  }
}
