package de.objectcode.time4u.server.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * Day information entity.
 * 
 * This entity is related to the workitem entities as it provides a summary of workitems per day.
 * 
 * @author junglas
 */
@Entity
@Table(name = "T4U_DAYINFOS")
public class DayInfoEntity
{
  /** Primary key */
  private long m_id;
  /** Revision number (increased every time something has changed) */
  private long m_revision;

  @Id
  @GeneratedValue(generator = "SEQ_T4U_DAYINFOS")
  @GenericGenerator(name = "SEQ_T4U_DAYINFOS", strategy = "native", parameters = @Parameter(name = "sequence", value = "SEQ_T4U_DAYINFOS"))
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

}
