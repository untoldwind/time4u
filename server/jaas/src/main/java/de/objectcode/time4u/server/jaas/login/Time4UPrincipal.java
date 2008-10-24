package de.objectcode.time4u.server.jaas.login;

import java.io.Serializable;
import java.security.Principal;

public class Time4UPrincipal implements Principal, Serializable
{
  private static final long serialVersionUID = 4020935239292561403L;
  private final String m_userId;

  public Time4UPrincipal(final String userId)
  {
    m_userId = userId;
  }

  public String getName()
  {
    return m_userId;
  }

  public boolean isGroup()
  {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(final Object another)
  {
    if (!(another instanceof Principal)) {
      return false;
    }
    final String anotherName = ((Principal) another).getName();
    boolean equals = false;
    if (m_userId == null) {
      equals = anotherName == null;
    } else {
      equals = m_userId.equals(anotherName);
    }
    return equals;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    return m_userId == null ? 0 : m_userId.hashCode();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return m_userId;
  }
}
