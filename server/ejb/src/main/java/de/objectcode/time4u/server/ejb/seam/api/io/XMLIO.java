package de.objectcode.time4u.server.ejb.seam.api.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

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
   * Read a report definition from an xml stream.
   * 
   * @param in
   *          The xml reader
   * @return The report definition
   * @throws IOException
   *           on error
   */
  public BaseReportDefinition read(final Reader in) throws IOException
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

  public static void main(final String[] args)
  {
    try {
      final JAXBContext context = JAXBContext
          .newInstance("de.objectcode.time4u.server.ejb.seam.api.filter:de.objectcode.time4u.server.ejb.seam.api.report");

      final Map<String, StringWriter> schemas = new HashMap<String, StringWriter>();

      context.generateSchema(new SchemaOutputResolver() {

        @Override
        public Result createOutput(final String systemId, final String file) throws IOException
        {
          final StringWriter out = new StringWriter();

          schemas.put(file, out);
          final StreamResult result = new StreamResult(systemId);

          result.setWriter(out);
          return result;
        }

      });

      for (final String file : schemas.keySet()) {
        System.out.println(">> " + file);
        System.out.println(schemas.get(file).toString());
      }
    } catch (final Exception e) {
      e.printStackTrace();
    }

  }
}
