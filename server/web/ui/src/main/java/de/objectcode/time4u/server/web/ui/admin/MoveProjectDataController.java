package de.objectcode.time4u.server.web.ui.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;

import de.objectcode.time4u.server.ejb.seam.api.IProjectServiceLocal;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.web.ui.util.ProjectTaskSelection;

@Name("admin.moveProjectDataController")
@Scope(ScopeType.CONVERSATION)
public class MoveProjectDataController
{
  public static final String VIEW_ID = "/admin/moveProjectData.xhtml";

  public static final String CONFIRM_VIEW_ID = "/admin/moveProjectDataConfirm.xhtml";

  private ProjectTaskSelection m_fromProject;
  private ProjectTaskSelection m_toProject;

  @In("ProjectService")
  IProjectServiceLocal m_projectService;

  @DataModel("admin.projectTransferDataList")
  List<PersonCount> m_projectTransferDataList;

  @Begin(join = true)
  public String enter()
  {
    m_fromProject = new ProjectTaskSelection(VIEW_ID, m_projectService);
    m_toProject = new ProjectTaskSelection(VIEW_ID, m_projectService);

    return VIEW_ID;
  }

  public ProjectTaskSelection getFromProject()
  {
    return m_fromProject;
  }

  public ProjectTaskSelection getToProject()
  {
    return m_toProject;
  }

  public String checkTransferData()
  {
    if (m_fromProject.getSelectedTaskId() != null && m_toProject.getSelectedTaskId() != null) {
      final Map<PersonEntity, Long> stat = m_projectService.checkTransferData(m_fromProject.getSelectedTaskId());
      final List<PersonCount> personCounts = new ArrayList<PersonCount>();

      for (final Map.Entry<PersonEntity, Long> entry : stat.entrySet()) {
        personCounts.add(new PersonCount(entry.getKey().getId(), entry.getKey().getGivenName(), entry.getKey()
            .getSurname(), entry.getValue()));
      }

      m_projectTransferDataList = personCounts;

      return CONFIRM_VIEW_ID;
    }
    return VIEW_ID;
  }

  public String ignorePerson(final String personId)
  {
    final Iterator<PersonCount> it = m_projectTransferDataList.iterator();

    while (it.hasNext()) {
      if (it.next().getId().equals(personId)) {
        it.remove();
        break;
      }
    }

    return CONFIRM_VIEW_ID;
  }

  public String performTransfer()
  {
    if (m_fromProject.getSelectedTaskId() != null && m_toProject.getSelectedTaskId() != null) {
      final List<String> personIds = new ArrayList<String>();

      for (final PersonCount personCount : m_projectTransferDataList) {
        personIds.add(personCount.getId());
      }

      m_projectService.transferData(personIds, m_fromProject.getSelectedTaskId(), m_toProject.getSelectedTaskId());
    }
    return VIEW_ID;
  }

  public static class PersonCount implements Serializable
  {
    private static final long serialVersionUID = -8573077028190177959L;

    String m_id;
    String m_givenName;
    String m_surname;
    long m_count;

    PersonCount(final String id, final String givenName, final String surname, final long count)
    {
      m_id = id;
      m_givenName = givenName;
      m_surname = surname;
      m_count = count;
    }

    public String getId()
    {
      return m_id;
    }

    public String getGivenName()
    {
      return m_givenName;
    }

    public String getSurname()
    {
      return m_surname;
    }

    public long getCount()
    {
      return m_count;
    }

  }
}
