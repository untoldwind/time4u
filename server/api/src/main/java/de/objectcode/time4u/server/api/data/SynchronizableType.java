package de.objectcode.time4u.server.api.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum SynchronizableType
{
  PERSON(0),
  TEAM(1),
  PROJECT(2),
  TASK(3),
  TODO(4),
  WORKITEM(5),
  ACTIVE_WORKITEM(6);

  private final int m_value;

  private final static Map<Integer, SynchronizableType> VALUE_MAP;

  private SynchronizableType(final int value)
  {
    m_value = value;
  }

  public int getValue()
  {
    return m_value;
  }

  public static SynchronizableType forValue(final int value)
  {
    return VALUE_MAP.get(value);
  }

  static {
    final Map<Integer, SynchronizableType> valueMap = new HashMap<Integer, SynchronizableType>();
    for (final SynchronizableType key : SynchronizableType.values()) {
      if (valueMap.containsKey(key.getValue())) {
        throw new ExceptionInInitializerError("duplicate code");
      }
      valueMap.put(key.getValue(), key);
    }
    VALUE_MAP = Collections.unmodifiableMap(valueMap);
  }

}
