package de.objectcode.time4u.server.web.gwt.utils.client.service;

public interface IProjection<DTOClass> {
	Object project(DTOClass dto);
	
	boolean isSortable();
}
