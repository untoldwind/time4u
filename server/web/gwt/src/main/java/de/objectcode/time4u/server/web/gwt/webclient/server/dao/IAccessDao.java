package de.objectcode.time4u.server.web.gwt.webclient.server.dao;

import java.util.Set;

public interface IAccessDao {
	Set<String> getAllowedPersonIds(String personId);
}
