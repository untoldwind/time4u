package de.objectcode.time4u.server.web.gwt.utils.client.ui;

public class TableHeader {
	String text;
	String width;	

	public TableHeader(String text) {
		this.text = text;
	}

	public TableHeader(String text, String width) {
		this.text = text;
		this.width = width;
	}

	public String getText() {
		return text;
	}

	public String getWidth() {
		return width;
	}

}
