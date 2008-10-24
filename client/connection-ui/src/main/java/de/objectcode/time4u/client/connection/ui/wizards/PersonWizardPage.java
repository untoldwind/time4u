package de.objectcode.time4u.client.connection.ui.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.objectcode.time4u.client.connection.ui.ConnectionUIPlugin;
import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.server.api.data.Person;

public class PersonWizardPage extends WizardPage
{
  Text m_givenNameText;
  Text m_surnameText;
  Text m_emailText;

  boolean m_active;

  public PersonWizardPage()
  {
    super("Person", "Personal Information", null);
  }

  public void createControl(final Composite parent)
  {
    final Person owner = RepositoryFactory.getRepository().getOwner();

    final Composite root = new Composite(parent, SWT.NONE);
    root.setLayout(new GridLayout(2, false));
    root.setLayoutData(new GridData(GridData.FILL_BOTH));
    setControl(root);

    final Label givenNameLabel = new Label(root, SWT.NONE);
    givenNameLabel.setText("Given name");
    m_givenNameText = new Text(root, SWT.BORDER);
    m_givenNameText.setText(owner.getGivenName() != null ? owner.getGivenName() : "");
    m_givenNameText.setTextLimit(50);
    m_givenNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

    final Label surnameLabel = new Label(root, SWT.NONE);
    surnameLabel.setText("Surname");
    m_surnameText = new Text(root, SWT.BORDER);
    m_surnameText.setText(owner.getSurname() != null ? owner.getSurname() : "");
    m_surnameText.setTextLimit(50);
    m_surnameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

    final Label emailLabel = new Label(root, SWT.NONE);
    emailLabel.setText("Email");
    m_emailText = new Text(root, SWT.BORDER);
    m_emailText.setText(owner.getEmail() != null ? owner.getEmail() : "");
    m_emailText.setTextLimit(200);
    m_emailText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
  }

  @Override
  public void setVisible(final boolean visible)
  {
    if (visible) {
      m_active = true;
    } else {
      if (m_active) {
        final Person owner = RepositoryFactory.getRepository().getOwner();

        if (!m_givenNameText.getText().equals(owner.getGivenName())
            || !m_surnameText.getText().equals(owner.getSurname()) || !m_emailText.getText().equals(owner.getEmail())) {
          owner.setGivenName(m_givenNameText.getText());
          owner.setSurname(m_surnameText.getText());
          owner.setEmail(m_emailText.getText());

          try {
            RepositoryFactory.getRepository().getPersonRepository().storePerson(owner, true);
          } catch (final Exception e) {
            ConnectionUIPlugin.getDefault().log(e);
          }
        }
      }
      m_active = false;
    }
    super.setVisible(visible);
  }
}
