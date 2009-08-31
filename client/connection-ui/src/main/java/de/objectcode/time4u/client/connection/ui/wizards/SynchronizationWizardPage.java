package de.objectcode.time4u.client.connection.ui.wizards;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.client.ui.controls.ComboTreeViewer;
import de.objectcode.time4u.client.ui.provider.ProjectContentProvider;
import de.objectcode.time4u.client.ui.provider.ProjectLabelProvider;
import de.objectcode.time4u.server.api.data.ProjectSummary;
import de.objectcode.time4u.server.api.data.ServerConnection;

public class SynchronizationWizardPage extends WizardPage
{
  ServerConnection m_serverConnection;

  Spinner m_synchronizeItervalSpinner;

  private ComboTreeViewer m_rootProjectTreeViewer;

  boolean m_active;

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

    final Label rootProjectLabel = new Label(root, SWT.NONE);
    rootProjectLabel.setText("Root project");
    m_rootProjectTreeViewer = new ComboTreeViewer(root, SWT.BORDER | SWT.DROP_DOWN);
    m_rootProjectTreeViewer.setContentProvider(new ProjectContentProvider(RepositoryFactory.getRepository()
        .getProjectRepository(), false, true));
    m_rootProjectTreeViewer.setLabelProvider(new ProjectLabelProvider());
    m_rootProjectTreeViewer.setInput(new Object());
    m_rootProjectTreeViewer.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    m_rootProjectTreeViewer.setSelection(new StructuredSelection(ProjectContentProvider.ROOT));
  }

  @Override
  public void setVisible(final boolean visible)
  {
    if (visible) {
      m_active = true;
    } else {
      if (m_active) {
        m_serverConnection.setSynchronizeInterval(m_synchronizeItervalSpinner.getSelection() * 60);
        final ISelection rootProjectSelection = m_rootProjectTreeViewer.getSelection();

        if (rootProjectSelection != null && rootProjectSelection instanceof IStructuredSelection
            && ((IStructuredSelection) rootProjectSelection).getFirstElement() != null
            && ((IStructuredSelection) rootProjectSelection).getFirstElement() instanceof ProjectSummary) {
          m_serverConnection.setRootProjectId(((ProjectSummary) ((IStructuredSelection) rootProjectSelection)
              .getFirstElement()).getId());
        } else {
          m_serverConnection.setRootProjectId(null);
        }
      }
      m_active = false;
    }
    super.setVisible(visible);
  }
}
