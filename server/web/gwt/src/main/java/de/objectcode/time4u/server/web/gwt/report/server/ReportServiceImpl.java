package de.objectcode.time4u.server.web.gwt.report.server;

import java.util.Date;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import de.objectcode.time4u.server.web.gwt.login.server.Time4UUserDetailsService.Time4UUserDetails;
import de.objectcode.time4u.server.web.gwt.report.client.service.CrossTable;
import de.objectcode.time4u.server.web.gwt.report.client.service.CrossTableColumnType;
import de.objectcode.time4u.server.web.gwt.report.client.service.CrossTableRowType;
import de.objectcode.time4u.server.web.gwt.report.client.service.ReportService;
import de.objectcode.time4u.server.web.gwt.report.server.dao.IInteractiveReportDao;
import de.objectcode.time4u.server.web.gwt.utils.server.GwtController;

@Controller
@RequestMapping( { "/MainUI/report.service" })
public class ReportServiceImpl extends GwtController implements ReportService {

	private static final long serialVersionUID = 1L;

	IInteractiveReportDao interactiveReportDao;

	@Transactional(readOnly = true)
	@RolesAllowed("ROLE_USER")
	public CrossTable generateCrossTable(CrossTableColumnType columnType,
			CrossTableRowType rowType, String projectId, Date from, Date until) {
		Time4UUserDetails userDetails = (Time4UUserDetails) SecurityContextHolder
		.getContext().getAuthentication().getPrincipal();

		String personId = userDetails.getPersonId();

		switch (columnType) {
		case PROJECT:
			switch (rowType) {
			case PERSON:
				return interactiveReportDao.generateProjectPersonCrossTable(
						projectId, from, until, personId);
			case TEAM:
				return interactiveReportDao.generateProjectTeamCrossTable(
						projectId, from, until, personId);
			}
		case TASK:
			switch (rowType) {
			case PERSON:
				return interactiveReportDao.generateTaskPersonCrossTable(
						projectId, from, until, personId);
			case TEAM:
				return interactiveReportDao.generateTaskTeamCrossTable(
						projectId, from, until, personId);
			}
		}

		throw new RuntimeException("Unkown type: " + columnType + " " + rowType);
	}

	@Resource(name = "interactiveReportDao")
	@Required
	public void setInteractiveReportDao(
			IInteractiveReportDao interactiveReportDao) {
		this.interactiveReportDao = interactiveReportDao;
	}

}
