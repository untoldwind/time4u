package de.objectcode.time4u.client.connection.ui.jobs;

import java.util.Date;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import de.objectcode.time4u.client.connection.api.ConnectionFactory;
import de.objectcode.time4u.client.connection.api.IConnection;
import de.objectcode.time4u.client.connection.ui.ConnectionUIPlugin;
import de.objectcode.time4u.server.api.data.ServerConnection;

public class SynchronizeJob extends Job
{
  private final ServerConnection m_serverConnection;
  private Date m_lastRun;
  private SynchronizationStatus m_status;

  public SynchronizeJob(final ServerConnection serverConnection)
  {
    super("Synchronize Job");

    m_serverConnection = serverConnection;
    m_status = SynchronizationStatus.NONE;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected IStatus run(final IProgressMonitor monitor)
  {
    try {
      final IConnection connection = ConnectionFactory.openConnection(m_serverConnection);

      connection.sychronizeNow(monitor);

      m_status = SynchronizationStatus.OK;
    } catch (final Exception e) {
      ConnectionUIPlugin.getDefault().log(e);
      m_status = SynchronizationStatus.FAILURE;
    } finally {
      monitor.done();
    }
    m_lastRun = new Date();

    reschedule();

    return Status.OK_STATUS;
  }

  public Date getLastRun()
  {
    return m_lastRun;
  }

  public SynchronizationStatus getStatus()
  {
    return m_status;
  }

  public void reschedule()
  {
    if (m_status == SynchronizationStatus.NONE) {
      m_status = SynchronizationStatus.SCHEDULED;
    }
    if (m_serverConnection.getSynchronizeInterval() > 0) {
      schedule(m_serverConnection.getSynchronizeInterval() * 1000L);
    }
  }
}
