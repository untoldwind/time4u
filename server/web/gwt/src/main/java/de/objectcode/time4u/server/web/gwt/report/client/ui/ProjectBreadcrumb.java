package de.objectcode.time4u.server.web.gwt.report.client.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasValue;

import de.objectcode.time4u.server.web.gwt.report.client.service.IdLabelPair;

public class ProjectBreadcrumb extends Composite implements
		HasValue<LinkedList<IdLabelPair>> {
	FlexTable breadcrumpTable = new FlexTable();

	LinkedList<IdLabelPair> projectStack = new LinkedList<IdLabelPair>();

	public ProjectBreadcrumb() {
		initWidget(breadcrumpTable);

		append(new IdLabelPair(null, "[Root]"));
	}

	public LinkedList<IdLabelPair> getProjectStack() {
		return projectStack;
	}

	public IdLabelPair getLastProject() {
		return projectStack.getLast();
	}

	public void setLastProject(IdLabelPair project) {
		LinkedList<IdLabelPair> newStack = new LinkedList<IdLabelPair>();

		for (IdLabelPair element : projectStack) {
			newStack.add(element);
			if (element.equals(project))
				break;
		}
		setValue(newStack);
	}

	public LinkedList<IdLabelPair> getValue() {
		return projectStack;
	}

	public void setValue(LinkedList<IdLabelPair> value) {
		setValue(value, true);
	}

	public List<String> getProjectPath() {
		List<String> ret = new ArrayList<String>();

		Iterator<IdLabelPair> it = projectStack.iterator();

		if (it.hasNext()) {
			it.next();
		}
		while (it.hasNext()) {
			ret.add(it.next().getId());
		}
		
		return ret;
	}

	public void setValue(LinkedList<IdLabelPair> value, boolean fireEvents) {
		breadcrumpTable.removeAllRows();

		projectStack = value;

		int i = 0;

		for (final IdLabelPair project : projectStack) {
			if (i > 0) {
				breadcrumpTable.setText(0, i - 1, ">");
			}
			Anchor link = new Anchor(project.getLabel());

			link.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					setLastProject(project);
				}
			});

			breadcrumpTable.setWidget(0, i, link);
			i += 2;
		}

		if (fireEvents) {
			ValueChangeEvent.fire(this, projectStack);
		}
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<LinkedList<IdLabelPair>> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	public void append(final IdLabelPair project) {

		Anchor link = new Anchor(project.getLabel());
		link.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				setLastProject(project);
			}
		});

		breadcrumpTable.setWidget(0, projectStack.size() * 2, link);
		if (projectStack.size() > 0) {
			breadcrumpTable.setText(0, projectStack.size() * 2 - 1, ">");
		}

		projectStack.add(project);

		ValueChangeEvent.fire(this, projectStack);
	}
}
