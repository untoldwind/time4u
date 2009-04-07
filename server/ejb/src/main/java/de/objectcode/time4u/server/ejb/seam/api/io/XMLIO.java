package de.objectcode.time4u.server.ejb.seam.api.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.objectcode.time4u.server.ejb.seam.api.report.BaseReportDefinition;

/**
 * Helper class to read/write report definition from/to xml files.
 * 
 * @author junglas
 */
public class XMLIO
{
  private static final Log LOG = LogFactory.getLog(XMLIO.class);

  /** Singleton instance. */
  public static final XMLIO INSTANCE = new XMLIO();

  private Marshaller marshaller;
  private Unmarshaller unmarshaller;

  /**
   * Singleton constructor.
   */
  private XMLIO()
  {
    try {
      final JAXBContext context = JAXBContext
          .newInstance("de.objectcode.time4u.server.ejb.seam.api.filter:de.objectcode.time4u.server.ejb.seam.api.report");

      marshaller = context.createMarshaller();
      marshaller.setProperty("jaxb.encoding", "UTF-8");
      marshaller.setProperty("jaxb.formatted.output", true);
      unmarshaller = context.createUnmarshaller();
    } catch (final Exception e) {
      e.printStackTrace();
      LOG.error("Exception", e);
    }
  }

  /**
   * Read a report definition from an xml stream.
   * 
   * @param in
   *          The xml intput stream
   * @return The report definition
   * @throws IOException
   *           on error
   */
  public BaseReportDefinition read(final InputStream in) throws IOException
  {
    try {
      return (BaseReportDefinition) unmarshaller.unmarshal(in);
    } catch (final JAXBException e) {
      LOG.error("Exception", e);
      throw new IOException(e.toString());
    }
  }

  /**
   * Write a report definition to an xml stream.
   * 
   * @param definition
   *          The report definition
   * @param out
   *          The xml output stream
   * @throws IOException
   *           on error
   */
  public void write(final BaseReportDefinition definition, final OutputStream out) throws IOException
  {
    try {
      marshaller.marshal(definition, out);
    } catch (final JAXBException e) {
      LOG.error("Exception", e);
      throw new IOException(e.toString());
    }
  }
}
