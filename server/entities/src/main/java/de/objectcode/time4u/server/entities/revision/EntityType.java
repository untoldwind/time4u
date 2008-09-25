package de.objectcode.time4u.server.entities.revision;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of all revisioned entity types.
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
  WORKITEM(5);

  private final int m_value;

  private final static Map<Integer, EntityType> VALUE_MAP;

  private EntityType(final int value)
  {
    m_value = value;
  }

  public int getValue()
  {
    return m_value;
  }

  public static EntityType forValue(final int value)
  {
    return VALUE_MAP.get(value);
  }

  static {
    final Map<Integer, EntityType> valueMap = new HashMap<Integer, EntityType>();
    for (final EntityType key : EntityType.values()) {
      if (valueMap.containsKey(key.getValue())) {
        throw new ExceptionInInitializerError("duplicate code");
      }
      valueMap.put(key.getValue(), key);
    }
    VALUE_MAP = Collections.unmodifiableMap(valueMap);
  }
}
