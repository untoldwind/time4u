package de.objectcode.time4u.server.web.ui.user;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;

import de.objectcode.time4u.server.ejb.seam.api.report.ValueLabelPair;

public class InteractiveFilter implements Serializable
{
  private static final long serialVersionUID = 8871229265077899251L;

  private Date m_from;
  private Date m_until;
  private final LinkedList<ValueLabelPair> m_projectStack = new LinkedList<ValueLabelPair>();
  private String m_lastProjectId;

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

  public void clearProject()
  {
    m_projectStack.clear();
    m_lastProjectId = null;
  }
}
