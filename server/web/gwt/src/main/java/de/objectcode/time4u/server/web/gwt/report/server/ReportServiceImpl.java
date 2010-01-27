package de.objectcode.time4u.server.web.gwt.report.server;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import de.objectcode.time4u.server.ejb.seam.api.filter.AndFilter;
import de.objectcode.time4u.server.ejb.seam.api.filter.DateRangeFilter;
import de.objectcode.time4u.server.ejb.seam.api.filter.PersonFilter;
import de.objectcode.time4u.server.ejb.seam.api.filter.ProjectPathFilter;
import de.objectcode.time4u.server.ejb.seam.api.report.DayInfoProjection;
import de.objectcode.time4u.server.ejb.seam.api.report.ProjectProjection;
import de.objectcode.time4u.server.ejb.seam.api.report.TaskProjection;
import de.objectcode.time4u.server.ejb.seam.api.report.WorkItemProjection;
import de.objectcode.time4u.server.ejb.seam.api.report.WorkItemReportDefinition;
import de.objectcode.time4u.server.ejb.seam.api.report.parameter.BaseParameterValue;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.web.gwt.login.server.Time4UUserDetailsService.Time4UUserDetails;
import de.objectcode.time4u.server.web.gwt.report.client.service.CrossTableColumnType;
import de.objectcode.time4u.server.web.gwt.report.client.service.CrossTableData;
import de.objectcode.time4u.server.web.gwt.report.client.service.CrossTableRowType;
import de.objectcode.time4u.server.web.gwt.report.client.service.ReportService;
import de.objectcode.time4u.server.web.gwt.report.client.service.ReportTableData;
import de.objectcode.time4u.server.web.gwt.report.server.dao.IInteractiveReportDao;
import de.objectcode.time4u.server.web.gwt.report.server.dao.IReportDao;
import de.objectcode.time4u.server.web.gwt.utils.server.GwtController;
import de.objectcode.time4u.server.web.gwt.webclient.server.dao.IPersonDao;

@Controller
@RequestMapping( { "/MainUI/report.service" })
public class ReportServiceImpl extends GwtController implements ReportService {

	private static final long serialVersionUID = 1L;

	IReportDao reportDao;
	IInteractiveReportDao interactiveReportDao;
	IPersonDao personDao;

	@Transactional(readOnly = true)
	@RolesAllowed("ROLE_USER")
	public ReportTableData generatePersonWorkItemReport(String personId,
			List<String> projectPath, Date from, Date until) {

		Time4UUserDetails userDetails = (Time4UUserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();

		String authPersonId = userDetails.getPersonId();

		WorkItemReportDefinition reportDefinition = createPersonWorkitemReportDefinition(
				personId, projectPath, from, until);

		return reportDao.generateReport(reportDefinition,
				new HashMap<String, BaseParameterValue>(), authPersonId);
	}

	@Transactional(readOnly = true)
	@RolesAllowed("ROLE_USER")
	public CrossTableData generateCrossTable(CrossTableColumnType columnType,
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

	private WorkItemReportDefinition createPersonWorkitemReportDefinition(
			String personId, List<String> projectPath, Date from, Date until) {

		PersonEntity person = personDao.findPerson(personId);

		final WorkItemReportDefinition definition = new WorkItemReportDefinition();

		definition.setName(person.getGivenName() + " " + person.getSurname());
		definition.setDescription("Interactive report");
		final AndFilter filters = new AndFilter();
		filters.add(new DateRangeFilter(from, until));
		filters.add(new PersonFilter(personId));

		if (!projectPath.isEmpty()) {
			final StringBuffer buffer = new StringBuffer();
			boolean first = true;

			for (final String projectId : projectPath) {
				if (!first) {
					buffer.append(":");
				}
				buffer.append(projectId);
				first = false;
			}

			filters.add(new ProjectPathFilter(buffer.toString()));
		}
		definition.setFilter(filters);
		definition.addProjection(DayInfoProjection.DATE);
		definition.addProjection(ProjectProjection.PATH);
		definition.addProjection(TaskProjection.NAME);
		definition.addProjection(WorkItemProjection.BEGIN);
		definition.addProjection(WorkItemProjection.END);
		definition.addProjection(WorkItemProjection.DURATION);
		definition.addProjection(WorkItemProjection.COMMENT);
		definition.setAggregate(true);

		return definition;
	}

	@Resource(name = "personDao")
	@Required
	public void setPersonDao(IPersonDao personDao) {
		this.personDao = personDao;
	}

	@Resource(name = "reportDao")
	@Required
	public void setReportDao(IReportDao reportDao) {
		this.reportDao = reportDao;
	}

	@Resource(name = "interactiveReportDao")
	@Required
	public void setInteractiveReportDao(
			IInteractiveReportDao interactiveReportDao) {
		this.interactiveReportDao = interactiveReportDao;
	}

}
