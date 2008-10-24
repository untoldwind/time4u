package de.objectcode.time4u.client.connection.ui.dialogs;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.jface.window.SameShellProvider;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;

import de.objectcode.time4u.client.connection.api.ConnectionFactory;
import de.objectcode.time4u.client.connection.api.IConnection;
import de.objectcode.time4u.client.connection.ui.ConnectionUIPlugin;
import de.objectcode.time4u.client.connection.ui.provider.ServerConnectionTableLabelProvider;
import de.objectcode.time4u.client.connection.ui.wizards.NewConnectionWizard;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.server.api.data.ServerConnection;

public class ManageConnectionsDialog extends Dialog
{
  private TableViewer m_connectionsViewer;
  private Button m_editButton;
  private Button m_removeButton;
  private Button m_synchronizeNowButton;

  private ServerConnection m_selection;

  public ManageConnectionsDialog(final IShellProvider shellProvider)
  {
    super(shellProvider);

    setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE | getDefaultOrientation());
  }

  @Override
  protected void configureShell(final Shell newShell)
  {
    super.configureShell(newShell);

    newShell.setText("Manage Connections");
  }

  @Override
  protected Control createDialogArea(final Composite parent)
  {
    final GC gc = new GC(parent);
    final FontMetrics fm = gc.getFontMetrics();
    final int width = fm.getAverageCharWidth();
    final int height = fm.getHeight();
    gc.dispose();

    final Composite composite = (Composite) super.createDialogArea(parent);
    final Composite root = new Composite(composite, SWT.NONE);
    root.setLayout(new GridLayout(2, false));
    root.setLayoutData(new GridData(GridData.FILL_BOTH));

    GridData gridData = new GridData(GridData.FILL_BOTH);
    gridData.verticalSpan = 5;
    gridData.grabExcessHorizontalSpace = true;
    gridData.grabExcessVerticalSpace = true;
    gridData.widthHint = 90 * width;
    gridData.heightHint = 8 * height;
    m_connectionsViewer = new TableViewer(root, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.SINGLE
        | SWT.FULL_SELECTION);
    m_connectionsViewer.getTable().setLayoutData(gridData);
    final TableLayout layout = new TableLayout();
    layout.addColumnData(new ColumnPixelData(50 * width, true));
    layout.addColumnData(new ColumnPixelData(40 * width, true));
    m_connectionsViewer.getTable().setHeaderVisible(true);
    m_connectionsViewer.getTable().setLinesVisible(true);
    m_connectionsViewer.getTable().setLayout(layout);
    final TableColumn urlColumn = new TableColumn(m_connectionsViewer.getTable(), SWT.LEFT);
    urlColumn.setText("Url");
    final TableColumn lastSynchronizeColumn = new TableColumn(m_connectionsViewer.getTable(), SWT.LEFT);
    lastSynchronizeColumn.setText("Last synchronize");
    m_connectionsViewer.setColumnProperties(new String[] { "url", "lastSynchronize" });
    m_connectionsViewer.setContentProvider(new ArrayContentProvider());
    m_connectionsViewer.setLabelProvider(new ServerConnectionTableLabelProvider());
    try {
      m_connectionsViewer.setInput(RepositoryFactory.getRepository().getServerConnectionRepository()
          .getServerConnections());
    } catch (final RepositoryException e) {
      ConnectionUIPlugin.getDefault().log(e);
    }
    m_connectionsViewer.addSelectionChangedListener(new ISelectionChangedListener() {
      public void selectionChanged(final SelectionChangedEvent event)
      {
        select(null);

        final ISelection selection = event.getSelection();

        if (selection != null && selection instanceof IStructuredSelection) {
          final Object sel = ((IStructuredSelection) selection).getFirstElement();

          if (sel instanceof ServerConnection) {
            select((ServerConnection) sel);
          }
        }
      }
    });

    final Button newButton = new Button(root, SWT.PUSH);
    newButton.setText("New");
    gridData = new GridData(GridData.FILL_HORIZONTAL);
    gridData.verticalIndent = 10;
    gridData.grabExcessHorizontalSpace = false;
    gridData.grabExcessVerticalSpace = false;
    newButton.setLayoutData(gridData);
    newButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(final SelectionEvent e)
      {
        newConnection();
      }
    });

    m_editButton = new Button(root, SWT.PUSH);
    m_editButton.setText("Edit");
    gridData = new GridData(GridData.FILL_HORIZONTAL);
    gridData.verticalIndent = 10;
    gridData.grabExcessHorizontalSpace = false;
    gridData.grabExcessVerticalSpace = false;
    m_editButton.setLayoutData(gridData);
    m_editButton.setEnabled(false);
    m_editButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(final SelectionEvent e)
      {
        editSelection();
      }
    });

    m_removeButton = new Button(root, SWT.PUSH);
    m_removeButton.setText("Remove");
    gridData = new GridData(GridData.FILL_HORIZONTAL);
    gridData.verticalIndent = 10;
    gridData.grabExcessHorizontalSpace = false;
    gridData.grabExcessVerticalSpace = false;
    m_removeButton.setLayoutData(gridData);
    m_removeButton.setEnabled(false);
    m_removeButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(final SelectionEvent e)
      {
        removeSelection();
      }
    });

    m_synchronizeNowButton = new Button(root, SWT.PUSH);
    m_synchronizeNowButton.setText("Synchronize Now");
    gridData = new GridData(GridData.FILL_HORIZONTAL);
    gridData.verticalIndent = 10;
    gridData.grabExcessHorizontalSpace = false;
    gridData.grabExcessVerticalSpace = false;
    m_synchronizeNowButton.setLayoutData(gridData);
    m_synchronizeNowButton.setEnabled(false);
    m_synchronizeNowButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(final SelectionEvent e)
      {
        synchronizeNowSelection();
      }
    });

    return composite;
  }

  @Override
  protected void createButtonsForButtonBar(final Composite parent)
  {
    createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
  }

  protected void newConnection()
  {
    final WizardDialog wizardDialog = new WizardDialog(getShell(), new NewConnectionWizard());

    wizardDialog.open();
  }

  protected void editSelection()
  {
    if (m_selection == null) {
      return;
    }
    final ConnectionDialog dialog = new ConnectionDialog(new SameShellProvider(getShell()), m_selection);

    dialog.open();

    try {
      RepositoryFactory.getRepository().getServerConnectionRepository().storeServerConnection(
          dialog.getServerConnection());

      m_connectionsViewer.setInput(RepositoryFactory.getRepository().getServerConnectionRepository()
          .getServerConnections());
    } catch (final RepositoryException e) {
      ConnectionUIPlugin.getDefault().log(e);
    }
  }

  protected void removeSelection()
  {
    if (m_selection == null) {
      return;
    }
    if (MessageDialog.openConfirm(this.getShell(), "Remove server connection",
        "Are you sure that you want to remove the connection to server '" + m_selection.getUrl() + "'?")) {
      try {
        RepositoryFactory.getRepository().getServerConnectionRepository().deleteServerConnection(m_selection);

        m_connectionsViewer.setInput(RepositoryFactory.getRepository().getServerConnectionRepository()
            .getServerConnections());
      } catch (final RepositoryException e) {
        ConnectionUIPlugin.getDefault().log(e);
      }
    }
  }

  protected void synchronizeNowSelection()
  {
    if (m_selection == null) {
      return;
    }
    try {
      final IConnection connection = ConnectionFactory.openConnection(m_selection);
      final ProgressMonitorDialog diaglog = new ProgressMonitorDialog(getShell());
      diaglog.run(true, true, new IRunnableWithProgress() {
        public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
        {
          try {
            connection.sychronizeNow(monitor);
          } catch (final Exception e) {
            ConnectionUIPlugin.getDefault().log(e);
          } finally {
            monitor.done();
          }
        }
      });
    } catch (final Exception e) {
      ConnectionUIPlugin.getDefault().log(e);
    }
  }

  protected void select(final ServerConnection selection)
  {
    m_selection = selection;
    m_editButton.setEnabled(m_selection != null);
    m_removeButton.setEnabled(m_selection != null);
    m_synchronizeNowButton.setEnabled(m_selection != null);
  }

}
