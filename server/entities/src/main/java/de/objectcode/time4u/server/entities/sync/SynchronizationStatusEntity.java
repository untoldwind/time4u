package de.objectcode.time4u.server.entities.sync;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.api.data.SynchronizationStatus;

@Entity
@Table(name = "T4U_SYNCHRONIZATIONSTATUS", uniqueConstraints = @UniqueConstraint(columnNames = {
    "server_connection_id", "entityType" }))
public class SynchronizationStatusEntity
{
  /** Primary key */
  private long m_id;
  private ServerConnectionEntity m_serverConnection;
  private EntityType m_entityType;
  private long m_lastSendRevision;
  private long m_lastReceivedRevision;

  @Id
  @GeneratedValue(generator = "SEQ_T4U_SYNCHRONIZATIONSTATUS")
  @GenericGenerator(name = "SEQ_T4U_SYNCHRONIZATIONSTATUS", strategy = "native", parameters = @Parameter(name = "sequence", value = "SEQ_T4U_SYNCHRONIZATIONSTATUS"))
  public long getId()
  {
    return m_id;
  }

  public void setId(final long id)
  {
    m_id = id;
  }

  @ManyToOne(optional = false)
  @JoinColumn(name = "server_connection_id")
  public ServerConnectionEntity getServerConnection()
  {
    return m_serverConnection;
  }

  public void setServerConnection(final ServerConnectionEntity serverConnection)
  {
    m_serverConnection = serverConnection;
  }

  @Type(type = "de.objectcode.time4u.server.entities.util.GenericEnumUserType", parameters = {
      @Parameter(name = "enumClass", value = "de.objectcode.time4u.server.api.data.EntityType"),
      @Parameter(name = "identifierMethod", value = "getCode"), @Parameter(name = "valueOfMethod", value = "forCode") })
  @Column(name = "entityType", nullable = false)
  public EntityType getEntityType()
  {
    return m_entityType;
  }

  public void setEntityType(final EntityType entityType)
  {
    m_entityType = entityType;
  }

  public long getLastSendRevision()
  {
    return m_lastSendRevision;
  }

  public void setLastSendRevision(final long lastSendRevision)
  {
    m_lastSendRevision = lastSendRevision;
  }

  public long getLastReceivedRevision()
  {
    return m_lastReceivedRevision;
  }

  public void setLastReceivedRevision(final long lastReceivedRevision)
  {
    m_lastReceivedRevision = lastReceivedRevision;
  }

  public void toDTO(final SynchronizationStatus status)
  {
    status.setEntityType(m_entityType);
    status.setLastReceivedRevision(m_lastReceivedRevision);
    status.setLastSendRevision(m_lastSendRevision);
  }

  public void fromDTO(final SynchronizationStatus status)
  {
    m_lastReceivedRevision = status.getLastReceivedRevision();
    m_lastSendRevision = status.getLastSendRevision();
  }
}
