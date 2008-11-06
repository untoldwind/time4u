package de.objectcode.time4u.client.ui.provider;

import java.util.Collection;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.objectcode.time4u.client.store.api.IPersonRepository;
import de.objectcode.time4u.client.store.api.ITeamRepository;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.server.api.data.PersonSummary;
import de.objectcode.time4u.server.api.data.TeamSummary;
import de.objectcode.time4u.server.api.filter.PersonFilter;
import de.objectcode.time4u.server.api.filter.TeamFilter;

public class TeamContentProvider implements IStructuredContentProvider, ITreeContentProvider
{
  IPersonRepository m_personRepository;
  private final ITeamRepository m_teamRepository;

  public TeamContentProvider(final IPersonRepository personRepository, final ITeamRepository teamRepository)
  {
    m_personRepository = personRepository;
    m_teamRepository = teamRepository;
  }

  public Object[] getChildren(final Object parentElement)
  {
    if (parentElement != null && parentElement instanceof TeamSummary) {
      try {
        final Collection<PersonSummary> teams = m_personRepository.getPersonSummaries(PersonFilter
            .filterMemberOf(((TeamSummary) parentElement).getId()));

        if (teams != null) {
          return teams.toArray();
        }
      } catch (final Exception e) {
        UIPlugin.getDefault().log(e);
      }

      return new Object[0];
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  public Object getParent(final Object element)
  {
    if (element != null && element instanceof PersonSummary) {
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  public boolean hasChildren(final Object element)
  {
    return element != null && element instanceof TeamSummary;
  }

  /**
   * {@inheritDoc}
   */
  public Object[] getElements(final Object inputElement)
  {
    try {
      final Collection<TeamSummary> teams = m_teamRepository.getTeamSummaries(TeamFilter.filterAll());

      if (teams != null) {
        return teams.toArray();
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
