package de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable;

import java.util.Collection;

public interface IDataViewer<RowClass> {
	void setData(Collection<RowClass> data);
}
