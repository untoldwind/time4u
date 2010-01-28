package de.objectcode.time4u.server.web.gwt.webclient.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface WebClientBundle extends ClientBundle {
	public static final WebClientBundle INSTANCE =  GWT.<WebClientBundle>create(WebClientBundle.class);

	ImageResource newProject();
	
	ImageResource editProject();
	
	ImageResource deleteProject();

	ImageResource newTask();
	
	ImageResource editTask();
	
	ImageResource deleteTask();

	ImageResource newWorkItem();
	
	ImageResource editWorkItem();
	
	ImageResource deleteWorkItem();
	
	ImageResource punchedInBig();
	
	ImageResource punchedOutBig();
	
	ImageResource panelMax();
	
	ImageResource panelMin();
}
