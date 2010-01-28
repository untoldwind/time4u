package de.objectcode.time4u.server.web.gwt.webclient.server.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.entities.ProjectEntity;
import de.objectcode.time4u.server.entities.revision.ILocalIdGenerator;
import de.objectcode.time4u.server.entities.revision.IRevisionGenerator;
import de.objectcode.time4u.server.entities.revision.IRevisionLock;
import de.objectcode.time4u.server.web.gwt.utils.server.JpaDaoBase;
import de.objectcode.time4u.server.web.gwt.webclient.client.service.Project;
import de.objectcode.time4u.server.web.gwt.webclient.server.dao.IProjectDao;

@Repository("projectDao")
@Transactional(propagation = Propagation.MANDATORY)
public class JpaProjectDao extends JpaDaoBase implements IProjectDao {

	private IRevisionGenerator revisionGenerator;
	private ILocalIdGenerator localIdGenerator;

	@SuppressWarnings("unchecked")
	public List<Project> findRootProjectsDTO() {
		Query query = entityManager
				.createQuery("select p, (select count(*) from "
						+ ProjectEntity.class.getName()
						+ " sp where sp.parent.id = p.id and sp.deleted = false) from "
						+ ProjectEntity.class.getName()
						+ " p where p.parent is null and p.deleted = false order by p.name asc");

		List<Object[]> result = query.getResultList();

		List<Project> ret = new ArrayList<Project>();

		for (Object[] row : result) {
			Project project = toDTO((ProjectEntity) row[0], (Long) row[1]);

			ret.add(project);
		}

		return ret;
	}

	@SuppressWarnings("unchecked")
	public List<Project> findChildProjectsDTO(final String projectId) {
		Query query = entityManager
				.createQuery("select p, (select count(*) from "
						+ ProjectEntity.class.getName()
						+ " sp where sp.parent.id = p.id and sp.deleted = false) from "
						+ ProjectEntity.class.getName()
						+ " p where p.parent.id = :parentId and p.deleted = false  order by p.name asc");

		query.setParameter("parentId", projectId);

		List<Object[]> result = query.getResultList();

		List<Project> ret = new ArrayList<Project>();

		for (Object[] row : result) {
			Project project = toDTO((ProjectEntity) row[0], (Long) row[1]);

			ret.add(project);
		}

		return ret;
	}

	public void storeProjectDTO(Project project) {
		final IRevisionLock revisionLock = revisionGenerator.getNextRevision(
				EntityType.PROJECT, null);

		ProjectEntity projectEntity = null;

		if (project.getId() != null) {
			projectEntity = entityManager.find(ProjectEntity.class, project
					.getId());
		} else {
			project.setId(localIdGenerator.generateLocalId(EntityType.PROJECT));
		}

		if (projectEntity != null) {
			projectEntity.setName(project.getName());
			projectEntity.setActive(project.isActive());
			projectEntity.setParent(project.getParentId() != null ? entityManager.find(ProjectEntity.class, project.getParentId()) : null);
			projectEntity.setRevision(revisionLock.getLatestRevision());
			projectEntity.updateParentKey();
		} else {
			projectEntity = new ProjectEntity(project.getId(), revisionLock
					.getLatestRevision(), localIdGenerator.getClientId(),
					project.getName());

			projectEntity.setName(project.getName());
			projectEntity.setActive(project.isActive());
			projectEntity.setParent(project.getParentId() != null ? entityManager.find(ProjectEntity.class, project.getParentId()) : null);
			projectEntity.updateParentKey();

			entityManager.persist(projectEntity);
		}
	}

	public void save(ProjectEntity project) {
		entityManager.persist(project);
	}

	public void update(ProjectEntity project) {
		entityManager.merge(project);
	}

	static Project toDTO(ProjectEntity projectEntity, long subProjectCount) {
		return new Project(projectEntity.getId(),
				projectEntity.getParent() != null ? projectEntity.getParent()
						.getId() : null, projectEntity.getName(), projectEntity
						.isActive(), subProjectCount > 0);
	}

	@Resource(name="revisionGenerator")
	@Required
	public void setRevisionGenerator(IRevisionGenerator revisionGenerator) {
		this.revisionGenerator = revisionGenerator;
	}

	@Resource(name="localIdGenerator")
	@Required
	public void setLocalIdGenerator(ILocalIdGenerator localIdGenerator) {
		this.localIdGenerator = localIdGenerator;
	}

}
