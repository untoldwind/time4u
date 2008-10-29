package de.objectcode.time4u.server.api.data;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Person summary DTO object.
 * 
 * @author junglas
 */
@XmlType(name = "person-summary")
@XmlRootElement(name = "person-summary")
public class PersonSummary implements ISynchronizableData
{
  private static final long serialVersionUID = 2399244105597561898L;

  /** Internal server id of the person. */
  private String m_id;
  /** Revision number. */
  private long m_revision;
  /** Client id of the last modification */
  private long m_lastModifiedByClient;
  /** Flag if the person is deleted. */
  private boolean m_deleted;
  /** Given name of the person. */
  private String m_givenName;
  /** Surname of the person. */
  private String m_surname;
  /** Email address of the person. */
  private String m_email;
  /** Timestamp of the last synchronization of that person. */
  private Date m_lastSynchronize;

  @XmlAttribute
  public String getId()
  {
    return m_id;
  }

  public void setId(final String id)
  {
    m_id = id;
  }

  @XmlAttribute
  public long getRevision()
  {
    return m_revision;
  }

  public void setRevision(final long revision)
  {
    m_revision = revision;
  }

  @XmlAttribute
  public long getLastModifiedByClient()
  {
    return m_lastModifiedByClient;
  }

  public void setLastModifiedByClient(final long lastModifiedByClient)
  {
    m_lastModifiedByClient = lastModifiedByClient;
  }

  @XmlAttribute
  public String getGivenName()
  {
    return m_givenName;
  }

  public void setGivenName(final String givenName)
  {
    m_givenName = givenName;
  }

  @XmlAttribute
  public String getSurname()
  {
    return m_surname;
  }

  public void setSurname(final String surname)
  {
    m_surname = surname;
  }

  @XmlAttribute
  public boolean isDeleted()
  {
    return m_deleted;
  }

  public void setDeleted(final boolean deleted)
  {
    m_deleted = deleted;
  }

  @XmlAttribute
  public String getEmail()
  {
    return m_email;
  }

  public void setEmail(final String email)
  {
    m_email = email;
  }

  @XmlAttribute
  public Date getLastSynchronize()
  {
    return m_lastSynchronize;
  }

  public void setLastSynchronize(final Date lastSynchronize)
  {
    m_lastSynchronize = lastSynchronize;
  }

}
