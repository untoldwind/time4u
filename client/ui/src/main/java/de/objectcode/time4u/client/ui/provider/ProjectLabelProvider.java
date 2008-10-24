package de.objectcode.time4u.client.ui.provider;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;

import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.server.api.data.ProjectSummary;

/**
 * Project label provider.
 * 
 * @author junglas
 */
public class ProjectLabelProvider extends LabelProvider implements IColorProvider
{

  /**
   * {@inheritDoc}
   */
  public Color getBackground(final Object element)
  {
    return PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
  }

  /**
   * {@inheritDoc}
   */
  public Color getForeground(final Object element)
  {
    if (element instanceof ProjectSummary) {
      if (!((ProjectSummary) element).isActive()) {
        return PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_GRAY);
      }
    }

    return PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_LIST_FOREGROUND);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getText(final Object obj)
  {
    if (obj instanceof ProjectSummary) {
      return ((ProjectSummary) obj).getName();
    }

    return obj.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Image getImage(final Object obj)
  {
    return UIPlugin.getDefault().getImage("/icons/Project.gif");
  }

}
