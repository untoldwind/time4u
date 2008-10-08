package de.objectcode.time4u.server.ejb.seam.impl;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Observer;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;

import de.objectcode.time4u.server.ejb.seam.api.IPersonServiceLocal;
import de.objectcode.time4u.server.entities.PersonEntity;

@Stateless
@Local(IPersonServiceLocal.class)
@LocalBinding(jndiBinding = "time4u-server/seam/PersonServiceSeam/local")
@Name("PersonService")
@AutoCreate
@Scope(ScopeType.CONVERSATION)
public class PersonServiceSeam implements IPersonServiceLocal
{
  @PersistenceContext(unitName = "time4u")
  private EntityManager m_manager;

  @DataModel("admin.personList")
  List<PersonEntity> m_persons;

  @SuppressWarnings("unchecked")
  @RolesAllowed("user")
  @Factory("admin.personList")
  @Observer("admin.personList.updated")
  public void initPersons()
  {
    final Query query = m_manager.createQuery("from " + PersonEntity.class.getName() + " t");

    m_persons = query.getResultList();
  }

  public PersonEntity getPerson(final String id)
  {
    return m_manager.find(PersonEntity.class, id);
  }

  @SuppressWarnings("unchecked")
  public List<PersonEntity> getPersons()
  {
    final Query query = m_manager.createQuery("from " + PersonEntity.class.getName() + " t");

    return query.getResultList();
  }

}
