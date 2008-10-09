package de.objectcode.time4u.server.api.data;

import java.io.Serializable;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "revision-status")
@XmlRootElement(name = "revision-status")
public class RevisionStatus implements Serializable
{
  private static final long serialVersionUID = 376719045033681490L;

  Map<EntityType, Long> m_latestRevisions;

  public RevisionStatus()
  {
  }

  public RevisionStatus(final Map<EntityType, Long> latestRevisions)
  {
    m_latestRevisions = latestRevisions;
  }

  public Map<EntityType, Long> getLatestRevisions()
  {
    return m_latestRevisions;
  }

  public void setLatestRevisions(final Map<EntityType, Long> latestRevisions)
  {
    m_latestRevisions = latestRevisions;
  }

  @Override
  public String toString()
  {
    final StringBuffer buffer = new StringBuffer("RevisionStatus(");
    buffer.append(m_latestRevisions);
    buffer.append(")");

    return buffer.toString();
  }

}
