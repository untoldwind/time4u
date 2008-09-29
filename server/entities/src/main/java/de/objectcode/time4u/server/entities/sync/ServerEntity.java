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

import de.objectcode.time4u.server.entities.ProjectEntity;

/**
 * Server entity.
 * 
 * Stores the informations of a connection to a server.
 * 
 * @author junglas
 */
@Entity
@Table(name = "T4U_SERVERS")
public class ServerEntity
{
  /** Primary key */
  private long m_id;
  /** Revision number (increased every time something has changed) */
  private long m_revision;
  /** Root project to be synchronized */
  private ProjectEntity m_rootProject;
  /** Connection url */
  private String m_url;
  /** User id */
  private String m_username;
  /** Crypted password */
  private String m_crypedPassword;

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

  public long getRevision()
  {
    return m_revision;
  }

  public void setRevision(final long revision)
  {
    m_revision = revision;
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

  @Column(length = 50, nullable = false)
  public String getUsername()
  {
    return m_username;
  }

  public void setUsername(final String username)
  {
    m_username = username;
  }

  @Column(length = 100, nullable = false)
  public String getCrypedPassword()
  {
    return m_crypedPassword;
  }

  public void setCrypedPassword(final String crypedPassword)
  {
    m_crypedPassword = crypedPassword;
  }

}
