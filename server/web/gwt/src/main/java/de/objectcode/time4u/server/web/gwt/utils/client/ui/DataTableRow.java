package de.objectcode.time4u.server.web.gwt.utils.client.ui;

public class DataTableRow {
	private Object userObject;
	private Object[] columns;
	private boolean selected;
	private int rowIndex;

	public DataTableRow() {
	}

	public DataTableRow(Object userObject, Object... columns) {
		this.userObject = userObject;
		this.columns = columns;
	}

	public Object getUserObject() {
		return userObject;
	}

	public void setUserObject(Object userObject) {
		this.userObject = userObject;
	}

	public Object[] getColumns() {
		return columns;
	}

	public void setColumns(Object[] columns) {
		this.columns = columns;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

}
