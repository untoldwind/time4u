package de.objectcode.time4u.server.web.gwt.utils.client.ui;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.layout.client.Layout;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;

public class LoadingLayoutPanel extends Composite implements HasWidgets,
		RequiresResize, ProvidesResize {

	private LayoutPanel layoutPanel = new LayoutPanel();
	private Label loadingLabel;

	private Widget widget;

	public LoadingLayoutPanel() {
		initWidget(layoutPanel);

		setStyleName("utils-loadingPanel");
		
		loadingLabel = new Label();
		loadingLabel.setStyleName("utils-loadingLabel");
		
		layoutPanel.add(loadingLabel);
		layoutPanel.setWidgetLeftRight(loadingLabel, 0, Unit.PX, 0, Unit.PX);
		layoutPanel.setWidgetTopBottom(loadingLabel, 0, Unit.PX, 0, Unit.PX);
		
//		loadingLabel.setVisible(false);
	}

	public void onResize() {
		layoutPanel.onResize();
	}

	public Iterator<Widget> iterator() {
		return new Iterator<Widget>() {
			boolean hasElement = widget != null;
			Widget returned = null;

			public boolean hasNext() {
				return hasElement;
			}

			public Widget next() {
				if (!hasElement || (widget == null)) {
					throw new NoSuchElementException();
				}
				hasElement = false;
				return (returned = widget);
			}

			public void remove() {
				if (returned != null) {
					LoadingLayoutPanel.this.remove(returned);
				}
			}
		};
	}

	public void add(Widget w) {
		if (widget != null) {
			throw new IllegalStateException(
					"SimplePanel can only contain one child widget");
		}

		widget = w;
		layoutPanel.insert(widget, 0);
		layoutPanel.setWidgetLeftRight(widget, 0, Unit.PX, 0, Unit.PX);
		layoutPanel.setWidgetTopBottom(widget, 0, Unit.PX, 0, Unit.PX);
	}

	public void clear() {
		if ( widget != null )
			remove(widget);
		// TODO Auto-generated method stub

	}

	public boolean remove(Widget w) {
		if ( w != widget)
			return false;

		layoutPanel.remove(widget);
		widget = null;
		
		return true;
	}
}
