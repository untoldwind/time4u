package de.objectcode.time4u.client.store.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import de.objectcode.time4u.client.store.api.IRepository;
import de.objectcode.time4u.client.store.impl.hibernate.HibernateRepository;
import de.objectcode.time4u.server.api.data.Project;

@Test
public class HibernateRepositoryTest
{
  private IRepository repository;

  List<Project> rootProjects = new ArrayList<Project>();

  @Test(dataProvider = "rootProjects")
  public void testCreateRoot(final Project project) throws Exception
  {
    final Project result = repository.getProjectRepository().storeProject(project);

    assertNotNull(result);
    assertTrue(result.getId() > 0);

    final Project result2 = repository.getProjectRepository().getProject(result.getId());

    assertEquals(result2.getId(), result.getId());
    assertEquals(result2.getName(), project.getName());
    assertEquals(result2.getDescription(), project.getDescription());

    rootProjects.add(result);
  }

  @Test(dependsOnMethods = "testCreateRoot", dataProvider = "subProjects")
  public void testCreateSub(final Project project) throws Exception
  {
    final Project result = repository.getProjectRepository().storeProject(project);

    assertNotNull(result);
    assertTrue(result.getId() > 0);
    assertNotNull(result.getParentId());

    final Project result2 = repository.getProjectRepository().getProject(result.getId());

    assertEquals(result2.getId(), result.getId());
    assertEquals(result2.getName(), project.getName());
    assertEquals(result2.getDescription(), project.getDescription());
  }

  @BeforeClass
  public void initialize() throws Exception
  {
    repository = new HibernateRepository(new File("./target/test"));
  }

  @DataProvider(name = "rootProjects")
  public Object[][] getRootProjects() throws Exception
  {
    final Object[][] result = new Object[10][];

    for (int i = 0; i < 10; i++) {
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
    final Object[][] result = new Object[10 * 10][];

    for (int i = 0; i < 10 * 10; i++) {
      final Project project = new Project();
      project.setName("TestSubProject " + (i + 1));
      project.setDescription("DescTestSubProject " + (i + 1));
      project.setActive(true);
      project.setParentId(rootProjects.get(i % 10).getId());

      result[i] = new Object[] { project };
    }

    return result;
  }
}
