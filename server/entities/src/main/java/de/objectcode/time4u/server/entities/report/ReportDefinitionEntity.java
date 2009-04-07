package de.objectcode.time4u.server.entities.report;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "T4U_REPORTS")
public class ReportDefinitionEntity
{
  private String m_id;
  private String m_name;
  private String m_description;
  private String m_type;
  private String m_definitionXml;

  /**
   * Default constructor for hibernate.
   */
  protected ReportDefinitionEntity()
  {
  }

  public ReportDefinitionEntity(final String id, final String name, final String type, final String description,
      final String definitionXml)
  {
    m_id = id;
    m_name = name;
    m_type = type;
    m_description = description;
    m_definitionXml = definitionXml;
  }

  @Id
  @Column(name = "ID", length = 128, nullable = false)
  public String getId()
  {
    return m_id;
  }

  public void setId(final String id)
  {
    m_id = id;
  }

  @Column(name = "NAME", length = 128, nullable = false)
  public String getName()
  {
    return m_name;
  }

  public void setName(final String name)
  {
    m_name = name;
  }

  @Column(name = "DESCRIPTION", length = 1000)
  public String getDescription()
  {
    return m_description;
  }

  public void setDescription(final String description)
  {
    m_description = description;
  }

  @Column(name = "REPORT_TYPE", length = 64, nullable = false)
  public String getType()
  {
    return m_type;
  }

  public void setType(final String type)
  {
    m_type = type;
  }

  @Column(name = "DEFINITION_XML")
  @Lob
  public String getDefinitionXml()
  {
    return m_definitionXml;
  }

  public void setDefinitionXml(final String definitionXml)
  {
    m_definitionXml = definitionXml;
  }

}
