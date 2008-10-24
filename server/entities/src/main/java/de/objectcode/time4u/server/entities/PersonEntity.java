package de.objectcode.time4u.server.entities;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import de.objectcode.time4u.server.api.data.MetaProperty;
import de.objectcode.time4u.server.api.data.Person;
import de.objectcode.time4u.server.api.data.PersonSummary;

/**
 * Person entity.
 * 
 * @author junglas
 */
@Entity
@Table(name = "T4U_PERSONS")
public class PersonEntity
{
  /** Primary key. */
  private String m_id;
  /** Revision number (increased every time something has changed) */
  private long m_revision;
  /** Flag if the person is deleted. */
  private boolean m_deleted;
  /** Client id of the last modification */
  private long m_lastModifiedByClient;
  /** Given name of the person. */
  private String m_givenName;
  /** Surname name of the person */
  private String m_surname;
  /** Email of the person. */
  private String m_email;
  /** Set of teams the person is responsible for (i.e. owning them) */
  private Set<TeamEntity> m_responsibleFor;
  /** Set of teams the person is member of */
  private Set<TeamEntity> m_memberOf;
  /** Timestamp of the last synchronization */
  private Date m_lastSynchronize;
  /** Time policies of the person */
  Set<TimePolicyEntity> m_timePolicies;
  /** Meta properties of the person */
  Map<String, PersonMetaPropertyEntity> m_metaProperties;

  protected PersonEntity()
  {
  }

  public PersonEntity(final String id, final long revision, final long lastModifiedByClient)
  {
    m_id = id;
    m_revision = revision;
    m_lastModifiedByClient = lastModifiedByClient;
  }

  @Id
  @Column(length = 36)
  public String getId()
  {
    return m_id;
  }

  public void setId(final String id)
  {
    m_id = id;
  }

  @Column(length = 50, nullable = true)
  public String getGivenName()
  {
    return m_givenName;
  }

  public void setGivenName(final String givenName)
  {
    m_givenName = givenName;
  }

  @Column(length = 50, nullable = false)
  public String getSurname()
  {
    return m_surname;
  }

  public void setSurname(final String surname)
  {
    m_surname = surname;
  }

  @Column(length = 200, nullable = true)
  public String getEmail()
  {
    return m_email;
  }

  public void setEmail(final String email)
  {
    m_email = email;
  }

  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "members")
  public Set<TeamEntity> getMemberOf()
  {
    return m_memberOf;
  }

  public void setMemberOf(final Set<TeamEntity> memberOf)
  {
    m_memberOf = memberOf;
  }

  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "owners")
  public Set<TeamEntity> getResponsibleFor()
  {
    return m_responsibleFor;
  }

  public void setResponsibleFor(final Set<TeamEntity> responsibleFor)
  {
    m_responsibleFor = responsibleFor;
  }

  @Column(nullable = true)
  public Date getLastSynchronize()
  {
    return m_lastSynchronize;
  }

  public void setLastSynchronize(final Date lastSynchronize)
  {
    m_lastSynchronize = lastSynchronize;
  }

  public long getRevision()
  {
    return m_revision;
  }

  public void setRevision(final long revision)
  {
    m_revision = revision;
  }

  public long getLastModifiedByClient()
  {
    return m_lastModifiedByClient;
  }

  public void setLastModifiedByClient(final long lastModifiedByClient)
  {
    m_lastModifiedByClient = lastModifiedByClient;
  }

  public boolean isDeleted()
  {
    return m_deleted;
  }

  public void setDeleted(final boolean deleted)
  {
    m_deleted = deleted;
  }

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "person")
  @OrderBy
  public Set<TimePolicyEntity> getTimePolicies()
  {
    return m_timePolicies;
  }

  public void setTimePolicies(final Set<TimePolicyEntity> timePolicies)
  {
    m_timePolicies = timePolicies;
  }

  @MapKey(name = "name")
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "entityId")
  public Map<String, PersonMetaPropertyEntity> getMetaProperties()
  {
    return m_metaProperties;
  }

  public void setMetaProperties(final Map<String, PersonMetaPropertyEntity> metaProperties)
  {
    m_metaProperties = metaProperties;
  }

  @Override
  public int hashCode()
  {
    return m_id.hashCode();
  }

  @Override
  public boolean equals(final Object obj)
  {
    if (obj == this) {
      return true;
    }

    if (obj == null || !(obj instanceof PersonEntity)) {
      return false;
    }

    final PersonEntity castObj = (PersonEntity) obj;

    return m_id.equals(castObj.m_id);
  }

  public void toSummaryDTO(final PersonSummary person)
  {
    person.setId(m_id);
    person.setRevision(m_revision);
    person.setLastModifiedByClient(m_lastModifiedByClient);
    person.setGivenName(m_givenName);
    person.setSurname(m_surname);
    person.setEmail(m_email);
    person.setDeleted(m_deleted);
    person.setLastSynchronize(m_lastSynchronize);

  }

  public void toDTO(final Person person)
  {
    toSummaryDTO(person);

    if (m_metaProperties != null) {
      for (final PersonMetaPropertyEntity property : m_metaProperties.values()) {
        person.setMetaProperty(property.toDTO());
      }
    }
  }

  public void fromDTO(final Person person)
  {
    m_lastModifiedByClient = person.getLastModifiedByClient();
    m_givenName = person.getGivenName();
    m_surname = person.getSurname();
    m_email = person.getEmail();
    m_deleted = person.isDeleted();

    if (m_metaProperties == null) {
      m_metaProperties = new HashMap<String, PersonMetaPropertyEntity>();
    }

    if (person.getMetaProperties() != null) {
      for (final MetaProperty metaProperty : person.getMetaProperties().values()) {
        PersonMetaPropertyEntity property = m_metaProperties.get(metaProperty.getName());

        if (property == null) {
          property = new PersonMetaPropertyEntity();

          m_metaProperties.put(metaProperty.getName(), property);
        }
        property.fromDTO(metaProperty);
      }
    }
  }
}
