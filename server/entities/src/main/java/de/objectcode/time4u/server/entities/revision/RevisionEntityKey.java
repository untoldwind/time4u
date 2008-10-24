package de.objectcode.time4u.server.entities.revision;

import java.io.Serializable;

import javax.persistence.Embeddable;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import de.objectcode.time4u.server.api.data.EntityType;

@Embeddable
public class RevisionEntityKey implements Serializable
{
  private static final long serialVersionUID = -143072929625119307L;

  private EntityType m_entityType;
  private String m_part;

  protected RevisionEntityKey()
  {
  }

  public RevisionEntityKey(final EntityType entityType, final String part)
  {
    m_part = part;
    m_entityType = entityType;
  }

  @Type(type = "de.objectcode.time4u.server.entities.util.GenericEnumUserType", parameters = {
      @Parameter(name = "enumClass", value = "de.objectcode.time4u.server.api.data.EntityType"),
      @Parameter(name = "identifierMethod", value = "getCode"), @Parameter(name = "valueOfMethod", value = "forCode") })
  public EntityType getEntityType()
  {
    return m_entityType;
  }

  public void setEntityType(final EntityType entityType)
  {
    m_entityType = entityType;
  }

  public String getPart()
  {
    return m_part;
  }

  public void setPart(final String part)
  {
    m_part = part;
  }

  @Override
  public boolean equals(final Object obj)
  {
    if (obj == null || !(obj instanceof RevisionEntityKey)) {
      return false;
    }

    final RevisionEntityKey castObj = (RevisionEntityKey) obj;

    return m_entityType == castObj.m_entityType && m_part.equals(castObj.m_part);
  }

  @Override
  public int hashCode()
  {
    int hash = m_entityType != null ? m_entityType.getCode() : -1;

    hash = hash + 13 * m_part.hashCode();

    return hash;
  }

  @Override
  public String toString()
  {
    final StringBuffer buffer = new StringBuffer("RevisionEntityKey(");
    buffer.append("entityType=").append(m_entityType);
    buffer.append(", part=").append(m_part);
    buffer.append(")");

    return buffer.toString();
  }

}
