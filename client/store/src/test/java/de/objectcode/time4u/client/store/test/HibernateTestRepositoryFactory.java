package de.objectcode.time4u.client.store.test;

import java.io.File;

import de.objectcode.time4u.client.store.api.IRepository;
import de.objectcode.time4u.client.store.impl.hibernate.HibernateRepository;

public class HibernateTestRepositoryFactory
{
  private static IRepository repositoryInstance;

  public static synchronized IRepository getInstance() throws Exception
  {
    if (repositoryInstance == null) {
      repositoryInstance = new HibernateRepository(new File("./target/test"));
    }

    return repositoryInstance;
  }
}
