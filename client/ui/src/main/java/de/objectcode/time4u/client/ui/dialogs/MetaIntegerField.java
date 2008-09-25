package de.objectcode.time4u.client.ui.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Spinner;

import de.objectcode.time4u.server.api.data.MetaType;

public class MetaIntegerField extends MetaField
{
  Spinner m_spinner;

  public MetaIntegerField(final String category, final String property)
  {
    super(category, property);
  }

  @Override
  public Control createControl(final Composite parent)
  {
    m_spinner = new Spinner(parent, SWT.BORDER);
    m_spinner.setMinimum(-1000);
    m_spinner.setMaximum(1000);

    return m_spinner;
  }

  @Override
  public MetaType getType()
  {
    return MetaType.INTEGER;
  }

  @Override
  public Object getValue()
  {
    return m_spinner.getSelection();
  }

  @Override
  public void setValue(final Object metaValue)
  {
    if (metaValue instanceof Integer) {
      m_spinner.setSelection((Integer) metaValue);
    }
  }

}
