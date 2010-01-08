package de.objectcode.time4u.server.web.gwt.main.server;

import java.util.Date;
import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import de.objectcode.time4u.server.web.gwt.login.server.Time4UUserDetailsService.Time4UUserDetails;
import de.objectcode.time4u.server.web.gwt.main.client.service.DayInfo;
import de.objectcode.time4u.server.web.gwt.main.client.service.DayInfoSummary;
import de.objectcode.time4u.server.web.gwt.main.client.service.WorkItemService;
import de.objectcode.time4u.server.web.gwt.main.server.dao.IWorkItemDao;
import de.objectcode.time4u.server.web.gwt.utils.server.GwtController;

@Controller
@RequestMapping("/MainUI/workItem.service")
public class WorkItemServiceImpl extends GwtController implements
		WorkItemService {

	private static final long serialVersionUID = 1L;

	private IWorkItemDao workItemDao;

	@Transactional(readOnly = true)
	@RolesAllowed("ROLE_USER")
	public List<DayInfoSummary> getDayInfoSummaries(Date start, Date end) {
		String personId = getPersonId();

		return workItemDao.findDayInfoDTOSummary(personId, start, end);
	}

	@Transactional(readOnly = true)
	@RolesAllowed("ROLE_USER")
	public DayInfo getDayInfo(Date day) {
		String personId = getPersonId();
		
		return workItemDao.findDayInfoDTO(personId, day);
	}

	@Autowired
	public void setWorkItemDao(IWorkItemDao workItemDao) {
		this.workItemDao = workItemDao;
	}

	private String getPersonId() {
		return ((Time4UUserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal()).getPersonId();
		
	}
}
