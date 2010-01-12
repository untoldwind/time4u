package de.objectcode.time4u.server.web.gwt.utils.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class ExtendedFlexTable extends FlexTable {

	private Element tHeadElement;
	private List<Widget> headerWidgets;

	public ExtendedFlexTable() {
	}

	public void setHeaderStyleName(String styleName) {
		UIObject.setStyleName(tHeadElement.getFirstChildElement(), styleName);
	}

	public void setHeaders(TableHeader... headers) {
		for (int i = 0; i < headers.length; i++) {
			TableHeader column = headers[i];

			setHeaderWidget(i, column.getWidth(), new Label(column.getText()));
		}

	}

	public void setHeaderWidget(int column, String width, Widget widget) {
		prepareHeaderCell(column);

		if (widget != null) {
			widget.removeFromParent();

			Element th = DOM.getChild(DOM.getFirstChild(getTHeadElement()),
					column);
			internalClearCell(th, true);

			// Physical attach.
			DOM.appendChild(th, widget.getElement());

			if (width != null)
				DOM.setStyleAttribute(th, "width", width);

			List<Widget> headerWidgets = getHeaderWidgets();
			if (headerWidgets.size() > column
					&& headerWidgets.get(column) != null)
				headerWidgets.set(column, widget);
			else
				headerWidgets.add(column, widget);

			adopt(widget);
		}
	}

	protected void prepareHeaderCell(int column) {
		if (column < 0) {
			throw new IndexOutOfBoundsException(
					"Cannot create a column with a negative index: " + column);
		}

		List<Widget> headerWidgets = getHeaderWidgets();
		if (headerWidgets.size() <= column || headerWidgets.get(column) == null) {
			int required = column + 1
					- DOM.getChildCount(DOM.getChild(getTHeadElement(), 0));
			if (required > 0)
				addHeaderCells(getTHeadElement(), required);
		}
	}

	protected List<Widget> getHeaderWidgets() {
		if (headerWidgets == null)
			headerWidgets = new ArrayList<Widget>();
		return headerWidgets;
	}

	protected Element getTHeadElement() {
		if (tHeadElement == null) {
			tHeadElement = DOM.createTHead();
			tHeadElement = DOM.createElement("thead");
			DOM.insertChild(getElement(), getTHeadElement(), 0);
			Element tr = DOM.createTR();
			DOM.insertChild(getTHeadElement(), tr, 0);
		}
		return tHeadElement;
	}

	protected native void addHeaderCells(Element tHead, int num)/*-{
		var rowElem = tHead.rows[0];
		for(var i = 0; i < num; i++){
		  var cell = $doc.createElement("th");
		  rowElem.appendChild(cell);
		}
	}-*/;

}
