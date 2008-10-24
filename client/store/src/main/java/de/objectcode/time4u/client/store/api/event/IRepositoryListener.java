package de.objectcode.time4u.client.store.api.event;

/**
 * Repository even listener.
 * 
 * @author junglas
 */
public interface IRepositoryListener
{
  void handleRepositoryEvent(RepositoryEvent event);
}
