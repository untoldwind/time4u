package de.objectcode.time4u.client.store.impl.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import de.objectcode.time4u.client.store.api.RepositoryException;

/**
 * Helper class for common transaction and error handling.
 * 
 * This is a lightweight adaption of the springframework pattern.
 * 
 * @author junglas
 */
public class HibernateTemplate
{
  private final SessionFactory m_sessionFactory;

  public HibernateTemplate(final SessionFactory sessionFactory)
  {
    m_sessionFactory = sessionFactory;
  }

  public SessionFactory getSessionFactory()
  {
    return m_sessionFactory;
  }

  /**
   * Execute a database operation in its own transaction.
   * 
   * @param operation
   *          The operation to be executed
   */
  public void executeInTransaction(final Operation operation) throws RepositoryException
  {
    Transaction trx = null;
    Session session = null;
    try {
      session = m_sessionFactory.openSession();
      trx = session.beginTransaction();

      operation.perform(session);

      trx.commit();
    } catch (final Exception e) {
      //      StorePlugin.getDefault().log(e);
      throw new RepositoryException(e);
    } finally {
      if (trx != null && trx.isActive()) {
        trx.rollback();
      }
      if (session != null) {
        session.close();
      }
    }
  }

  /**
   * Execute a database operation in its own transaction.
   * 
   * @param <T>
   *          The result class
   * @param operation
   *          The operation to be executed
   * @return The result of <tt>operation<tt>
   */
  public <T> T executeInTransaction(final OperationWithResult<T> operation) throws RepositoryException
  {
    Transaction trx = null;
    Session session = null;
    try {
      session = m_sessionFactory.openSession();
      trx = session.beginTransaction();

      final T result = operation.perform(session);

      trx.commit();

      return result;
    } catch (final Exception e) {
      //      StorePlugin.getDefault().log(e);
      throw new RepositoryException(e);
    } finally {
      if (trx != null && trx.isActive()) {
        trx.rollback();
      }
      if (session != null) {
        session.close();
      }
    }
  }

  /**
   * A generic database operation.
   */
  public interface Operation
  {
    void perform(Session session);
  }

  /**
   * A generic database operation with result.
   * 
   * @author junglas
   * 
   * @param <T>
   *          The result class
   */
  public interface OperationWithResult<T>
  {
    T perform(Session session);
  }
}
