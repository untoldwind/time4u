package de.objectcode.time4u.client.connection.ui.views;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import de.objectcode.time4u.client.connection.ui.ConnectionUIPlugin;
import de.objectcode.time4u.client.connection.ui.jobs.SynchronizationStatus;
import de.objectcode.time4u.client.connection.ui.jobs.SynchronizeJob;
import de.objectcode.time4u.server.api.data.ServerConnection;

public class SynchronizeViewTab
{
  ServerConnection m_serverConnection;

  Composite m_top;
  Label m_statusLabel;
  Label m_lastRunLabel;
  Label m_lastErrorLabel;
  Button m_synchronizeNow;

  SimpleDateFormat m_lastRunFormat;

  public SynchronizeViewTab(final ServerConnection serverConnection)
  {
    m_serverConnection = serverConnection;
  }

  public void createControls(final TabFolder parent)
  {
    final TabItem item = new TabItem(parent, SWT.NONE);
    item.setText(m_serverConnection.getName());

    final Composite top = new Composite(parent, SWT.NONE);

    m_top = top;
    item.setControl(top);

    final GridLayout layout = new GridLayout();
    layout.marginHeight = 5;
    layout.marginWidth = 10;
    layout.numColumns = 1;
    top.setLayout(layout);

    final Composite labelBar = new Composite(top, SWT.NONE);

    labelBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    labelBar.setLayout(new GridLayout(2, false));

    final Label statusLabel = new Label(labelBar, SWT.LEFT);

    statusLabel.setText(ConnectionUIPlugin.getDefault().getString("synchronizeView.status.label"));

    m_statusLabel = new Label(labelBar, SWT.CENTER | SWT.BORDER);
    m_statusLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

    final Label lastRunLabel = new Label(labelBar, SWT.LEFT);

    lastRunLabel.setText(ConnectionUIPlugin.getDefault().getString("synchronizeView.lastRun.label"));

    m_lastRunLabel = new Label(labelBar, SWT.CENTER | SWT.BORDER);
    m_lastRunLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    m_lastRunFormat = new SimpleDateFormat(ConnectionUIPlugin.getDefault().getString("synchronizeView.lastRun.format"));

    final Label lastErrorLabel = new Label(labelBar, SWT.LEFT);

    lastErrorLabel.setText(ConnectionUIPlugin.getDefault().getString("synchronizeView.lastError.label"));

    m_lastErrorLabel = new Label(labelBar, SWT.CENTER | SWT.BORDER);
    m_lastErrorLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

    final Composite buttonBar = new Composite(top, SWT.NONE);

    buttonBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    buttonBar.setLayout(new RowLayout());

    m_synchronizeNow = new Button(buttonBar, SWT.PUSH);
    m_synchronizeNow.setText(ConnectionUIPlugin.getDefault().getString("synchronizeView.synchronize.now"));
    m_synchronizeNow.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(final SelectionEvent e)
      {
        final SynchronizeJob job = ConnectionUIPlugin.getDefault().getSynchronizeJob(m_serverConnection.getId());

        if (job != null) {
          job.schedule();
          job.wakeUp();
        }
      }
    });

    final SynchronizeJob job = ConnectionUIPlugin.getDefault().getSynchronizeJob(m_serverConnection.getId());

    if (job != null) {
      job.addJobChangeListener(new JobChangeAdapter() {
        @Override
        public void done(final IJobChangeEvent event)
        {
          updateLabels();
        }
      });
    }

    updateLabels();
  }

  protected void updateLabels()
  {
    if (!m_top.isDisposed()) {
      m_top.getDisplay().asyncExec(new Runnable() {
        public void run()
        {
          final SynchronizeJob job = ConnectionUIPlugin.getDefault().getSynchronizeJob(m_serverConnection.getId());
          final SynchronizationStatus jobStatus = job != null ? job.getStatus() : SynchronizationStatus.NONE;
          final Date lastRun = job != null ? job.getLastRun() : null;

          switch (jobStatus) {
            case NONE:
              m_statusLabel.setText(ConnectionUIPlugin.getDefault().getString("synchronizeView.status.none"));
              m_statusLabel.setBackground(m_statusLabel.getDisplay().getSystemColor(SWT.COLOR_GRAY));
              m_lastErrorLabel.setText("");
              break;
            case SCHEDULED:
              m_statusLabel.setText(ConnectionUIPlugin.getDefault().getString("synchronizeView.status.scheduled"));
              m_statusLabel.setBackground(m_statusLabel.getDisplay().getSystemColor(SWT.COLOR_YELLOW));
              m_lastErrorLabel.setText("");
              break;
            case OK:
              m_statusLabel.setText(ConnectionUIPlugin.getDefault().getString("synchronizeView.status.ok"));
              m_statusLabel.setBackground(m_statusLabel.getDisplay().getSystemColor(SWT.COLOR_GREEN));
              m_lastErrorLabel.setText("");
              break;
            case FAILURE:
              m_statusLabel.setText(ConnectionUIPlugin.getDefault().getString("synchronizeView.status.failure"));
              m_statusLabel.setBackground(m_statusLabel.getDisplay().getSystemColor(SWT.COLOR_RED));
              m_lastErrorLabel.setText(job.getLastError());
              break;
          }
          if (lastRun != null) {
            m_lastRunLabel.setText(m_lastRunFormat.format(lastRun));
          }
        }
      });
    }
  }
}
