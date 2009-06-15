package de.objectcode.time4u.server.ejb.seam.impl;

import java.util.List;

import de.objectcode.time4u.server.ejb.seam.api.report.IReportDataCollector;

/**
 * Internal interface to execute a query and iterate over the result set.
 * 
 * @author junglas
 */
public interface IRowDataIterator<T>
{
  void iterate(List<T> result, IReportDataCollector collector);
}
