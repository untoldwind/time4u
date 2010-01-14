package de.objectcode.time4u.server.web.gwt.webclient.server.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.objectcode.time4u.server.entities.TaskEntity;
import de.objectcode.time4u.server.web.gwt.utils.server.JpaDaoBase;
import de.objectcode.time4u.server.web.gwt.webclient.client.service.Task;
import de.objectcode.time4u.server.web.gwt.webclient.server.dao.ITaskDao;

@Repository("taskDao")
@Transactional(propagation=Propagation.MANDATORY)
public class JpaTaskDao extends JpaDaoBase implements ITaskDao {

	@SuppressWarnings("unchecked")
	public List<Task> findTasksDTO(final String projectId) {
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

	static Task toDTO(TaskEntity taskEntity) {
		return new Task(taskEntity.getId(), taskEntity.getProject().getId(),
				taskEntity.getName());
	}

}
