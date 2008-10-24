package de.objectcode.time4u.server.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "T4U_CLIENTS")
public class ClientEntity
{
  long m_clientId;
  boolean m_myself;
  boolean m_server;
  Date m_registeredAt;
  PersonEntity m_person;

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

  public Date getRegisteredAt()
  {
    return m_registeredAt;
  }

  public void setRegisteredAt(final Date registeredAt)
  {
    m_registeredAt = registeredAt;
  }

  @ManyToOne
  @JoinColumn(name = "person_id")
  public PersonEntity getPerson()
  {
    return m_person;
  }

  public void setPerson(final PersonEntity person)
  {
    m_person = person;
  }
}
