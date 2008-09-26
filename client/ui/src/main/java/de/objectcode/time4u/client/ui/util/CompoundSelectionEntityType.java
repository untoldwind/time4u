package de.objectcode.time4u.client.ui.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public enum CompoundSelectionEntityType
{
  PROJECT,
  TASK(PROJECT),
  WORKITEM(PROJECT, TASK),
  CALENDARDAY,
  TODO;

  private Set<CompoundSelectionEntityType> m_depends;

  private CompoundSelectionEntityType(final CompoundSelectionEntityType... depends)
  {
    m_depends = Collections.unmodifiableSet(new HashSet<CompoundSelectionEntityType>(Arrays.asList(depends)));
  }

  public boolean dependsOn(final CompoundSelectionEntityType type)
  {
    return m_depends.contains(type);
  }
}
