package de.objectcode.time4u.client.connection.ui.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

import de.objectcode.time4u.server.api.data.ServerConnection;

public class SynchronizationWizardPage extends WizardPage
{
  ServerConnection m_serverConnection;

  Spinner m_synchronizeItervalSpinner;

  public SynchronizationWizardPage(final ServerConnection serverConnection)
  {
    super("Syncrhonization", "Synchronization Information", null);

    m_serverConnection = serverConnection;
  }

  public void createControl(final Composite parent)
  {
    final Composite root = new Composite(parent, SWT.NONE);
    root.setLayout(new GridLayout(2, false));
    root.setLayoutData(new GridData(GridData.FILL_BOTH));
    setControl(root);

    final Label synchronizeIntervalLabel = new Label(root, SWT.NONE);
    synchronizeIntervalLabel.setText("Synchronize interval (minutes)");
    m_synchronizeItervalSpinner = new Spinner(root, SWT.BORDER);
    m_synchronizeItervalSpinner.setIncrement(30);
    m_synchronizeItervalSpinner.setValues(m_serverConnection.getSynchronizeInterval() / 60, 0, 120, 0, 1, 5);
  }
}
