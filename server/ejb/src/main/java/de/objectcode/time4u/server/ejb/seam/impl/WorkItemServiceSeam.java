package de.objectcode.time4u.server.ejb.seam.impl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.security.Identity;

import de.objectcode.time4u.server.ejb.seam.api.IWorkItemServiceLocal;
import de.objectcode.time4u.server.ejb.seam.api.WorkItemData;
import de.objectcode.time4u.server.entities.WorkItemEntity;
import de.objectcode.time4u.server.entities.account.UserAccountEntity;

@Stateless
@Local(IWorkItemServiceLocal.class)
@LocalBinding(jndiBinding = "time4u-server/seam/WorkItemServiceSeam/local")
@Name("WorkItemService")
@AutoCreate
@Scope(ScopeType.CONVERSATION)
public class WorkItemServiceSeam implements IWorkItemServiceLocal
{
  @PersistenceContext(unitName = "time4u")
  private EntityManager m_manager;

  @In("org.jboss.seam.security.identity")
  Identity m_identity;

  @Restrict("#{s:hasRole('user')}")
  public List<WorkItemData> getWorkItemData(final Date from, final Date until)
  {
    final UserAccountEntity userAccount = m_manager.find(UserAccountEntity.class, m_identity.getPrincipal().getName());

    final Query query = m_manager
        .createQuery("select w.dayInfo.date, w.begin, w.end, w.comment, w.dayInfo.person.id, w.project.id, w.project.parentKey, w.task.id from "
            + WorkItemEntity.class.getName()
            + " w where w.dayInfo.date >= :from and w.dayInfo.date < :until and w.dayInfo.person = :person");

    query.setParameter("from", from);
    query.setParameter("until", until);
    query.setParameter("person", userAccount.getPerson());

    final List<WorkItemData> result = new ArrayList<WorkItemData>();
    for (final Object row : query.getResultList()) {
      final Object[] rowData = (Object[]) row;

      result.add(new WorkItemData((Date) rowData[0], (Integer) rowData[1], (Integer) rowData[2], (String) rowData[3],
          (String) rowData[4], (String) rowData[5], ((String) rowData[6]).split(":"), (String) rowData[7]));
    }
    return result;
  }
}
