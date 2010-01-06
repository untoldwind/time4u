package de.objectcode.time4u.server.web.gwt.server;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import de.objectcode.time4u.server.entities.ProjectEntity;
import de.objectcode.time4u.server.web.gwt.client.Project;
import de.objectcode.time4u.server.web.gwt.client.ProjectService;
import de.objectcode.time4u.server.web.gwt.server.dao.IProjectDao;

@Controller
@RequestMapping("/MainUI/project.service")
public class ProjectServiceImpl extends GwtController implements ProjectService {

	private static final long serialVersionUID = 1L;

	IProjectDao projectDao;

	public List<Project> getRootProjects() {
		List<Project> result = projectDao.findRootProjectsDTO();
		System.out.println("Calli");

		return result;
	}

	@Autowired
	public void setProjectDao(IProjectDao projectDao) {
		this.projectDao = projectDao;
	}

}
