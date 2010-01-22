package de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable;

import java.util.Collection;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class ToManyDataTable<RowClass> extends Composite implements
		IDataViewer<RowClass> {
	private static UI uiBinder = GWT.create(UI.class);

	@SuppressWarnings("unchecked")
	interface UI extends UiBinder<Widget, ToManyDataTable> {
	}

	DataTable<RowClass> dataTable;

	PagedDataTable<RowClass> candidatesTable;

	@UiField
	ScrollPanel scrollPanel;

	@UiField
	PushButton addButton;

	@UiField
	PushButton removeButton;

	public ToManyDataTable(DataTableColumn<RowClass>... columns) {
		this(true, columns);
	}

	public ToManyDataTable(boolean showHeader,
			DataTableColumn<RowClass>... columns) {
		initWidget(uiBinder.createAndBindUi(this));

		dataTable = new DataTable<RowClass>(showHeader, columns);
		dataTable.setWidth("100%");

		candidatesTable = new PagedDataTable<RowClass>(10, columns);

		scrollPanel.add(dataTable);
	}

	public void setData(Collection<RowClass> data) {
		dataTable.setData(data);
	}

	public void setCandidatesDataProvider(IPagedDataProvider<RowClass> provider){
		candidatesTable.setDataProvider(provider);
	}
	
	@UiHandler("addButton")
	protected void onAddClick(ClickEvent event) {
		DialogBox addDialog = createAddDialog(candidatesTable);

		addDialog.center();
		addDialog.show();
	}

	@UiHandler("removeButton")
	protected void onRemoveClick(ClickEvent event) {

	}

	protected abstract void addCandidate(RowClass candidate);

	protected DialogBox createAddDialog(
			final PagedDataTable<RowClass> candidatesTable) {
		final DialogBox addDialog = new DialogBox(true, true);
		addDialog.setGlassEnabled(true);
		addDialog.setAnimationEnabled(true);

		VerticalPanel dialogPanel = new VerticalPanel();
		HorizontalPanel buttonPanel = new HorizontalPanel();

		dialogPanel.add(candidatesTable);
		dialogPanel.add(buttonPanel);

		final PushButton okButton = new PushButton("Add");

		buttonPanel.add(okButton);
		okButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addCandidate(candidatesTable.getCurrentSelection());
				addDialog.hide();
			}
		});
		okButton.setEnabled(false);

		PushButton cancelButton = new PushButton("Cancel");

		buttonPanel.add(cancelButton);
		cancelButton.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				addDialog.hide();
			}
		});

		candidatesTable.addSelectionHandler(new SelectionHandler<RowClass>() {
			public void onSelection(SelectionEvent<RowClass> event) {
				okButton.setEnabled(true);
			}
		});
		addDialog.setWidget(dialogPanel);

		return addDialog;
	}
}
