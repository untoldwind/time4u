package de.objectcode.time4u.server.entities.sync;

import java.util.Date;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.api.data.ServerConnection;
import de.objectcode.time4u.server.entities.ProjectEntity;
import de.objectcode.time4u.server.entities.context.IPersistenceContext;
import de.objectcode.time4u.server.utils.IKeyChainEncoder;

/**
 * Server entity.
 * 
 * Stores the informations of a connection to a server.
 * 
 * @author junglas
 */
@Entity
@Table(name = "T4U_SERVERS")
public class ServerConnectionEntity
{
  /** Primary key. */
  private long m_id;
  /** Root project to be synchronized. */
  private ProjectEntity m_rootProject;
  /** Logical name of the server. */
  private String m_name;
  /** Connection url. */
  private String m_url;
  /** Server credentials. */
  private String m_credentials;
  /** Timestamp of the last synchronization. */
  private Date m_lastSynchronize;
  /** Synchronize every x seconds (0 = never). */
  private int m_synchronizeInterval;

  private Map<EntityType, SynchronizationStatusEntity> m_synchronizationStatus;

  @Id
  @GeneratedValue(generator = "SEQ_T4U_SERVERS")
  @GenericGenerator(name = "SEQ_T4U_SERVERS", strategy = "native", parameters = @Parameter(name = "sequence", value = "SEQ_T4U_SERVERS"))
  public long getId()
  {
    return m_id;
  }

  public void setId(final long id)
  {
    m_id = id;
  }

  @ManyToOne
  @JoinColumn(name = "root_project_id")
  public ProjectEntity getRootProject()
  {
    return m_rootProject;
  }

  public void setRootProject(final ProjectEntity rootProject)
  {
    m_rootProject = rootProject;
  }

  @Column(length = 200, nullable = false)
  public String getUrl()
  {
    return m_url;
  }

  public void setUrl(final String url)
  {
    m_url = url;
  }

  @Column(length = 1000, nullable = false)
  public String getCredentials()
  {
    return m_credentials;
  }

  public void setCredentials(final String credentials)
  {
    m_credentials = credentials;
  }

  public Date getLastSynchronize()
  {
    return m_lastSynchronize;
  }

  public void setLastSynchronize(final Date lastSynchronize)
  {
    m_lastSynchronize = lastSynchronize;
  }

  public int getSynchronizeInterval()
  {
    return m_synchronizeInterval;
  }

  public void setSynchronizeInterval(final int synchronizeInterval)
  {
    m_synchronizeInterval = synchronizeInterval;
  }

  @Column(length = 50, nullable = true)
  public String getName()
  {
    return m_name;
  }

  public void setName(final String name)
  {
    m_name = name;
  }

  @MapKey(name = "entityType")
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "serverConnection")
  public Map<EntityType, SynchronizationStatusEntity> getSynchronizationStatus()
  {
    return m_synchronizationStatus;
  }

  public void setSynchronizationStatus(final Map<EntityType, SynchronizationStatusEntity> synchronizationStatus)
  {
    m_synchronizationStatus = synchronizationStatus;
  }

  /**
   * Write data to DTO.
   * 
   * @param serverConnection
   *          The DTO.
   * @param encoder
   *          The encoder for stored passwords
   */
  public void toDTO(final ServerConnection serverConnection, final IKeyChainEncoder encoder)
  {
    serverConnection.setId(m_id);
    serverConnection.setRootProjectId(m_rootProject != null ? m_rootProject.getId() : null);
    serverConnection.setUrl(m_url);
    serverConnection.setCredentials(encoder.decrypt(m_credentials));
    serverConnection.setLastSynchronize(m_lastSynchronize);
    serverConnection.setSynchronizeInterval(m_synchronizeInterval);
    serverConnection.setName(m_name);
  }

  public void fromDTO(final IPersistenceContext context, final ServerConnection serverConnection,
      final IKeyChainEncoder encoder, final long clientId)
  {
    m_rootProject = serverConnection.getRootProjectId() != null ? context.findProject(serverConnection
        .getRootProjectId(), clientId) : null;
    m_url = serverConnection.getUrl();
    m_credentials = encoder.encrypt(serverConnection.getCredentials());
    m_synchronizeInterval = serverConnection.getSynchronizeInterval();
    m_name = serverConnection.getName();
  }
}
