package de.objectcode.time4u.client.store.api.data;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;

import de.objectcode.time4u.server.api.data.ProjectSummary;

/**
 * Client wrapper of the DTO object that implements IAdaptable.
 * 
 * @author junglas
 */
public class ClientProjectSummary extends ProjectSummary implements IAdaptable
{
  private static final long serialVersionUID = 6372563957541417564L;

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  public Object getAdapter(final Class adapter)
  {
    return Platform.getAdapterManager().getAdapter(this, adapter);
  }
}
