package de.objectcode.time4u.server.api.data;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Helper class to encode client id lists in XML.
 * 
 * The is a limitation in JBoss' web-service implementation that does not allow <tt>java.util.List</tt> results.
 * 
 * @author junglas
 * 
 * @param <T>
 */
@XmlType(name = "client-id-list")
@XmlRootElement(name = "client-id-list")
public class ClientIdList
{
  private List<Long> results;

  public ClientIdList()
  {
  }

  public ClientIdList(final List<Long> results)
  {
    this.results = results;
  }

  @XmlElement(name = "client-id")
  public List<Long> getResults()
  {
    return results;
  }

  public void setResults(final List<Long> results)
  {
    this.results = results;
  }
}
