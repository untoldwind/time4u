package de.objectcode.time4u.client.ui.controls;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.accessibility.AccessibleTextAdapter;
import org.eclipse.swt.accessibility.AccessibleTextEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TypedListener;
import org.eclipse.swt.widgets.Widget;

public class ComboTreeViewer extends Composite
{
  Text text;
  TreeViewer viewer;
  int visibleItemCount = 10;
  Shell popup;
  Button arrow;
  boolean hasFocus;
  Listener listener, filter;
  Color foreground, background;
  Font font;

  List<ISelectionChangedListener> selectionChangeListeners = new ArrayList<ISelectionChangedListener>();

  IContentProvider contentProvider;
  ILabelProvider labelProvider;
  Object inputObject;

  /**
   * Constructs a new instance of this class given its parent and a style value describing its behavior and appearance.
   * <p>
   * The style value is either one of the style constants defined in class <code>SWT</code> which is applicable to
   * instances of this class, or must be built by <em>bitwise OR</em>'ing together (that is, using the <code>int</code>
   * "|" operator) two or more of those <code>SWT</code> style constants. The class description lists the style
   * constants that are applicable to the class. Style bits are also inherited from superclasses.
   * </p>
   * 
   * @param parent
   *          a widget which will be the parent of the new instance (cannot be null)
   * @param style
   *          the style of widget to construct
   * 
   * @exception IllegalArgumentException
   *              <ul>
   *              <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
   *              </ul>
   * @exception SWTException
   *              <ul>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
   *              </ul>
   * 
   * @see SWT#BORDER
   * @see SWT#READ_ONLY
   * @see SWT#FLAT
   * @see Widget#getStyle()
   */
  public ComboTreeViewer(final Composite parent, final int style)
  {
    super(parent, checkStyle(style));

    int textStyle = SWT.SINGLE;
    if ((style & SWT.READ_ONLY) != 0) {
      textStyle |= SWT.READ_ONLY;
    }
    if ((style & SWT.FLAT) != 0) {
      textStyle |= SWT.FLAT;
    }
    if ((style & SWT.BORDER) != 0) {
      textStyle |= SWT.BORDER;
    }

    text = new Text(this, textStyle);
    int arrowStyle = SWT.ARROW | SWT.DOWN;
    if ((style & SWT.FLAT) != 0) {
      arrowStyle |= SWT.FLAT;
    }
    arrow = new Button(this, arrowStyle);

    listener = new Listener() {
      public void handleEvent(final Event event)
      {
        if (popup == event.widget) {
          popupEvent(event);
          return;
        }
        if (text == event.widget) {
          textEvent(event);
          return;
        }
        if (viewer != null && viewer.getTree() == event.widget) {
          listEvent(event);
        }
        if (arrow == event.widget) {
          arrowEvent(event);
          return;
        }
        if (ComboTreeViewer.this == event.widget) {
          comboEvent(event);
          return;
        }
        if (getShell() == event.widget) {
          handleFocus(SWT.FocusOut);
        }
      }
    };
    filter = new Listener() {
      public void handleEvent(final Event event)
      {
        final Shell shell = ((Control) event.widget).getShell();
        if (shell == ComboTreeViewer.this.getShell()) {
          handleFocus(SWT.FocusOut);
        }
      }
    };

    final int[] comboEvents = { SWT.Dispose, SWT.Move, SWT.Resize };
    for (int i = 0; i < comboEvents.length; i++) {
      this.addListener(comboEvents[i], listener);
    }

    final int[] textEvents = { SWT.KeyDown, SWT.KeyUp, SWT.MenuDetect, SWT.Modify, SWT.MouseDown, SWT.MouseUp,
        SWT.Traverse, SWT.FocusIn };
    for (int i = 0; i < textEvents.length; i++) {
      text.addListener(textEvents[i], listener);
    }

    final int[] arrowEvents = { SWT.Selection, SWT.FocusIn };
    for (int i = 0; i < arrowEvents.length; i++) {
      arrow.addListener(arrowEvents[i], listener);
    }

    createPopup(null);
    initAccessible();
  }

  static int checkStyle(final int style)
  {
    final int mask = SWT.READ_ONLY | SWT.FLAT | SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
    return style & mask;
  }

