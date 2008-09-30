package de.objectcode.time4u.server.api.data;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "registration-info")
public class RegistrationInfo implements Serializable
{
  private static final long serialVersionUID = 4748697946904941543L;

  long m_clientId;
  String m_personId;
  String m_userId;
  String m_hashedPassword;
  String m_name;
  String m_email;

  public long getClientId()
  {
    return m_clientId;
  }

  public void setClientId(final long clientId)
  {
    m_clientId = clientId;
  }

  public String getPersonId()
  {
    return m_personId;
  }

  public void setPersonId(final String personId)
  {
    m_personId = personId;
  }

  public String getUserId()
  {
    return m_userId;
  }

  public void setUserId(final String userId)
  {
    m_userId = userId;
  }

  public String getHashedPassword()
  {
    return m_hashedPassword;
  }

  public void setHashedPassword(final String hashedPassword)
  {
    m_hashedPassword = hashedPassword;
  }

  public String getName()
  {
    return m_name;
  }

  public void setName(final String name)
  {
    m_name = name;
  }

  public String getEmail()
  {
    return m_email;
  }

  public void setEmail(final String email)
  {
    m_email = email;
  }

  @Override
  public String toString()
  {
    final StringBuffer buffer = new StringBuffer("RegistrationInfo(");
    buffer.append("clientId=").append(m_clientId);
    buffer.append(",personId=").append(m_personId);
    buffer.append(",userId=").append(m_userId);
    buffer.append(",name=").append(m_name);
    buffer.append(",email=").append(m_email);
    buffer.append(")");

    return buffer.toString();
  }

}
