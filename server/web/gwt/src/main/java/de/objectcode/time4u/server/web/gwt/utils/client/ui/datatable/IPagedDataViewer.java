package de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable;

import de.objectcode.time4u.server.web.gwt.utils.client.service.IDataPage;

public interface IPagedDataViewer<RowClass> {
	void setDataPage(IDataPage<RowClass> dataPage);

	int getCurrentPage();

	boolean isCurrentSortingAscending();

	DataTableColumn<RowClass> getCurrentSortingColumn();
}
