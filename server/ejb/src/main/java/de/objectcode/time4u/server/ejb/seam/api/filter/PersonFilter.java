package de.objectcode.time4u.server.ejb.seam.api.filter;

import java.util.Map;

import javax.persistence.Query;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.ejb.seam.api.report.parameter.BaseParameterValue;

@XmlType(name = "person")
@XmlRootElement(name = "person")
public class PersonFilter implements IFilter
{
  private static final long serialVersionUID = 8349111785006964973L;

  private String m_personId;

  public PersonFilter()
  {
  }

  public PersonFilter(final String personId)
  {
    m_personId = personId;
  }

  @XmlAttribute(name = "person-id")
  public String getPersonId()
  {
    return m_personId;
  }

  public void setPersonId(final String personId)
  {
    m_personId = personId;
  }

  public String getWhereClause(final EntityType entityType, final Map<String, BaseParameterValue> parameters)
  {
    switch (entityType) {
      case DAYINFO:
        return "(d.person.id = :personId)";
      case WORKITEM:
        return "(w.dayInfo.person.id = :personId)";
      case TODO:
        return "(t.reporter.id = :personId)";
      default:
        throw new RuntimeException("PersonFilter not applicable for entity type: " + entityType);
    }
  }

  public void setQueryParameters(final EntityType entityType, final Query query,
      final Map<String, BaseParameterValue> parameters)
  {
    query.setParameter("personId", m_personId);
  }

  public static PersonFilter filterPerson(final String personId)
  {
    return new PersonFilter(personId);
  }
}
