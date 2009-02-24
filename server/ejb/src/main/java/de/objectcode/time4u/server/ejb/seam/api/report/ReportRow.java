package de.objectcode.time4u.server.ejb.seam.api.report;

/**
 * A row in a report.
 * 
 * @author junglas
 */
public class ReportRow
{
  /** The row index. */
  int index;
  /** The values of each column. */
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
