package de.objectcode.time4u.client.store.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.Calendar;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import de.objectcode.time4u.client.store.api.IRepository;
import de.objectcode.time4u.client.store.api.event.RepositoryEventType;
import de.objectcode.time4u.server.api.data.CalendarDay;
import de.objectcode.time4u.server.api.data.DayInfo;
import de.objectcode.time4u.server.api.data.TaskSummary;
import de.objectcode.time4u.server.api.data.WorkItem;
import de.objectcode.time4u.server.api.filter.TaskFilter;

@Test(groups = "workitem", dependsOnGroups = { "project", "task" })
public class HibernateWorkItemRepositoryTest
{
  private IRepository repository;

  @BeforeClass
  public void initialize() throws Exception
  {
    repository = HibernateTestRepositoryFactory.getInstance();
  }

  @Test(dataProvider = "workitems")
  public void testCreate(final WorkItem workItem) throws Exception
  {
    assertNull(workItem.getId());

    repository.getWorkItemRepository().storeWorkItem(workItem);

    assertNotNull(workItem.getId());

    RepositoryEventCollector collector = HibernateTestRepositoryFactory.getEventCollector(RepositoryEventType.WORKITEM);

    assertNotNull(collector);
    assertEquals(collector.getEvents().size(), 1);
    collector.clearEvents();

    collector = HibernateTestRepositoryFactory.getEventCollector(RepositoryEventType.DAYINFO);

    assertNotNull(collector);
    assertEquals(collector.getEvents().size(), 1);
    collector.clearEvents();
  }

  @Test(dependsOnMethods = "testCreate")
  public void testGetDay() throws Exception
  {
    final Calendar calendar = Calendar.getInstance();
    for (int i = 0; i < 100; i++) {
      calendar.set(1980, 0, 1, 0, 0, 0);

      final DayInfo dayInfo = repository.getWorkItemRepository().getDayInfo(new CalendarDay(calendar));

      assertTrue(dayInfo.isHasWorkItems());
      assertFalse(dayInfo.isHasInvalidWorkItems());
      assertNotNull(dayInfo.getWorkItems());
      assertFalse(dayInfo.getWorkItems().isEmpty());

      for (final WorkItem workItem : dayInfo.getWorkItems()) {
        assertTrue(workItem.isValid());
      }
      calendar.add(Calendar.DAY_OF_MONTH, 1);
    }
  }

  @Test(dependsOnMethods = "testCreate")
  public void testActiveWorkItem() throws Exception
  {
    final DayInfo dayInfo = HibernateTestRepositoryFactory.getInstance().getWorkItemRepository().getDayInfo(
        new CalendarDay(1, 1, 1980));

    assertNotNull(dayInfo);
    assertTrue(dayInfo.getWorkItems().size() > 0);

    WorkItem activeWorkItem = HibernateTestRepositoryFactory.getInstance().getWorkItemRepository().getActiveWorkItem();

    assertNull(activeWorkItem);

    HibernateTestRepositoryFactory.getInstance().getWorkItemRepository().setActiveWorkItem(
        dayInfo.getWorkItems().get(0));

    final RepositoryEventCollector collector = HibernateTestRepositoryFactory
        .getEventCollector(RepositoryEventType.ACTIVE_WORKITEM);

    assertNotNull(collector);
    assertEquals(collector.getEvents().size(), 1);
    collector.clearEvents();

    activeWorkItem = HibernateTestRepositoryFactory.getInstance().getWorkItemRepository().getActiveWorkItem();

    assertNotNull(activeWorkItem);
    assertEquals(activeWorkItem.getId(), dayInfo.getWorkItems().get(0).getId());

    HibernateTestRepositoryFactory.getInstance().getWorkItemRepository().setActiveWorkItem(null);

    assertEquals(collector.getEvents().size(), 1);
    collector.clearEvents();

    activeWorkItem = HibernateTestRepositoryFactory.getInstance().getWorkItemRepository().getActiveWorkItem();

    assertNull(activeWorkItem);
  }

  @DataProvider(name = "workitems")
  public Object[][] getWorkItems() throws Exception
  {
    final List<TaskSummary> taskSummaries = repository.getTaskRepository().getTaskSummaries(
        new TaskFilter(null, null, null, null, null, TaskFilter.Order.ID));

    assertNotNull(taskSummaries);
    assertEquals(taskSummaries.size(), 90);

    final Object[][] result = new Object[300][];

    final Calendar calendar = Calendar.getInstance();

    calendar.set(1980, 0, 1, 0, 0, 0);

    for (int i = 0; i < 300; i++) {
      final WorkItem workItem = new WorkItem();
      workItem.setBegin(8 * 3600 + 3600 * (i % 3));
      workItem.setEnd(9 * 3600 + 3600 * (i % 3));
      workItem.setDay(new CalendarDay(calendar));
      workItem.setComment("Testworkitem " + i);
      workItem.setTaskId(taskSummaries.get(i % 90).getId());
      workItem.setProjectId(taskSummaries.get(i % 90).getProjectId());

      result[i] = new Object[] { workItem };

      if (i % 3 == 0) {
        calendar.add(Calendar.DAY_OF_MONTH, 1);
      }
    }

    return result;
  }
}