  public void setContentProvider(final IContentProvider contentProvider)
  {
    this.contentProvider = contentProvider;
    if (viewer != null) {
      viewer.setContentProvider(contentProvider);
    }
  }

  public void setLabelProvider(final ILabelProvider labelProvider)
  {
    this.labelProvider = labelProvider;
    if (viewer != null) {
      viewer.setLabelProvider(labelProvider);
    }
  }

  public void setInput(final Object input)
  {
    inputObject = input;
    if (viewer != null) {
      viewer.setInput(input);
    }
  }

  /**
   * Adds the listener to the collection of listeners who will be notified when the receiver's text is modified, by
   * sending it one of the messages defined in the <code>ModifyListener</code> interface.
   * 
   * @param listener
   *          the listener which should be notified
   * 
   * @exception IllegalArgumentException
   *              <ul>
   *              <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
   *              </ul>
   * @exception SWTException
   *              <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   *              </ul>
   * 
   * @see ModifyListener
   * @see #removeModifyListener
   */
  public void addModifyListener(final ModifyListener listener)
  {
    checkWidget();
    if (listener == null) {
      SWT.error(SWT.ERROR_NULL_ARGUMENT);
    }
    final TypedListener typedListener = new TypedListener(listener);
    addListener(SWT.Modify, typedListener);
  }

  public void addSelectionChangedListener(final ISelectionChangedListener listener)
  {
    selectionChangeListeners.add(listener);
    if (viewer != null) {
      viewer.addPostSelectionChangedListener(listener);
    }
  }

  void arrowEvent(final Event event)
  {
    switch (event.type) {
      case SWT.FocusIn: {
        handleFocus(SWT.FocusIn);
        break;
      }
      case SWT.Selection: {
        dropDown(!isDropped());
        break;
      }
    }
  }

  /**
   * Sets the selection in the receiver's text field to an empty selection starting just before the first character. If
   * the text field is editable, this has the effect of placing the i-beam at the start of the text.
   * <p>
   * Note: To clear the selected items in the receiver's list, use <code>deselectAll()</code>.
   * </p>
   * 
   * @exception SWTException
   *              <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   *              </ul>
   * 
   * @see #deselectAll
   */
  public void clearSelection()
  {
    checkWidget();
    text.clearSelection();
    viewer.setSelection(new StructuredSelection());
  }

  void comboEvent(final Event event)
  {
    switch (event.type) {
      case SWT.Dispose:
        if (popup != null && !popup.isDisposed()) {
          viewer.getTree().removeListener(SWT.Dispose, listener);
          popup.dispose();
        }
        final Shell shell = getShell();
        shell.removeListener(SWT.Deactivate, listener);
        final Display display = getDisplay();
        display.removeFilter(SWT.FocusIn, filter);
        popup = null;
        text = null;
        viewer = null;
        arrow = null;
        break;
      case SWT.Move:
        dropDown(false);
        break;
      case SWT.Resize:
        internalLayout(false);
        break;
    }
  }

  @Override
  public Point computeSize(final int wHint, final int hHint, final boolean changed)
  {
    checkWidget();
    int width = 0, height = 0;
    final int textWidth = 0;
    final GC gc = new GC(text);
    final int spacer = gc.stringExtent(" ").x; //$NON-NLS-1$
    gc.dispose();
    final Point textSize = text.computeSize(SWT.DEFAULT, SWT.DEFAULT, changed);
    final Point arrowSize = arrow.computeSize(SWT.DEFAULT, SWT.DEFAULT, changed);
    final Point listSize = viewer.getTree().computeSize(SWT.DEFAULT, SWT.DEFAULT, changed);
    final int borderWidth = getBorderWidth();

    height = Math.max(textSize.y, arrowSize.y);
    width = Math.max(textWidth + 2 * spacer + arrowSize.x + 2 * borderWidth, listSize.x);
    if (wHint != SWT.DEFAULT) {
      width = wHint;
    }
    if (hHint != SWT.DEFAULT) {
      height = hHint;
    }
    return new Point(width + 2 * borderWidth, height + 2 * borderWidth);
  }

