package de.objectcode.time4u.server.entities.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.entities.revision.IRevisionGenerator;
import de.objectcode.time4u.server.entities.revision.SessionRevisionGenerator;

@Test(groups = "session-revision-generator")
public class SessionRevisionGeneratorTest
{
  private SessionFactory m_sessionFactory;

  @BeforeClass
  public void initialize() throws Exception
  {
    m_sessionFactory = TestSessionFactory.getInstance();
  }

  @Test
  public void testSequential() throws Exception
  {
    for (int i = 0; i < 100; i++) {
      final Session session = m_sessionFactory.openSession();
      final Transaction trx = session.beginTransaction();

      final IRevisionGenerator generator = new SessionRevisionGenerator(session);

      final long nextRevision = generator.getNextRevision(EntityType.PROJECT, null).getLatestRevision();

      trx.commit();
      session.close();

      assertEquals(nextRevision, (i + 1));
    }
  }

  @Test(dependsOnMethods = "testSequential", dataProvider = "concurrentRevisions")
  public void testConcurrentExisting(final long rev1, final long rev2) throws Exception
  {
    final CountDownLatch startLatch = new CountDownLatch(2);
    final CountDownLatch startContinueLatch = new CountDownLatch(1);
    final CountDownLatch startRevisionLatch = new CountDownLatch(2);
    final CountDownLatch startRevisionContinueLatch = new CountDownLatch(1);
    final CountDownLatch getRevisionLatch = new CountDownLatch(1);
    final CountDownLatch getRevisionContinueLatch = new CountDownLatch(1);
    final AtomicInteger inTransaction = new AtomicInteger(0);
    final AtomicInteger inNextRevision = new AtomicInteger(0);
    final List<Exception> innerExceptions = Collections.synchronizedList(new ArrayList<Exception>());
    final List<Long> revisions = Collections.synchronizedList(new ArrayList<Long>());

    final Runnable job = new Runnable() {
      public void run()
      {
        try {
          startLatch.countDown();
          startContinueLatch.await();

          final Session session = m_sessionFactory.openSession();
          final Transaction trx = session.beginTransaction();

          inTransaction.incrementAndGet();

          startRevisionLatch.countDown();
          startRevisionContinueLatch.await();

          final IRevisionGenerator generator = new SessionRevisionGenerator(session);

          revisions.add(generator.getNextRevision(EntityType.PROJECT, null).getLatestRevision());

          inNextRevision.incrementAndGet();

          getRevisionLatch.countDown();
          getRevisionContinueLatch.await();

          trx.commit();

          inNextRevision.decrementAndGet();
          inTransaction.decrementAndGet();
        } catch (final Exception e) {
          innerExceptions.add(e);
        }
      }
    };

    final Thread thread1 = new Thread(job);
    final Thread thread2 = new Thread(job);

    thread1.start();
    thread2.start();

    startLatch.await();

    assertEquals(inTransaction.intValue(), 0);
    assertEquals(inNextRevision.intValue(), 0);

    startContinueLatch.countDown();
    startRevisionLatch.await();

    assertEquals(inTransaction.intValue(), 2);
    assertEquals(inNextRevision.intValue(), 0);

    startRevisionContinueLatch.countDown();
    getRevisionLatch.await();

    assertEquals(inTransaction.intValue(), 2);
    assertEquals(inNextRevision.intValue(), 1);

    getRevisionContinueLatch.countDown();

    thread1.join(2000);
    thread2.join(2000);

    assertEquals(inTransaction.intValue(), 0);
    assertEquals(inNextRevision.intValue(), 0);

    for (final Exception e : innerExceptions) {
      fail("InnerException", e);
    }

    assertEquals(revisions.size(), 2);
    assertEquals((long) revisions.get(0), rev1);
    assertEquals((long) revisions.get(1), rev2);
  }

  @Test(dataProvider = "concurrentNewRevisions")
  public void testConcurrentNew(final long rev1, final long rev2) throws Exception
  {
    final CountDownLatch startLatch = new CountDownLatch(2);
    final CountDownLatch startContinueLatch = new CountDownLatch(1);
    final CountDownLatch startRevisionLatch = new CountDownLatch(2);
    final CountDownLatch startRevisionContinueLatch = new CountDownLatch(1);
    final CountDownLatch getRevisionLatch = new CountDownLatch(1);
    final CountDownLatch getRevisionContinueLatch = new CountDownLatch(1);
    final AtomicInteger inTransaction = new AtomicInteger(0);
    final AtomicInteger inNextRevision = new AtomicInteger(0);
    final List<Exception> innerExceptions = Collections.synchronizedList(new ArrayList<Exception>());
    final List<Long> revisions = Collections.synchronizedList(new ArrayList<Long>());

    final Runnable job = new Runnable() {
      public void run()
      {
        try {
          startLatch.countDown();
          startContinueLatch.await();

          final Session session = m_sessionFactory.openSession();
          final Transaction trx = session.beginTransaction();

          inTransaction.incrementAndGet();

          startRevisionLatch.countDown();
          startRevisionContinueLatch.await();

          final IRevisionGenerator generator = new SessionRevisionGenerator(session);

          revisions.add(generator.getNextRevision(EntityType.TASK, null).getLatestRevision());

          inNextRevision.incrementAndGet();

          getRevisionLatch.countDown();
          getRevisionContinueLatch.await();

          trx.commit();

          inNextRevision.decrementAndGet();
          inTransaction.decrementAndGet();
        } catch (final Exception e) {
          innerExceptions.add(e);
        }
      }
    };

    final Thread thread1 = new Thread(job);
    final Thread thread2 = new Thread(job);

    thread1.start();
    thread2.start();

    startLatch.await();

    assertEquals(inTransaction.intValue(), 0);
    assertEquals(inNextRevision.intValue(), 0);

    startContinueLatch.countDown();
    startRevisionLatch.await();

    assertEquals(inTransaction.intValue(), 2);
    assertEquals(inNextRevision.intValue(), 0);

    startRevisionContinueLatch.countDown();
    getRevisionLatch.await();

    assertEquals(inTransaction.intValue(), 2);
    assertEquals(inNextRevision.intValue(), 1);

    getRevisionContinueLatch.countDown();

    thread1.join(2000);
    thread2.join(2000);

    assertEquals(inTransaction.intValue(), 0);
    assertEquals(inNextRevision.intValue(), 0);

    for (final Exception e : innerExceptions) {
      fail("InnerException", e);
    }

    assertEquals(revisions.size(), 2);
    assertEquals((long) revisions.get(0), rev1);
    assertEquals((long) revisions.get(1), rev2);
  }

  @DataProvider(name = "concurrentRevisions")
  public Object[][] getGetConcurrentRevisions()
  {
    final Object[][] revisions = new Object[5][];

    for (int i = 0; i < 5; i++) {
      revisions[i] = new Object[] { 101L + 2 * i, 102L + 2 * i };
    }

    return revisions;
  }

  @DataProvider(name = "concurrentNewRevisions")
  public Object[][] getGetConcurrentNewRevisions()
  {
    final Object[][] revisions = new Object[5][];

    for (int i = 0; i < 5; i++) {
      revisions[i] = new Object[] { 1L + 2 * i, 2L + 2 * i };
    }

    return revisions;
  }
}
