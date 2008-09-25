package de.objectcode.time4u.server.entities.sync;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import de.objectcode.time4u.server.entities.PersonEntity;

@Entity
@Table(name = "T4U_PERSONS_IDMAP")
public class PersonIdMapEntity
{
  /** Primary key */
  private long m_id;
  ServerEntity m_server;
  PersonEntity m_person;
  /** Id on the server */
  private long m_serverId;
  /** Revision on the client (known to the server). */
  private long m_clientRevision;
  /** Revision on the server */
  private long m_serverRevision;

  @Id
  @GeneratedValue(generator = "SEQ_T4U_PERSONS_IDMAP")
  @GenericGenerator(name = "SEQ_T4U_PERSONS_IDMAP", strategy = "native", parameters = @Parameter(name = "sequence", value = "SEQ_T4U_PERSONS_IDMAP"))
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
  @JoinColumn(name = "person_id", nullable = false)
  public PersonEntity getPerson()
  {
    return m_person;
  }

  public void setPerson(final PersonEntity person)
  {
    m_person = person;
  }

  public long getServerId()
  {
    return m_serverId;
  }

  public void setServerId(final long serverId)
  {
    m_serverId = serverId;
  }

  public long getClientRevision()
  {
    return m_clientRevision;
  }

  public void setClientRevision(final long clientRevision)
  {
    m_clientRevision = clientRevision;
  }

  public long getServerRevision()
  {
    return m_serverRevision;
  }

  public void setServerRevision(final long serverRevision)
  {
    m_serverRevision = serverRevision;
  }

}
