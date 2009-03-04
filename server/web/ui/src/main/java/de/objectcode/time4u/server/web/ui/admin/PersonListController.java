package de.objectcode.time4u.server.web.ui.admin;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.international.StatusMessages;

import de.objectcode.time4u.server.ejb.seam.api.IPersonServiceLocal;
import de.objectcode.time4u.server.entities.PersonEntity;

@Name("admin.personListController")
@Scope(ScopeType.CONVERSATION)
public class PersonListController
{
  public static final String VIEW_ID = "/admin/persons.xhtml";

  @In("PersonService")
  IPersonServiceLocal m_personService;

  PersonEntity m_selectedPerson;
  int m_currentPage;

  @Begin(join = true)
  public String enter()
  {
    return VIEW_ID;
  }

  public String select(final PersonEntity personEntity)
  {
    m_selectedPerson = personEntity;

    return VIEW_ID;
  }

  public boolean isHasSelection()
  {
    return m_selectedPerson != null;
  }

  public PersonEntity getSelectedPerson()
  {
    return m_selectedPerson;
  }

  public int getCurrentPage()
  {
    return m_currentPage;
  }

  public void setCurrentPage(final int currentPage)
  {
    m_currentPage = currentPage;
  }

  public String updatePerson()
  {
    if (m_selectedPerson != null) {
      m_personService.storePerson(m_selectedPerson);
      StatusMessages.instance().add("Person information updated");
    }
    return VIEW_ID;
  }
}
