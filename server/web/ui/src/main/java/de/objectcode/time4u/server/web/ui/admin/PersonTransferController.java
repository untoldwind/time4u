package de.objectcode.time4u.server.web.ui.admin;

import java.sql.Date;
import java.util.List;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
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
  @Out("admin.selectedPerson")
  PersonEntity m_selectedPerson;

  String m_targetPersonId;

  @DataModel("admin.transferDataConflictList")
  List<Date> m_transferDataConflictList;
  List<Date> m_transferDataList;
  int m_transferOverwriteCount;

  public String getTargetPersonId()
  {
    return m_targetPersonId;
  }

  public void setTargetPersonId(final String targetPersonId)
  {
    m_targetPersonId = targetPersonId;
  }

  public boolean isHasConficts()
  {
    if (m_transferDataConflictList != null) {
      return !m_transferDataConflictList.isEmpty();
    }
    return false;
  }

  public int getTransferDataSize()
  {
    if (m_transferDataList != null) {
      return m_transferDataList.size();
    }
    return 0;
  }

  public int getTransferOverwriteCount()
  {
    return m_transferOverwriteCount;
  }

  public String ignoreDate(final Date date)
  {
    m_transferDataConflictList.remove(date);

    return CONFLICTS_VIEW_ID;
  }

  public String overwriteDate(final Date date)
  {
    m_transferDataConflictList.remove(date);
    m_transferDataList.add(date);
    m_transferOverwriteCount++;

    return CONFLICTS_VIEW_ID;

  }

  public String transferPerson()
  {
    if (m_selectedPerson.getId().equals(m_targetPersonId)) {
      return VIEW_ID;
    }

    final DataTransferList dataTransferList = m_personService.checkTransferDataPerson(m_selectedPerson.getId(),
        m_targetPersonId);
    m_transferDataList = dataTransferList.getOkDays();
    m_transferDataConflictList = dataTransferList.getConfictDays();
    m_transferOverwriteCount = 0;

    return CONFLICTS_VIEW_ID;
  }

  public String performTransfer()
  {
    m_personService.transferDataPerson(m_selectedPerson.getId(), m_targetPersonId, m_transferDataList);

    return PersonListController.VIEW_ID;
  }
}
