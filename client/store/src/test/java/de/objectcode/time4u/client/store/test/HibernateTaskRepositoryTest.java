package de.objectcode.time4u.client.store.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import de.objectcode.time4u.client.store.api.IRepository;
import de.objectcode.time4u.client.store.api.event.RepositoryEventType;
import de.objectcode.time4u.server.api.data.ProjectSummary;
import de.objectcode.time4u.server.api.data.Task;
import de.objectcode.time4u.server.api.data.TaskSummary;
import de.objectcode.time4u.server.api.filter.ProjectFilter;
import de.objectcode.time4u.server.api.filter.TaskFilter;
import de.objectcode.time4u.server.api.filter.ProjectFilter.Order;

@Test(groups = "task", dependsOnGroups = "project")
public class HibernateTaskRepositoryTest
{
  private IRepository repository;

  @BeforeClass
  public void initialize() throws Exception
  {
    repository = HibernateTestRepositoryFactory.getInstance();
  }

  @Test(dataProvider = "tasks")
  public void testCreate(final Task task) throws Exception
  {
    assertNull(task.getId());

    repository.getTaskRepository().storeTask(task, true);

    assertNotNull(task.getId());

    final RepositoryEventCollector collector = HibernateTestRepositoryFactory
        .getEventCollector(RepositoryEventType.TASK);

    assertNotNull(collector);
    assertEquals(collector.getEvents().size(), 1);
    collector.clearEvents();

    final Task result2 = repository.getTaskRepository().getTask(task.getId());

    assertEquals(result2.getId(), task.getId());
    assertEquals(result2.getName(), task.getName());
    assertEquals(result2.getDescription(), task.getDescription());
    assertEquals(result2.getProjectId(), task.getProjectId());
  }

  @Test(dependsOnMethods = "testCreate")
  public void testFind() throws Exception
  {
    final List<TaskSummary> taskSummaries = repository.getTaskRepository().getTaskSummaries(
        new TaskFilter(null, null, null, null, null, TaskFilter.Order.ID));

    assertNotNull(taskSummaries);
    assertEquals(taskSummaries.size(), 90);

    final List<ProjectSummary> projectSummaries = repository.getProjectRepository().getProjectSumaries(
        new ProjectFilter(null, null, null, null, null, Order.ID));

    assertNotNull(projectSummaries);
    assertEquals(projectSummaries.size(), 30);

    for (final ProjectSummary project : projectSummaries) {
      final List<TaskSummary> subTaskSumaries = repository.getTaskRepository().getTaskSummaries(
          TaskFilter.filterProjectTasks(project.getId(), false));

      assertNotNull(subTaskSumaries);
      assertEquals(subTaskSumaries.size(), 3);
    }

  }

  @DataProvider(name = "tasks")
  public Object[][] getTasks() throws Exception
  {
    final List<ProjectSummary> projectSummaries = repository.getProjectRepository().getProjectSumaries(
        new ProjectFilter(null, null, null, null, null, Order.ID));

    assertNotNull(projectSummaries);
    assertEquals(projectSummaries.size(), 30);

    final Object[][] result = new Object[90][];

    for (int i = 0; i < 90; i++) {
      final Task task = new Task();
      task.setName("TestTask " + (i + 1));
      task.setDescription("DescTestTask " + (i + 1));
      task.setActive(true);
      task.setProjectId(projectSummaries.get(i % 30).getId());

      result[i] = new Object[] { task };
    }

    return result;
  }
}
