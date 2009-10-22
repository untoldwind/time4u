package de.objectcode.time4u.client.ui.controls;

import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import de.objectcode.time4u.client.ui.dnd.ProjectTaskHolder;

public class ComboViewerCellEditor extends ComboBoxViewerCellEditor
{
  Object m_selection;

  private static final int defaultStyle = SWT.SIMPLE | SWT.READ_ONLY | SWT.DROP_DOWN;

  public ComboViewerCellEditor(final Composite parent, final IStructuredContentProvider contentProvider,
      final ILabelProvider labelProvider)
  {
    this(parent, contentProvider, labelProvider, defaultStyle);
  }

  public ComboViewerCellEditor(final Composite parent, final IStructuredContentProvider contentProvider,
      final ILabelProvider labelProvider, final int style)
  {
    super(parent, style);

    setContenProvider(contentProvider);
    setLabelProvider(labelProvider);
  }

  @Override
  protected void doSetValue(final Object value)
  {
    if (value instanceof ProjectTaskHolder) {
      getViewer().setInput(((ProjectTaskHolder) value).getProject());
      super.doSetValue(((ProjectTaskHolder) value).getTask());
    } else {
      super.doSetValue(value);
    }
  }
}
