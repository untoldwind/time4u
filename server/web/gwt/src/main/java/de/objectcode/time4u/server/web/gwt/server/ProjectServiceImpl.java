package de.objectcode.time4u.server.web.gwt.server;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import de.objectcode.time4u.server.web.gwt.client.Project;
import de.objectcode.time4u.server.web.gwt.client.ProjectService;
import de.objectcode.time4u.server.web.gwt.server.dao.IProjectDao;

@Controller
@RequestMapping("/MainUI/project.service")
public class ProjectServiceImpl extends GwtController implements ProjectService {

	private static final long serialVersionUID = 1L;

	private IProjectDao projectDao;

	@Transactional(readOnly = true)
	public List<Project> getRootProjects() {
		List<Project> result = projectDao.findRootProjectsDTO();

		System.out.println("Call Root");

		return result;
	}

	@Transactional(readOnly = true)
	public List<Project> getChildProjects(String projectId) {
		List<Project> result = projectDao.findChildProjectsDTO(projectId);

		System.out.println("Call Child");

		return result;
	}

	@Autowired
	public void setProjectDao(IProjectDao projectDao) {
		this.projectDao = projectDao;
	}

}
