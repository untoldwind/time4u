package de.objectcode.time4u.server.entities.test;

import static org.testng.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import de.objectcode.time4u.server.api.data.SynchronizableType;
import de.objectcode.time4u.server.entities.revision.ILocalIdGenerator;
import de.objectcode.time4u.server.entities.revision.SessionLocalIdGenerator;

@Test(groups = "session-localid-generator")
public class SessionLocalIdGeneratorTest
{
  Map<SynchronizableType, Set<String>> m_generatedIds = new HashMap<SynchronizableType, Set<String>>();

  @Test(dataProvider = "types")
  public void test1(final SynchronizableType type) throws Exception
  {
    Set<String> idSet;
    synchronized (m_generatedIds) {
      idSet = m_generatedIds.get(type);

      if (idSet == null) {
        idSet = new HashSet<String>();
        m_generatedIds.put(type, idSet);
      }
    }

    final ILocalIdGenerator localIdGenerator = new SessionLocalIdGenerator(TestSessionFactory.getInstance(), 1L);

    for (int i = 0; i < 550; i++) {
      idSet.add(localIdGenerator.generateLocalId(type));
    }

    assertEquals(idSet.size(), 550);
  }

  @Test(dependsOnMethods = "test1", dataProvider = "types")
  public void test2(final SynchronizableType type) throws Exception
  {
    Set<String> idSet;
    synchronized (m_generatedIds) {
      idSet = m_generatedIds.get(type);

      if (idSet == null) {
        idSet = new HashSet<String>();
        m_generatedIds.put(type, idSet);
      }
    }

    final ILocalIdGenerator localIdGenerator = new SessionLocalIdGenerator(TestSessionFactory.getInstance(), 1L);

    for (int i = 0; i < 550; i++) {
      idSet.add(localIdGenerator.generateLocalId(type));
    }

    assertEquals(idSet.size(), 1100);
  }

  @DataProvider(name = "types")
  public Object[][] getEntityTypes()
  {
    final Object[][] result = new Object[SynchronizableType.values().length][];

    int i = 0;
    for (final SynchronizableType type : SynchronizableType.values()) {
      result[i++] = new Object[] { type };
    }

    return result;
  }

}
