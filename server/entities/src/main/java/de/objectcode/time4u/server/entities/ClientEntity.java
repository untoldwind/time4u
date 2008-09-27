package de.objectcode.time4u.server.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T4U_CLIENTS")
public class ClientEntity
{
  long m_clientId;
  boolean m_myself;
  boolean m_server;

  @Id
  public long getClientId()
  {
    return m_clientId;
  }

  public void setClientId(final long clientId)
  {
    m_clientId = clientId;
  }

  public boolean isMyself()
  {
    return m_myself;
  }

  public void setMyself(final boolean myself)
  {
    m_myself = myself;
  }

  public boolean isServer()
  {
    return m_server;
  }

  public void setServer(final boolean server)
  {
    m_server = server;
  }

}
