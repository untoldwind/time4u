package de.objectcode.time4u.client.ui.controls;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.client.ui.provider.TeamContentProvider;
import de.objectcode.time4u.client.ui.provider.TeamPersonLabelProvider;
import de.objectcode.time4u.server.api.data.Person;
import de.objectcode.time4u.server.api.data.TeamSummary;
import de.objectcode.time4u.server.api.data.Todo;
import de.objectcode.time4u.server.api.data.TodoGroup;

public class TodoVisibilityControl extends Composite
{
  private final CheckboxTreeViewer m_visibilityViewer;

  public TodoVisibilityControl(final Composite parent, final int style)
  {
    super(parent, style);

    final GridLayout layout = new GridLayout(1, false);
    layout.marginWidth = 0;
    layout.marginHeight = 0;
    setLayout(layout);
    m_visibilityViewer = new CheckboxTreeViewer(this, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.SINGLE
        | SWT.FULL_SELECTION | SWT.CHECK);
    m_visibilityViewer.setContentProvider(new TeamContentProvider(RepositoryFactory.getRepository()
        .getPersonRepository(), RepositoryFactory.getRepository().getTeamRepository()));
    m_visibilityViewer.setLabelProvider(new TeamPersonLabelProvider());
    m_visibilityViewer.setInput(new Object());
    m_visibilityViewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
  }

  public void setTodo(final Todo todo)
  {
    if (todo.getVisibleToPersonIds() != null) {
      checkPersons(todo.getVisibleToPersonIds());
    }
    if (todo.getVisibleToTeamIds() != null) {
      checkTeams(todo.getVisibleToTeamIds());
    }
  }

  public void setTodoGroup(final TodoGroup todoGroup)
  {
    if (todoGroup.getVisibleToPersonIds() != null) {
      checkPersons(todoGroup.getVisibleToPersonIds());
    }
    if (todoGroup.getVisibleToTeamIds() != null) {
      checkTeams(todoGroup.getVisibleToTeamIds());
    }
  }

  public void updateData(final Todo todo)
  {
    todo.setVisibleToPersonIds(new ArrayList<String>());
    todo.setVisibleToTeamIds(new ArrayList<String>());
    for (final Object checked : m_visibilityViewer.getCheckedElements()) {
      if (checked instanceof TeamSummary) {
        todo.getVisibleToTeamIds().add(((TeamSummary) checked).getId());
      } else if (checked instanceof TeamContentProvider.TeamPerson) {
        todo.getVisibleToPersonIds().add(((TeamContentProvider.TeamPerson) checked).getPerson().getId());
      }
    }
  }

  public void updateData(final TodoGroup todoGroup)
  {
    todoGroup.setVisibleToPersonIds(new ArrayList<String>());
    todoGroup.setVisibleToTeamIds(new ArrayList<String>());
    for (final Object checked : m_visibilityViewer.getCheckedElements()) {
      if (checked instanceof TeamSummary) {
        todoGroup.getVisibleToTeamIds().add(((TeamSummary) checked).getId());
      } else if (checked instanceof TeamContentProvider.TeamPerson) {
        todoGroup.getVisibleToPersonIds().add(((TeamContentProvider.TeamPerson) checked).getPerson().getId());
      }
    }
  }

  private void checkPersons(final List<String> personIds)
  {
    for (final String personId : personIds) {
      try {
        final Person person = RepositoryFactory.getRepository().getPersonRepository().getPerson(personId);

        if (person.getMemberOfTeamIds() != null) {
          for (final String teamId : person.getMemberOfTeamIds()) {
            final TeamSummary team = RepositoryFactory.getRepository().getTeamRepository().getTeamSummary(teamId);

            m_visibilityViewer.setChecked(new TeamContentProvider.TeamPerson(team, person), true);
          }
        }
      } catch (final Exception e) {
        UIPlugin.getDefault().log(e);
      }
    }
  }

  private void checkTeams(final List<String> teamIds)
  {
    for (final String teamId : teamIds) {
      try {
        m_visibilityViewer.setChecked(RepositoryFactory.getRepository().getTeamRepository().getTeamSummary(teamId),
            true);
      } catch (final Exception e) {
        UIPlugin.getDefault().log(e);
      }
    }
  }
}
