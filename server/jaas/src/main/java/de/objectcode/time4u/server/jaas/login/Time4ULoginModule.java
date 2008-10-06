package de.objectcode.time4u.server.jaas.login;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.time4u.server.entities.account.UserAccountEntity;
import de.objectcode.time4u.server.entities.account.UserRoleEntity;
import de.objectcode.time4u.server.utils.DefaultPasswordEncoder;
import de.objectcode.time4u.server.utils.IPasswordEncoder;

public class Time4ULoginModule implements LoginModule
{
  private static Log LOG = LogFactory.getLog(Time4ULoginModule.class);

  protected Subject m_subject;
  protected Principal m_identity;
  protected Set<String> m_roles;
  protected CallbackHandler m_callbackHandler;
  protected Map<String, ?> m_sharedState;
  protected Map<String, ?> m_options;
  protected boolean m_loginOk;
  protected char[] m_credential;
  EntityManagerFactory m_entityManagerFactory;

  public Time4ULoginModule()
  {
    try {
      final InitialContext ctx = new InitialContext();

      m_entityManagerFactory = (EntityManagerFactory) ctx.lookup("java:/time4u-server/EntityManagerFactory");

      LOG.info("Time4ULoginModule instanciated");
    } catch (final Exception e) {
      LOG.fatal("Failed to initialize Time4ULoginModule", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  public boolean abort() throws LoginException
  {
    LOG.info("abort");

    return true;
  }

  /**
   * {@inheritDoc}
   */
  public boolean commit() throws LoginException
  {
    if (LOG.isInfoEnabled()) {
      LOG.info("commit, loginOk=" + m_loginOk);
    }

    if (m_loginOk == false) {
      return false;
    }

    final Set<Principal> principals = m_subject.getPrincipals();
    final Principal identity = getIdentity();

    principals.add(identity);

    final Time4UGroup roles = new Time4UGroup("Roles");
    principals.add(roles);
    if (getRoles() != null) {
      for (final String role : getRoles()) {
        roles.addMember(new Time4UPrincipal(role));
      }
    }

    return true;

  }

  /**
   * {@inheritDoc}
   */
  public void initialize(final Subject subject, final CallbackHandler callbackHandler,
      final Map<String, ?> sharedState, final Map<String, ?> options)
  {
    m_subject = subject;
    m_callbackHandler = callbackHandler;
    m_sharedState = sharedState;
    m_options = options;
  }

  /**
   * {@inheritDoc}
   */
  public boolean login() throws LoginException
  {
    LOG.info("login");

    m_loginOk = false;

    final String[] info = getUsernameAndPassword();
    final String username = info[0];
    final String password = info[1];

    LOG.info("username: " + username);

    if (m_identity == null) {
      m_identity = new Time4UPrincipal(username);
    }

    EntityManager manager = null;
    try {
      manager = m_entityManagerFactory.createEntityManager();
      final UserAccountEntity userAccount = manager.find(UserAccountEntity.class, username);

      final IPasswordEncoder encoder = new DefaultPasswordEncoder();

      if (userAccount == null || !encoder.verify(password.toCharArray(), userAccount.getHashedPassword())) {
        throw new FailedLoginException("Password Incorrect/Password Required");
      }

      m_roles = new HashSet<String>();
      for (final UserRoleEntity role : userAccount.getRoles()) {
        m_roles.add(role.getRoleId());
      }
      userAccount.setLastLogin(new Date());

      m_loginOk = true;
      return true;
    } catch (final FailedLoginException e) {
      LOG.error("Exception", e);

      throw e;
    } catch (final Exception e) {
      LOG.error("Exception", e);

      throw new FailedLoginException("Password Incorrect/Password Required");
    } finally {
      if (manager != null) {
        manager.close();
      }
    }

  }

  /**
   * {@inheritDoc}
   */
  public boolean logout() throws LoginException
  {
    LOG.info("logout");

    // Remove the user identity
    final Principal identity = getIdentity();
    final Set<Principal> principals = m_subject.getPrincipals();
    principals.remove(identity);
    // Remove any added Groups...

    return true;

  }

  protected Principal getIdentity()
  {
    return m_identity;
  }

  protected Set<String> getRoles()
  {
    return m_roles;
  }

  protected String[] getUsernameAndPassword() throws LoginException

  {
    final String[] info = { null, null };

    // prompt for a username and password

    if (m_callbackHandler == null) {
      throw new LoginException("Error: no CallbackHandler available " + "to collect authentication information");
    }

    final NameCallback nc = new NameCallback("User name: ", "guest");
    final PasswordCallback pc = new PasswordCallback("Password: ", false);
    final Callback[] callbacks = { nc, pc };

    String username = null;
    String password = null;

    try {
      m_callbackHandler.handle(callbacks);

      username = nc.getName();

      final char[] tmpPassword = pc.getPassword();

      if (tmpPassword != null) {
        m_credential = new char[tmpPassword.length];

        System.arraycopy(tmpPassword, 0, m_credential, 0, tmpPassword.length);
        pc.clearPassword();
        password = new String(m_credential);
      }

    } catch (final IOException e) {
      final LoginException le = new LoginException("Failed to get username/password");

      le.initCause(e);

      throw le;
    } catch (final UnsupportedCallbackException e) {
      final LoginException le = new LoginException("CallbackHandler does not support: " + e.getCallback());

      le.initCause(e);

      throw le;
    }

    info[0] = username;
    info[1] = password;

    return info;
  }

}
