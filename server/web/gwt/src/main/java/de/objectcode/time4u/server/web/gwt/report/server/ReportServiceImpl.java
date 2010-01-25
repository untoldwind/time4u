package de.objectcode.time4u.server.web.gwt.report.server;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import de.objectcode.time4u.server.web.gwt.report.client.service.CrossTable;
import de.objectcode.time4u.server.web.gwt.report.client.service.ReportService;
import de.objectcode.time4u.server.web.gwt.utils.server.GwtController;

@Controller
@RequestMapping( { "/MainUI/report.service" })
public class ReportServiceImpl extends GwtController implements ReportService {

	private static final long serialVersionUID = 1L;

	public CrossTable generateProjectPersonCrossTable(String mainProjectId,
			Date from, Date until) {
		// TODO Auto-generated method stub
		return null;
	}

	public CrossTable generateProjectTeamCrossTable(String mainProjectId,
			Date from, Date until) {
		// TODO Auto-generated method stub
		return null;
	}

	public CrossTable generateTaskPersonCrossTable(String lastProjectId,
			Date from, Date until) {
		// TODO Auto-generated method stub
		return null;
	}

	public CrossTable generateTaskTeamCrossTable(String lastProjectId,
			Date from, Date until) {
		// TODO Auto-generated method stub
		return null;
	}

}
