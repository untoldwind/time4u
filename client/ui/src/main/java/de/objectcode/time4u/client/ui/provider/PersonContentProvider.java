package de.objectcode.time4u.client.ui.provider;

import java.util.Collection;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.objectcode.time4u.client.store.api.IPersonRepository;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.server.api.data.PersonSummary;
import de.objectcode.time4u.server.api.filter.PersonFilter;

public class PersonContentProvider implements IStructuredContentProvider
{
  private final IPersonRepository m_personRepository;

  public PersonContentProvider(final IPersonRepository personRepository)
  {
    m_personRepository = personRepository;
  }

  /**
   * {@inheritDoc}
   */
  public Object[] getElements(final Object inputElement)
  {
    try {
      final Collection<PersonSummary> tasks = m_personRepository.getPersonSummaries(PersonFilter.filterAll());

      if (tasks != null) {
        return tasks.toArray();
      }
    } catch (final Exception e) {
      UIPlugin.getDefault().log(e);
    }

    return new Object[0];
  }

  /**
   * {@inheritDoc}
   */
  public void dispose()
  {
  }

  /**
   * {@inheritDoc}
   */
  public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput)
  {
  }

}
