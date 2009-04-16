package de.objectcode.time4u.server.ejb.seam.api.report;

/**
 * Column type of a report.
 * 
 * @author junglas
 */
public enum ColumnType
{
  /** A name is a short text like a project name, task name, user name, ... */
  NAME,
  /** An array of names (to be displayed in a single column) */
  NAME_ARRAY,
  /** A description is a long text like a project description, workitem comment, ... */
  DESCRIPTION,
  /** Time of day 00:00:00 to 24:00:00 */
  TIME,
  /** A date */
  DATE,
  /** A timestamp */
  TIMESTAMP
}
