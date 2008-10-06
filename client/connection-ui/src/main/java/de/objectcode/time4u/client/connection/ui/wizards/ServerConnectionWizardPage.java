package de.objectcode.time4u.client.connection.ui.wizards;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.objectcode.time4u.client.connection.api.ConnectionFactory;
import de.objectcode.time4u.client.connection.api.IConnection;
import de.objectcode.time4u.server.api.data.ServerConnection;

public class ServerConnectionWizardPage extends WizardPage
{
  ServerConnection m_serverConnection;

  Text m_nameText;
  Text m_urlText;
  Button m_testButton;
  Label m_testResultLabel;

  boolean m_active;

  public ServerConnectionWizardPage(final ServerConnection serverConnection)
  {
    super("ServerConnection", "Server connection Information", null);

    m_serverConnection = serverConnection;
  }

  public void createControl(final Composite parent)
  {
    final Composite root = new Composite(parent, SWT.NONE);
    root.setLayout(new GridLayout(2, false));
    root.setLayoutData(new GridData(GridData.FILL_BOTH));
    setControl(root);

    final Label nameLabel = new Label(root, SWT.NONE);
    nameLabel.setText("Name");
    m_nameText = new Text(root, SWT.BORDER);
    m_nameText.setText(m_serverConnection.getName() != null ? m_serverConnection.getName() : "");
    m_nameText.setTextLimit(50);
    m_nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

    final Label urlLabel = new Label(root, SWT.NONE);
    urlLabel.setText("URL");
    m_urlText = new Text(root, SWT.BORDER);
    m_urlText.setText(m_serverConnection.getUrl() != null ? m_serverConnection.getUrl() : "");
    m_urlText.setTextLimit(200);
    m_urlText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    m_urlText.addKeyListener(new KeyAdapter() {
      @Override
      public void keyReleased(final KeyEvent e)
      {
        enableTestButton();
      }
    });

    m_testButton = new Button(root, SWT.PUSH);
    m_testButton.setText("Test");
    GridData gridData = new GridData();
    gridData.horizontalAlignment = GridData.CENTER;
    gridData.horizontalSpan = 2;
    m_testButton.setLayoutData(gridData);
    m_testButton.setEnabled(false);
    m_testButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(final SelectionEvent e)
      {
        testConnection();
      }
    });

    m_testResultLabel = new Label(root, SWT.CENTER);
    m_testResultLabel.setText("");
    gridData = new GridData(GridData.FILL_HORIZONTAL);
    gridData.horizontalSpan = 2;
    m_testResultLabel.setLayoutData(gridData);

    setPageComplete(false);
  }

  protected void enableTestButton()
  {
    setPageComplete(false);
    m_testResultLabel.setText("");
    if (m_urlText.getText() != null && m_urlText.getText().length() > 0) {
      try {
        new URL(m_urlText.getText());

        m_testButton.setEnabled(true);
      } catch (final MalformedURLException e) {
        m_testButton.setEnabled(false);
      }
    } else {
      m_testButton.setEnabled(false);
    }
  }

  protected void testConnection()
  {
    m_serverConnection.setName(m_nameText.getText());
    m_serverConnection.setUrl(m_urlText.getText());

    try {
      final IConnection connection = ConnectionFactory.openConnection(m_serverConnection);
      if (!connection.testConnection()) {
        m_testResultLabel.setText("Server API not compatible with this version");
        m_testResultLabel.setForeground(getShell().getDisplay().getSystemColor(SWT.COLOR_RED));
        return;
      }
      m_testResultLabel.setText("Success");
      m_testResultLabel.setForeground(getShell().getDisplay().getSystemColor(SWT.COLOR_GREEN));
      setPageComplete(true);
    } catch (final Exception e) {
      m_testResultLabel.setText("Connection failed: " + e.getMessage());
      m_testResultLabel.setForeground(getShell().getDisplay().getSystemColor(SWT.COLOR_RED));
      setPageComplete(false);
    }
  }

  @Override
  public void setVisible(final boolean visible)
  {
    if (visible) {
      m_active = true;
    } else {
      if (m_active) {
        m_serverConnection.setName(m_nameText.getText());
        m_serverConnection.setUrl(m_urlText.getText());
      }
      m_active = false;
    }
    super.setVisible(visible);
  }

}
