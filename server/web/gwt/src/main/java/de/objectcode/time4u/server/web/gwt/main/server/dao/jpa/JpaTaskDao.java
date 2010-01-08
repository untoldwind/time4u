package de.objectcode.time4u.server.web.gwt.main.server.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.springframework.orm.jpa.JpaCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.objectcode.time4u.server.entities.TaskEntity;
import de.objectcode.time4u.server.web.gwt.main.client.service.Task;
import de.objectcode.time4u.server.web.gwt.main.server.dao.ITaskDao;

@Transactional(propagation = Propagation.MANDATORY)
@Repository("taskDao")
public class JpaTaskDao extends JpaDaoBase implements ITaskDao {

	@SuppressWarnings("unchecked")
	public List<Task> findTasksDTO(final String projectId) {
		return getJpaTemplate().executeFind(new JpaCallback() {

			public Object doInJpa(EntityManager entityManager)
					throws PersistenceException {
				Query query = entityManager
						.createQuery("from "
								+ TaskEntity.class.getName()
								+ " t where t.project.id = :projectId and t.deleted = false  order by t.name asc");

				query.setParameter("projectId", projectId);
				List<TaskEntity> result = query.getResultList();

				List<Task> ret = new ArrayList<Task>();

				for (TaskEntity taskEntity : result) {
					ret.add(toDTO(taskEntity));
				}

				return ret;
			}
		});
	}

	protected Task toDTO(TaskEntity taskEntity) {
		return new Task(taskEntity.getId(), taskEntity.getProject().getId(),
				taskEntity.getName());
	}

}
