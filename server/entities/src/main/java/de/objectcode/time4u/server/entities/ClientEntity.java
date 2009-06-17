package de.objectcode.time4u.server.entities;

import java.util.Date;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.entities.sync.ClientSynchronizationStatusEntity;

@Entity
@Table(name = "T4U_CLIENTS")
public class ClientEntity
{
  long m_clientId;
  boolean m_myself;
  boolean m_server;
  Date m_registeredAt;
  PersonEntity m_person;
  Map<EntityType, ClientSynchronizationStatusEntity> m_synchronizationStatus;

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

  @MapKey(name = "entityType")
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "client")
  public Map<EntityType, ClientSynchronizationStatusEntity> getSynchronizationStatus()
  {
    return m_synchronizationStatus;
  }

  public void setSynchronizationStatus(final Map<EntityType, ClientSynchronizationStatusEntity> synchronizationStatus)
  {
    m_synchronizationStatus = synchronizationStatus;
  }

}
