package de.objectcode.time4u.client.ui.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
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
import de.objectcode.time4u.server.api.data.MetaProperty;
import de.objectcode.time4u.server.api.data.Project;
import de.objectcode.time4u.server.api.data.Task;

public class TaskDialog extends Dialog
{
  private Text m_nameText;
  private Text m_descriptionText;
  private Text m_parentText;
  private Button m_activeCheck;
  private final Project m_project;
  private Task m_task;
  private List<MetaField> m_metaFields;
  private boolean m_create;

  public TaskDialog(final IShellProvider shellProvider, final Project project)
  {
    this(shellProvider, project, null);
  }

  public TaskDialog(final IShellProvider shellProvider, final Project project, final Task task)
  {
    super(shellProvider);

    setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE | getDefaultOrientation());

    m_project = project;
    if (task == null) {
      m_task = new Task();
      m_task.setActive(true);
      m_task.setName("");
      m_create = true;
    } else {
      m_task = task;
      m_create = false;
    }
  }

  public Task getTask()
  {
    return m_task;
  }

  public void setTask(final Task task)
  {
    m_task = task;
  }

  /*
   * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
   */
  @Override
  protected Control createDialogArea(final Composite parent)
  {
    if (m_create) {
      parent.getShell().setText("New Task");
    } else {
      parent.getShell().setText("Edit Task");
    }

    final Composite composite = (Composite) super.createDialogArea(parent);
    final Composite root = new Composite(composite, SWT.NONE);
    root.setLayout(new GridLayout(2, false));
    root.setLayoutData(new GridData(GridData.FILL_BOTH));

    final Label parentLabel = new Label(root, SWT.NONE);
    parentLabel.setText("Parent");
    m_parentText = new Text(root, SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
    m_parentText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    m_parentText.setText(m_project.getName());

    final Label nameLabel = new Label(root, SWT.NONE);
    nameLabel.setText("Name");
    m_nameText = new Text(root, SWT.BORDER);
    m_nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    m_nameText.setText(m_task.getName());
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
    m_activeCheck.setSelection(m_task.isActive());

    final Label descriptionLabel = new Label(root, SWT.LEFT);
    descriptionLabel.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
    descriptionLabel.setText("Description");
    m_descriptionText = new Text(root, SWT.BORDER | SWT.MULTI);
    GridData gridData = new GridData(GridData.FILL_BOTH);
    gridData.widthHint = convertWidthInCharsToPixels(60);
    gridData.heightHint = convertHeightInCharsToPixels(4);
    m_descriptionText.setLayoutData(gridData);
    m_descriptionText.setText(m_task.getDescription() != null ? m_task.getDescription() : "");
    m_descriptionText.setTextLimit(1000);

    m_metaFields = new ArrayList<MetaField>();
    for (final MetaCategory category : RepositoryFactory.getMetaRepository().getCategories()) {
      if (category.getTaskProperties().isEmpty()) {
        continue;
      }

      final Group categoryGroup = new Group(root, SWT.SHADOW_IN | SWT.SHADOW_OUT);
      gridData = new GridData(GridData.FILL_HORIZONTAL);

      gridData.horizontalSpan = 2;
      categoryGroup.setLayoutData(gridData);
      categoryGroup.setText(category.getLabel());
      categoryGroup.setLayout(new GridLayout(2, false));

      for (final MetaDefinition definition : category.getTaskProperties()) {
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
        final MetaProperty value = m_task.getMetaProperty(category.getName() + "." + definition.getName());
        if (value != null) {
          field.setValue(value.getValue());
        }
        m_metaFields.add(field);
      }
    }

    return composite;
  }

  /*
   * @see org.eclipse.jface.dialogs.Dialog#okPressed()
   */
  @Override
  protected void okPressed()
  {
    m_task.setName(m_nameText.getText());
    m_task.setDescription(m_descriptionText.getText());
    m_task.setActive(m_activeCheck.getSelection());
    if (m_create) {
      m_task.setProjectId(m_project.getId());
    }

    for (final MetaField field : m_metaFields) {
      m_task.setMetaProperty(new MetaProperty(field.getCategory() + "." + field.getProperty(), field.getType(), field
          .getValue()));
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
}
