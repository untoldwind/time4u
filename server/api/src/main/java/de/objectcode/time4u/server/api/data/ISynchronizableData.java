package de.objectcode.time4u.server.api.data;

import java.io.Serializable;

/**
 * Common interface of all data objects that represent data that can be synchronized with the server.
 * 
 * @author junglas
 */
public interface ISynchronizableData extends Serializable
{
  /**
   * Get the internal server id of the data.
   * 
   * @return The server id of the data
   */
  String getId();

  /**
   * Revision number.
   * 
   * @return The revision number
   */
  long getRevision();

  /**
   * Get the id of the client performing the last modification.
   * 
   * @return Id of the client performing the last modification
   */
  long getLastModifiedByClient();
}
