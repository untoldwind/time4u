package de.objectcode.time4u.client.store.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import de.objectcode.time4u.client.store.api.IRepository;
import de.objectcode.time4u.client.store.api.event.RepositoryEventType;
import de.objectcode.time4u.server.api.data.Project;
import de.objectcode.time4u.server.api.data.ProjectSummary;
import de.objectcode.time4u.server.api.filter.ProjectFilter;
import de.objectcode.time4u.server.api.filter.ProjectFilter.Order;

@Test(groups = "project")
public class HibernateProjectRepositoryTest
{
  private IRepository repository;

  List<Project> rootProjects = new ArrayList<Project>();

  @Test(dataProvider = "rootProjects")
  public void testCreateRoot(final Project project) throws Exception
  {
    assertNull(project.getId());

    repository.getProjectRepository().storeProject(project, true);

    assertNotNull(project.getId());

    final RepositoryEventCollector collector = HibernateTestRepositoryFactory
        .getEventCollector(RepositoryEventType.PROJECT);

    assertNotNull(collector);
    assertEquals(collector.getEvents().size(), 1);
    collector.clearEvents();

    final Project result2 = repository.getProjectRepository().getProject(project.getId());

    assertEquals(result2.getId(), project.getId());
    assertEquals(result2.getName(), project.getName());
    assertEquals(result2.getDescription(), project.getDescription());

    rootProjects.add(project);
  }

  @Test(dependsOnMethods = "testCreateRoot", dataProvider = "subProjects")
  public void testCreateSub(final Project project) throws Exception
  {
    assertNull(project.getId());

    repository.getProjectRepository().storeProject(project, true);

    assertNotNull(project.getId());
    assertNotNull(project.getParentId());

    final RepositoryEventCollector collector = HibernateTestRepositoryFactory
        .getEventCollector(RepositoryEventType.PROJECT);

    assertNotNull(collector);
    assertEquals(collector.getEvents().size(), 1);
    collector.clearEvents();

    final Project result2 = repository.getProjectRepository().getProject(project.getId());

    assertEquals(result2.getId(), project.getId());
    assertEquals(result2.getName(), project.getName());
    assertEquals(result2.getDescription(), project.getDescription());
  }

  @Test(dependsOnMethods = "testCreateSub")
  public void testFind() throws Exception
  {
    List<ProjectSummary> projectSummaries = repository.getProjectRepository().getProjectSumaries(
        new ProjectFilter(null, null, null, null, null, Order.ID));

    assertNotNull(projectSummaries);
    assertEquals(projectSummaries.size(), 30);

    projectSummaries = repository.getProjectRepository().getProjectSumaries(ProjectFilter.filterRootProjects(false));

    assertNotNull(projectSummaries);
    assertEquals(projectSummaries.size(), 5);

    for (final ProjectSummary project : projectSummaries) {
      final List<ProjectSummary> subProjectSumaries = repository.getProjectRepository().getProjectSumaries(
          ProjectFilter.filterChildProjects(project.getId(), false));

      assertNotNull(subProjectSumaries);
      assertEquals(subProjectSumaries.size(), 5);
    }
  }

  @BeforeClass
  public void initialize() throws Exception
  {
    repository = HibernateTestRepositoryFactory.getInstance();
  }

  @DataProvider(name = "rootProjects")
  public Object[][] getRootProjects() throws Exception
  {
    final Object[][] result = new Object[5][];

    for (int i = 0; i < 5; i++) {
      final Project project = new Project();
      project.setName("TestProject " + (i + 1));
      project.setDescription("DescTestProject " + (i + 1));
      project.setActive(true);

      result[i] = new Object[] { project };
    }

    return result;
  }

  @DataProvider(name = "subProjects")
  public Object[][] getSubProjects() throws Exception
  {
    final Object[][] result = new Object[5 * 5][];

    for (int i = 0; i < 5 * 5; i++) {
      final Project project = new Project();
      project.setName("TestSubProject " + (i + 1));
      project.setDescription("DescTestSubProject " + (i + 1));
      project.setActive(true);
      project.setParentId(rootProjects.get(i % 5).getId());

      result[i] = new Object[] { project };
    }

    return result;
  }
}
