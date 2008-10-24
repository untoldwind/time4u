package de.objectcode.time4u.client.ui.dialogs;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Simple exception feedback dialog.
 * 
 * Display an unexpected exception feedback for quick copy-paste of an exception for error-reporting.
 * 
 * @author junglas
 */
public class ExceptionDialog extends MessageDialog
{
  String m_trace;

  public ExceptionDialog(final Shell parent, final String title, final String message, final Throwable e)
  {
    super(parent, title, null, message, ERROR, new String[] { IDialogConstants.OK_LABEL }, 0);

    setShellStyle(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE | getDefaultOrientation());

    final StringWriter writer = new StringWriter();
    final PrintWriter out = new PrintWriter(writer);
    e.printStackTrace(out);
    out.close();

    m_trace = writer.toString();
  }

  @Override
  protected Control createCustomArea(final Composite parent)
  {
    final Text text = new Text(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI);

    final GC gc = new GC(text);
    final FontMetrics fm = gc.getFontMetrics();
    final int width = 40 * fm.getAverageCharWidth();
    final int height = 20 * fm.getHeight();
    gc.dispose();

    final GridData gridData = new GridData(GridData.FILL_BOTH);
    gridData.widthHint = width;
    gridData.heightHint = height;
    text.setLayoutData(gridData);
    text.setText(m_trace);

    return text;
  }

}
