package de.objectcode.time4u.server.api.data;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlType;

/**
 * Assignment of a todo to a person.
 * 
 * @author junglas
 */
@XmlType(name = "todo-assigment")
public class TodoAssignment implements Serializable
{
  private static final long serialVersionUID = -5853803351855639525L;

  /** Id of the person the todo is assigned to. */
  String m_personId;
  /** Estimated workitem for the todo for this person in seconds. */
  Integer m_estimatedTime;

  public String getPersonId()
  {
    return m_personId;
  }

  public void setPersonId(final String personId)
  {
    m_personId = personId;
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
