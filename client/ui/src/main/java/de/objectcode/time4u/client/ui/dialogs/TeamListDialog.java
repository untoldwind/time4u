package de.objectcode.time4u.client.ui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;

import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.client.ui.provider.TeamContentProvider;
import de.objectcode.time4u.client.ui.provider.TeamTableLabelProvider;

public class TeamListDialog extends Dialog
{
  TableViewer m_teamsViewer;

  public TeamListDialog(final IShellProvider shellProvider)
  {
    super(shellProvider);

    setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE | getDefaultOrientation());
  }

  @Override
  protected void configureShell(final Shell newShell)
  {
    super.configureShell(newShell);

    newShell.setText("Team list");
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
    root.setLayout(new GridLayout(1, false));
    root.setLayoutData(new GridData(GridData.FILL_BOTH));

    final GridData gridData = new GridData(GridData.FILL_BOTH);
    gridData.grabExcessHorizontalSpace = true;
    gridData.grabExcessVerticalSpace = true;
    gridData.widthHint = 90 * width;
    gridData.heightHint = 8 * height;
    m_teamsViewer = new TableViewer(root, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
    m_teamsViewer.getTable().setLayoutData(gridData);
    final TableLayout layout = new TableLayout();
    layout.addColumnData(new ColumnPixelData(30 * width, true));
    layout.addColumnData(new ColumnPixelData(60 * width, true));
    m_teamsViewer.getTable().setHeaderVisible(true);
    m_teamsViewer.getTable().setLinesVisible(true);
    m_teamsViewer.getTable().setLayout(layout);
    final TableColumn surnameColumn = new TableColumn(m_teamsViewer.getTable(), SWT.LEFT);
    surnameColumn.setText("Name");
    final TableColumn givenNameColumn = new TableColumn(m_teamsViewer.getTable(), SWT.LEFT);
    givenNameColumn.setText("Description");

    m_teamsViewer.setColumnProperties(new String[] { "name", "description" });
    m_teamsViewer.setContentProvider(new TeamContentProvider(RepositoryFactory.getRepository().getPersonRepository(),
        RepositoryFactory.getRepository().getTeamRepository()));
    m_teamsViewer.setLabelProvider(new TeamTableLabelProvider());
    m_teamsViewer.setInput(new Object());

    return composite;
  }
}
