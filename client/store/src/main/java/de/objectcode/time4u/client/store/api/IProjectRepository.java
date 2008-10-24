package de.objectcode.time4u.client.store.api;

import java.util.List;

import de.objectcode.time4u.server.api.data.Project;
import de.objectcode.time4u.server.api.data.ProjectSummary;
import de.objectcode.time4u.server.api.filter.ProjectFilter;

/**
 * Interface to the client side project repository.
 * 
 * @author junglas
 */
public interface IProjectRepository
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
  Project getProject(String projectId) throws RepositoryException;

  /**
   * Get a project summary by its identifier.
   * 
   * @param projectId
   *          The project id
   * @return The project summary with of <tt>>projectId</tt> or <tt>null</tt>
   * @throws RepositoryException
   *           on error
   */
  ProjectSummary getProjectSummary(String projectId) throws RepositoryException;

  /**
   * Get all projects that match a filter condition.
   * 
   * @param filter
   *          The filter condition
   * @return All projects matching <tt>filter</tt>
   * @throws RepositoryException
   *           on error
   */
  List<Project> getProjects(ProjectFilter filter) throws RepositoryException;

  /**
   * Get summaries of all projects that match a filter condition.
   * 
   * @param filter
   *          The filter condition
   * @return All projects matching <tt>filter</tt>
   * @throws RepositoryException
   *           on error
   */
  List<ProjectSummary> getProjectSumaries(ProjectFilter filter) throws RepositoryException;

  /**
   * Store a project. This method either updates an existing project or inserts a new one.
   * 
   * @param project
   *          The project to store
   * @param modifiedByOwner
   *          <tt>true</tt> If the modification is done by the repository owner (in UI this should always be
   *          <tt>true</tt>)
   * @return The stored project (including generated id for new projects)
   * @throws RepositoryException
   *           on error
   */
  void storeProject(Project project, boolean modifiedByOwner) throws RepositoryException;

  /**
   * Delete a project. Only the deleted flag of the project is set for the project and all its sub projects and tasks.
   * 
   * @param project
   *          The project to be deleted
   * @throws RepositoryException
   *           on error
   */
  void deleteProject(Project project) throws RepositoryException;

  List<ProjectSummary> getProjectPath(String projectId) throws RepositoryException;
}
