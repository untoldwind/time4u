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
import de.objectcode.time4u.client.ui.provider.PersonContentProvider;
import de.objectcode.time4u.client.ui.provider.PersonTableLabelProvider;

public class PersonListDialog extends Dialog
{
  TableViewer m_personsViewer;

  public PersonListDialog(final IShellProvider shellProvider)
  {
    super(shellProvider);

    setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE | getDefaultOrientation());
  }

  @Override
  protected void configureShell(final Shell newShell)
  {
    super.configureShell(newShell);

    newShell.setText("Person list");
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
    m_personsViewer = new TableViewer(root, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
    m_personsViewer.getTable().setLayoutData(gridData);
    final TableLayout layout = new TableLayout();
    layout.addColumnData(new ColumnPixelData(30 * width, true));
    layout.addColumnData(new ColumnPixelData(30 * width, true));
    layout.addColumnData(new ColumnPixelData(30 * width, true));
    m_personsViewer.getTable().setHeaderVisible(true);
    m_personsViewer.getTable().setLinesVisible(true);
    m_personsViewer.getTable().setLayout(layout);
    final TableColumn surnameColumn = new TableColumn(m_personsViewer.getTable(), SWT.LEFT);
    surnameColumn.setText("Surname");
    final TableColumn givenNameColumn = new TableColumn(m_personsViewer.getTable(), SWT.LEFT);
    givenNameColumn.setText("Given name");
    final TableColumn emailColumn = new TableColumn(m_personsViewer.getTable(), SWT.LEFT);
    emailColumn.setText("Email");
    m_personsViewer.setColumnProperties(new String[] { "surname", "givenName", "email" });
    m_personsViewer.setContentProvider(new PersonContentProvider(RepositoryFactory.getRepository()
        .getPersonRepository()));
    m_personsViewer.setLabelProvider(new PersonTableLabelProvider());
    m_personsViewer.setInput(new Object());

    return composite;
  }
}
