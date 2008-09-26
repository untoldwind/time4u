package de.objectcode.time4u.client.ui.provider;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.ui.PlatformUI;

import de.objectcode.time4u.server.api.data.CalendarDay;
import de.objectcode.time4u.server.api.data.DayInfoSummary;
import de.objectcode.time4u.server.api.data.TimePolicy;

public class DayFontColorProvider implements IFontProvider, IColorProvider
{
  Map<CalendarDay, DayInfoSummary> m_dayInfos = null;
  Font m_boldFont = null;
  List<TimePolicy> m_timePolicies;
  Color m_regularBackground;
  Color m_regularForeground;
  Font m_regularFont;

  public DayFontColorProvider(final Color regularBackground, final Color regularForeground, final Font regularFont,
      final Font boldFont, final List<DayInfoSummary> dayInfos, final List<TimePolicy> timePolicies)
  {
    m_regularBackground = regularBackground;
    m_regularForeground = regularForeground;
    m_regularFont = regularFont;
    m_boldFont = boldFont;

    m_dayInfos = new HashMap<CalendarDay, DayInfoSummary>();
    for (final DayInfoSummary dayInfo : dayInfos) {
      m_dayInfos.put(dayInfo.getDay(), dayInfo);
    }
    m_timePolicies = timePolicies;
  }

  public Color getBackground(final Object element)
  {
    return m_regularBackground;
  }

  public Color getForeground(final Object element)
  {
    if (element instanceof Calendar) {
      final CalendarDay day = new CalendarDay((Calendar) element);

      if (m_dayInfos != null) {
        final DayInfoSummary dayInfo = m_dayInfos.get(day);

        if (dayInfo != null && dayInfo.isHasInvalidWorkItems()) {
          return PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_RED);
        } else if (dayInfo != null && dayInfo.getRegularTime() == 0) {
          return PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_DARK_RED);
        } else if (dayInfo == null || dayInfo.getRegularTime() < 0) {
          if (m_timePolicies != null) {
            int regularTime = -1;
            for (final TimePolicy timePolicy : m_timePolicies) {
              if ((regularTime = timePolicy.getRegularTime(day)) >= 0) {
                break;
              }
            }

            if (regularTime == 0) {
              return PlatformUI.getWorkbench().getDisplay().getSystemColor(SWT.COLOR_DARK_RED);
            }
          }
        }
      }
    }

    return m_regularForeground;
  }

  public Font getFont(final Object element)
  {
    if (element instanceof Calendar) {
      final CalendarDay day = new CalendarDay((Calendar) element);

      if (m_dayInfos != null) {
        final DayInfoSummary dayInfo = m_dayInfos.get(day);

        if (dayInfo != null && dayInfo.isHasWorkItems()) {
          return m_boldFont;
        }
      }
    }

    return m_regularFont;

  }

}
