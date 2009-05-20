package de.objectcode.time4u.server.web.ui.user;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;

import de.objectcode.time4u.server.ejb.seam.api.filter.AndFilter;
import de.objectcode.time4u.server.ejb.seam.api.filter.DateRangeFilter;
import de.objectcode.time4u.server.ejb.seam.api.filter.PersonFilter;
import de.objectcode.time4u.server.ejb.seam.api.report.DayInfoProjection;
import de.objectcode.time4u.server.ejb.seam.api.report.PersonProjection;
import de.objectcode.time4u.server.ejb.seam.api.report.ProjectProjection;
import de.objectcode.time4u.server.ejb.seam.api.report.TaskProjection;
import de.objectcode.time4u.server.ejb.seam.api.report.TodoProjection;
import de.objectcode.time4u.server.ejb.seam.api.report.ValueLabelPair;
import de.objectcode.time4u.server.ejb.seam.api.report.WorkItemProjection;
import de.objectcode.time4u.server.ejb.seam.api.report.WorkItemReportDefinition;

public class InteractiveFilter implements Serializable
{
  private static final long serialVersionUID = 8871229265077899251L;

  private Date m_from;
  private Date m_until;
  private final LinkedList<ValueLabelPair> m_projectStack = new LinkedList<ValueLabelPair>();
  private String m_lastProjectId;
  private ValueLabelPair m_person;
  private String m_personId;

  public Date getFrom()
  {
    return m_from;
  }

  public void setFrom(final Date from)
  {
    m_from = from;
  }

  public Date getUntil()
  {
    return m_until;
  }

  public void setUntil(final Date until)
  {
    m_until = until;
  }

  public String getLastProjectId()
  {
    return m_lastProjectId;
  }

  public LinkedList<ValueLabelPair> getProjectStack()
  {
    return m_projectStack;
  }

  public void addProject(final ValueLabelPair project)
  {
    if (project.getValue().equals(m_lastProjectId)) {
      return;
    }
    m_projectStack.addLast(project);
    m_lastProjectId = project.getValue().toString();
  }

  public void setProject(final ValueLabelPair project)
  {
    while (!m_projectStack.isEmpty()) {
      if (project.getValue().equals(m_projectStack.getLast().getValue())) {
        break;
      }

      m_projectStack.removeLast();
    }
    m_lastProjectId = project.getValue().toString();
  }

  public boolean isHasPerson()
  {
    return m_person != null;
  }

  public ValueLabelPair getPerson()
  {
    return m_person;
  }

  public void setPerson(final ValueLabelPair person)
  {
    m_person = person;
    if (m_person != null) {
      m_personId = m_person.getValue().toString();
    } else {
      m_personId = null;
    }
  }

  public void clearProject()
  {
    m_projectStack.clear();
    m_lastProjectId = null;
  }

  public WorkItemReportDefinition getReportDefinition()
  {
    final WorkItemReportDefinition definition = new WorkItemReportDefinition();

    definition.setName("Interactive Report");
    definition.setDescription("Interactive report");
    definition.setFilter(new AndFilter(new DateRangeFilter(m_from, m_until), new PersonFilter(m_personId)));
    definition.addProjection(PersonProjection.NAME);
    definition.addProjection(DayInfoProjection.DATE);
    definition.addProjection(ProjectProjection.PATH);
    definition.addProjection(TaskProjection.NAME);
    definition.addProjection(WorkItemProjection.BEGIN);
    definition.addProjection(WorkItemProjection.END);
    definition.addProjection(WorkItemProjection.DURATION);
    definition.addProjection(WorkItemProjection.COMMENT);
    definition.addProjection(TodoProjection.GROUPS);
    definition.addProjection(TodoProjection.HEADER);
    definition.setAggregate(true);

    return definition;
  }
}
