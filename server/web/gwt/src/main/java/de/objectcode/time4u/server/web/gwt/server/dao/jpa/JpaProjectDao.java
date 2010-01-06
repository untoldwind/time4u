package de.objectcode.time4u.server.web.gwt.server.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import de.objectcode.time4u.server.entities.ProjectEntity;
import de.objectcode.time4u.server.web.gwt.client.Project;
import de.objectcode.time4u.server.web.gwt.server.dao.IProjectDao;

@Repository("projectDao")
public class JpaProjectDao extends JpaDaoBase implements IProjectDao {

	@SuppressWarnings("unchecked")
	public List<Project> findRootProjectsDTO() {
		Query query = entityManager.createQuery("from " + ProjectEntity.class.getName()
				+ " p where p.parent is null");

		List<ProjectEntity> result = query.getResultList();
		
		List<Project> ret = new ArrayList<Project>();
		
		for ( ProjectEntity row : result ) {
			Project project = new Project(row.getId(), null, row.getName());
			
			ret.add(project);
		}
		
		return ret;		
	}

	public void save(ProjectEntity project) {
		entityManager.persist(project);
	}

	public void update(ProjectEntity project) {
		entityManager.merge(project);
	}
}
