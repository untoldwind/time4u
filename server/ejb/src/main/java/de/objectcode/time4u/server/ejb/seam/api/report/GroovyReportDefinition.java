package de.objectcode.time4u.server.ejb.seam.api.report;

import groovy.lang.GroovyClassLoader;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.xml.txw2.annotation.XmlCDATA;

import de.objectcode.time4u.server.api.data.EntityType;

/**
 * Most generic report definition using a groovy script.
 * 
 * Use this care.
 * 
 * @author junglas
 */
@XmlType(name = "groovy-report")
@XmlRootElement(name = "groovy-report")
public class GroovyReportDefinition extends BaseReportDefinition
{
  private static final long serialVersionUID = -6095504754766228539L;

  private final static Log LOG = LogFactory.getLog(GroovyReportDefinition.class);

  EntityType m_entityType;
  String m_script;

  @XmlElement(name = "script", namespace = "http://objectcode.de/time4u/ejb/seam/report")
  @XmlCDATA
  public String getScript()
  {
    return m_script;
  }

  public void setScript(final String script)
  {
    m_script = script;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @XmlAttribute(name = "entity-type")
  public EntityType getEntityType()
  {
    return EntityType.WORKITEM;
  }

  public void setEntityType(final EntityType entityType)
  {
    m_entityType = entityType;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public IReportDataCollector createDataCollector()
  {
    try {
      final GroovyClassLoader gcl = new GroovyClassLoader();
      final Class<? extends IReportDataCollector> gclass = gcl.parseClass(m_script);

      return gclass.newInstance();
    } catch (final Exception e) {
      LOG.error("Exception", e);

      throw new RuntimeException("Groovy error", e);
    }
  }
}
