package de.objectcode.time4u.server.jaas.login;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;

public class Time4UGroup extends Time4UPrincipal implements Group
{
  private static final long serialVersionUID = 5675636419702967199L;

  private final HashSet<Principal> m_members;

  public Time4UGroup(final String name)
  {
    super(name);
    m_members = new HashSet<Principal>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isGroup()
  {
    return true;
  }

  public boolean addMember(final Principal user)
  {
    final boolean isMember = m_members.contains(user);
    if (isMember == false) {
      m_members.add(user);
    }
    return isMember == false;
  }

  public boolean isMember(final Principal member)
  {
    boolean isMember = m_members.contains(member);
    if (isMember == false) { // Check any Groups for membership
      final Iterator<Principal> iter = m_members.iterator();
      while (isMember == false && iter.hasNext()) {
        final Object next = iter.next();
        if (next instanceof Group) {
          final Group group = (Group) next;
          isMember = group.isMember(member);
        }
      }
    }
    return isMember;
  }

  public Enumeration<? extends Principal> members()
  {
    return Collections.enumeration(m_members);
  }

  public boolean removeMember(final Principal user)
  {
    final Object prev = m_members.remove(user);
    return prev != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    final StringBuffer tmp = new StringBuffer(getName());
    tmp.append("(members:");
    final Iterator<Principal> iter = m_members.iterator();
    while (iter.hasNext()) {
      tmp.append(iter.next());
      tmp.append(',');
    }
    tmp.setCharAt(tmp.length() - 1, ')');
    return tmp.toString();
  }
}
