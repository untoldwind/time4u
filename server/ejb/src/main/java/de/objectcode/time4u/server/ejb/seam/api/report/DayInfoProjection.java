package de.objectcode.time4u.server.ejb.seam.api.report;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

import de.objectcode.time4u.server.api.data.TimeContingent;
import de.objectcode.time4u.server.entities.DayTagEntity;
import de.objectcode.time4u.server.entities.TimePolicyEntity;

@XmlEnum
@XmlType(name = "dayinfo-projection")
public enum DayInfoProjection implements IProjection
{
  DATE(ColumnType.DATE, "Date") {
    public Object project(final IRowDataAdapter rowData)
    {
      return rowData.getDayInfo().getDate();
    }
  },
  SUM_DURATIONS(ColumnType.TIME, "Sum durations") {
    public Object project(final IRowDataAdapter rowData)
    {
      final Map<TimeContingent, Integer> timeContingents = rowData.getDayInfo().getTimeContingents();
      int sumDurations = 0;

      for (final Integer duration : timeContingents.values()) {
        sumDurations += duration;
      }

      return sumDurations;
    }

    @Override
    public IAggregation createAggregation()
    {
      return new SumAggregation();
    }
  },
  REGULAR_TIME(ColumnType.TIME, "Regular time") {
    public Object project(final IRowDataAdapter rowData)
    {
      if (rowData.getDayInfo().getRegularTime() < 0) {
        final List<TimePolicyEntity> timePolicies = rowData.getTimePolicies();
        final Date date = rowData.getDayInfo().getDate();

        if (timePolicies != null) {
          for (final TimePolicyEntity timePolicy : timePolicies) {
            final int regularTime = timePolicy.getRegularTime(date);

            if (regularTime >= 0) {
              return regularTime;
            }
          }
        }

        return 0;
      }
      return rowData.getDayInfo().getRegularTime();
    }
  },
  TAGS(ColumnType.NAME_ARRAY, "Tags") {
    public Object project(final IRowDataAdapter rowData)
    {
      final List<String> tags = new ArrayList<String>();

      for (final DayTagEntity dayTag : rowData.getDayInfo().getTags()) {
        tags.add(dayTag.getName());
      }
      return tags;
    }
  };

  ColumnType m_columnType;
  String m_header;

  private DayInfoProjection(final ColumnType columnType, final String header)
  {
    m_columnType = columnType;
    m_header = header;
  }

  public ColumnDefinition getColumnDefinition(final int index)
  {
    return new ColumnDefinition(m_columnType, m_header, index);
  }

  public IAggregation createAggregation()
  {
    return null;
  }
}
