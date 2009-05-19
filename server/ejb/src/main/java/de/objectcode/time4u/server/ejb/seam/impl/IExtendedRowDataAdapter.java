package de.objectcode.time4u.server.ejb.seam.impl;

import de.objectcode.time4u.server.ejb.seam.api.report.IRowDataAdapter;

public interface IExtendedRowDataAdapter extends IRowDataAdapter
{
  void setCurrentRow(final Object row);
}