  void createPopup(final ISelection selection)
  {
    // create shell and list
    popup = new Shell(getShell(), SWT.NO_TRIM | SWT.ON_TOP);
    final int style = getStyle();
    int listStyle = SWT.SINGLE | SWT.V_SCROLL;
    if ((style & SWT.FLAT) != 0) {
      listStyle |= SWT.FLAT;
    }
    if ((style & SWT.RIGHT_TO_LEFT) != 0) {
      listStyle |= SWT.RIGHT_TO_LEFT;
    }
    if ((style & SWT.LEFT_TO_RIGHT) != 0) {
      listStyle |= SWT.LEFT_TO_RIGHT;
    }
    viewer = new TreeViewer(popup, listStyle);
    if (font != null) {
      viewer.getTree().setFont(font);
    }
    if (foreground != null) {
      viewer.getTree().setForeground(foreground);
    }
    if (background != null) {
      viewer.getTree().setBackground(background);
    }

    final int[] popupEvents = { SWT.Close, SWT.Paint, SWT.Deactivate };
    for (int i = 0; i < popupEvents.length; i++) {
      popup.addListener(popupEvents[i], listener);
    }
    final int[] listEvents = { SWT.Traverse, SWT.MouseDoubleClick, SWT.KeyDown, SWT.KeyUp, SWT.FocusIn, SWT.Dispose };
    for (int i = 0; i < listEvents.length; i++) {
      viewer.getTree().addListener(listEvents[i], listener);
    }

    if (contentProvider != null) {
      viewer.setContentProvider(contentProvider);
    }
    if (labelProvider != null) {
      viewer.setLabelProvider(labelProvider);
    }
    if (inputObject != null) {
      viewer.setInput(inputObject);
    }
    viewer.addPostSelectionChangedListener(new ISelectionChangedListener() {
      public void selectionChanged(final SelectionChangedEvent event)
      {
        final ISelection selection = event.getSelection();

        if (selection != null && selection instanceof IStructuredSelection) {
          final Object sel = ((IStructuredSelection) selection).getFirstElement();

          if (sel != null && labelProvider != null) {
            text.setText(labelProvider.getText(sel));
          } else {
            text.setText("");
          }
        }
      }

    });
    for (final ISelectionChangedListener listener : selectionChangeListeners) {
      viewer.addSelectionChangedListener(listener);
    }
    if (selection != null) {
      viewer.setSelection(selection);
    }
  }

  /**
   * Deselects all selected items in the receiver's list.
   * <p>
   * Note: To clear the selection in the receiver's text field, use <code>clearSelection()</code>.
   * </p>
   * 
   * @exception SWTException
   *              <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   *              </ul>
   * 
   * @see #clearSelection
   */
  public void deselectAll()
  {
    checkWidget();
    viewer.setSelection(new StructuredSelection());
  }

  void dropDown(final boolean drop)
  {
    if (drop == isDropped()) {
      return;
    }
    if (!drop) {
      popup.setVisible(false);
      if (!isDisposed() && arrow.isFocusControl()) {
        text.setFocus();
      }
      return;
    }

    if (getShell() != popup.getParent()) {
      final ISelection selection = viewer.getSelection();
      viewer.getTree().removeListener(SWT.Dispose, listener);
      popup.dispose();
      popup = null;
      viewer = null;
      createPopup(selection);
    }

    final Point size = getSize();
    //    int itemCount = viewer.getTree().getItemCount();
    //    itemCount = itemCount == 0 ? visibleItemCount : Math.min(visibleItemCount, itemCount);
    final int itemHeight = viewer.getTree().getItemHeight() * visibleItemCount;
    final Point listSize = viewer.getTree().computeSize(SWT.DEFAULT, itemHeight, false);
    viewer.getTree().setBounds(1, 1, Math.max(size.x - 2, listSize.x), listSize.y);

    final Display display = getDisplay();
    final Rectangle listRect = viewer.getTree().getBounds();
    final Rectangle parentRect = display.map(getParent(), null, getBounds());
    final Point comboSize = getSize();
    final Rectangle displayRect = getMonitor().getClientArea();
    final int width = Math.max(comboSize.x, listRect.width + 2);
    final int height = listRect.height + 2;
    int x = parentRect.x;
    int y = parentRect.y + comboSize.y;
    if (y + height > displayRect.y + displayRect.height) {
      y = parentRect.y - height;
    }
    if (x + width > displayRect.x + displayRect.width) {
      x = displayRect.x + displayRect.width - listRect.width;
    }
    popup.setBounds(x, y, width, height);
    popup.setVisible(true);
    viewer.getTree().setFocus();
  }

