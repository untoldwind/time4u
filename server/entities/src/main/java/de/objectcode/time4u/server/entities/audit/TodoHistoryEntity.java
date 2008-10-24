package de.objectcode.time4u.server.entities.audit;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.TodoEntity;

/**
 * Todo history entity. This is part of the audit information.
 * 
 * @author junglas
 */
@Entity
@Table(name = "T4U_TODOS_HISTORY")
public class TodoHistoryEntity
{
  /** Primary key. */
  private long m_id;
  /** The todo the history entry belongs to. */
  private TodoEntity m_todo;
  /** Person who performed the change. */
  private PersonEntity m_performedBy;
  /** Timestamp of the change. */
  private Date m_performedAt;
  /** Original data */
  private String m_data;

  public TodoHistoryEntity()
  {
  }

  public TodoHistoryEntity(final TodoEntity todo, final PersonEntity performedBy)
  {
    m_todo = todo;
    m_performedBy = performedBy;
    m_performedAt = new Date();
  }

  @Id
  @GeneratedValue(generator = "SEQ_T4U_TODOS_HISTORY")
  @GenericGenerator(name = "SEQ_T4U_TODOS_HISTORY", strategy = "native", parameters = @Parameter(name = "sequence", value = "SEQ_T4U_TODOS_HISTORY"))
  public long getId()
  {
    return m_id;
  }

  public void setId(final long id)
  {
    m_id = id;
  }

  @Lob()
  @Basic(fetch = FetchType.LAZY)
  public String getData()
  {
    return m_data;
  }

  public void setData(final String data)
  {
    m_data = data;
  }

  @ManyToOne(optional = false)
  @JoinColumn(name = "todo_id")
  public TodoEntity getTodo()
  {
    return m_todo;
  }

  public void setTodo(final TodoEntity todo)
  {
    m_todo = todo;
  }

  @ManyToOne(optional = false)
  @JoinColumn(name = "performedBy_id")
  public PersonEntity getPerformedBy()
  {
    return m_performedBy;
  }

  public void setPerformedBy(final PersonEntity performedBy)
  {
    m_performedBy = performedBy;
  }

  @Column(nullable = false)
  public Date getPerformedAt()
  {
    return m_performedAt;
  }

  public void setPerformedAt(final Date performedAt)
  {
    m_performedAt = performedAt;
  }
}
