package de.objectcode.time4u.client.ui.dialogs;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import de.objectcode.time4u.client.ui.controls.DateCombo;
import de.objectcode.time4u.server.api.data.MetaType;

public class MetaDateField extends MetaField
{
  DateCombo m_datePicker;

  public MetaDateField(final String category, final String property)
  {
    super(category, property);
  }

  @Override
  public Control createControl(final Composite parent)
  {
    m_datePicker = new DateCombo(parent, SWT.BORDER);

    return m_datePicker;
  }

  @Override
  public MetaType getType()
  {
    return MetaType.DATE;
  }

  @Override
  public Object getValue()
  {
    if (m_datePicker.getSelection() != null) {
      return m_datePicker.getSelection().getTime();
    }
    return null;
  }

  @Override
  public void setValue(final Object metaValue)
  {
    if (metaValue instanceof Date) {
      final Calendar calendar = Calendar.getInstance();
      calendar.setTime((Date) metaValue);
      m_datePicker.select(calendar);
    }
  }

}
