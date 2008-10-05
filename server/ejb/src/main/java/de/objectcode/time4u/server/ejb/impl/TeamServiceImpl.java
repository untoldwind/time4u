package de.objectcode.time4u.server.ejb.impl;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.JndiName;
import org.jboss.seam.annotations.Name;

import de.objectcode.time4u.server.ejb.local.ITeamServiceLocal;
import de.objectcode.time4u.server.entities.TeamEntity;

@Stateless
@Local(ITeamServiceLocal.class)
@LocalBinding(jndiBinding = "time4u-server/TeamService/local")
@Name("TeamService")
@JndiName("time4u-server/TeamService/local")
@AutoCreate
public class TeamServiceImpl implements ITeamServiceLocal
{
  @PersistenceContext(unitName = "time4u")
  private EntityManager m_manager;

  @SuppressWarnings("unchecked")
  public List<TeamEntity> getTeams()
  {
    final Query query = m_manager.createQuery("from " + TeamEntity.class.getName() + " t");

    return query.getResultList();
  }

}