  /*
   * Return the lowercase of the first non-'&' character following an '&' character in the given string. If there are no
   * '&' characters in the given string, return '\0'.
   */
  char _findMnemonic(final String string)
  {
    if (string == null) {
      return '\0';
    }
    int index = 0;
    final int length = string.length();
    do {
      while (index < length && string.charAt(index) != '&') {
        index++;
      }
      if (++index >= length) {
        return '\0';
      }
      if (string.charAt(index) != '&') {
        return Character.toLowerCase(string.charAt(index));
      }
      index++;
    } while (index < length);
    return '\0';
  }

  /*
   * Return the Label immediately preceding the receiver in the z-order, or null if none.
   */
  Label getAssociatedLabel()
  {
    final Control[] siblings = getParent().getChildren();
    for (int i = 0; i < siblings.length; i++) {
      if (siblings[i] == this) {
        if (i > 0 && siblings[i - 1] instanceof Label) {
          return (Label) siblings[i - 1];
        }
      }
    }
    return null;
  }

  @Override
  public Control[] getChildren()
  {
    checkWidget();
    return new Control[0];
  }

  /**
   * Gets the editable state.
   * 
   * @return whether or not the receiver is editable
   * 
   * @exception SWTException
   *              <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   *              </ul>
   * 
   * @since 3.0
   */
  public boolean getEditable()
  {
    checkWidget();
    return text.getEditable();
  }

  /**
   * Returns the number of items contained in the receiver's list.
   * 
   * @return the number of items
   * 
   * @exception SWTException
   *              <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   *              </ul>
   */
  public int getItemCount()
  {
    checkWidget();
    return viewer.getTree().getItemCount();
  }

  /**
   * Returns the height of the area which would be used to display <em>one</em> of the items in the receiver's list.
   * 
   * @return the height of one item
   * 
   * @exception SWTException
   *              <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   *              </ul>
   */
  public int getItemHeight()
  {
    checkWidget();
    return viewer.getTree().getItemHeight();
  }

  @Override
  public Menu getMenu()
  {
    return text.getMenu();
  }

  @Override
  public int getStyle()
  {
    int style = super.getStyle();
    style &= ~SWT.READ_ONLY;
    if (!text.getEditable()) {
      style |= SWT.READ_ONLY;
    }
    return style;
  }

  /**
   * Returns a string containing a copy of the contents of the receiver's text field.
   * 
   * @return the receiver's text
   * 
   * @exception SWTException
   *              <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   *              </ul>
   */
  public String getText()
  {
    checkWidget();
    return text.getText();
  }

  /**
   * Returns the height of the receivers's text field.
   * 
   * @return the text height
   * 
   * @exception SWTException
   *              <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   *              </ul>
   */
  public int getTextHeight()
  {
    checkWidget();
    return text.getLineHeight();
  }

  /**
   * Returns the maximum number of characters that the receiver's text field is capable of holding. If this has not been
   * changed by <code>setTextLimit()</code>, it will be the constant <code>Combo.LIMIT</code>.
   * 
   * @return the text limit
   * 
   * @exception SWTException
   *              <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   *              </ul>
   */
  public int getTextLimit()
  {
    checkWidget();
    return text.getTextLimit();
  }

  /**
   * Gets the number of items that are visible in the drop down portion of the receiver's list.
   * 
   * @return the number of items that are visible
   * 
   * @exception SWTException
   *              <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   *              </ul>
   * 
   * @since 3.0
   */
  public int getVisibleItemCount()
  {
    checkWidget();
    return visibleItemCount;
  }

  void handleFocus(final int type)
  {
    if (isDisposed()) {
      return;
    }
    switch (type) {
      case SWT.FocusIn: {
        if (hasFocus) {
          return;
        }
        if (getEditable()) {
          text.selectAll();
        }
        hasFocus = true;
        final Shell shell = getShell();
        shell.removeListener(SWT.Deactivate, listener);
        shell.addListener(SWT.Deactivate, listener);
        final Display display = getDisplay();
        display.removeFilter(SWT.FocusIn, filter);
        display.addFilter(SWT.FocusIn, filter);
        final Event e = new Event();
        notifyListeners(SWT.FocusIn, e);
        break;
      }
      case SWT.FocusOut: {
        if (!hasFocus) {
          return;
        }
        final Control focusControl = getDisplay().getFocusControl();
        if (focusControl == arrow || focusControl == viewer.getTree() || focusControl == text) {
          return;
        }
        hasFocus = false;
        final Shell shell = getShell();
        shell.removeListener(SWT.Deactivate, listener);
        final Display display = getDisplay();
        display.removeFilter(SWT.FocusIn, filter);
        final Event e = new Event();
        notifyListeners(SWT.FocusOut, e);
        break;
      }
    }
  }

