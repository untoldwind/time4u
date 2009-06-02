package de.objectcode.time4u.server.web.ui.admin;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.international.StatusMessages;

import de.objectcode.time4u.server.ejb.seam.api.IPersonServiceLocal;
import de.objectcode.time4u.server.ejb.seam.api.PersonStatisticData;
import de.objectcode.time4u.server.entities.PersonEntity;

@Name("admin.personListController")
@Scope(ScopeType.CONVERSATION)
public class PersonListController
{
  public static final String VIEW_ID = "/admin/persons.xhtml";

  public static final String DELETE_VIEW_ID = "/admin/deletePerson.xhtml";

  @In("PersonService")
  IPersonServiceLocal m_personService;

  @Out(value = "admin.selectedPerson", required = false)
  PersonEntity m_selectedPerson;
  PersonStatisticData m_selectedPersonStatistic;
  int m_currentPage;

  @Begin(join = true)
  public String enter()
  {
    return VIEW_ID;
  }

  public String select(final PersonEntity personEntity)
  {
    m_selectedPerson = personEntity;
    m_selectedPersonStatistic = null;

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

  public PersonStatisticData getSelectedPersonStatistic()
  {
    if (m_selectedPersonStatistic != null) {
      return m_selectedPersonStatistic;
    }
    if (m_selectedPerson == null) {
      return null;
    }
    m_selectedPersonStatistic = m_personService.getPersonStatistics(m_selectedPerson.getId());
    return m_selectedPersonStatistic;
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

  public String confirmDeletePerson()
  {
    return DELETE_VIEW_ID;
  }

  public String deletePerson()
  {
    if (m_selectedPerson != null) {
      m_personService.deletePerson(m_selectedPerson.getId());
      StatusMessages.instance().add("Person information deleted");
      m_selectedPerson = null;
      m_selectedPersonStatistic = null;
    }
    return VIEW_ID;
  }

  public String transferPerson()
  {
    if (m_selectedPerson != null) {
      return PersonTransferController.VIEW_ID;
    }
    return VIEW_ID;
  }
}
