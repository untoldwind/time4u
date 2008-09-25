package de.objectcode.time4u.client.store.impl.hibernate.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.WorkItemEntity;

@Entity
@Table(name = "CLIENT_DATA")
public class ClientDataEntity
{
  private int m_id;
  private PersonEntity m_ownerPerson;
  private WorkItemEntity m_activeWorkItem;

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

  @ManyToOne
  @JoinColumn(name = "active_workitem_id", nullable = true)
  public WorkItemEntity getActiveWorkItem()
  {
    return m_activeWorkItem;
  }

  public void setActiveWorkItem(final WorkItemEntity activeWorkItem)
  {
    m_activeWorkItem = activeWorkItem;
  }

}
