package de.objectcode.time4u.server.entities.audit;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.ProjectEntity;

/**
 * Project history entity. This is part of the audit information.
 * 
 * @author junglas
 */
@Entity
@Table(name = "T4U_PROJECTS_HISTORY")
public class ProjectHistoryEntity
{
  /** Primary key. */
  private long m_id;
  /** The project his histroy entry belongs to. */
  private ProjectEntity m_project;
  /** Person who performed the change. */
  private PersonEntity m_performedBy;
  /** Timestamp of the change. */
  private Date m_performedAt;
  /** Original name of the project. */
  private String m_name;
  /** Original description of the project. */
  private String m_description;
  /** Original active flag. */
  private boolean m_active;
  /** Original deleted flag. */
  private boolean m_deleted;
  /** Original parent project. */
  private ProjectEntity m_parent;

  public ProjectHistoryEntity()
  {
  }

  public ProjectHistoryEntity(final ProjectEntity project, final PersonEntity performedBy)
  {
    m_project = project;
    m_performedBy = performedBy;
    m_performedAt = new Date();

    m_name = project.getName();
    m_description = project.getDescription();
    m_active = project.isActive();
    m_deleted = project.isDeleted();
    m_parent = project.getParent();
  }

  @Id
  @GeneratedValue(generator = "SEQ_T4U_PROJECTS_HISTORY")
  @GenericGenerator(name = "SEQ_T4U_PROJECTS_HISTORY", strategy = "native", parameters = @Parameter(name = "sequence", value = "SEQ_T4U_PROJECTS_HISTORY"))
  public long getId()
  {
    return m_id;
  }

  public void setId(final long id)
  {
    m_id = id;
  }

  public boolean isActive()
  {
    return m_active;
  }

  public void setActive(final boolean active)
  {
    m_active = active;
  }

  public boolean isDeleted()
  {
    return m_deleted;
  }

  public void setDeleted(final boolean deleted)
  {
    m_deleted = deleted;
  }

  @Column(length = 30, nullable = true)
  public String getName()
  {
    return m_name;
  }

  public void setName(final String name)
  {
    m_name = name;
  }

  @Column(length = 1000, nullable = true)
  public String getDescription()
  {
    return m_description;
  }

  public void setDescription(final String description)
  {
    m_description = description;
  }

  @ManyToOne
  @JoinColumns( { @JoinColumn(name = "parent_clientId"), @JoinColumn(name = "parent_localId") })
  public ProjectEntity getParent()
  {
    return m_parent;
  }

  public void setParent(final ProjectEntity parent)
  {
    m_parent = parent;
  }

  @ManyToOne
  @JoinColumns( { @JoinColumn(name = "project_clientId"), @JoinColumn(name = "project_localId") })
  public ProjectEntity getProject()
  {
    return m_project;
  }

  public void setProject(final ProjectEntity project)
  {
    m_project = project;
  }

  @ManyToOne
  @JoinColumns( { @JoinColumn(name = "performedBy_clientId"), @JoinColumn(name = "performedBy_localId") })
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
