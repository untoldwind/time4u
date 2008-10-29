package de.objectcode.time4u.server.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "T4U_TODOS_ASSIGNMENTS")
@IdClass(TodoAssignmentEntityKey.class)
public class TodoAssignmentEntity
{
  String m_personId;
  String m_todoId;
  TodoEntity m_todo;
  PersonEntity m_person;
  Integer m_estimatedTime;

  @Id
  @Column(name = "person_id", length = 36)
  public String getPersonId()
  {
    return m_personId;
  }

  public void setPersonId(final String personId)
  {
    m_personId = personId;
  }

  @Id
  @Column(name = "todo_id", length = 36)
  public String getTodoId()
  {
    return m_todoId;
  }

  public void setTodoId(final String todoId)
  {
    m_todoId = todoId;
  }

  @ManyToOne(optional = true, fetch = FetchType.LAZY)
  @JoinColumn(name = "person_id", insertable = false, updatable = false)
  public PersonEntity getPerson()
  {
    return m_person;
  }

  public void setPerson(final PersonEntity person)
  {
    m_person = person;
  }

  @ManyToOne(optional = true, fetch = FetchType.LAZY)
  @JoinColumn(name = "todo_id", insertable = false, updatable = false)
  public TodoEntity getTodo()
  {
    return m_todo;
  }

  public void setTodo(final TodoEntity todo)
  {
    m_todo = todo;
  }

  public Integer getEstimatedTime()
  {
    return m_estimatedTime;
  }

  public void setEstimatedTime(final Integer estimatedTime)
  {
    m_estimatedTime = estimatedTime;
  }
}
