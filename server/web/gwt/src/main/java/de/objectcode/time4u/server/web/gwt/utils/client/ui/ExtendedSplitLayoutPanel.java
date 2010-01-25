package de.objectcode.time4u.server.web.gwt.utils.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class ExtendedSplitLayoutPanel extends DockLayoutPanel {
	private static final int SPLITTER_SIZE = 8;

	protected int centerMinSize = 0;

	public ExtendedSplitLayoutPanel() {
		super(Unit.PX);
		setStyleName("gwt-SplitLayoutPanel");
	}

	@Override
	public void insert(Widget child, Direction direction, double size,
			Widget before) {
		super.insert(child, direction, size, before);
		if (direction != Direction.CENTER) {
			insertSplitter(before);
		}
	}

	@Override
	public boolean remove(Widget child) {
		assert !(child instanceof Splitter) : "Splitters may not be directly removed";

		if (super.remove(child)) {
			// Remove the associated splitter, if any.
			int idx = getWidgetIndex(child);
			if (idx < getWidgetCount() - 1) {
				remove(idx + 1);
			}
			return true;
		}
		return false;
	}

	public void setCenterMinSize(int minSize) {
		centerMinSize = minSize;
	}

	public void setWidgetMinSize(Widget child, int minSize) {
		Splitter splitter = getAssociatedSplitter(child);
		splitter.setMinSize(minSize);
	}

	private Splitter getAssociatedSplitter(Widget child) {
		// If a widget has a next sibling, it must be a splitter, because the
		// only
		// widget that *isn't* followed by a splitter must be the CENTER, which
		// has
		// no associated splitter.
		int idx = getWidgetIndex(child);
		if (idx < getWidgetCount() - 2) {
			Widget splitter = getWidget(idx + 1);
			assert splitter instanceof Splitter : "Expected child widget to be splitter";
			return (Splitter) splitter;
		}
		return null;
	}

	private void insertSplitter(Widget before) {
		assert getChildren().size() > 0 : "Can't add a splitter before any children";
		assert getCenter() == null : "Can't add a splitter after the CENTER widget";

		Widget lastChild = getChildren().get(getChildren().size() - 1);
		LayoutData lastChildLayout = (LayoutData) lastChild.getLayoutData();
		Splitter splitter = null;
		switch (lastChildLayout.direction) {
		case WEST:
			splitter = new HSplitter(lastChild, false);
			break;
		case EAST:
			splitter = new HSplitter(lastChild, true);
			break;
		case NORTH:
			splitter = new VSplitter(lastChild, false);
			break;
		case SOUTH:
			splitter = new VSplitter(lastChild, true);
			break;
		default:
			assert false : "Unexpected direction";
		}

		super
				.insert(splitter, lastChildLayout.direction, SPLITTER_SIZE,
						before);
	}

	public abstract class Splitter extends Widget {
		protected final Widget target;

		private int offset;
		private boolean mouseDown;
		private Command layoutCommand;

		private final boolean reverse;
		private int minSize;

		public Splitter(Widget target, boolean reverse) {
			this.target = target;
			this.reverse = reverse;

			setElement(Document.get().createDivElement());
			sinkEvents(Event.ONMOUSEDOWN | Event.ONMOUSEUP | Event.ONMOUSEMOVE
					| Event.ONDBLCLICK);
		}

		@Override
		public void onBrowserEvent(Event event) {
			switch (event.getTypeInt()) {
			case Event.ONMOUSEDOWN:
				mouseDown = true;
				offset = getEventPosition(event) - getAbsolutePosition();
				Event.setCapture(getElement());
				event.preventDefault();
				break;

			case Event.ONMOUSEUP:
				mouseDown = false;
				Event.releaseCapture(getElement());
				event.preventDefault();
				break;

			case Event.ONMOUSEMOVE:
				if (mouseDown) {
					int size;
					if (reverse) {
						size = getTargetPosition() + getTargetSize()
								- getEventPosition(event) - offset;
					} else {
						size = getEventPosition(event) - getTargetPosition()
								- offset;
					}
					setAssociatedWidgetSize(size);
					event.preventDefault();
				}
				break;
			}
		}

		public void setMinSize(int minSize) {
			this.minSize = minSize;
			LayoutData layout = (LayoutData) target.getLayoutData();

			// Try resetting the associated widget's size, which will enforce
			// the new
			// minSize value.
			setAssociatedWidgetSize((int) layout.size);
		}

		protected abstract int getAbsolutePosition();

		protected abstract int getEventPosition(Event event);

		protected abstract int getTargetPosition();

		protected abstract int getTargetSize();

		protected abstract int getCenterSize();

		private void setAssociatedWidgetSize(int size) {
			LayoutData layout = (LayoutData) target.getLayoutData();
			if (size == layout.size) {
				return;
			}

			if (size > layout.size) {
				if (getCenterSize() - (size - layout.size) < centerMinSize)
					return;
			}

			if (size < minSize) {
				size = minSize;
			}

			layout.size = size;

			// Defer actually updating the layout, so that if we receive many
			// mouse events before layout/paint occurs, we'll only update once.
			if (layoutCommand == null) {
				layoutCommand = new Command() {
					public void execute() {
						layoutCommand = null;
						forceLayout();
					}
				};
				DeferredCommand.addCommand(layoutCommand);
			}
		}
	}

	public class HSplitter extends Splitter {
		public HSplitter(Widget target, boolean reverse) {
			super(target, reverse);
			getElement().getStyle().setPropertyPx("width", SPLITTER_SIZE);
			setStyleName("gwt-SplitLayoutPanel-HDragger");
		}

		@Override
		protected int getAbsolutePosition() {
			return getAbsoluteLeft();
		}

		@Override
		protected int getEventPosition(Event event) {
			return event.getClientX();
		}

		@Override
		protected int getTargetPosition() {
			return target.getAbsoluteLeft();
		}

		@Override
		protected int getTargetSize() {
			return target.getOffsetWidth();
		}

		@Override
		protected int getCenterSize() {
			return getCenter().getOffsetWidth();
		}
	}

	public class VSplitter extends Splitter {
		public VSplitter(Widget target, boolean reverse) {
			super(target, reverse);
			getElement().getStyle().setPropertyPx("height", SPLITTER_SIZE);
			setStyleName("gwt-SplitLayoutPanel-VDragger");
		}

		@Override
		protected int getAbsolutePosition() {
			return getAbsoluteTop();
		}

		@Override
		protected int getEventPosition(Event event) {
			return event.getClientY();
		}

		@Override
		protected int getTargetPosition() {
			return target.getAbsoluteTop();
		}

		@Override
		protected int getTargetSize() {
			return target.getOffsetHeight();
		}

		@Override
		protected int getCenterSize() {
			return getCenter().getOffsetHeight();
		}
	}

}
