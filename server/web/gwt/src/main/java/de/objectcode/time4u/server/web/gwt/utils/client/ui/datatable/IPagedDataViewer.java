package de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable;

import de.objectcode.time4u.server.web.gwt.utils.client.service.IDataPage;

public interface IPagedDataViewer<RowClass> {
	int getPageSize();

	void setDataPage(IDataPage<RowClass> dataPage);

	int getCurrentPage();

	boolean isCurrentSortingAscending();

	DataTableColumn<RowClass> getCurrentSortingColumn();
}
