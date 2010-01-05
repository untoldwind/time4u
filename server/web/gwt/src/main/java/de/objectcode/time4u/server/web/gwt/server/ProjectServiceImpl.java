package de.objectcode.time4u.server.web.gwt.server;

import java.util.ArrayList;
import java.util.List;

import org.gwtwidgets.server.spring.GWTSpringController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import de.objectcode.time4u.server.web.gwt.client.Project;
import de.objectcode.time4u.server.web.gwt.client.ProjectService;

@Controller
@RequestMapping("/MainUI/project.service")
public class ProjectServiceImpl extends GWTSpringController implements
		ProjectService {

	private static final long serialVersionUID = 1L;

	public List<Project> getRootProjects() {
		List<Project> result = new ArrayList<Project>();
		
		result.add(new Project("1", null, "Project 1"));
		result.add(new Project("2", null, "Project 2"));
		result.add(new Project("3", null, "Project 3"));
		
		System.out.println("Calli");
		return result;
	}

}
