package de.objectcode.time4u.server.web.ui.admin;

import java.sql.Date;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;

import de.objectcode.time4u.server.ejb.seam.api.DataTransferList;
import de.objectcode.time4u.server.ejb.seam.api.IPersonServiceLocal;
import de.objectcode.time4u.server.entities.PersonEntity;

@Name("admin.personTransferController")
@Scope(ScopeType.CONVERSATION)
public class PersonTransferController
{
  public static final String VIEW_ID = "/admin/transferPerson.xhtml";

  public static final String CONFLICTS_VIEW_ID = "/admin/transferConficts.xhtml";

  @In("PersonService")
  IPersonServiceLocal m_personService;

  @In("admin.selectedPerson")
  PersonEntity m_selectedPerson;

  String m_targetPersonId;
  DataTransferList m_dataTransferList;

  @DataModel("admin.transferDataConflictList")
  List<Date> m_transferDataConflictList;

  public String getTargetPersonId()
  {
    return m_targetPersonId;
  }

  public void setTargetPersonId(final String targetPersonId)
  {
    m_targetPersonId = targetPersonId;
  }

  public DataTransferList getDataTransferList()
  {
    return m_dataTransferList;
  }

  public void setDataTransferList(final DataTransferList dataTransferList)
  {
    m_dataTransferList = dataTransferList;
  }

  public String transferPerson()
  {
    m_dataTransferList = m_personService.checkTransferDataPerson(m_selectedPerson.getId(), m_targetPersonId);
    m_transferDataConflictList = m_dataTransferList.getConfictDays();

    return CONFLICTS_VIEW_ID;
  }
}
