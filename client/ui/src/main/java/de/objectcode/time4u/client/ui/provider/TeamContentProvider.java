package de.objectcode.time4u.client.ui.provider;

import java.util.Collection;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.objectcode.time4u.client.store.api.ITeamRepository;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.server.api.data.TeamSummary;
import de.objectcode.time4u.server.api.filter.TeamFilter;

public class TeamContentProvider implements IStructuredContentProvider
{
  private final ITeamRepository m_teamRepository;

  public TeamContentProvider(final ITeamRepository teamRepository)
  {
    m_teamRepository = teamRepository;
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
