package de.objectcode.time4u.server.web.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTML;

public class LoadingLabel extends HTML {
	public LoadingLabel() {
		super("<div><img src=\"" + GWT.getModuleBaseURL()
				+ "images/loading.gif\"/>&nbsp;Loading ...</div>");
	}
}
