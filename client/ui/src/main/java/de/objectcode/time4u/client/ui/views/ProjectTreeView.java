package de.objectcode.time4u.client.ui.views;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class ProjectTreeView extends ViewPart
{
  public static final String ID = "de.objectcode.client.ui.view.projectTree";

  private TreeViewer m_viewer;
  private boolean m_showOnlyActive;

  private final int m_refreshCounter = 0;

  @Override
  public void createPartControl(final Composite parent)
  {
    // TODO Auto-generated method stub

  }

  @Override
  public void setFocus()
  {
    m_viewer.getControl().setFocus();
  }

}
