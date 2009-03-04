package de.objectcode.time4u.client.store.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import de.objectcode.time4u.client.store.api.IRepository;
import de.objectcode.time4u.client.store.api.event.RepositoryEventType;
import de.objectcode.time4u.server.api.data.Person;
import de.objectcode.time4u.server.api.data.PersonSummary;
import de.objectcode.time4u.server.api.data.TaskSummary;
import de.objectcode.time4u.server.api.data.Todo;
import de.objectcode.time4u.server.api.data.TodoAssignment;
import de.objectcode.time4u.server.api.data.TodoState;
import de.objectcode.time4u.server.api.data.TodoSummary;
import de.objectcode.time4u.server.api.filter.PersonFilter;
import de.objectcode.time4u.server.api.filter.TaskFilter;
import de.objectcode.time4u.server.api.filter.TodoFilter;

@Test(groups = "todo", dependsOnGroups = { "project", "task", "person" })
public class HibernateTodoRepositoryTest
{
  private IRepository repository;

  @BeforeClass
  public void initialize() throws Exception
  {
    repository = HibernateTestRepositoryFactory.getInstance();
  }

  @Test(dataProvider = "todos")
  public void testCreate(final Todo todo) throws Exception
  {
    assertNull(todo.getId());

    repository.getTodoRepository().storeTodo(todo, true);

    assertNotNull(todo.getId());

    final RepositoryEventCollector collector = HibernateTestRepositoryFactory
        .getEventCollector(RepositoryEventType.TODO);

    assertNotNull(collector);
    assertEquals(collector.getEvents().size(), 1);
    collector.clearEvents();

    final Todo result2 = repository.getTodoRepository().getTodo(todo.getId());

    assertEquals(result2.getId(), todo.getId());
    assertEquals(result2.getHeader(), todo.getHeader());
    assertEquals(result2.getDescription(), todo.getDescription());
    assertEquals(result2.getTaskId(), todo.getTaskId());
  }

  @Test(dependsOnMethods = "testCreate")
  public void testFind1() throws Exception
  {
    final List<TodoSummary> todoSummaries = repository.getTodoRepository().getTodoSummaries(
        new TodoFilter(null, null, null, null, TodoFilter.Order.ID), true);

    assertNotNull(todoSummaries);
    assertEquals(todoSummaries.size(), 90);

    final List<TodoSummary> todos = repository.getTodoRepository().getTodos(
        new TodoFilter(null, null, null, null, TodoFilter.Order.ID), true);

    assertNotNull(todos);
    assertEquals(todos.size(), 90);

    for (final TodoSummary todoSummary : todos) {
      assertTrue(todoSummary instanceof Todo);

      final Todo todo = (Todo) todoSummary;

      assertEquals(todo.getAssignments().size(), 0);
    }
  }

  @Test(dependsOnMethods = "testFind1")
  public void testAssign() throws Exception
  {
    final List<TodoSummary> todos = repository.getTodoRepository().getTodos(
        new TodoFilter(null, null, null, null, TodoFilter.Order.ID), true);

    assertNotNull(todos);
    assertEquals(todos.size(), 90);

    final Person owner = repository.getOwner();

    assertNotNull(owner);
    for (int i = 10; i < 20; i++) {
      final Todo todo = (Todo) todos.get(i);
      final TodoAssignment todoAssignment = new TodoAssignment();

      todoAssignment.setPersonId(owner.getId());
      todoAssignment.setEstimatedTime(8);

      todo.getAssignments().add(todoAssignment);
      todo.setState(TodoState.ASSIGNED_INPROGRESS);

      repository.getTodoRepository().storeTodo(todo, true);
    }

    final List<PersonSummary> personSummaries = repository.getPersonRepository().getPersonSummaries(
        new PersonFilter(null, null, null, null, PersonFilter.Order.ID));

    assertNotNull(personSummaries);
    assertTrue(personSummaries.size() > 1);

    for (int i = 20; i < 90; i++) {
      final Todo todo = (Todo) todos.get(i);
      final TodoAssignment todoAssignment = new TodoAssignment();

      todoAssignment.setPersonId(personSummaries.get(i % (personSummaries.size() - 1) + 1).getId());
      assertFalse(todoAssignment.getPersonId().equals(owner.getId()));
      todoAssignment.setEstimatedTime(8);

      todo.getAssignments().add(todoAssignment);
      todo.setState(TodoState.ASSIGNED_OPEN);

      repository.getTodoRepository().storeTodo(todo, true);
    }
  }

  @Test(dependsOnMethods = "testAssign")
  public void testFind2() throws Exception
  {
    List<TodoSummary> todoSummaries = repository.getTodoRepository().getTodoSummaries(
        new TodoFilter(null, null, null, null, TodoFilter.Order.ID), true);

    assertNotNull(todoSummaries);
    assertEquals(todoSummaries.size(), 90);

    final TodoFilter filter = new TodoFilter(null, null, null, null, TodoFilter.Order.ID);

    filter.setTodoStates(new TodoState[] { TodoState.UNASSIGNED });

    todoSummaries = repository.getTodoRepository().getTodoSummaries(filter, true);

    assertNotNull(todoSummaries);
    assertEquals(todoSummaries.size(), 10);

    filter.setTodoStates(new TodoState[] { TodoState.ASSIGNED_INPROGRESS, TodoState.ASSIGNED_OPEN });

    todoSummaries = repository.getTodoRepository().getTodoSummaries(filter, true);

    assertNotNull(todoSummaries);
    assertEquals(todoSummaries.size(), 80);

    filter.setTodoStates(null);

    final Person owner = repository.getOwner();

    filter.setAssignmentFilter(new TodoFilter.AssignmentFilter(false, false, false, owner.getId()));

    todoSummaries = repository.getTodoRepository().getTodoSummaries(filter, true);

    assertNotNull(todoSummaries);
    assertEquals(todoSummaries.size(), 0);

    filter.setAssignmentFilter(new TodoFilter.AssignmentFilter(true, false, false, owner.getId()));

    todoSummaries = repository.getTodoRepository().getTodoSummaries(filter, true);

    assertNotNull(todoSummaries);
    assertEquals(todoSummaries.size(), 10);

    filter.setAssignmentFilter(new TodoFilter.AssignmentFilter(true, true, false, owner.getId()));

    todoSummaries = repository.getTodoRepository().getTodoSummaries(filter, true);

    assertNotNull(todoSummaries);
    assertEquals(todoSummaries.size(), 20);

    filter.setAssignmentFilter(new TodoFilter.AssignmentFilter(false, false, true, owner.getId()));

    todoSummaries = repository.getTodoRepository().getTodoSummaries(filter, true);

    assertNotNull(todoSummaries);
    assertEquals(todoSummaries.size(), 70);
  }

  @DataProvider(name = "todos")
  public Object[][] getTodos() throws Exception
  {
    final List<TaskSummary> taskSummaries = repository.getTaskRepository().getTaskSummaries(
        new TaskFilter(null, null, null, null, null, TaskFilter.Order.ID));

    assertNotNull(taskSummaries);
    assertEquals(taskSummaries.size(), 90);

    final Object[][] result = new Object[90][];

    for (int i = 0; i < 90; i++) {
      final Todo todo = new Todo();

      todo.setHeader("TestTodo " + (i + 1));
      todo.setDescription("DescTestTodo " + (i + 1));
      todo.setState(TodoState.UNASSIGNED);
      todo.setTaskId(taskSummaries.get(i).getId());
      todo.setReporterId(repository.getOwner().getId());

      result[i] = new Object[] { todo };
    }

    return result;
  }
}
