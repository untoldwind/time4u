package de.objectcode.time4u.client.ui.controls;

import java.text.MessageFormat;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class TimeComboCellEditor extends CellEditor
{
  private TimeCombo m_timeCombo;
  private int m_selection;

  private static final int defaultStyle = SWT.NONE;

  public TimeComboCellEditor()
  {
    setStyle(defaultStyle);
  }

  public TimeComboCellEditor(final Composite parent)
  {
    this(parent, defaultStyle);
  }

  public TimeComboCellEditor(final Composite parent, final int style)
  {
    super(parent, style);
  }

  @Override
  protected Control createControl(final Composite parent)
  {
    m_timeCombo = new TimeCombo(parent, getStyle());
    m_timeCombo.setFont(parent.getFont());

    m_timeCombo.addKeyListener(new KeyAdapter() {
      // hook key pressed - see PR 14201
      @Override
      public void keyPressed(final KeyEvent e)
      {
        keyReleaseOccured(e);
      }
    });

    m_timeCombo.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetDefaultSelected(final SelectionEvent event)
      {
        applyEditorValueAndDeactivate();
      }

      @Override
      public void widgetSelected(final SelectionEvent event)
      {
        m_selection = m_timeCombo.getSelection();
      }
    });
    m_timeCombo.addTraverseListener(new TraverseListener() {
      public void keyTraversed(final TraverseEvent e)
      {
        if (e.detail == SWT.TRAVERSE_ESCAPE || e.detail == SWT.TRAVERSE_RETURN) {
          e.doit = false;
        }
      }
    });

    m_timeCombo.addFocusListener(new FocusAdapter() {
      @Override
      public void focusLost(final FocusEvent e)
      {
        TimeComboCellEditor.this.focusLost();
      }
    });

    return m_timeCombo;
  }

  @Override
  protected Object doGetValue()
  {
    return new Integer(m_selection);
  }

  @Override
  protected void doSetFocus()
  {
    m_timeCombo.setFocus();
  }

  @Override
  protected void doSetValue(final Object value)
  {
    Assert.isTrue(m_timeCombo != null && value instanceof Integer);
    m_selection = ((Integer) value).intValue();
    m_timeCombo.select(m_selection);
  }

  /**
   * Applies the currently selected value and deactiavates the cell editor
   */
  void applyEditorValueAndDeactivate()
  {
    // must set the selection before getting value
    m_selection = m_timeCombo.getSelection();
    final Object newValue = doGetValue();
    markDirty();
    final boolean isValid = isCorrect(newValue);
    setValueValid(isValid);

    if (!isValid) {
      // Since we don't have a valid index, assume we're using an 'edit'
      // combo so format using its text value
      setErrorMessage(MessageFormat.format(getErrorMessage(), new Object[] { m_timeCombo.getText() }));
    }

    fireApplyEditorValue();
    deactivate();
  }

  @Override
  protected void focusLost()
  {
    if (isActivated()) {
      applyEditorValueAndDeactivate();
    }
  }

  @Override
  protected void keyReleaseOccured(final KeyEvent keyEvent)
  {
    if (keyEvent.character == '\u001b') { // Escape character
      fireCancelEditor();
    } else if (keyEvent.character == '\t') { // tab key
      applyEditorValueAndDeactivate();
    }
  }

}
