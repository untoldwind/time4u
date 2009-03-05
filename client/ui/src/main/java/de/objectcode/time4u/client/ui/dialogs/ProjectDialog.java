package de.objectcode.time4u.client.ui.dialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.client.store.api.meta.MetaCategory;
import de.objectcode.time4u.client.store.api.meta.MetaDefinition;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.client.ui.provider.ProjectLabelProvider;
import de.objectcode.time4u.server.api.data.MetaProperty;
import de.objectcode.time4u.server.api.data.Project;
import de.objectcode.time4u.server.api.filter.ProjectFilter;

public class ProjectDialog extends Dialog
{
  private Text m_nameText;
  private Text m_descriptionText;
  private Button m_activeCheck;
  private ComboViewer m_parentCombo;
  private Project m_project;
  private List<MetaField> m_metaFields;
  private boolean m_create;

  public ProjectDialog(final IShellProvider shellProvider)
  {
    this(shellProvider, null);
  }

  public ProjectDialog(final IShellProvider shellProvider, final Project project)
  {
    super(shellProvider);

    setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE | getDefaultOrientation());

    if (project == null) {
      m_project = new Project();
      m_project.setName("");
      m_project.setActive(true);
      m_create = true;
    } else {
      m_project = project;
      m_create = false;
    }
  }

  /**
   * @return the project
   */
  public Project getProject()
  {
    return m_project;
  }

  /**
   * @param project
   *          the project to set
   */
  public void setProject(final Project project)
  {
    m_project = project;
  }

  @Override
  protected Control createDialogArea(final Composite parent)
  {
    if (m_create) {
      parent.getShell().setText("New Project");
    } else {
      parent.getShell().setText("Edit Project");
    }

    final Composite composite = (Composite) super.createDialogArea(parent);
    final Composite root = new Composite(composite, SWT.NONE);
    root.setLayout(new GridLayout(2, false));
    root.setLayoutData(new GridData(GridData.FILL_BOTH));

    final Label parentLabel = new Label(root, SWT.NONE);
    parentLabel.setText("Parent");
    m_parentCombo = new ComboViewer(root, SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
    m_parentCombo.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    m_parentCombo.setContentProvider(new ProjectContentProvider());
    m_parentCombo.setLabelProvider(new ProjectLabelProvider());
    m_parentCombo.getControl().setEnabled(m_create);
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
    m_nameText.addKeyListener(new KeyAdapter() {
      @Override
      public void keyReleased(final KeyEvent e)
      {
        enableOkButton();
      }
    });

    final Label activeLabel = new Label(root, SWT.NONE);
    activeLabel.setText("Active");
    m_activeCheck = new Button(root, SWT.CHECK);
    m_activeCheck.setSelection(m_project.isActive());

    final Label descriptionLabel = new Label(root, SWT.LEFT);
    descriptionLabel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
    descriptionLabel.setText("Description");
    m_descriptionText = new Text(root, SWT.BORDER | SWT.MULTI);
    GridData gridData = new GridData(GridData.FILL_BOTH);
    gridData.widthHint = convertWidthInCharsToPixels(60);
    gridData.heightHint = convertHeightInCharsToPixels(4);
    m_descriptionText.setLayoutData(gridData);
    m_descriptionText.setText(m_project.getDescription() != null ? m_project.getDescription() : "");
    m_descriptionText.setTextLimit(1000);
    m_descriptionText.addTraverseListener(new TraverseListener() {
      public void keyTraversed(final TraverseEvent e)
      {
        if (e.detail == SWT.TRAVERSE_TAB_NEXT || e.detail == SWT.TRAVERSE_TAB_PREVIOUS) {
          e.doit = true;
        } else if (e.detail == SWT.TRAVERSE_RETURN && e.stateMask != 0) {
          e.doit = true;
        }
      }
    });

    m_metaFields = new ArrayList<MetaField>();
    for (final MetaCategory category : RepositoryFactory.getMetaRepository().getCategories()) {
      if (category.getProjectProperties().isEmpty()) {
        continue;
      }

      final Group categoryGroup = new Group(root, SWT.SHADOW_IN | SWT.SHADOW_OUT);
      gridData = new GridData(GridData.FILL_HORIZONTAL);

      gridData.horizontalSpan = 2;
      categoryGroup.setLayoutData(gridData);
      categoryGroup.setText(category.getLabel());
      categoryGroup.setLayout(new GridLayout(2, false));

      for (final MetaDefinition definition : category.getProjectProperties()) {
        final Label label = new Label(categoryGroup, SWT.NONE);

        label.setText(definition.getLabel());

        MetaField field = null;
        switch (definition.getType()) {
          case STRING:
            field = new MetaStringField(category.getName(), definition.getName());
            break;
          case BOOLEAN:
            field = new MetaBooleanField(category.getName(), definition.getName());
            break;
          case INTEGER:
            field = new MetaIntegerField(category.getName(), definition.getName());
            break;
          case DATE:
            field = new MetaDateField(category.getName(), definition.getName());
            break;
        }
        final Control control = field.createControl(categoryGroup);
        control.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        final MetaProperty value = m_project.getMetaProperty(category.getName() + "." + definition.getName());
        if (value != null) {
          field.setValue(value.getValue());
        }
        m_metaFields.add(field);
      }
    }

    return composite;
  }

  @Override
  protected void okPressed()
  {
    m_project.setName(m_nameText.getText());
    m_project.setDescription(m_descriptionText.getText());
    m_project.setActive(m_activeCheck.getSelection());
    if (m_create) {
      m_project.setParentId(null);
      final ISelection selection = m_parentCombo.getSelection();
      if (selection instanceof IStructuredSelection) {
        final Object sel = ((IStructuredSelection) selection).getFirstElement();

        if (sel != null && sel instanceof Project) {
          m_project.setParentId(((Project) sel).getId());
        }
      }
    }

    for (final MetaField field : m_metaFields) {
      m_project.setMetaProperty(new MetaProperty(field.getCategory() + "." + field.getProperty(), field.getType(),
          field.getValue()));
    }

    super.okPressed();
  }

  @Override
  protected Control createButtonBar(final Composite parent)
  {
    final Control control = super.createButtonBar(parent);

    enableOkButton();

    return control;
  }

  private void enableOkButton()
  {
    final String str = m_nameText.getText();

    final Button button = getButton(IDialogConstants.OK_ID);

    button.setEnabled(str != null && str.trim().length() > 0);
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
