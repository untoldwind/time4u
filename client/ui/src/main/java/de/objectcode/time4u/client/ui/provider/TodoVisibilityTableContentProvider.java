package de.objectcode.time4u.client.ui.provider;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.objectcode.time4u.client.store.api.IPersonRepository;
import de.objectcode.time4u.client.store.api.ITeamRepository;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.server.api.data.Todo;

public class TodoVisibilityTableContentProvider implements IStructuredContentProvider
{
  IPersonRepository m_personRepository;
  ITeamRepository m_teamRepository;

  public TodoVisibilityTableContentProvider(final IPersonRepository personRepository, final ITeamRepository teamRepository)
  {
    m_personRepository = personRepository;
    m_teamRepository = teamRepository;
  }

  public Object[] getElements(final Object inputElement)
  {
    if (inputElement != null && inputElement instanceof Todo) {
      final Todo todo = (Todo) inputElement;

      final List<Object> result = new ArrayList<Object>();

      if (todo.getVisibleToTeamIds() != null) {
        for (final String teamId : todo.getVisibleToTeamIds()) {
          try {
            result.add(m_teamRepository.getTeamSummary(teamId));
          } catch (final Exception e) {
            UIPlugin.getDefault().log(e);
          }
        }
      }
      if (todo.getVisibleToPersonIds() != null) {
        for (final String personId : todo.getVisibleToPersonIds()) {
          try {
            result.add(m_personRepository.getPersonSummary(personId));
          } catch (final Exception e) {
            UIPlugin.getDefault().log(e);
          }
        }
      }
    }
    return new Object[0];
  }

  public void dispose()
  {
  }

  public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput)
  {
  }
}
