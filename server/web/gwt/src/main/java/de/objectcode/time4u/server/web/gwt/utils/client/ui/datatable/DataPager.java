package de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;

import de.objectcode.time4u.server.web.gwt.utils.client.UtilsClientBundle;
import de.objectcode.time4u.server.web.gwt.utils.client.event.DataPageEvent;
import de.objectcode.time4u.server.web.gwt.utils.client.event.DataPageHandler;
import de.objectcode.time4u.server.web.gwt.utils.client.event.HasDataPageHandlers;
import de.objectcode.time4u.server.web.gwt.utils.client.service.IDataPage;

public class DataPager extends Composite implements HasDataPageHandlers {
	private FlexTable controlTable = new FlexTable();
	private PushButton leftButton;
	private PushButton rightButton;

	private int numberOfPages = 1;
	private int currentPage = 0;

	public DataPager() {
		initWidget(controlTable);

		setStyleName("utils-dataPager");

		leftButton = new PushButton(
				new Image(UtilsClientBundle.INSTANCE.left()));
		leftButton.getUpDisabledFace().setImage(
				new Image(UtilsClientBundle.INSTANCE.leftDisabled()));
		leftButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (currentPage > 0) {
					DataPageEvent.fire(DataPager.this, currentPage - 1);
				}
			}
		});

		rightButton = new PushButton(new Image(UtilsClientBundle.INSTANCE
				.right()));
		rightButton.getUpDisabledFace().setImage(
				new Image(UtilsClientBundle.INSTANCE.rightDisabled()));
		rightButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (currentPage < numberOfPages - 1) {
					DataPageEvent.fire(DataPager.this, currentPage + 1);
				}
			}
		});

		updateControls(true);
	}

	public int getNumberOfPages() {
		return numberOfPages;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public HandlerRegistration addDataPageHandler(DataPageHandler handler) {
		return addHandler(handler, DataPageEvent.getType());
	}

	public void setDataPage(IDataPage<?> dataPage) {
		int newNumberOfPages;

		if (dataPage.getTotalNumber() <= 0)
			newNumberOfPages = 1;
		else
			newNumberOfPages = (dataPage.getTotalNumber() - 1)
					/ dataPage.getPageSize() + 1;

		currentPage = dataPage.getPageNumber();

		if (newNumberOfPages != numberOfPages) {
			numberOfPages = newNumberOfPages;
			updateControls(true);
		} else {
			updateControls(false);
		}
	}

	protected void updateControls(boolean recreate) {
		if (recreate) {
			controlTable.removeAllRows();

			controlTable.setWidget(0, 0, leftButton);
			for (int i = 0; i < numberOfPages; i++) {
				final int pageNumber = i;
				Anchor pageLink = new Anchor(String.valueOf(i + 1), "");

				controlTable.setWidget(0, i + 1, pageLink);

				pageLink.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						event.preventDefault();

						DataPageEvent.fire(DataPager.this, pageNumber);
					}
				});
			}
			controlTable.setWidget(0, numberOfPages + 1, rightButton);
		}
		for (int i = 0; i < numberOfPages; i++) {
			controlTable.getWidget(0, i + 1).setStyleName(
					i == currentPage ? "utils-dataPager-currentPage"
							: "utils-dataPager-page");
		}
		leftButton.setEnabled(currentPage > 0);
		rightButton.setEnabled(currentPage < numberOfPages - 1);
	}
}