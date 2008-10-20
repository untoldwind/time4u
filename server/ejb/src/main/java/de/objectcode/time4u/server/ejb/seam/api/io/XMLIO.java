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

public class XMLIO
{
  private static final Log LOG = LogFactory.getLog(XMLIO.class);

  public static final XMLIO INSTANCE = new XMLIO();

  private Marshaller marshaller;
  private Unmarshaller unmarshaller;

  public XMLIO()
  {
    try {
      final JAXBContext context = JAXBContext
          .newInstance("de.objectcode.time4u.server.ejb.seam.api.filter:de.objectcode.time4u.server.ejb.seam.api.report");

      marshaller = context.createMarshaller();
      unmarshaller = context.createUnmarshaller();
    } catch (final Exception e) {
      e.printStackTrace();
      LOG.error("Exception", e);
    }
  }

  public BaseReportDefinition read(final InputStream in) throws IOException
  {
    try {
      return (BaseReportDefinition) unmarshaller.unmarshal(in);
    } catch (final JAXBException e) {
      LOG.error("Exception", e);
      throw new IOException(e.toString());
    }
  }

  public void write(final BaseReportDefinition messageFormatModel, final OutputStream out) throws IOException
  {
    try {
      marshaller.marshal(messageFormatModel, out);
    } catch (final JAXBException e) {
      LOG.error("Exception", e);
      throw new IOException(e.toString());
    }
  }
}
