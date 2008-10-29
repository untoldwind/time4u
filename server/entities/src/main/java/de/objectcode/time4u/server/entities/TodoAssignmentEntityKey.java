package de.objectcode.time4u.server.entities;

import java.io.Serializable;

public class TodoAssignmentEntityKey implements Serializable
{
  private static final long serialVersionUID = -7097871766421122564L;

  private String m_personId;
  private String m_todoId;

  public String getPersonId()
  {
    return m_personId;
  }

  public void setPersonId(final String personId)
  {
    m_personId = personId;
  }

  public String getTodoId()
  {
    return m_todoId;
  }

  public void setTodoId(final String todoId)
  {
    m_todoId = todoId;
  }

  @Override
  public boolean equals(final Object obj)
  {
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof TodoAssignmentEntityKey)) {
      return false;
    }

    final TodoAssignmentEntityKey castObj = (TodoAssignmentEntityKey) obj;

    return m_personId.equals(castObj.m_personId) && m_todoId.equals(castObj.m_todoId);
  }

  @Override
  public int hashCode()
  {
    int hash = m_personId.hashCode();

    hash = 13 * hash + m_todoId.hashCode();

    return hash;
  }

  @Override
  public String toString()
  {
    final StringBuffer buffer = new StringBuffer("TodoAssignmentEntityKey(");
    buffer.append("person=").append(m_personId);
    buffer.append(", todo=").append(m_todoId);
    buffer.append(")");

    return buffer.toString();
  }

}
