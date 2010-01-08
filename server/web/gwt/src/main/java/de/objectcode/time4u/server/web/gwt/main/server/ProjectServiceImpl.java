package de.objectcode.time4u.server.web.gwt.main.server;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.annotation.Secured;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import de.objectcode.time4u.server.web.gwt.main.client.Project;
import de.objectcode.time4u.server.web.gwt.main.client.ProjectService;
import de.objectcode.time4u.server.web.gwt.main.server.dao.IProjectDao;
import de.objectcode.time4u.server.web.gwt.utils.server.GwtController;

@Controller
@RequestMapping("/MainUI/project.service")
public class ProjectServiceImpl extends GwtController implements ProjectService {

	private static final long serialVersionUID = 1L;

	private IProjectDao projectDao;

	@Transactional(readOnly = true)
	@Secured( "ROLE_USER")
	public List<Project> getRootProjects() {
		if (SecurityContextHolder.getContext().getAuthentication() != null) {
			System.out.println(">>> "
					+ Arrays.toString(SecurityContextHolder.getContext()
							.getAuthentication().getAuthorities()));
		}
		List<Project> result = projectDao.findRootProjectsDTO();

		System.out.println("Call Root");

		return result;
	}

	@Transactional(readOnly = true)
	@Secured( "ROLE_USER")
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
