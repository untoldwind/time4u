package de.objectcode.time4u.client.ui.views;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class TodoTreeView extends ViewPart
{
  public static final String ID = "de.objectcode.time4u.client.ui.view.todoTree";

  private TreeViewer m_viewer;

  /**
   * {@inheritDoc}
   */
  @Override
  public void createPartControl(final Composite parent)
  {
    m_viewer = new TreeViewer(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setFocus()
  {
    m_viewer.getControl().setFocus();
  }
}
