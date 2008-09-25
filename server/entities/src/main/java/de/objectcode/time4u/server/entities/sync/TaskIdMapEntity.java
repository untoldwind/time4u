package de.objectcode.time4u.server.entities.sync;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import de.objectcode.time4u.server.entities.TaskEntity;

@Entity
@Table(name = "T4U_TASKS_IDMAP")
public class TaskIdMapEntity
{
  /** Primary key */
  private long m_id;
  ServerEntity m_server;
  TaskEntity m_task;
  /** Id on the server */
  private long serverId;
  /** Revision on the server */
  private long serverRevision;

  @Id
  @GeneratedValue(generator = "SEQ_T4U_TASKS_IDMAP")
  @GenericGenerator(name = "SEQ_T4U_TASKS_IDMAP", strategy = "native", parameters = @Parameter(name = "sequence", value = "SEQ_T4U_TASKS_IDMAP"))
  public long getId()
  {
    return m_id;
  }

  public void setId(final long id)
  {
    m_id = id;
  }

  @ManyToOne
  @Column(name = "server_id", nullable = false)
  public ServerEntity getServer()
  {
    return m_server;
  }

  public void setServer(final ServerEntity server)
  {
    m_server = server;
  }

  @ManyToOne
  @Column(name = "task_id", nullable = false)
  public TaskEntity getTask()
  {
    return m_task;
  }

  public void setTask(final TaskEntity task)
  {
    m_task = task;
  }

  public long getServerId()
  {
    return serverId;
  }

  public void setServerId(final long serverId)
  {
    this.serverId = serverId;
  }

  public long getServerRevision()
  {
    return serverRevision;
  }

  public void setServerRevision(final long serverRevision)
  {
    this.serverRevision = serverRevision;
  }
}
