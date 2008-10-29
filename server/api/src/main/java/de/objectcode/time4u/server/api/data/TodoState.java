package de.objectcode.time4u.server.api.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum TodoState
{
  /** The todo is not assigned to anyone. */
  UNASSIGNED(0),
  /** The todo is assigned to at least one person but not yet started. */
  ASSIGNED_OPEN(1),
  /** The todo is assigned to at least one person who is working on it. */
  ASSIGNED_INPROGRESS(2),
  /** The todo is completed. */
  COMPLETED(3),
  /** The todo has been rejected for some reason. */
  REJECTED(4);

  private final int m_code;

  private final static Map<Integer, TodoState> CODE_MAP;

  private TodoState(final int value)
  {
    m_code = value;
  }

  public int getCode()
  {
    return m_code;
  }

  public static TodoState forCode(final int value)
  {
    return CODE_MAP.get(value);
  }

  static {
    final Map<Integer, TodoState> valueMap = new HashMap<Integer, TodoState>();
    for (final TodoState key : TodoState.values()) {
      if (valueMap.containsKey(key.getCode())) {
        throw new ExceptionInInitializerError("duplicate code");
      }
      valueMap.put(key.getCode(), key);
    }
    CODE_MAP = Collections.unmodifiableMap(valueMap);
  }
}
