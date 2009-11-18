package de.objectcode.time4u.server.ejb.seam.api.filter;

import java.util.Map;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.persistence.Query;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.ejb.seam.api.report.parameter.BaseParameterValue;
import de.objectcode.time4u.server.ejb.util.ReportEL;

@XmlType(name = "person")
@XmlRootElement(name = "person")
public class PersonFilter implements IFilter
{
  private static final long serialVersionUID = 8349111785006964973L;

  private String m_personId;
  private String m_personIdExpression;

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

  @XmlAttribute(name = "person-id-expression")
  public String getPersonIdExpression()
  {
    return m_personIdExpression;
  }

  public void setPersonIdExpression(final String personIdExpression)
  {
    m_personIdExpression = personIdExpression;
  }

  /**
   * {@inheritDoc}
   */
  public String getWhereClause(final EntityType entityType, final Map<String, BaseParameterValue> parameters,
      final ELContext context)
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

  /**
   * {@inheritDoc}
   */
  public void setQueryParameters(final EntityType entityType, final Query query,
      final Map<String, BaseParameterValue> parameters, final ELContext context)
  {
    if (m_personIdExpression != null) {
      final ExpressionFactory factory = ReportEL.getExpressionFactory();

      query.setParameter("personId", factory.createValueExpression(context, m_personIdExpression, String.class)
          .getValue(context));
    } else {
      query.setParameter("personId", m_personId);
    }
  }

  public static PersonFilter filterPerson(final String personId)
  {
    return new PersonFilter(personId);
  }
}
