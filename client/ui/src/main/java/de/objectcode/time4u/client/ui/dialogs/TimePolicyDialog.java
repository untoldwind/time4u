package de.objectcode.time4u.client.ui.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.jface.window.SameShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;

import de.objectcode.time4u.client.store.api.IWorkItemRepository;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.client.ui.util.DateFormat;
import de.objectcode.time4u.server.api.data.CalendarDay;
import de.objectcode.time4u.server.api.data.TimePolicy;
import de.objectcode.time4u.server.api.data.WeekTimePolicy;

public class TimePolicyDialog extends Dialog
{
  IWorkItemRepository m_workItemRepository;
  List<TimePolicy> m_timePolicies;

  TableViewer m_timePolicyTable;
  Button m_upButton;
  Button m_downButton;
  Button m_editButton;
  Button m_removeButton;

  TimePolicy m_selection;

  public TimePolicyDialog(final IShellProvider parentShell, final IWorkItemRepository workItemRepository)
  {
    super(parentShell);

    setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE | getDefaultOrientation());

    m_workItemRepository = workItemRepository;
    m_timePolicies = new ArrayList<TimePolicy>();
    try {
      final List<TimePolicy> policies = m_workItemRepository.getTimePolicies();

      if (policies != null) {
        for (final TimePolicy timePolicy : policies) {
          m_timePolicies.add(timePolicy);
        }
      }
    } catch (final Exception e) {
      UIPlugin.getDefault().log(e);
    }
  }

  @Override
  protected void configureShell(final Shell newShell)
  {
    super.configureShell(newShell);

    newShell.setText(UIPlugin.getDefault().getString("dialog.timepolicy.manage.title"));
  }

  @Override
  protected Control createDialogArea(final Composite parent)
  {
    final Composite composite = (Composite) super.createDialogArea(parent);
    final Composite root = new Composite(composite, SWT.NONE);
    root.setLayout(new GridLayout(2, false));
    root.setLayoutData(new GridData(GridData.FILL_BOTH));

    m_timePolicyTable = new TableViewer(root, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.SINGLE
        | SWT.FULL_SELECTION);
    final TableLayout layout = new TableLayout();
    layout.addColumnData(new ColumnWeightData(10, 50, true));
    layout.addColumnData(new ColumnWeightData(10, 50, true));
    layout.addColumnData(new ColumnWeightData(40, 100, true));
    m_timePolicyTable.getTable().setHeaderVisible(true);
    m_timePolicyTable.getTable().setLinesVisible(true);
    m_timePolicyTable.getTable().setLayout(layout);
    final TableColumn beginColumn = new TableColumn(m_timePolicyTable.getTable(), SWT.LEFT);
    beginColumn.setText("From");
    beginColumn.setMoveable(true);
    final TableColumn endColumn = new TableColumn(m_timePolicyTable.getTable(), SWT.LEFT);
    endColumn.setText("Until");
    endColumn.setMoveable(true);
    final TableColumn durationColumn = new TableColumn(m_timePolicyTable.getTable(), SWT.LEFT);
    durationColumn.setText("Policy");
    durationColumn.setMoveable(true);
    GridData gridData = new GridData(GridData.FILL_BOTH);
    gridData.verticalSpan = 7;
    gridData.grabExcessHorizontalSpace = true;
    gridData.grabExcessVerticalSpace = true;
    gridData.widthHint = convertWidthInCharsToPixels(90);
    gridData.heightHint = convertHeightInCharsToPixels(8);
    m_timePolicyTable.getTable().setLayoutData(gridData);
    m_timePolicyTable.setContentProvider(new ArrayContentProvider());
    m_timePolicyTable.setLabelProvider(new TimePolicyLabelProvider());
    m_timePolicyTable.setInput(m_timePolicies);
    m_timePolicyTable.addSelectionChangedListener(new ISelectionChangedListener() {
      public void selectionChanged(final SelectionChangedEvent event)
      {
        select(null);

        final ISelection selection = event.getSelection();

        if (selection != null && selection instanceof IStructuredSelection) {
          final Object sel = ((IStructuredSelection) selection).getFirstElement();

          if (sel instanceof TimePolicy) {
            select((TimePolicy) sel);
          }
        }
      }
    });

    final Button addWeekButton = new Button(root, SWT.PUSH);
    addWeekButton.setText("Add Week");
    gridData = new GridData(GridData.FILL_HORIZONTAL);
    gridData.grabExcessHorizontalSpace = false;
    gridData.grabExcessVerticalSpace = false;
    addWeekButton.setLayoutData(gridData);
    addWeekButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(final SelectionEvent e)
      {
        addWeekPolicy();
      }
    });

    m_upButton = new Button(root, SWT.PUSH);
    m_upButton.setText("Up");
    gridData = new GridData(GridData.FILL_HORIZONTAL);
    gridData.verticalIndent = 10;
    gridData.grabExcessHorizontalSpace = false;
    gridData.grabExcessVerticalSpace = false;
    m_upButton.setLayoutData(gridData);
    m_upButton.setEnabled(false);
    m_upButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(final SelectionEvent e)
      {
        upPolicy();
      }
    });

    m_downButton = new Button(root, SWT.PUSH);
    m_downButton.setText("Down");
    gridData = new GridData(GridData.FILL_HORIZONTAL);
    gridData.grabExcessHorizontalSpace = false;
    gridData.grabExcessVerticalSpace = false;
    m_downButton.setLayoutData(gridData);
    m_downButton.setEnabled(false);
    m_downButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(final SelectionEvent e)
      {
        downPolicy();
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
        editPolicy();
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
        removePolicy();
      }
    });

    return composite;
  }

  @Override
  protected void okPressed()
  {
    try {
      m_workItemRepository.storeTimePolicies(m_timePolicies);
    } catch (final Exception e) {
      UIPlugin.getDefault().log(e);
    }

    super.okPressed();
  }

  protected void select(final TimePolicy selection)
  {
    m_selection = selection;

    m_upButton.setEnabled(m_selection != null);
    m_downButton.setEnabled(m_selection != null);
    m_editButton.setEnabled(m_selection != null);
    m_removeButton.setEnabled(m_selection != null);
  }

  protected void addWeekPolicy()
  {
    final WeekTimePolicyDialog dialog = new WeekTimePolicyDialog(new SameShellProvider(getShell()),
        new WeekTimePolicy());

    if (dialog.open() == Dialog.OK) {
      m_timePolicies.add(dialog.getWeekTimePolicy());
      m_timePolicyTable.setInput(m_timePolicies);
    }
  }

  protected void upPolicy()
  {
    if (m_selection == null) {
      return;
    }
    final int idx = m_timePolicies.indexOf(m_selection);
    if (idx < 1) {
      return;
    }

    final TimePolicy prevPolicy = m_timePolicies.get(idx - 1);
    m_timePolicies.set(idx - 1, m_selection);
    m_timePolicies.set(idx, prevPolicy);
    m_timePolicyTable.setInput(m_timePolicies);
  }

  protected void downPolicy()
  {
    if (m_selection == null) {
      return;
    }
    final int idx = m_timePolicies.indexOf(m_selection);
    if (idx < 0 || idx + 1 >= m_timePolicies.size()) {
      return;
    }

    final TimePolicy nextPolicy = m_timePolicies.get(idx + 1);
    m_timePolicies.set(idx + 1, m_selection);
    m_timePolicies.set(idx, nextPolicy);
    m_timePolicyTable.setInput(m_timePolicies);
  }

  protected void editPolicy()
  {
    if (m_selection == null) {
      return;
    }
    if (m_selection instanceof WeekTimePolicy) {
      final WeekTimePolicyDialog dialog = new WeekTimePolicyDialog(new SameShellProvider(getShell()),
          (WeekTimePolicy) m_selection);

      if (dialog.open() == Dialog.OK) {
        m_timePolicyTable.update(m_selection, null);
      }
    }
  }

  protected void removePolicy()
  {
    if (m_selection == null) {
      return;
    }
    m_timePolicies.remove(m_selection);
    m_timePolicyTable.setInput(m_timePolicies);
  }

  public class TimePolicyLabelProvider extends LabelProvider implements ITableLabelProvider
  {
    public Image getColumnImage(final Object element, final int columnIndex)
    {
      return null;
    }

    public String getColumnText(final Object element, final int columnIndex)
    {
      switch (columnIndex) {
        case 0:
          if (element instanceof TimePolicy) {
            final CalendarDay from = ((TimePolicy) element).getValidFrom();
            return from != null ? DateFormat.format(from) : "<any>";
          }
          break;
        case 1:
          if (element instanceof TimePolicy) {
            final CalendarDay until = ((TimePolicy) element).getValidUntil();
            return until != null ? DateFormat.format(until) : "<any>";
          }
          break;
        case 2:
          return element.toString();
      }
      return "";
    }

    @Override
    public String getText(final Object element)
    {
      return element.toString();
    }

  }
}
