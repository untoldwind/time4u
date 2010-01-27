package de.objectcode.time4u.server.web.gwt.webclient.server.dao;

import de.objectcode.time4u.server.entities.PersonEntity;

public interface IPersonDao {
	PersonEntity findPerson(String personId);
}
