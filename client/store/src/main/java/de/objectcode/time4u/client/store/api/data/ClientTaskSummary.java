package de.objectcode.time4u.client.store.api.data;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;

import de.objectcode.time4u.server.api.data.TaskSummary;

/**
 * Client wrapper of the DTO object that implements IAdaptable.
 * 
 * @author junglas
 */
public class ClientTaskSummary extends TaskSummary implements IAdaptable
{
  private static final long serialVersionUID = 8584350967481951002L;

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public Object getAdapter(final Class adapter)
  {
    return Platform.getAdapterManager().getAdapter(this, adapter);
  }

}
