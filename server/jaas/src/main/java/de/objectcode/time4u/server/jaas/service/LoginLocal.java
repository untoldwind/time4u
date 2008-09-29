package de.objectcode.time4u.server.jaas.service;


public class LoginLocal implements java.io.Serializable
{
  private static final long serialVersionUID = 8837127467578825603L;

  private final String m_id;
  private final String m_userId;
  private final String m_hashedPassword;

  public LoginLocal(final String id, final String userId, final String hashedPassword)
  {
    m_id = id;
    m_userId = userId;
    m_hashedPassword = hashedPassword;
  }

  public String getHashedPassword()
  {
    return m_hashedPassword;
  }

  public String getId()
  {
    return m_id;
  }

  public String getUserId()
  {
    return m_userId;
  }
}