  void initAccessible()
  {
    final AccessibleAdapter accessibleAdapter = new AccessibleAdapter() {
      @Override
      public void getName(final AccessibleEvent e)
      {
        String name = null;
        final Label label = getAssociatedLabel();
        if (label != null) {
          name = stripMnemonic(label.getText());
        }
        e.result = name;
      }

      @Override
      public void getKeyboardShortcut(final AccessibleEvent e)
      {
        String shortcut = null;
        final Label label = getAssociatedLabel();
        if (label != null) {
          final String text = label.getText();
          if (text != null) {
            final char mnemonic = _findMnemonic(text);
            if (mnemonic != '\0') {
              shortcut = "Alt+" + mnemonic; //$NON-NLS-1$
            }
          }
        }
        e.result = shortcut;
      }

      @Override
      public void getHelp(final AccessibleEvent e)
      {
        e.result = getToolTipText();
      }
    };
    getAccessible().addAccessibleListener(accessibleAdapter);
    text.getAccessible().addAccessibleListener(accessibleAdapter);
    viewer.getTree().getAccessible().addAccessibleListener(accessibleAdapter);

    arrow.getAccessible().addAccessibleListener(new AccessibleAdapter() {
      @Override
      public void getName(final AccessibleEvent e)
      {
        e.result = isDropped() ? SWT.getMessage("SWT_Close") : SWT.getMessage("SWT_Open"); //$NON-NLS-1$ //$NON-NLS-2$
      }

      @Override
      public void getKeyboardShortcut(final AccessibleEvent e)
      {
        e.result = "Alt+Down Arrow"; //$NON-NLS-1$
      }

      @Override
      public void getHelp(final AccessibleEvent e)
      {
        e.result = getToolTipText();
      }
    });

    getAccessible().addAccessibleTextListener(new AccessibleTextAdapter() {
      @Override
      public void getCaretOffset(final AccessibleTextEvent e)
      {
        e.offset = text.getCaretPosition();
      }

      @Override
      public void getSelectionRange(final AccessibleTextEvent e)
      {
        final Point sel = text.getSelection();
        e.offset = sel.x;
        e.length = sel.y - sel.x;
      }
    });

    getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {
      @Override
      public void getChildAtPoint(final AccessibleControlEvent e)
      {
        final Point testPoint = toControl(e.x, e.y);
        if (getBounds().contains(testPoint)) {
          e.childID = ACC.CHILDID_SELF;
        }
      }

      @Override
      public void getLocation(final AccessibleControlEvent e)
      {
        final Rectangle location = getBounds();
        final Point pt = toDisplay(location.x, location.y);
        e.x = pt.x;
        e.y = pt.y;
        e.width = location.width;
        e.height = location.height;
      }

      @Override
      public void getChildCount(final AccessibleControlEvent e)
      {
        e.detail = 0;
      }

      @Override
      public void getRole(final AccessibleControlEvent e)
      {
        e.detail = ACC.ROLE_COMBOBOX;
      }

      @Override
      public void getState(final AccessibleControlEvent e)
      {
        e.detail = ACC.STATE_NORMAL;
      }

      @Override
      public void getValue(final AccessibleControlEvent e)
      {
        e.result = getText();
      }
    });

