package de.objectcode.time4u.server.ejb.seam.api.filter;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.Query;

import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.ejb.seam.api.report.parameter.BaseParameterValue;

/**
 * Generic filter condition.
 * 
 * Note that not every filter condition is suppose to be used for every entity type.
 * 
 * @author junglas
 */
public interface IFilter extends Serializable
{
  String getWhereClause(EntityType entityType, Map<String, BaseParameterValue> parameters);

  void setQueryParameters(EntityType entityType, Query query, Map<String, BaseParameterValue> parameters);
}
