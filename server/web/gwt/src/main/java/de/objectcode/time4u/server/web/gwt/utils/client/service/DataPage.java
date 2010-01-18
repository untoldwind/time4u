package de.objectcode.time4u.server.web.gwt.utils.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DataPage<T> implements IsSerializable{
	private int pageNumber;
	private int pageSize;
	private int totalNumber;
	
	private List<T> pageData;


	public DataPage() {		
	}
	
	public DataPage(int pageNumber, int pageSize, int totalNumber,
			List<T> pageData) {
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.totalNumber = totalNumber;
		this.pageData = pageData;
	}

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

	public List<T> getPageData() {
		return pageData;
	}

	public void setPageData(List<T> pageData) {
		this.pageData = pageData;
	}
	
	
}
