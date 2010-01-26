package de.objectcode.time4u.server.web.gwt.report.client.service;

import com.google.gwt.user.client.rpc.IsSerializable;


public class ReportColumnDefinition implements IsSerializable {
	/** Column header. */
	String m_header;
	/** Column data type. */
	ReportColumnType m_columnType;
	/** Index of the column. */
	int m_index;

	public ReportColumnDefinition() {		
	}
	
	public ReportColumnDefinition(final ReportColumnType columnType,
			final String header, final int index) {
		m_columnType = columnType;
		m_header = header;
		m_index = index;
	}

	public ReportColumnType getColumnType() {
		return m_columnType;
	}

	public void setColumnType(final ReportColumnType columnType) {
		m_columnType = columnType;
	}

	public String getHeader() {
		return m_header;
	}

	public void setHeader(final String header) {
		m_header = header;
	}

	public int getIndex() {
		return m_index;
	}

	public void setIndex(final int index) {
		m_index = index;
	}

}
