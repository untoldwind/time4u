package de.objectcode.time4u.server.web.gwt.admin.server.dao;

import de.objectcode.time4u.server.web.gwt.admin.client.service.PersonSummary;


public interface IPersonDao {
	PersonSummary.Page findPersonSummaryPage(int pageNumber, int pageSize,  PersonSummary.Projections sorting, boolean ascending);	

}
