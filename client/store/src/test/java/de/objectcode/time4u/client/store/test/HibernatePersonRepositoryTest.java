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
import de.objectcode.time4u.server.api.data.Person;
import de.objectcode.time4u.server.api.data.PersonSummary;
import de.objectcode.time4u.server.api.filter.PersonFilter;

@Test(groups = "person")
public class HibernatePersonRepositoryTest
{
  private IRepository repository;

  @BeforeClass
  public void initialize() throws Exception
  {
    repository = HibernateTestRepositoryFactory.getInstance();
  }

  @Test
  public void testOwner() throws Exception
  {
    final Person owner = repository.getOwner();

    assertNotNull(owner);
    assertNotNull(owner.getId());
  }

  @Test(dependsOnMethods = "testOwner", dataProvider = "persons")
  public void testCreate(final Person person) throws Exception
  {
    assertNull(person.getId());

    repository.getPersonRepository().storePerson(person, true);

    assertNotNull(person.getId());

    final RepositoryEventCollector collector = HibernateTestRepositoryFactory
        .getEventCollector(RepositoryEventType.PERSON);

    assertNotNull(collector);
    assertEquals(collector.getEvents().size(), 1);
    collector.clearEvents();

    final Person result2 = repository.getPersonRepository().getPerson(person.getId());

    assertEquals(result2.getId(), person.getId());
    assertEquals(result2.getGivenName(), person.getGivenName());
    assertEquals(result2.getSurname(), person.getSurname());
  }

  @Test(dependsOnMethods = "testCreate")
  public void testFind() throws Exception
  {
    final List<PersonSummary> personSummaries = repository.getPersonRepository().getPersonSummaries(
        new PersonFilter(null, null, null, null, PersonFilter.Order.ID));

    assertNotNull(personSummaries);
    assertEquals(personSummaries.size(), 11);

    final Person owner = repository.getOwner();

    assertEquals(personSummaries.get(0).getId(), owner.getId());
  }

  @DataProvider(name = "persons")
  public Object[][] getPersons() throws Exception
  {
    final Object[][] result = new Object[10][];

    for (int i = 0; i < 10; i++) {
      final Person person = new Person();
      person.setGivenName("TestGiven " + (i + 1));
      person.setSurname("TestSur " + (i + 1));

      result[i] = new Object[] { person };
    }

    return result;
  }
}
