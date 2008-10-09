package de.objectcode.time4u.server.ejb.seam.api.filter;

import javax.persistence.Query;

import de.objectcode.time4u.server.api.data.EntityType;

/**
 * Generic filter condition.
 * 
 * Note that not every filter condition is suppose to be used for every entity type.
 * 
 * @author junglas
 */
public interface IFilter
{
  String getWhereClause(EntityType entityType);

  void setParameters(EntityType entityType, Query query);
}
