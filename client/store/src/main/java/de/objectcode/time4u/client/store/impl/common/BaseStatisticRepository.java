package de.objectcode.time4u.client.store.impl.common;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class BaseStatisticRepository
{
  private final Map<Integer, Map<String, MutableStatisticEntry>> m_yearCache;
  private final Map<CalendarMonth, Map<String, MutableStatisticEntry>> m_monthCache;
  private final Map<CalendarWeek, Map<String, MutableStatisticEntry>> m_weekCache;

  protected BaseStatisticRepository()
  {
    // Cache no more than 5 years
    m_yearCache = new LinkedHashMap<Integer, Map<String, MutableStatisticEntry>>() {
      private static final long serialVersionUID = 2978982921561881053L;

      @Override
      protected boolean removeEldestEntry(final Map.Entry<Integer, Map<String, MutableStatisticEntry>> eldest)
      {
        return size() > 5;
      }
    };

    // Cache no more than 24 month
    m_monthCache = new LinkedHashMap<CalendarMonth, Map<String, MutableStatisticEntry>>() {
      private static final long serialVersionUID = 2978982921561881053L;

      @Override
      protected boolean removeEldestEntry(final Map.Entry<CalendarMonth, Map<String, MutableStatisticEntry>> eldest)
      {
        return size() > 24;
      }
    };

    // Cache no more than 54 weeks
    m_weekCache = new LinkedHashMap<CalendarWeek, Map<String, MutableStatisticEntry>>() {
      private static final long serialVersionUID = 2978982921561881053L;

      @Override
      protected boolean removeEldestEntry(final Map.Entry<CalendarWeek, Map<String, MutableStatisticEntry>> eldest)
      {
        return size() > 54;
      }
    };
  }
}
