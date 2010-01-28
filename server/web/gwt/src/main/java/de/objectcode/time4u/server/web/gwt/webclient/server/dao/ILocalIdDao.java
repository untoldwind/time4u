package de.objectcode.time4u.server.web.gwt.webclient.server.dao;

import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.entities.revision.LocalIdEntity;

public interface ILocalIdDao {
	  LocalIdEntity getNextChunk(final EntityType entityType);

}
