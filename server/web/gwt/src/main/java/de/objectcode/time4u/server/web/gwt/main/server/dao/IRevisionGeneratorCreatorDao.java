package de.objectcode.time4u.server.web.gwt.main.server.dao;

import de.objectcode.time4u.server.entities.revision.RevisionEntityKey;

public interface IRevisionGeneratorCreatorDao {
	void createRevisionEntity(final RevisionEntityKey key);
}
