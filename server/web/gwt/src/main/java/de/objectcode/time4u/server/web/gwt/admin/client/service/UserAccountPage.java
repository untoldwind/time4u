package de.objectcode.time4u.server.web.gwt.admin.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

import de.objectcode.time4u.server.web.gwt.utils.client.service.IDataPage;

public class UserAccountPage implements IDataPage<UserAccount>, IsSerializable {

	public UserAccountPage() {
	}

	public UserAccountPage(int pageNumber, int pageSize, int totalNumber,
			List<UserAccount> pageData) {
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.totalNumber = totalNumber;
		this.pageData = pageData;
	}

	private int pageNumber;
	private int pageSize;
	private int totalNumber;

	private List<UserAccount> pageData;

	public int getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(int totalNumber) {
		this.totalNumber = totalNumber;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<UserAccount> getPageData() {
		return pageData;
	}

	public void setPageData(List<UserAccount> pageData) {
		this.pageData = pageData;
	}
}
