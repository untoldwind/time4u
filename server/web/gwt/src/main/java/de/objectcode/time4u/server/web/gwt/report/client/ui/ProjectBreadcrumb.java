package de.objectcode.time4u.server.web.gwt.report.client.ui;

import java.util.LinkedList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasValue;

import de.objectcode.time4u.server.web.gwt.report.client.service.IdValuePair;

public class ProjectBreadcrumb extends Composite implements
		HasValue<LinkedList<IdValuePair>> {
	FlexTable breadcrumpTable = new FlexTable();

	LinkedList<IdValuePair> projectStack = new LinkedList<IdValuePair>();

	public ProjectBreadcrumb() {
		initWidget(breadcrumpTable);

		append(new IdValuePair(null, "[Root]"));
	}

	public LinkedList<IdValuePair> getProjectStack() {
		return projectStack;
	}

	public IdValuePair getLastProject() {
		return projectStack.getLast();
	}
	
	public void setLastProject(IdValuePair project) {
		LinkedList<IdValuePair> newStack = new LinkedList<IdValuePair>();

		for ( IdValuePair element : projectStack ) {
			newStack.add(element);
			if ( element.equals(project))
				break;
		}
		setValue(newStack);
	}

	public LinkedList<IdValuePair> getValue() {
		return projectStack;
	}

	public void setValue(LinkedList<IdValuePair> value) {
		setValue(value, true);
	}

	public void setValue(LinkedList<IdValuePair> value, boolean fireEvents) {
		breadcrumpTable.removeAllRows();

		projectStack = value;

		int i = 0;

		for (final IdValuePair project : projectStack) {
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
		
		if ( fireEvents ) {
			ValueChangeEvent.fire(this, projectStack);
		}
	}

	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<LinkedList<IdValuePair>> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	public void append(final IdValuePair project) {

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
