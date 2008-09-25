package de.objectcode.time4u.client.ui.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import de.objectcode.time4u.server.api.data.MetaType;

public class MetaBooleanField extends MetaField
{
  Button m_button;

  public MetaBooleanField(final String category, final String property)
  {
    super(category, property);
  }

  @Override
  public Control createControl(final Composite parent)
  {
    m_button = new Button(parent, SWT.CHECK);

    return m_button;
  }

  @Override
  public MetaType getType()
  {
    return MetaType.BOOLEAN;
  }

  @Override
  public Object getValue()
  {
    return m_button.getSelection();
  }

  @Override
  public void setValue(final Object metaValue)
  {
    if (metaValue instanceof Boolean) {
      m_button.setSelection((Boolean) metaValue);
    }
  }
}
