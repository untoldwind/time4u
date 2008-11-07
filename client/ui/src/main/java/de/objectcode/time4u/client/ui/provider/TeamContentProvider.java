package de.objectcode.time4u.client.ui.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

  /**
   * {@inheritDoc}
   */
  public Object[] getChildren(final Object parentElement)
  {
    if (parentElement != null && parentElement instanceof TeamSummary) {
      try {
        final Collection<PersonSummary> persons = m_personRepository.getPersonSummaries(PersonFilter
            .filterMemberOf(((TeamSummary) parentElement).getId()));

        if (persons != null) {
          final List<TeamPerson> result = new ArrayList<TeamPerson>();

          for (final PersonSummary person : persons) {
            result.add(new TeamPerson((TeamSummary) parentElement, person));
          }
          return result.toArray();
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
    if (element != null && element instanceof TeamPerson) {
      return ((TeamPerson) element).getTeam();
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

  public static class TeamPerson
  {
    private final TeamSummary m_team;
    private final PersonSummary m_person;

    public TeamPerson(final TeamSummary team, final PersonSummary person)
    {
      m_team = team;
      m_person = person;
    }

    public TeamSummary getTeam()
    {
      return m_team;
    }

    public PersonSummary getPerson()
    {
      return m_person;
    }

    @Override
    public boolean equals(final Object obj)
    {
      if (obj == this) {
        return true;
      }

      if (obj == null || !(obj instanceof TeamPerson)) {
        return false;
      }

      final TeamPerson castObj = (TeamPerson) obj;

      return m_person.equals(castObj.m_person) && m_team.equals(castObj.m_team);
    }

    @Override
    public int hashCode()
    {
      return m_person.hashCode() * 13 + m_team.hashCode();
    }

  }
}
