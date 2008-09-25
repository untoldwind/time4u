package de.objectcode.time4u.client.ui.views;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;
import org.vafada.swtcalendar.SWTCalendar;
import org.vafada.swtcalendar.SWTCalendarEvent;
import org.vafada.swtcalendar.SWTCalendarListener;

import de.objectcode.time4u.client.store.api.IWorkItemRepository;
import de.objectcode.time4u.client.store.api.RepositoryFactory;
import de.objectcode.time4u.client.store.api.event.IRepositoryListener;
import de.objectcode.time4u.client.store.api.event.RepositoryEvent;
import de.objectcode.time4u.client.store.api.event.RepositoryEventType;
import de.objectcode.time4u.client.ui.UIPlugin;
import de.objectcode.time4u.client.ui.util.MultiEntitySelection;
import de.objectcode.time4u.client.ui.util.MultiEntitySelectionProvider;
import de.objectcode.time4u.client.ui.util.SelectionEntityType;
import de.objectcode.time4u.client.ui.util.SelectionServiceAdapter;
import de.objectcode.time4u.server.api.data.CalendarDay;

public class CalendarView extends ViewPart implements SWTCalendarListener, IRepositoryListener
{
  public static final String ID = "de.objectcode.client.ui.view.calendarView";

  SWTCalendar m_calendar;

  int m_currentMonth;
  int m_currentYear;

  AtomicInteger m_refreshCounter = new AtomicInteger(0);

  private MultiEntitySelectionProvider m_selectionProvider;

  /**
   * This is a callback that will allow us to create the viewer and initialize it.
   */
  @Override
  public void createPartControl(final Composite parent)
  {
    m_calendar = new SWTCalendar(parent, SWTCalendar.SHOW_WEEK_NUMBERS);
    m_calendar.addSWTCalendarListener(this);

    final Calendar calendar = m_calendar.getCalendar();

    m_currentMonth = calendar.get(Calendar.MONTH) + 1;
    m_currentYear = calendar.get(Calendar.YEAR);

    try {
      final IWorkItemRepository workItemRepository = RepositoryFactory.getRepository().getWorkItemRepository();

      //      final DayFontColorProvider provider = new DayFontColorProvider(workItemStore.getMonth(m_currentMonth,
      //          m_currentYear), workItemStore.getTimePolicies());

      //      m_calendar.setColorProvider(provider);
      //      m_calendar.setFontProvider(provider);
    } catch (final Exception e) {
      UIPlugin.getDefault().log(e);

    }

    m_selectionProvider = new MultiEntitySelectionProvider();
    getSite().setSelectionProvider(m_selectionProvider);
    getSite().getPage().addSelectionListener(m_selectionProvider);

    final MenuManager menuMgr = new MenuManager();

    menuMgr.add(new GroupMarker("calendarGroup"));
    menuMgr.add(new Separator());
    menuMgr.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));

    final Menu menu = menuMgr.createContextMenu(m_calendar);

    m_calendar.setMenu(menu);

    getSite().registerContextMenu(menuMgr, new SelectionServiceAdapter(getSite().getPage()));

    RepositoryFactory.getRepository().addRepositoryListener(RepositoryEventType.WORKITEM, this);
  }

  @Override
  public void setFocus()
  {
    m_calendar.setFocus();
  }

  @Override
  public void dispose()
  {
    RepositoryFactory.getRepository().removeRepositoryListener(RepositoryEventType.WORKITEM, this);

    super.dispose();
  }

  public void dateChanged(final SWTCalendarEvent event)
  {
    final Calendar calendar = event.getCalendar();

    if (calendar.get(Calendar.MONTH) + 1 != m_currentMonth || calendar.get(Calendar.YEAR) != m_currentYear) {
      m_currentMonth = calendar.get(Calendar.MONTH) + 1;
      m_currentYear = calendar.get(Calendar.YEAR);

      //      try {
      //        final IWorkItemStore workItemStore = Activator.getDefault().getRepository().getWorkItemStore();
      //        final DayFontColorProvider provider = new DayFontColorProvider(
      //
      //        workItemStore.getMonth(m_currentMonth, m_currentYear), workItemStore.getTimePolicies());
      //        m_calendar.setColorProvider(provider);
      //        m_calendar.setFontProvider(provider);
      //      } catch (final Exception e) {
      //        UIPlugin.getDefault().log(e);
      //      }
    }

    ((MultiEntitySelection) m_selectionProvider.getSelection()).setSelection(SelectionEntityType.CALENDARDAY,
        new CalendarDay(calendar));
    m_selectionProvider.fireSelectionChanged();
  }

  public void handleRepositoryEvent(final RepositoryEvent event)
  {
    // TODO Auto-generated method stub

  }

}
