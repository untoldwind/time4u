package de.objectcode.time4u.client.store.test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.objectcode.time4u.client.store.api.IRepository;
import de.objectcode.time4u.client.store.api.event.RepositoryEventType;
import de.objectcode.time4u.client.store.backend.IDatabaseBackend;
import de.objectcode.time4u.client.store.impl.hibernate.HibernateRepository;

public class HibernateTestRepositoryFactory
{
  private static IRepository repositoryInstance;

  private static Map<RepositoryEventType, RepositoryEventCollector> eventCollectors;

  public static synchronized IRepository getInstance() throws Exception
  {
    if (repositoryInstance == null) {
      final Class<?> databaseBackendClass = Class.forName(System.getProperty("test.databaseBackend"));
      final IDatabaseBackend databaseBackend = (IDatabaseBackend) databaseBackendClass.newInstance();

      repositoryInstance = new HibernateRepository(databaseBackend, new File("./target/test"));

      eventCollectors = new HashMap<RepositoryEventType, RepositoryEventCollector>();

      for (final RepositoryEventType eventType : RepositoryEventType.values()) {
        final RepositoryEventCollector collector = new RepositoryEventCollector(eventType);

        eventCollectors.put(eventType, collector);
        repositoryInstance.addRepositoryListener(eventType, collector);
      }
    }

    return repositoryInstance;
  }

  public static RepositoryEventCollector getEventCollector(final RepositoryEventType type)
  {
    return eventCollectors.get(type);
  }
}
