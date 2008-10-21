package de.objectcode.time4u.server.ejb.seam.api.report.parameter;

import de.objectcode.time4u.server.ejb.seam.api.filter.IFilter;
import de.objectcode.time4u.server.ejb.seam.api.filter.PersonFilter;
import de.objectcode.time4u.server.ejb.seam.api.report.ReportParameterType;

public class PersonParameterValue extends BaseParameterValue
{
  private static final long serialVersionUID = 2152381811591513728L;

  private String m_personId;

  public PersonParameterValue(final String name, final String label)
  {
    super(name, label, ReportParameterType.PERSON);
  }

  public PersonParameterValue(final String name, final String label, final String personId)
  {
    super(name, label, ReportParameterType.PERSON);

    m_personId = personId;
  }

  public String getPersonId()
  {
    return m_personId;
  }

  public void setPersonId(final String personId)
  {
    m_personId = personId;
  }

  @Override
  public IFilter getFilter()
  {
    return PersonFilter.filterPerson(m_personId);
  }
}
