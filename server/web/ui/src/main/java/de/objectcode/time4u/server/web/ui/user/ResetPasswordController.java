package de.objectcode.time4u.server.web.ui.user;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.faces.Renderer;
import org.jboss.seam.international.StatusMessages;

import de.objectcode.time4u.server.ejb.seam.api.ConfigurationData;
import de.objectcode.time4u.server.ejb.seam.api.IAccountServiceLocal;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.account.UserAccountEntity;
import de.objectcode.time4u.server.utils.Base64;
import de.objectcode.time4u.server.utils.DefaultPasswordEncoder;

@Name("user.resetPasswordController")
@Scope(ScopeType.EVENT)
public class ResetPasswordController
{
  private final static Log LOG = LogFactory.getLog(ReportController.class);

  public static final String VIEW_ID = "/reset-password.xhtml";
  public static final String PERFORM_VIEW_ID = "/reset-password-perform.xhtml";

  private static final RequestValidator REQUEST_VALIDATOR = new RequestValidator();

  @In("user.configuration")
  ConfigurationData m_configurationData;

  @In("AccountService")
  IAccountServiceLocal m_accountService;

  @In(create = true, value = "renderer")
  Renderer m_renderer;

  String m_email;
  PersonEntity m_person;
  List<AccountResetData> m_accounts;

  @RequestParameter("userId")
  String m_userIdParam;
  @RequestParameter("validation")
  String m_validationParam;
  String m_userId;
  String m_validation;
  String m_newPassword;
  String m_newPasswordConfirm;

  public String getEmail()
  {
    return m_email;
  }

  public void setEmail(final String email)
  {
    m_email = email;
  }

  public PersonEntity getPerson()
  {
    return m_person;
  }

  public List<AccountResetData> getAccounts()
  {
    return m_accounts;
  }

  public String getUserId()
  {
    if (m_userId == null) {
      return m_userIdParam;
    }
    return m_userId;
  }

  public void setUserId(final String userId)
  {
    m_userId = userId;
  }

  public String getValidation()
  {
    if (m_validation == null) {
      return m_validationParam;
    }
    return m_validation;
  }

  public void setValidation(final String validation)
  {
    m_validation = validation;
  }

  public String getNewPassword()
  {
    return m_newPassword;
  }

  public void setNewPassword(final String newPassword)
  {
    m_newPassword = newPassword;
  }

  public String getNewPasswordConfirm()
  {
    return m_newPasswordConfirm;
  }

  public void setNewPasswordConfirm(final String newPasswordConfirm)
  {
    m_newPasswordConfirm = newPasswordConfirm;
  }

  public String enter()
  {
    return VIEW_ID;
  }

  public String sendMail()
  {
    final List<UserAccountEntity> accounts = m_accountService.findUserAccountsByEmail(m_email);

    if (accounts == null || accounts.isEmpty()) {
      StatusMessages.instance().add("No account found with email address: " + m_email);

      return VIEW_ID;
    }

    m_person = accounts.get(0).getPerson();
    m_accounts = new ArrayList<AccountResetData>();

    final String serverUrl = m_configurationData.getServerUrl();

    for (final UserAccountEntity account : accounts) {
      try {
        final String validation = REQUEST_VALIDATOR.digest(account);
        final String url = serverUrl + "/time4u/reset-password-perform.seam?userId="
            + URLEncoder.encode(account.getUserId(), "UTF-8") + "&validation=" + URLEncoder.encode(validation, "UTF-8");

        m_accounts.add(new AccountResetData(account.getUserId(), validation, url));
      } catch (final UnsupportedEncodingException e) {
        LOG.error("Exception", e);
      }
    }

    try {
      m_renderer.render("/mail/reset-password.xhtml");
      StatusMessages.instance().add("An email has been send to you.");

      return PERFORM_VIEW_ID;
    } catch (final Exception e) {
      LOG.error("Exception", e);
      StatusMessages.instance().add("Email sending failed: " + e.getMessage());
    }

    return VIEW_ID;
  }

  public String resetPassword()
  {
    final UserAccountEntity account = m_accountService.getUserAccount(m_userId);

    if (account == null) {
      StatusMessages.instance().add("Account " + m_userId + " not found");

      return PERFORM_VIEW_ID;
    }
    final String validation = REQUEST_VALIDATOR.digest(account);

    if (!validation.equals(m_validation)) {
      StatusMessages.instance().add("Validation string invalid");

      return PERFORM_VIEW_ID;
    }

    if (!m_newPassword.equals(m_newPasswordConfirm)) {
      StatusMessages.instance().add("Password and password confirmation do not match");

      return PERFORM_VIEW_ID;
    }

    account.setHashedPassword(new DefaultPasswordEncoder().encrypt(m_newPassword.toCharArray()));

    return "/login.xhtml";
  }

  public static class AccountResetData
  {
    String m_userId;
    String m_validation;
    String m_url;

    private AccountResetData(final String userId, final String validation, final String url)
    {
      m_userId = userId;
      m_validation = validation;
      m_url = url;
    }

    public String getUserId()
    {
      return m_userId;
    }

    public String getValidation()
    {
      return m_validation;
    }

    public String getUrl()
    {
      return m_url;
    }

  }

  private static class RequestValidator
  {
    private final byte[] m_random;

    public RequestValidator()
    {
      final SecureRandom random = new SecureRandom();

      m_random = new byte[256];
      random.nextBytes(m_random);
    }

    String digest(final UserAccountEntity account)
    {
      try {
        final MessageDigest digest = MessageDigest.getInstance("SHA-1");

        digest.reset();

        digest.update(account.getUserId().getBytes("UTF-8"));
        digest.update(account.getLastLogin().toString().getBytes("UTF-8"));
        digest.update(account.getPerson().getGivenName().getBytes("UTF-8"));
        digest.update(account.getPerson().getSurname().getBytes("UTF-8"));
        digest.update(account.getPerson().getEmail().getBytes("UTF-8"));
        digest.update(m_random);

        return new String(Base64.altEncode(digest.digest()));
      } catch (final Exception e) {
        LOG.error("Exception", e);
        throw new RuntimeException(e);
      }
    }
  }
}
