package de.objectcode.time4u.server.api.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Value type of a meta property.
 * 
 * @author junglas
 */
public enum MetaType
{
  /** String value. */
  STRING(0),
  /** Integer value. */
  INTEGER(1),
  /** Boolean value. */
  BOOLEAN(2),
  /** Timestamp value. */
  DATE(3);

  private final int m_code;

  private final static Map<Integer, MetaType> CODE_MAP;

  private MetaType(final int value)
  {
    m_code = value;
  }

  public int getCode()
  {
    return m_code;
  }

  public static MetaType forCode(final int value)
  {
    return CODE_MAP.get(value);
  }

  static {
    final Map<Integer, MetaType> valueMap = new HashMap<Integer, MetaType>();
    for (final MetaType key : MetaType.values()) {
      if (valueMap.containsKey(key.getCode())) {
        throw new ExceptionInInitializerError("duplicate code");
      }
      valueMap.put(key.getCode(), key);
    }
    CODE_MAP = Collections.unmodifiableMap(valueMap);
  }
}
