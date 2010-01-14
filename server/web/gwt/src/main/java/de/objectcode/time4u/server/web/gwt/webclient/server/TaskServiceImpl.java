package de.objectcode.time4u.server.web.gwt.webclient.server;

import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import de.objectcode.time4u.server.web.gwt.utils.server.GwtController;
import de.objectcode.time4u.server.web.gwt.webclient.client.service.Task;
import de.objectcode.time4u.server.web.gwt.webclient.client.service.TaskService;
import de.objectcode.time4u.server.web.gwt.webclient.server.dao.ITaskDao;

@Controller
@RequestMapping({"/MainUI/task.service"})
public class TaskServiceImpl extends GwtController implements TaskService {

	private static final long serialVersionUID = 1L;

	private ITaskDao taskDao;

	@Transactional(readOnly = true)
	@RolesAllowed("ROLE_USER")
	public List<Task> getTasks(String projectId) {
		return taskDao.findTasksDTO(projectId);
	}

	@Resource(name="taskDao")
	@Required
	public void setTaskDao(ITaskDao taskDao) {
		this.taskDao = taskDao;
	}

}
