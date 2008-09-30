package de.objectcode.time4u.server.api;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import de.objectcode.time4u.server.api.data.FilterResult;
import de.objectcode.time4u.server.api.data.Project;
import de.objectcode.time4u.server.api.data.ProjectSummary;
import de.objectcode.time4u.server.api.filter.ProjectFilter;

@WebService(targetNamespace = "http://objectcode.de/time4u/api/ws")
@SOAPBinding(style = Style.RPC)
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
  @WebMethod
  Project getProject(String projectId);

  /**
   * Get a project summary by its identifier.
   * 
   * @param projectId
   *          The project id
   * @return The project summary with of <tt>>projectId</tt> or <tt>null</tt>
   * @throws RepositoryException
   *           on error
   */
  @WebMethod
  ProjectSummary getProjectSummary(String projectId);

  /**
   * Get all projects that match a filter condition.
   * 
   * @param filter
   *          The filter condition
   * @return All projects matching <tt>filter</tt>
   * @throws RepositoryException
   *           on error
   */
  @WebMethod
  FilterResult<Project> getProjects(ProjectFilter filter);

  /**
   * Get summaries of all projects that match a filter condition.
   * 
   * @param filter
   *          The filter condition
   * @return All projects matching <tt>filter</tt>
   * @throws RepositoryException
   *           on error
   */
  @WebMethod
  FilterResult<ProjectSummary> getProjectSumaries(ProjectFilter filter);

  /**
   * Store a project. This method either updates an existing project or inserts a new one.
   * 
   * @param project
   *          The project to store
   * @return The stored project (including generated id for new projects)
   * @throws RepositoryException
   *           on error
   */
  @WebMethod
  Project storeProject(Project project);

}
