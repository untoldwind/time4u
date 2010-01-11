package de.objectcode.time4u.server.web.gwt.main.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface MainClientBundle extends ClientBundle {
	public static final MainClientBundle INSTANCE =  GWT.<MainClientBundle>create(MainClientBundle.class);

	ImageResource newProject();
	
	ImageResource editProject();
	
	ImageResource deleteProject();

	ImageResource newTask();
	
	ImageResource editTask();
	
	ImageResource deleteTask();

	ImageResource newWorkItem();
	
	ImageResource editWorkItem();
	
	ImageResource deleteWorkItem();
}
