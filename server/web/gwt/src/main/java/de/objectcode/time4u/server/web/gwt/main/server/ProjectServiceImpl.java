package de.objectcode.time4u.server.web.gwt.main.server;

import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import de.objectcode.time4u.server.web.gwt.main.client.service.Project;
import de.objectcode.time4u.server.web.gwt.main.client.service.ProjectService;
import de.objectcode.time4u.server.web.gwt.main.server.dao.IProjectDao;
import de.objectcode.time4u.server.web.gwt.utils.server.GwtController;

@Controller
@RequestMapping("/MainUI/project.service")
public class ProjectServiceImpl extends GwtController implements ProjectService {

	private static final long serialVersionUID = 1L;

	private IProjectDao projectDao;

	@Transactional(readOnly = true)
	@RolesAllowed("ROLE_USER")
	public List<Project> getRootProjects() {
		List<Project> result = projectDao.findRootProjectsDTO();

		return result;
	}

	@Transactional(readOnly = true)
	@RolesAllowed("ROLE_USER")
	public List<Project> getChildProjects(String projectId) {
		List<Project> result = projectDao.findChildProjectsDTO(projectId);

		return result;
	}

	@Transactional
	@RolesAllowed("ROLE_USER")
	public void storeProject(Project project) {

		projectDao.storeProjectDTO(project);
	}

	@Autowired
	public void setProjectDao(IProjectDao projectDao) {
		this.projectDao = projectDao;
	}

}
