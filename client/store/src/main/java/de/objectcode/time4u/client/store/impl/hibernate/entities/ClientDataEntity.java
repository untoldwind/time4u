package de.objectcode.time4u.client.store.impl.hibernate.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.objectcode.time4u.server.entities.PersonEntity;

@Entity
@Table(name = "CLIENT_DATA")
public class ClientDataEntity
{
  private int m_id;
  private PersonEntity m_ownerPerson;
  private long m_clientId;
  private String m_keyChainKey;

  @Id
  public int getId()
  {
    return m_id;
  }

  public void setId(final int id)
  {
    m_id = id;
  }

  @ManyToOne
  @JoinColumn(name = "owner_person_id")
  public PersonEntity getOwnerPerson()
  {
    return m_ownerPerson;
  }

  public void setOwnerPerson(final PersonEntity ownerPerson)
  {
    m_ownerPerson = ownerPerson;
  }

  public long getClientId()
  {
    return m_clientId;
  }

  public void setClientId(final long clientId)
  {
    m_clientId = clientId;
  }

  @Column(length = 100)
  public String getKeyChainKey()
  {
    return m_keyChainKey;
  }

  public void setKeyChainKey(final String keyChainKey)
  {
    m_keyChainKey = keyChainKey;
  }

}
