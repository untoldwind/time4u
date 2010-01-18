package de.objectcode.time4u.server.web.gwt.utils.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface UtilsClientBundle extends ClientBundle {
	public static final UtilsClientBundle INSTANCE =  GWT.<UtilsClientBundle>create(UtilsClientBundle.class);

	ImageResource bulletLeft();

	ImageResource bulletRight();
}
