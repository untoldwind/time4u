package de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable;

public interface IPagedDataProvider<RowClass> {
	void updateDataPage(int pageNumber, IPagedDataViewer<RowClass> viewer);
}
