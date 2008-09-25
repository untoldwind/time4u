package de.objectcode.time4u.client.ui.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public enum SelectionEntityType
{
  PROJECT,
  TASK(PROJECT),
  WORKITEM(PROJECT, TASK),
  TODO;

  private Set<SelectionEntityType> m_depends;

  private SelectionEntityType(final SelectionEntityType... depends)
  {
    m_depends = Collections.unmodifiableSet(new HashSet<SelectionEntityType>(Arrays.asList(depends)));
  }

  public boolean dependsOn(final SelectionEntityType type)
  {
    return m_depends.contains(type);
  }
}
