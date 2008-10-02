package de.objectcode.time4u.client.store.api;

import java.util.Map;

/**
 * Interface to the client side statistic repository.
 * 
 * @author junglas
 */
public interface IStatisticRepository
{
  Map<String, ? extends StatisticEntry> getDayStatistic(int day, int month, int year) throws RepositoryException;

  Map<String, ? extends StatisticEntry> getWeekStatistic(int week, int year) throws RepositoryException;

  Map<String, ? extends StatisticEntry> getMonthStatistic(int month, int year) throws RepositoryException;

  Map<String, ? extends StatisticEntry> getYearStatistic(int year) throws RepositoryException;
}