    text.getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {
      @Override
      public void getRole(final AccessibleControlEvent e)
      {
        e.detail = text.getEditable() ? ACC.ROLE_TEXT : ACC.ROLE_LABEL;
      }
    });

    arrow.getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {
      @Override
      public void getDefaultAction(final AccessibleControlEvent e)
      {
        e.result = isDropped() ? SWT.getMessage("SWT_Close") : SWT.getMessage("SWT_Open"); //$NON-NLS-1$ //$NON-NLS-2$
      }
    });
  }

  boolean isDropped()
  {
    return popup.getVisible();
  }

  @Override
  public boolean isFocusControl()
  {
    checkWidget();
    if (text.isFocusControl() || arrow.isFocusControl() || viewer.getTree().isFocusControl() || popup.isFocusControl()) {
      return true;
    }
    return super.isFocusControl();
  }

  void internalLayout(final boolean changed)
  {
    if (isDropped()) {
      dropDown(false);
    }
    final Rectangle rect = getClientArea();
    final int width = rect.width;
    final int height = rect.height;
    final Point arrowSize = arrow.computeSize(SWT.DEFAULT, height, changed);
    text.setBounds(0, 0, width - arrowSize.x, height);
    arrow.setBounds(width - arrowSize.x, 0, arrowSize.x, arrowSize.y);
  }

  void listEvent(final Event event)
  {
    switch (event.type) {
      case SWT.Dispose:
        if (getShell() != popup.getParent()) {
          final ISelection selection = viewer.getSelection();
          popup = null;
          viewer = null;
          createPopup(selection);
        }
        break;
      case SWT.FocusIn: {
        handleFocus(SWT.FocusIn);
        break;
      }
      case SWT.MouseDoubleClick: {
        dropDown(false);
        break;
      }
      case SWT.Traverse: {
        switch (event.detail) {
          case SWT.TRAVERSE_RETURN:
          case SWT.TRAVERSE_ESCAPE:
          case SWT.TRAVERSE_ARROW_PREVIOUS:
          case SWT.TRAVERSE_ARROW_NEXT:
            event.doit = false;
            break;
        }
        final Event e = new Event();
        e.time = event.time;
        e.detail = event.detail;
        e.doit = event.doit;
        e.character = event.character;
        e.keyCode = event.keyCode;
        notifyListeners(SWT.Traverse, e);
        event.doit = e.doit;
        event.detail = e.detail;
        break;
      }
      case SWT.KeyUp: {
        final Event e = new Event();
        e.time = event.time;
        e.character = event.character;
        e.keyCode = event.keyCode;
        e.stateMask = event.stateMask;
        notifyListeners(SWT.KeyUp, e);
        break;
      }
      case SWT.KeyDown: {
        if (event.character == SWT.ESC) {
          // Escape key cancels popup list
          dropDown(false);
        }
        if ((event.stateMask & SWT.ALT) != 0 && (event.keyCode == SWT.ARROW_UP || event.keyCode == SWT.ARROW_DOWN)) {
          dropDown(false);
        }
        if (event.character == SWT.CR) {
          // Enter causes default selection
          dropDown(false);
          final Event e = new Event();
          e.time = event.time;
          e.stateMask = event.stateMask;
          notifyListeners(SWT.DefaultSelection, e);
        }
        // At this point the widget may have been disposed.
        // If so, do not continue.
        if (isDisposed()) {
          break;
        }
        final Event e = new Event();
        e.time = event.time;
        e.character = event.character;
        e.keyCode = event.keyCode;
        e.stateMask = event.stateMask;
        notifyListeners(SWT.KeyDown, e);
        break;

      }
    }
  }

  void popupEvent(final Event event)
  {
    switch (event.type) {
      case SWT.Paint:
        // draw black rectangle around list
        final Rectangle listRect = viewer.getTree().getBounds();
        final Color black = getDisplay().getSystemColor(SWT.COLOR_BLACK);
        event.gc.setForeground(black);
        event.gc.drawRectangle(0, 0, listRect.width + 1, listRect.height + 1);
        break;
      case SWT.Close:
        event.doit = false;
        dropDown(false);
        break;
      case SWT.Deactivate:
        dropDown(false);
        break;
    }
  }

  @Override
  public void redraw()
  {
    super.redraw();
    text.redraw();
    arrow.redraw();
    if (popup.isVisible()) {
      viewer.getTree().redraw();
    }
  }

  @Override
  public void redraw(final int x, final int y, final int width, final int height, final boolean all)
  {
    super.redraw(x, y, width, height, true);
  }

  /**
   * Removes the listener from the collection of listeners who will be notified when the receiver's text is modified.
   * 
   * @param listener
   *          the listener which should no longer be notified
   * 
   * @exception IllegalArgumentException
   *              <ul>
   *              <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
   *              </ul>
   * @exception SWTException
   *              <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   *              </ul>
   * 
   * @see ModifyListener
   * @see #addModifyListener
   */
  public void removeModifyListener(final ModifyListener listener)
  {
    checkWidget();
    if (listener == null) {
      SWT.error(SWT.ERROR_NULL_ARGUMENT);
    }
    removeListener(SWT.Modify, listener);
  }

  /**
   * Removes the listener from the collection of listeners who will be notified when the receiver's selection changes.
   * 
   * @param listener
   *          the listener which should no longer be notified
   * 
   * @exception IllegalArgumentException
   *              <ul>
   *              <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
   *              </ul>
   * @exception SWTException
   *              <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   *              </ul>
   * 
   * @see SelectionListener
   * @see #addSelectionListener
   */
  public void removeSelectionListener(final SelectionListener listener)
  {
    checkWidget();
    if (listener == null) {
      SWT.error(SWT.ERROR_NULL_ARGUMENT);
    }
    removeListener(SWT.Selection, listener);
    removeListener(SWT.DefaultSelection, listener);
  }

  @Override
  public void setBackground(final Color color)
  {
    super.setBackground(color);
    background = color;
    if (text != null) {
      text.setBackground(color);
    }
    if (viewer != null) {
      viewer.getTree().setBackground(color);
    }
    if (arrow != null) {
      arrow.setBackground(color);
    }
  }

  /**
   * Sets the editable state.
   * 
   * @param editable
   *          the new editable state
   * 
   * @exception SWTException
   *              <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   *              </ul>
   * 
   * @since 3.0
   */
  public void setEditable(final boolean editable)
  {
    checkWidget();
    text.setEditable(editable);
  }

  @Override
  public void setEnabled(final boolean enabled)
  {
    super.setEnabled(enabled);
    if (popup != null) {
      popup.setVisible(false);
    }
    if (text != null) {
      text.setEnabled(enabled);
    }
    if (arrow != null) {
      arrow.setEnabled(enabled);
    }
  }

  @Override
  public boolean setFocus()
  {
    checkWidget();
    return text.setFocus();
  }

  @Override
  public void setFont(final Font font)
  {
    super.setFont(font);
    this.font = font;
    text.setFont(font);
    viewer.getTree().setFont(font);
    internalLayout(true);
  }

  @Override
  public void setForeground(final Color color)
  {
    super.setForeground(color);
    foreground = color;
    if (text != null) {
      text.setForeground(color);
    }
    if (viewer != null) {
      viewer.getTree().setForeground(color);
    }
    if (arrow != null) {
      arrow.setForeground(color);
    }
  }

  /**
   * Sets the layout which is associated with the receiver to be the argument which may be null.
   * <p>
   * Note: No Layout can be set on this Control because it already manages the size and position of its children.
   * </p>
   * 
   * @param layout
   *          the receiver's new layout or null
   * 
   * @exception SWTException
   *              <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   *              </ul>
   */
  @Override
  public void setLayout(final Layout layout)
  {
    checkWidget();
    return;
  }

  @Override
  public void setMenu(final Menu menu)
  {
    text.setMenu(menu);
  }

  public ISelection getSelection()
  {
    if (viewer != null) {
      return viewer.getSelection();
    }
    return null;
  }

  public void setSelection(final ISelection selection)
  {
    if (viewer != null) {
      viewer.setSelection(selection);
    }
  }

  /**
   * Sets the contents of the receiver's text field to the given string.
   * <p>
   * Note: The text field in a <code>Combo</code> is typically only capable of displaying a single line of text. Thus,
   * setting the text to a string containing line breaks or other special characters will probably cause it to display
   * incorrectly.
   * </p>
   * 
   * @param string
   *          the new text
   * 
   * @exception IllegalArgumentException
   *              <ul>
   *              <li>ERROR_NULL_ARGUMENT - if the string is null</li>
   *              </ul>
   * @exception SWTException
   *              <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   *              </ul>
   */
  public void setText(final String string)
  {
    checkWidget();
    if (string == null) {
      SWT.error(SWT.ERROR_NULL_ARGUMENT);
    }
    text.setText(string);
    text.selectAll();
  }

  /**
   * Sets the maximum number of characters that the receiver's text field is capable of holding to be the argument.
   * 
   * @param limit
   *          new text limit
   * 
   * @exception IllegalArgumentException
   *              <ul>
   *              <li>ERROR_CANNOT_BE_ZERO - if the limit is zero</li>
   *              </ul>
   * @exception SWTException
   *              <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   *              </ul>
   */
  public void setTextLimit(final int limit)
  {
    checkWidget();
    text.setTextLimit(limit);
  }

  @Override
  public void setToolTipText(final String string)
  {
    checkWidget();
    super.setToolTipText(string);
    arrow.setToolTipText(string);
    text.setToolTipText(string);
  }

  @Override
  public void setVisible(final boolean visible)
  {
    super.setVisible(visible);
    if (!visible) {
      popup.setVisible(false);
    }
  }

  /**
   * Sets the number of items that are visible in the drop down portion of the receiver's list.
   * 
   * @param count
   *          the new number of items to be visible
   * 
   * @exception SWTException
   *              <ul>
   *              <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
   *              <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
   *              </ul>
   * 
   * @since 3.0
   */
  public void setVisibleItemCount(final int count)
  {
    checkWidget();
    if (count < 0) {
      return;
    }
    visibleItemCount = count;
  }

  String stripMnemonic(final String string)
  {
    int index = 0;
    final int length = string.length();
    do {
      while (index < length && string.charAt(index) != '&') {
        index++;
      }
      if (++index >= length) {
        return string;
      }
      if (string.charAt(index) != '&') {
        return string.substring(0, index - 1) + string.substring(index, length);
      }
      index++;
    } while (index < length);
    return string;
  }

  void textEvent(final Event event)
  {
    switch (event.type) {
      case SWT.FocusIn: {
        handleFocus(SWT.FocusIn);
        break;
      }
      case SWT.KeyDown: {
        if (event.character == SWT.CR) {
          dropDown(false);
          final Event e = new Event();
          e.time = event.time;
          e.stateMask = event.stateMask;
          notifyListeners(SWT.DefaultSelection, e);
        }
        // At this point the widget may have been disposed.
        // If so, do not continue.
        if (isDisposed()) {
          break;
        }

        if (event.keyCode == SWT.ARROW_UP || event.keyCode == SWT.ARROW_DOWN) {
          event.doit = false;
          final boolean dropped = isDropped();
          text.selectAll();
          if (!dropped) {
            setFocus();
          }
          dropDown(!dropped);
          break;
        }

        // Further work : Need to add support for incremental search in
        // pop up list as characters typed in text widget

        final Event e = new Event();
        e.time = event.time;
        e.character = event.character;
        e.keyCode = event.keyCode;
        e.stateMask = event.stateMask;
        notifyListeners(SWT.KeyDown, e);
        break;
      }
      case SWT.KeyUp: {
        final Event e = new Event();
        e.time = event.time;
        e.character = event.character;
        e.keyCode = event.keyCode;
        e.stateMask = event.stateMask;
        notifyListeners(SWT.KeyUp, e);
        break;
      }
      case SWT.MenuDetect: {
        final Event e = new Event();
        e.time = event.time;
        notifyListeners(SWT.MenuDetect, e);
        break;
      }
      case SWT.Modify: {
        final Event e = new Event();
        e.time = event.time;
        notifyListeners(SWT.Modify, e);
        break;
      }
      case SWT.MouseDown: {
        if (event.button != 1) {
          return;
        }
        if (text.getEditable()) {
          return;
        }
        final boolean dropped = isDropped();
        text.selectAll();
        if (!dropped) {
          setFocus();
        }
        dropDown(!dropped);
        break;
      }
      case SWT.MouseUp: {
        if (event.button != 1) {
          return;
        }
        if (text.getEditable()) {
          return;
        }
        text.selectAll();
        break;
      }
      case SWT.Traverse: {
        switch (event.detail) {
          case SWT.TRAVERSE_RETURN:
          case SWT.TRAVERSE_ARROW_PREVIOUS:
          case SWT.TRAVERSE_ARROW_NEXT:
            // The enter causes default selection and
            // the arrow keys are used to manipulate the list contents so
            // do not use them for traversal.
            event.doit = false;
            break;
        }

        final Event e = new Event();
        e.time = event.time;
        e.detail = event.detail;
        e.doit = event.doit;
        e.character = event.character;
        e.keyCode = event.keyCode;
        notifyListeners(SWT.Traverse, e);
        event.doit = e.doit;
        event.detail = e.detail;
        break;
      }
    }
  }

}
