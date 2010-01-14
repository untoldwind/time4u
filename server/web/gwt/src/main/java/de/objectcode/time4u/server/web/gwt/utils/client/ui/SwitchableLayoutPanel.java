package de.objectcode.time4u.server.web.gwt.utils.client.ui;

import java.util.Iterator;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.layout.client.Layout;
import com.google.gwt.layout.client.Layout.AnimationCallback;
import com.google.gwt.layout.client.Layout.Layer;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AnimatedLayout;
import com.google.gwt.user.client.ui.LayoutCommand;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;

public class SwitchableLayoutPanel extends Panel implements AnimatedLayout,
		RequiresResize, ProvidesResize {

	private final Layout layout;
	private final LayoutCommand layoutCmd;

	private Widget child;
	private Layer layer;

	public SwitchableLayoutPanel() {
		setElement(Document.get().createDivElement());
		layout = new Layout(getElement());
		layoutCmd = new LayoutCommand(layout);
	}

	public void setChild(Widget widget) {
		if (child != null) {
			child.removeFromParent();
		}
		child = widget;
		layer = layout.attachChild(child.getElement());
		layer.setLeftRight(0, Unit.PX, 0, Unit.PX);
		layer.setTopBottom(0, Unit.PX, 0, Unit.PX);

		adopt(widget);

		animate(0);
	}

	public void animate(int duration) {
		animate(duration, null);
	}

	public void animate(int duration, AnimationCallback callback) {
		layoutCmd.schedule(duration, callback);
	}

	public void forceLayout() {
		layoutCmd.cancel();
		layout.layout();
		onResize();
	}

	public void onResize() {
		if (child != null && child instanceof RequiresResize) {
			((RequiresResize) child).onResize();
		}
	}

	@Override
	public boolean remove(Widget widget) {
		if (widget.getParent() != this) {
			return false;
		}
		// Orphan.
		try {
			orphan(widget);
		} finally {
			// Physical detach.
			Element elem = widget.getElement();
			DOM.removeChild(DOM.getParent(elem), elem);

			this.layout.removeChild(layer);
			this.layer = null;
			this.child = null;
		}

		return true;
	}

	public Iterator<Widget> iterator() {
		if (child == null)
			return new Iterator<Widget>() {
				public boolean hasNext() {
					return false;
				}

				public Widget next() {
					return null;
				}

				public void remove() {
				}
			};
		else
			return new Iterator<Widget>() {
				boolean first = true;

				public boolean hasNext() {
					return first;
				}

				public Widget next() {
					if (first) {
						first = false;
						return child;
					}
					return null;
				}

				public void remove() {
				}
			};
	}
}
