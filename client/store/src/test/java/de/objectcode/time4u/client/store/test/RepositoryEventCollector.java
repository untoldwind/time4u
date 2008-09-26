package de.objectcode.time4u.client.store.test;

import java.util.ArrayList;
import java.util.List;

import de.objectcode.time4u.client.store.api.event.IRepositoryListener;
import de.objectcode.time4u.client.store.api.event.RepositoryEvent;
import de.objectcode.time4u.client.store.api.event.RepositoryEventType;

public class RepositoryEventCollector implements IRepositoryListener
{
  RepositoryEventType m_eventType;
  List<RepositoryEvent> m_events = new ArrayList<RepositoryEvent>();

  public RepositoryEventCollector(final RepositoryEventType eventType)
  {
    m_eventType = eventType;
  }

  public void handleRepositoryEvent(final RepositoryEvent event)
  {
    synchronized (m_events) {
      m_events.add(event);
    }
  }

  public List<RepositoryEvent> getEvents()
  {
    synchronized (m_events) {
      return new ArrayList<RepositoryEvent>(m_events);
    }
  }

  public void clearEvents()
  {
    synchronized (m_events) {
      m_events.clear();
    }
  }
}
