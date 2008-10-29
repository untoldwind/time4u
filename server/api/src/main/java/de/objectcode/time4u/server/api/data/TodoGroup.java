package de.objectcode.time4u.server.api.data;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "todo-group")
@XmlRootElement(name = "todo-group")
public class TodoGroup extends TodoSummary
{
  private static final long serialVersionUID = -4564074138499769629L;

}
