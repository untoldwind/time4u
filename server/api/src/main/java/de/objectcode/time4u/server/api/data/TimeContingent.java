package de.objectcode.time4u.server.api.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Definition of a time contingent.
 * 
 * This enumeration is a placeholder for a future expansion that will allow users to define there own time contingents
 * or time accounts. The calculation of overtime has to be made much more flexible for this
 * 
 * @author junglas
 */
public enum TimeContingent
{
  WORKTIME(0),
  NONWORKTIME(1);

  private final int m_code;

  private final static Map<Integer, TimeContingent> CODE_MAP;

  private TimeContingent(final int value)
  {
    m_code = value;
  }

  public int getCode()
  {
    return m_code;
  }

  public static TimeContingent forCode(final int value)
  {
    return CODE_MAP.get(value);
  }

  static {
    final Map<Integer, TimeContingent> valueMap = new HashMap<Integer, TimeContingent>();
    for (final TimeContingent key : TimeContingent.values()) {
      if (valueMap.containsKey(key.getCode())) {
        throw new ExceptionInInitializerError("duplicate code");
      }
      valueMap.put(key.getCode(), key);
    }
    CODE_MAP = Collections.unmodifiableMap(valueMap);
  }
}
