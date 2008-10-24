package de.objectcode.time4u.server.api.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Enum of all entity types.
 * 
 * This is is especially useful for generic revision management.
 * 
 * @author junglas
 */
public enum EntityType
{
  PERSON(0),
  TEAM(1),
  PROJECT(2),
  TASK(3),
  TODO(4),
  DAYINFO(5),
  WORKITEM(6),
  ACTIVE_WORKITEM(7),
  TIMEPOLICY(8);

  private final int m_code;

  private final static Map<Integer, EntityType> CODE_MAP;

  private EntityType(final int value)
  {
    m_code = value;
  }

  public int getCode()
  {
    return m_code;
  }

  public static EntityType forCode(final int value)
  {
    return CODE_MAP.get(value);
  }

  static {
    final Map<Integer, EntityType> valueMap = new HashMap<Integer, EntityType>();
    for (final EntityType key : EntityType.values()) {
      if (valueMap.containsKey(key.getCode())) {
        throw new ExceptionInInitializerError("duplicate code");
      }
      valueMap.put(key.getCode(), key);
    }
    CODE_MAP = Collections.unmodifiableMap(valueMap);
  }

}
