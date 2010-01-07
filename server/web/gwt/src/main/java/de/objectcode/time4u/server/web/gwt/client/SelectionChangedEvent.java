package de.objectcode.time4u.server.web.gwt.client;

public class SelectionChangedEvent {
	public enum Type {
		PROJECT,
		TASK
	}
	
	Type type;
	
	public SelectionChangedEvent(Type type) {
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}
}
