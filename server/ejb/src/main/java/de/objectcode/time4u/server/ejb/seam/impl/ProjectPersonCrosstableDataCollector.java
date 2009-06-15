package de.objectcode.time4u.server.ejb.seam.impl;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import de.objectcode.time4u.server.ejb.seam.api.report.CrossTableResult;
import de.objectcode.time4u.server.ejb.seam.api.report.IReportDataCollector;
import de.objectcode.time4u.server.ejb.seam.api.report.IRowDataAdapter;
import de.objectcode.time4u.server.ejb.seam.api.report.ReportResult;
import de.objectcode.time4u.server.ejb.seam.api.report.ValueLabelPair;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.ProjectEntity;
import de.objectcode.time4u.server.entities.WorkItemEntity;

public class ProjectPersonCrosstableDataCollector implements IReportDataCollector
{
  private final ProjectEntity m_mainProject;
  private final SortedSet<ProjectEntity> m_sortedProjects;
  private final SortedSet<PersonEntity> m_sortedPersons;
  private final Map<String, Map<String, Integer>> m_dataMap;

  public ProjectPersonCrosstableDataCollector(final ProjectEntity mainProject)
  {
    m_mainProject = mainProject;
    m_sortedProjects = new TreeSet<ProjectEntity>(new ProjectComparator());
    m_sortedPersons = new TreeSet<PersonEntity>(new PersonComparator());

    m_dataMap = new HashMap<String, Map<String, Integer>>();
  }

  public void collect(final IRowDataAdapter rowData)
  {
    final ProjectEntity project = getParentProject(rowData.getProject());

    if (project != null) {
      final PersonEntity person = rowData.getPerson();
      final WorkItemEntity workItem = rowData.getWorkItem();

      if (m_mainProject == null || !m_mainProject.getId().equals(project.getId())) {
        m_sortedProjects.add(project);
      }
      m_sortedPersons.add(person);

      Map<String, Integer> dataSubMap = m_dataMap.get(project.getId());

      if (dataSubMap == null) {
        dataSubMap = new HashMap<String, Integer>();

        m_dataMap.put(project.getId(), dataSubMap);
      }

      final Integer sumDurations = dataSubMap.get(person.getId());

      dataSubMap.put(person.getId(), (sumDurations != null ? sumDurations.intValue() : 0) + workItem.getDuration());
    }
  }

  public void finish()
  {

  }

  public ReportResult getReportResult()
  {
    return null;
  }

  public CrossTableResult getCrossTable()
  {
    final ValueLabelPair projects[] = new ValueLabelPair[m_sortedProjects.size() + (m_mainProject != null ? 1 : 0)];
    int columnCount = 0;

    if (m_mainProject != null) {
      projects[columnCount++] = new ValueLabelPair(m_mainProject.getId(), m_mainProject.getName());
    }
    for (final ProjectEntity project : m_sortedProjects) {
      projects[columnCount++] = new ValueLabelPair(project.getId(), project.getName());
    }

    int totalAggregate = 0;
    final Object[] columnAggregates = new Object[projects.length];
    final CrossTableResult.CrossTableRow[] rows = new CrossTableResult.CrossTableRow[m_sortedPersons.size()];

    int rowCount = 0;
    for (final PersonEntity person : m_sortedPersons) {
      final ValueLabelPair rowHeader = new ValueLabelPair(person.getId(), person.getGivenName() + " "
          + person.getSurname());
      final Object[] data = new Object[projects.length];

      int rowAggregate = 0;
      for (int i = 0; i < projects.length; i++) {
        final Map<String, Integer> dataSubMap = m_dataMap.get(projects[i].getValue().toString());

        if (dataSubMap != null) {
          final Integer value = dataSubMap.get(person.getId());
          data[i] = value;
          totalAggregate += value != null ? value.intValue() : 0;
          rowAggregate += value != null ? value.intValue() : 0;
          columnAggregates[i] = (columnAggregates[i] != null ? ((Integer) columnAggregates[i]).intValue() : 0)
              + (value != null ? value.intValue() : 0);
        }
      }

      rows[rowCount] = new CrossTableResult.CrossTableRow(rowHeader, data, rowAggregate, rowCount);
      rowCount++;
    }

    return new CrossTableResult(projects, rows, columnAggregates, totalAggregate);
  }

  private ProjectEntity getParentProject(ProjectEntity project)
  {
    while (project != null) {
      if (m_mainProject == null) {
        if (project.getParent() == null) {
          return project;
        }
      } else if (m_mainProject.getId().equals(project.getId()) || project.getParent() != null
          && m_mainProject.getId().equals(project.getParent().getId())) {
        return project;
      }

      project = project.getParent();
    }

    return null;
  }

  private static class ProjectComparator implements Comparator<ProjectEntity>
  {
    public int compare(final ProjectEntity o1, final ProjectEntity o2)
    {
      final int result = o1.getName().compareTo(o2.getName());

      if (result != 0) {
        return result;
      }
      return o1.getId().compareTo(o2.getId());
    }
  }

  private static class PersonComparator implements Comparator<PersonEntity>
  {
    public int compare(final PersonEntity o1, final PersonEntity o2)
    {
      final String name1 = o1.getSurname() + " " + o1.getGivenName();
      final String name2 = o2.getSurname() + " " + o2.getGivenName();
      final int result = name1.compareTo(name2);

      if (result != 0) {
        return result;
      }

      return o1.getId().compareTo(o2.getId());
    }

  }
}
