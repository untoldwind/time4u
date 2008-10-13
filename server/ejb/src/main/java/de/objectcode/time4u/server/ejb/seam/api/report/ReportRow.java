package de.objectcode.time4u.server.ejb.seam.api.report;

public class ReportRow
{
  int index;
  Object[] data;

  public ReportRow(final int index, final Object[] data)
  {
    this.index = index;
    this.data = data;
  }

  public int getIndex()
  {
    return index;
  }

  public void setIndex(final int index)
  {
    this.index = index;
  }

  public Object[] getData()
  {
    return data;
  }

  public void setData(final Object[] data)
  {
    this.data = data;
  }

}
