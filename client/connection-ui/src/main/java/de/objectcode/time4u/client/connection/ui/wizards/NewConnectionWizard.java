package de.objectcode.time4u.client.connection.ui.wizards;

import org.eclipse.jface.wizard.Wizard;

import de.objectcode.time4u.client.connection.ui.ConnectionUIPlugin;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.server.api.data.RegistrationInfo;
import de.objectcode.time4u.server.api.data.ServerConnection;

public class NewConnectionWizard extends Wizard
{
  RegistrationInfo m_registrationInfo;
  ServerConnection m_serverConnection;

  public NewConnectionWizard()
  {
    m_serverConnection = new ServerConnection();

    addPage(new PersonWizardPage());
    addPage(new ServerConnectionWizardPage(m_serverConnection));
    addPage(new UserAccountWizardPage(m_serverConnection));
    addPage(new SynchronizationWizardPage(m_serverConnection));
  }

  @Override
  public boolean performFinish()
  {
    if (getContainer().getCurrentPage() != null) {
      getContainer().getCurrentPage().setVisible(false);
    }

    try {
      RepositoryFactory.getRepository().getServerConnectionRepository().storeServerConnection(m_serverConnection);
    } catch (final RepositoryException e) {
      ConnectionUIPlugin.getDefault().log(e);
    }

    return true;
  }

}
