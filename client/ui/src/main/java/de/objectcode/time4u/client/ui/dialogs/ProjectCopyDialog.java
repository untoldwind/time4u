package de.objectcode.time4u.client.ui.dialogs;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.client.ui.provider.ProjectLabelProvider;
import de.objectcode.time4u.server.api.data.Project;
import de.objectcode.time4u.server.api.filter.ProjectFilter;

public class ProjectCopyDialog extends Dialog
{
  private Text m_nameText;
  private ComboViewer m_parentCombo;
  private final Project m_project;
  private Button m_copyTasksCheckbox;
  private Button m_copySubProjectsCheckbox;

  private Project m_newParent;
  private String m_newName;
  private boolean m_copyTasks;
  private boolean m_copySubProjects;

  public ProjectCopyDialog(final IShellProvider shellProvider, final Project project)
  {
    super(shellProvider);

    setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE | getDefaultOrientation());

    m_project = project;
  }

  public String getNewName()
  {
    return m_newName;
  }

  public Project getNewParent()
  {
    return m_newParent;
  }

  public boolean isCopyTasks()
  {
    return m_copyTasks;
  }

  public boolean isCopySubProjects()
  {
    return m_copySubProjects;
  }

  @Override
  protected Control createDialogArea(final Composite parent)
  {
    parent.getShell().setText("Project copy");

    final Composite composite = (Composite) super.createDialogArea(parent);
    final Composite root = new Composite(composite, SWT.NONE);
    root.setLayout(new GridLayout(2, false));
    root.setLayoutData(new GridData(GridData.FILL_BOTH));

    final Label originalLabel = new Label(root, SWT.NONE);
    originalLabel.setText("Original");
    final Text originalText = new Text(root, SWT.BORDER);
    originalText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    originalText.setText(m_project.getName());
    originalText.setEnabled(false);

    final Label parentLabel = new Label(root, SWT.NONE);
    parentLabel.setText("Parent");
    m_parentCombo = new ComboViewer(root, SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
    m_parentCombo.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    m_parentCombo.setContentProvider(new ProjectContentProvider());
    m_parentCombo.setLabelProvider(new ProjectLabelProvider());
    m_parentCombo.setInput(new Object());
    if (m_project.getParentId() != null) {
      try {
        m_parentCombo.setSelection(new StructuredSelection(RepositoryFactory.getRepository().getProjectRepository()
            .getProject(m_project.getParentId())));
      } catch (final Exception e) {
        UIPlugin.getDefault().log(e);
      }
    } else {
      m_parentCombo.setSelection(new StructuredSelection(""));
    }

    final Label nameLabel = new Label(root, SWT.NONE);
    nameLabel.setText("Name");
    m_nameText = new Text(root, SWT.BORDER);
    m_nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    m_nameText.setText(m_project.getName());
    m_nameText.setTextLimit(30);

    final Label copyTasksLabel = new Label(root, SWT.NONE);
    copyTasksLabel.setText("Copy Tasks");
    m_copyTasksCheckbox = new Button(root, SWT.CHECK);
    m_copyTasksCheckbox.setSelection(true);

    final Label copySubProjectsLabel = new Label(root, SWT.NONE);
    copySubProjectsLabel.setText("Copy Sub-Projects");
    m_copySubProjectsCheckbox = new Button(root, SWT.CHECK);

    return composite;
  }

  @Override
  protected void okPressed()
  {
    m_newName = m_nameText.getText();
    m_newParent = null;
    m_copyTasks = m_copyTasksCheckbox.getSelection();
    m_copySubProjects = m_copySubProjectsCheckbox.getSelection();
    final ISelection selection = m_parentCombo.getSelection();
    if (selection instanceof IStructuredSelection) {
      final Object sel = ((IStructuredSelection) selection).getFirstElement();

      if (sel != null && sel instanceof Project) {
        m_newParent = (Project) sel;
      }
    }
    super.okPressed();
  }

  private static class ProjectContentProvider implements IStructuredContentProvider
  {
    public void dispose()
    {
    }

    public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput)
    {
    }

    public Object[] getElements(final Object inputElement)
    {
      try {
        final ProjectFilter filter = new ProjectFilter();
        filter.setDeleted(false);
        final Collection<Project> projects = RepositoryFactory.getRepository().getProjectRepository().getProjects(
            filter);
        final Object[] ret = new Object[projects.size() + 1];
        final Iterator<Project> it = projects.iterator();

        ret[0] = "";
        for (int i = 1; it.hasNext(); i++) {
          ret[i] = it.next();
        }
        return ret;
      } catch (final Exception e) {
        UIPlugin.getDefault().log(e);
      }
      return new Object[0];
    }

  }
}
