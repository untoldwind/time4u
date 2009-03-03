package de.objectcode.time4u.server.entities.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum ConfigValueType
{
  BOOLEAN(0),
  LONG(1),
  STRING(2),
  XML(3);

  private final int m_code;

  private final static Map<Integer, ConfigValueType> CODE_MAP;

  private ConfigValueType(final int value)
  {
    m_code = value;
  }

  public int getCode()
  {
    return m_code;
  }

  public static ConfigValueType forCode(final int value)
  {
    return CODE_MAP.get(value);
  }

  static {
    final Map<Integer, ConfigValueType> valueMap = new HashMap<Integer, ConfigValueType>();
    for (final ConfigValueType key : ConfigValueType.values()) {
      if (valueMap.containsKey(key.getCode())) {
        throw new ExceptionInInitializerError("duplicate code");
      }
      valueMap.put(key.getCode(), key);
    }
    CODE_MAP = Collections.unmodifiableMap(valueMap);
  }
}
