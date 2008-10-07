package de.objectcode.time4u.client.connection.impl.common;

import org.eclipse.core.runtime.IProgressMonitor;

import de.objectcode.time4u.client.connection.api.ConnectionException;
import de.objectcode.time4u.client.store.api.RepositoryException;

/**
 * Generic synchronization command.
 * 
 * @author junglas
 */
public interface ISynchronizationCommand
{
  /**
   * Test if the command needs to be executed.
   * 
   * @param context
   *          The synchronization context
   * @return <tt>true</tt> if the command should be executed
   */
  boolean shouldRun(SynchronizationContext context);

  /**
   * Execute the command.
   * 
   * @param context
   *          The synchronization context.
   * 
   * @throws RepositoryException
   *           on error
   * @throws ConnectionException
   *           on error
   */
  void execute(SynchronizationContext context, IProgressMonitor monitor) throws RepositoryException,
      ConnectionException;
}
