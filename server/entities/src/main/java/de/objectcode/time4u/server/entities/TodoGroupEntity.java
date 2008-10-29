package de.objectcode.time4u.server.entities;

import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
@DiscriminatorValue("g")
public class TodoGroupEntity extends TodoBaseEntity
{
  /** Todos that a port of this group. */
  private Set<TodoBaseEntity> m_parts;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "group")
  public Set<TodoBaseEntity> getParts()
  {
    return m_parts;
  }

  public void setParts(final Set<TodoBaseEntity> parts)
  {
    m_parts = parts;
  }

}
