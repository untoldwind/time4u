package de.objectcode.time4u.client.store.impl.hibernate.entities;

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
  @JoinColumn(name = "owner_person_id", nullable = false)
  public PersonEntity getOwnerPerson()
  {
    return m_ownerPerson;
  }

  public void setOwnerPerson(final PersonEntity ownerPerson)
  {
    m_ownerPerson = ownerPerson;
  }
}
