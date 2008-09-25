package de.objectcode.time4u.server.api;

import java.util.List;

import de.objectcode.time4u.server.api.data.Project;
import de.objectcode.time4u.server.api.data.ProjectSummary;
import de.objectcode.time4u.server.api.filter.ProjectFilter;

public interface IProjectService
{
  /**
   * Get a project by its identifier.
   * 
   * @param projectId
   *          The project id
   * @return The project with id <tt>projectId</tt> or <tt>null</tt>
   * @throws RepositoryException
   *           on error
   */
  Project getProject(long projectId) throws ServiceException;

  /**
   * Get all projects that match a filter condition.
   * 
   * @param filter
   *          The filter condition
   * @return All projects matching <tt>filter</tt>
   * @throws RepositoryException
   *           on error
   */
  List<Project> getProjects(ProjectFilter filter) throws ServiceException;

  /**
   * Get summaries of all projects that match a filter condition.
   * 
   * @param filter
   *          The filter condition
   * @return All projects matching <tt>filter</tt>
   * @throws RepositoryException
   *           on error
   */
  List<ProjectSummary> getProjectSumaries(ProjectFilter filter) throws ServiceException;

  /**
   * Store a project. This method either updates an existing project or inserts a new one.
   * 
   * @param project
   *          The project to store
   * @return The stored project (including generated id for new projects)
   * @throws RepositoryException
   *           on error
   */
  Project storeProject(Project project) throws ServiceException;
}
