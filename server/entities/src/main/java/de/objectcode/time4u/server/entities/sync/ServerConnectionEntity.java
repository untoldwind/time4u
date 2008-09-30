package de.objectcode.time4u.server.entities.sync;

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

import de.objectcode.time4u.server.api.data.ServerConnection;
import de.objectcode.time4u.server.api.data.SynchronizableType;
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
  /** Primary key */
  private long m_id;
  /** Root project to be synchronized */
  private ProjectEntity m_rootProject;
  /** Connection url */
  private String m_url;
  /** Server credentials */
  private String m_credentials;
  private Map<SynchronizableType, SynchronizationStatusEntity> m_synchronizationStatus;

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

  @MapKey(name = "entityType")
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "serverConnection")
  public Map<SynchronizableType, SynchronizationStatusEntity> getSynchronizationStatus()
  {
    return m_synchronizationStatus;
  }

  public void setSynchronizationStatus(final Map<SynchronizableType, SynchronizationStatusEntity> synchronizationStatus)
  {
    m_synchronizationStatus = synchronizationStatus;
  }

  public void toDTO(final ServerConnection serverConnection, final IKeyChainEncoder encoder)
  {
    serverConnection.setId(m_id);
    serverConnection.setRootProjectId(m_rootProject != null ? m_rootProject.getId() : null);
    serverConnection.setUrl(m_url);
    serverConnection.setCredentials(encoder.decrypt(m_credentials));
  }

  public void fromDTO(final IPersistenceContext context, final ServerConnection serverConnection,
      final IKeyChainEncoder encoder)
  {
    m_rootProject = serverConnection.getRootProjectId() != null ? context.findProject(serverConnection
        .getRootProjectId()) : null;
    m_url = serverConnection.getUrl();
    m_credentials = encoder.encrypt(serverConnection.getCredentials());
  }
}
