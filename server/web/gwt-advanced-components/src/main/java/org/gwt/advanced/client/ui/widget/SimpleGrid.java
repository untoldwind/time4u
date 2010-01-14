/*
 * Copyright 2010 Sergey Skladchikov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gwt.advanced.client.ui.widget;

import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.*;
import org.gwt.advanced.client.ui.Resizable;
import org.gwt.advanced.client.ui.TableDoubleClickListener;
import org.gwt.advanced.client.util.GWTUtil;

import java.util.List;

/**
 * This is a super class for all advanced grids.<p/>
 * It represents a grid as a pair of flex tables: header and body. And delegates method calls to these parts.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.4.5
 */
public class SimpleGrid extends AdvancedFlexTable implements Resizable {
    /** header container */
    private AdvancedFlexTable headerTable;
    /** body container */
    private AdvancedFlexTable bodyTable;
    /** body scroll panel */
    private ScrollPanel scrollPanel;
    /** flag meaning that the grid has already benen initialized */
    private boolean initialized;
    /**
     * a flag that means whether the grid columns are resizable
     */
    private boolean resizable;
    /**
     * a flag meaning whether the grid has resizable columns
     */
    private boolean columnResizingAllowed = true;

    /** Constructs a new SimpleGrid. */
    public SimpleGrid() {
        this(true);
    }

    /**
     * Creates an instance of this class and does nothing else.
     *  
     * @param resizable is a resizable option flag.
     */
    public SimpleGrid(boolean resizable) {
        super.setWidget(0, 0, getHeaderTable());
        Element tr = getRowFormatter().getElement(0);
        DOM.setStyleAttribute(tr, "border", "0");
        DOM.setStyleAttribute(tr, "padding", "0");
        DOM.setStyleAttribute(tr, "margin", "0");
        getCellFormatter().setStyleName(0, 0, "header-table-cell");
        getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
        getHeaderTable().setStyleName("advanced-Grid");
        getHeaderTable().setCellSpacing(0);
        getHeaderTable().setCellPadding(0);

        super.setWidget(1, 0, getScrollPanel());
        ((ScrollPanel)getScrollPanel()).setWidget(getBodyTable());
        tr = getRowFormatter().getElement(1);
        DOM.setStyleAttribute(tr, "border", "0");
        DOM.setStyleAttribute(tr, "padding", "0");
        DOM.setStyleAttribute(tr, "margin", "0");
        getCellFormatter().setStyleName(1, 0, "body-table-cell");
        getCellFormatter().setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_TOP);
        getBodyTable().setStyleName("advanced-Grid");
        getBodyTable().setCellSpacing(0);
        getBodyTable().setCellPadding(0);

        DOM.setStyleAttribute(getElement(), "border", "0");
        DOM.setStyleAttribute(getElement(), "padding", "0");
        DOM.setStyleAttribute(getElement(), "margin", "0");

        setCellSpacing(0);
        setCellPadding(0);
        setResizable(resizable);

        if (isResizable()) {
            Element thead = getTHeadElement();
            DOM.sinkEvents(thead, Event.MOUSEEVENTS);
            DOM.setEventListener(thead, new ResizeListener(this));
        }

        this.initialized  = true;
    }

    /**
     * This method resizes the grid making it to fit as much space as possible.
     */
    public void resize() {
        Element parent = DOM.getParent(getElement());
        if (parent == null || !isResizable() || !isAttached())
            return;

        GWTUtil.adjustWidgetSize(this, parent, false);
        getScrollPanel().remove(getBodyTable());
        GWTUtil.adjustWidgetSize(getScrollPanel(), DOM.getParent(getScrollPanel().getElement()), isScrollable());
        getScrollPanel().add(getBodyTable());
        parent = getScrollPanel().getElement();

        GWTUtil.adjustWidgetSize(getHeaderTable(), DOM.getParent(getHeaderTable().getElement()), false);
        GWTUtil.adjustWidgetSize(getBodyTable(), parent, false);

        if (getBodyTable().getRowCount() > 0) {
            int parentWidth = DOM.getElementPropertyInt(parent, "offsetWidth");
            int count = getBodyTable().getCellCount(0);
            int size = parentWidth / count;
            for (int i = 0; i < count; i++)
                setColumnWidth(i, size);

            int offsetWidth = 0;
            for (int i = 0; i < count - 1; i++) {
                Element th = DOM.getChild(DOM.getChild(getTHeadElement(), 0), i);
                offsetWidth += DOM.getElementPropertyInt(th, "offsetWidth");
            }

            setColumnWidth(count - 1, parentWidth - offsetWidth);
        }
    }

    /**
     * Sets header width property to the specified value.<p/>
     * Use this method only for those grids that mustn't be auto resisable (see {@link #setResizable(boolean)}).
     *
     * @param width is a value of the property.
     */
    public void setHeaderWidth(String width) {
        DOM.setStyleAttribute(DOM.getParent(getScrollPanel().getElement()), "width", width);
    }

    /**
     * Sets header height property to the specified value.<p/>
     * This method is rather rarely used. Typically you don't have to invoke it.
     *
     * @param height is a value of the property.
     */
    public void setHeaderHeight(String height) {
        DOM.setStyleAttribute(DOM.getParent(getHeaderTable().getElement()), "height", height);
    }

    /**
     * Sets body width property to the specified value.<p/>
     * Use this method only for those grids that mustn't be auto resisable (see {@link #setResizable(boolean)}).
     *
     * @param width is a value of the property.
     */
    public void setBodyWidth(String width) {
        DOM.setStyleAttribute(DOM.getParent(getScrollPanel().getElement()), "width", width);
    }

    /**
     * Sets body height property to the specified value.<p/>
     * This method might be very useful when you need to enebale vertical scrolling
     * (see {@link #enableVerticalScrolling(boolean)}).
     *
     * @param height is a value of the property.
     */
    public void setBodyHeight(String height) {
        DOM.setStyleAttribute(DOM.getParent(getScrollPanel().getElement()), "height", height);
    }

    /** {@inheritDoc} */
    public void setHeaderWidget(int column, Widget widget) {
        getHeaderTable().setHeaderWidget(column, widget);
        Element tr = DOM.getChild(getTHeadElement(), 0);
        Element th = DOM.getChild(tr, column);
        if (isResizable()) {
            DOM.setStyleAttribute(th, "overflow", "hidden");
            DOM.setStyleAttribute(th, "whiteSpace", "nowrap");
        }
    }

    /** {@inheritDoc} */                              
    public void removeHeaderWidget(int column) {
        getHeaderTable().removeHeaderWidget(column);
    }

    /** {@inheritDoc} */
    public void enableVerticalScrolling(boolean enabled) {
        if (isScrollable() != enabled) {
            super.enableVerticalScrolling(enabled);
            resize();
        }
    }

    /** {@inheritDoc} */
    protected void prepareScrolling(boolean enabled) {
        if (enabled) {
            DOM.setStyleAttribute(getScrollPanel().getElement(), "overflowY", "auto");
        } else {
            DOM.setStyleAttribute(getScrollPanel().getElement(), "overflowX", "visible");
        }

        this.initialized = false;
        Element td = getCellFormatter().getElement(1, 0);
        DOM.setStyleAttribute(td, "border", "0");
        DOM.setStyleAttribute(td, "padding", "0");
        DOM.setStyleAttribute(td, "margin", "0");

        getCellFormatter().setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_TOP);
        this.initialized = true;
    }

    /** {@inheritDoc} */
    protected int getTableHeight() {
        return DOM.getElementPropertyInt(getBodyElement(), "offsetHeight")
                + DOM.getElementPropertyInt(getTHeadElement(), "offsetHeight");
    }

    /** {@inheritDoc} */
    protected int getTableWidth() {
        return getBodyTable().getTableWidth();
    }

    /** {@inheritDoc} */
    protected void prepareHeaderCell(int column) {
        getHeaderTable().prepareHeaderCell(column);
    }

    /** {@inheritDoc} */
    protected void addHeaderCells(Element tHead, int num) {
        getHeaderTable().addHeaderCells(tHead, num);
    }

    /** {@inheritDoc} */
    public void insertHeaderCell(int column) {
        getHeaderTable().insertHeaderCell(column);
    }

    /** {@inheritDoc} */
    public Element getTHeadElement() {
        return getHeaderTable().getTHeadElement();
    }

    /** {@inheritDoc} */
    protected List getHeaderWidgets() {
        return getHeaderTable().getHeaderWidgets();
    }

    /** {@inheritDoc} */
    protected Panel getScrollPanel() {
        if (scrollPanel == null) {
            scrollPanel = new ScrollPanel(getBodyTable());
            DOM.setStyleAttribute(scrollPanel.getElement(), "overflow", "");
            DOM.setStyleAttribute(scrollPanel.getElement(), "overflowX", "hidden");
            DOM.setStyleAttribute(scrollPanel.getElement(), "overflowY", "auto");
        }
        return scrollPanel;
    }

    /** {@inheritDoc} */
    public void addCell(int row) {
        if (initialized)
            getBodyTable().addCell(row);
        else
            super.addCell(row);
    }

    /** {@inheritDoc} */
    public int getCellCount(int row) {
        if (initialized)
            return getBodyTable().getCellCount(row);
        else
            return super.getCellCount(row);
    }

    /** {@inheritDoc} */
    public FlexCellFormatter getFlexCellFormatter() {
        if (initialized)
            return getBodyTable().getFlexCellFormatter();
        else
            return super.getFlexCellFormatter();
    }

    /** {@inheritDoc} */
    public int getRowCount() {
        if (initialized)
            return getBodyTable().getRowCount();
        else
            return super.getRowCount();
    }

    /** {@inheritDoc} */
    public void insertCell(int beforeRow, int beforeColumn) {
        if (initialized)
            getBodyTable().insertCell(beforeRow, beforeColumn);
        else
            super.insertCell(beforeRow, beforeColumn);
    }

    /** {@inheritDoc} */
    public int insertRow(int beforeRow) {
        if (initialized)
            return getBodyTable().insertRow(beforeRow);
        else
            return super.insertRow(beforeRow);
    }

    /** {@inheritDoc} */
    public void removeCell(int row, int col) {
        if (initialized)
            getBodyTable().removeCell(row, col);
        else
            super.removeCell(row, col);
    }

    /** {@inheritDoc} */
    public void removeCells(int row, int column, int num) {
        if (initialized)
            getBodyTable().removeCells(row, column, num);
        else
            super.removeCells(row, column, num);
    }

    /** {@inheritDoc} */
    public void removeRow(int row) {
        if (initialized)
            getBodyTable().removeRow(row);
        else
            super.removeRow(row);
    }

    /** {@inheritDoc} */
    protected void prepareCell(int row, int column) {
        if (initialized)
            getBodyTable().prepareCell(row, column);
        else
            super.prepareCell(row, column);
    }

    /** {@inheritDoc} */
    protected void prepareRow(int row) {
        if (initialized)
            getBodyTable().prepareRow(row);
        else
            super.prepareRow(row);
    }

    /** {@inheritDoc} */
    public void addTableListener(TableListener listener) {
        if (initialized)
            getBodyTable().addTableListener(listener);
        else
            super.addTableListener(listener);
    }

    /** {@inheritDoc} */
    public void addDoubleClickListener(TableDoubleClickListener listener) {
        if (initialized)
            getBodyTable().addDoubleClickListener(listener);
        else
            super.addDoubleClickListener(listener);
    }

    /** {@inheritDoc} */
    public void removeDoubleClickListener(TableDoubleClickListener listener) {
        if (initialized)
            getBodyTable().removeDoubleClickListener(listener);
        else
            super.removeDoubleClickListener(listener);
    }

    /** {@inheritDoc} */
    public void clear() {
        if (initialized) {
            getBodyTable().clear();
            getHeaderTable().clear();
        } else
            super.clear();
    }

    /** {@inheritDoc} */
    public boolean clearCell(int row, int column) {
        if (initialized)
            return getBodyTable().clearCell(row, column);
        else
            return super.clearCell(row, column);
    }

    /** {@inheritDoc} */
    public CellFormatter getCellFormatter() {
        if (initialized)
            return getBodyTable().getCellFormatter();
        else
            return super.getCellFormatter();
    }

    /** {@inheritDoc} */
    public int getCellPadding() {
        if (initialized)
            return getBodyTable().getCellPadding();
        else
            return super.getCellPadding();
    }

    /** {@inheritDoc} */
    public int getCellSpacing() {
        if (initialized)
            return getBodyTable().getCellSpacing();
        else
            return super.getCellSpacing();
    }

    /** {@inheritDoc} */
    public ColumnFormatter getColumnFormatter() {
        if (initialized)
            return getBodyTable().getColumnFormatter();
        else
            return super.getColumnFormatter();
    }

    /** {@inheritDoc} */
    public RowFormatter getRowFormatter() {
        if (initialized)
            return getBodyTable().getRowFormatter();
        else
            return super.getRowFormatter();
    }

    /** {@inheritDoc} */
    public String getText(int row, int column) {
        if (initialized)
            return getBodyTable().getText(row, column);
        else
            return super.getText(row, column);
    }

    /** {@inheritDoc} */
    public boolean isCellPresent(int row, int column) {
        if (initialized)
            return getBodyTable().isCellPresent(row, column);
        else
            return super.isCellPresent(row, column);
    }

    /** {@inheritDoc} */
    public boolean remove(Widget widget) {
        if (initialized)
            return getBodyTable().remove(widget) || getHeaderTable().remove(widget);
        else
            return super.remove(widget);
    }

    /** {@inheritDoc} */
    public void onBrowserEvent(Event event) {
        if (initialized)
            getBodyTable().onBrowserEvent(event);
        else
            super.onBrowserEvent(event);
    }

    /** {@inheritDoc} */
    public void removeTableListener(TableListener listener) {
        if (initialized)
            getBodyTable().removeTableListener(listener);
        else
            super.removeTableListener(listener);
    }

    /** {@inheritDoc} */
    public void setBorderWidth(int width) {
        if (initialized)
            getBodyTable().setBorderWidth(width);
        else
            super.setBorderWidth(width);
    }

    /** {@inheritDoc} */
    public void setCellSpacing(int spacing) {
        if (initialized)
            getBodyTable().setCellSpacing(spacing);
        else
            super.setCellSpacing(spacing);
    }

    /** {@inheritDoc} */
    public void setCellPadding(int padding) {
        if (initialized)
            getBodyTable().setCellPadding(padding);
        else
            super.setCellPadding(padding);
    }

    /** {@inheritDoc} */
    public void setText(int row, int column, String text) {
        if (initialized) {
            getBodyTable().setText(row, column, text);
            wrapContent(row, column);
        } else
            super.setText(row, column, text);
    }

    /** {@inheritDoc} */
    public void setWidget(int row, int column, Widget widget) {
        if (initialized) {
            getBodyTable().setWidget(row, column, widget);
            wrapContent(row, column);
        } else
            super.setWidget(row, column, widget);
    }

    /**
     * This method sets column size in pixels.
     *
     * @param column is a column number.
     * @param size is a size value in pixels.
     */                                          
    public void setColumnWidth(int column, int size) {
        if (isResizable()) {
            if (size < 0)
                size = 0;
            Element th = getThElement(column);
            DOM.setStyleAttribute(th, "width", size + "px");
            if (getRowCount() > 0) {
                HTMLTable.CellFormatter formatter = getBodyTable().getCellFormatter();
                Element td = formatter.getElement(0, column);
                DOM.setStyleAttribute(td, "width", size + "px");

                for (int i = 1; !GWTUtil.isIE() && i < getRowCount(); i++) {
                    td = formatter.getElement(i, column);
                    DOM.setStyleAttribute(td, "width", size + "px");
                }
            }
        }
    }

    /**
     * This method gets a TH element.
     *
     * @param column is a column number.
     * @return an element.
     */
    protected Element getThElement(int column) {
        Element tr = DOM.getChild(getHeaderTable().getTHeadElement(), 0);
        return DOM.getChild(tr, column);
    }

    /**
     * This method wraps cell content into the special styles which are responsible for cell clipping.
     *
     * @param row is a row number.
     * @param column is a column number.
     * @return a wrapping td element.
     */
    protected Element wrapContent(int row, int column) {
        if (!isResizable())
            return null;
      
        Element td = getBodyTable().getCellFormatter().getElement(row, column);
        DOM.setStyleAttribute(td, "overflow", "hidden");
        DOM.setStyleAttribute(td, "whiteSpace", "nowrap");
        return td;
    }

    /** {@inheritDoc} */
    protected void checkCellBounds(int row, int column) {
        if (initialized)
            getBodyTable().checkCellBounds(row, column);
        else
            super.checkCellBounds(row, column);    
    }

    /** {@inheritDoc} */
    protected void checkRowBounds(int row) {
        if (initialized)
            getBodyTable().checkRowBounds(row);
        else
            super.checkRowBounds(row);
    }

    /** {@inheritDoc} */
    protected Element createCell() {
        if (initialized)
            return getBodyTable().createCell();
        else
            return super.createCell();
    }

    /** {@inheritDoc} */
    protected int getDOMCellCount(Element tableBody, int row) {
        if (initialized)
            return getBodyTable().getDOMCellCount(tableBody, row);
        else
            return super.getDOMCellCount(tableBody, row);
    }

    /** {@inheritDoc} */
    protected int getDOMCellCount(int row) {
        if (initialized)
            return getBodyTable().getDOMCellCount(row);
        else
            return super.getDOMCellCount(row);
    }

    /** {@inheritDoc} */
    protected int getDOMRowCount() {
        if (initialized)
            return getBodyTable().getDOMRowCount();
        else
            return super.getDOMRowCount();
    }

    /** {@inheritDoc} */
    protected int getDOMRowCount(Element elem) {
        if (initialized)
            return getBodyTable().getDOMRowCount(elem);
        else
            return super.getDOMRowCount(elem);
    }

    /** {@inheritDoc} */
    protected Element getEventTargetCell(Event event) {
        if (initialized)
            return getBodyTable().getEventTargetCell(event);
        else
            return super.getEventTargetCell(event);
    }

    /** {@inheritDoc} */
    protected void insertCells(int row, int column, int count) {
        if (initialized)
            getBodyTable().insertCells(row, column, count);
        else
            super.insertCells(row, column, count);
    }

    /** {@inheritDoc} */
    protected boolean internalClearCell(Element td, boolean clearInnerHTML) {
        if (initialized)
            return getBodyTable().internalClearCell(td, clearInnerHTML);
        else
            return super.internalClearCell(td, clearInnerHTML);
    }

    /** {@inheritDoc} */
    protected void prepareColumn(int column) {
        if (initialized)
            getBodyTable().prepareColumn(column);
        else
            super.prepareColumn(column);
    }

    /** {@inheritDoc} */
    public String getHTML(int row, int column) {
        if (initialized)
            return getBodyTable().getHTML(row, column);
        else
            return super.getHTML(row, column);
    }

    /** {@inheritDoc} */
    public void setHTML(int row, int column, String html) {
        if (initialized)
            getBodyTable().setHTML(row, column, html);
        else
            super.setHTML(row, column, html);
    }

    /** {@inheritDoc} */
    public Widget getWidget(int row, int column) {
        if (initialized)
            return getBodyTable().getWidget(row, column);
        else
            return super.getWidget(row, column);
    }

    /**
     * Getter for property 'headerTable'.
     *
     * @return Value for property 'headerTable'.
     */
    protected AdvancedFlexTable getHeaderTable() {
        if (headerTable == null)
            headerTable = new AdvancedFlexTable(true);
        return headerTable;
    }

    /**
     * Getter for property 'bodyTable'.
     *
     * @return Value for property 'bodyTable'.
     */
    protected AdvancedFlexTable getBodyTable() {
        if (bodyTable == null)
            bodyTable = new AdvancedFlexTable();
        return bodyTable;
    }

    /**
     * Getter for property 'resizable'.
     *
     * @return Value for property 'resizable'.
     */
    public boolean isResizable() {
        return resizable;
    }

    /**
     * Sets the resizability of column flag.<p/>
     * This method also sets table-layout style.
     *
     * @param resizable resizability flag value.
     */
    public void setResizable(boolean resizable) {
        this.resizable = resizable;
        makeResizable(resizable);
    }

    /**
     * Enables or disables columns resizability.
     *
     * @param resizable a flag to enable or disable resizable columns.
     */
    protected void makeResizable(boolean resizable) {
        if (resizable){
            DOM.setStyleAttribute(getBodyTable().getElement(), "tableLayout", "fixed");
            DOM.setStyleAttribute(getHeaderTable().getElement(), "tableLayout", "fixed");
            DOM.setStyleAttribute(getElement(), "tableLayout", "fixed");
        } else {
            DOM.setStyleAttribute(getBodyTable().getElement(), "tableLayout", "");
            DOM.setStyleAttribute(getHeaderTable().getElement(), "tableLayout", "");
            DOM.setStyleAttribute(getElement(), "tableLayout", "");
        }
    }

    /**
     * This method returns column resizability flag.
     *
     * @return a flag value.
     */
    public boolean isColumnResizingAllowed() {
        return isResizable() && columnResizingAllowed;
    }

    /**
     * This method switches on / off column resizability.
     *
     * @param columnResizingAllowed is a flag value.
     */
    public void setColumnResizingAllowed(boolean columnResizingAllowed) {
        this.columnResizingAllowed = columnResizingAllowed;
    }

    /**
     * This listener is invoked on different column resizing events.
     *
     * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
     */
    protected class ResizeListener implements EventListener {
        /** currently selected TH element */
        private Element th;
        /** start mouse position */
        private int startX;
        /** current X position of the cursor */
        private int currentX;
        /** the timer that check current cursor position and resizes columns */
        private ResizeTimer timer = new ResizeTimer(this);
        /** link to the simple grid */
        private SimpleGrid grid;

        /**
         * Creates an instnace of this class and saves the grid into the internal field.
         *
         * @param grid is a link to the owner.
         */
        public ResizeListener(SimpleGrid grid) {
            this.grid = grid;
        }

        /**
         * This method handles all mouse events related to resizing.
         *
         * @param event is an event.
         */
        public void onBrowserEvent(Event event) {
            SimpleGrid grid = getGrid();

            if (grid.isColumnResizingAllowed()) {
                if (DOM.eventGetType(event) == Event.ONMOUSEDOWN) {
                    startResizing(event);
                } else if (DOM.eventGetType(event) == Event.ONMOUSEUP && th != null) {
                    stopResizing();
                } else if (DOM.eventGetType(event) == Event.ONMOUSEOUT && th != null) {
                    interruptResizing(event);
                }
            }
            if (DOM.eventGetType(event) == Event.ONMOUSEMOVE)
                setCursor(event);
        }

        /**
         * Sets a cursor style.
         *
         * @param event is an event.
         */
        protected void setCursor(Event event) {
            SimpleGrid grid = getGrid();
            Element th = getTh(event);
            if (this.th != null || grid.isColumnResizingAllowed() && isOverBorder(event, th)) {
                DOM.setStyleAttribute(DOM.eventGetTarget(event), "cursor", "e-resize");
                this.currentX = getPositionX(event);
                timer.schedule(20);
            } else
                DOM.setStyleAttribute(DOM.eventGetTarget(event), "cursor", "");
        }

        /**
         * This method interrupts resiszing.
         *
         * @param event is an event.
         */
        protected void interruptResizing(Event event) {
            SimpleGrid grid = getGrid();
            int positionX = getPositionX(event);
            int positionY = getPositionY(event);
            Element thead = grid.getTHeadElement();
            int left = DOM.getAbsoluteLeft(thead);
            int top = DOM.getAbsoluteTop(thead);
            int width = getElementWidth(thead);
            int height = getElementHeight(thead);

            if (positionX < left || positionX > left + width || positionY < top || positionY > top + height) {
                th = null;
                timer.cancel();
            }
        }

        /**
         * This method normally stops resisng and changes column width.
         */
        protected void stopResizing() {
            resize();
            timer.cancel();
            th = null;
        }

        /**
         * Resizes selected and sibling columns.
         */
        protected void resize() {
            int position = this.currentX;
            int delta = position - startX;
            Element tr = DOM.getParent(th);
            int left = DOM.getAbsoluteLeft(th);
            int width = getElementWidth(th);

            Element sibling = null;
            int sign = 0;
            int thIndex = DOM.getChildIndex(tr, th);

            if (startX <= left + 2) {
                sign = 1;
                sibling = DOM.getChild(tr, thIndex - 1);
            } else if (startX >= left + width - 2) {
                sign = -1;
                sibling = DOM.getChild(tr, thIndex + 1);
            }

            SimpleGrid grid = getGrid();
            int thExpectedWidth = width - sign * delta;

            int siblingExpectedWidth;
            int siblingIndex;
            if (sibling != null) {
                siblingExpectedWidth = getElementWidth(sibling) + sign * delta;
                siblingIndex = DOM.getChildIndex(tr, sibling);
            } else {
                siblingExpectedWidth = 0;
                siblingIndex = -1;
            }

            //interrupt immediately
            if (thExpectedWidth < 3 || siblingExpectedWidth < 3 && siblingIndex > -1) {
                th = null;
                return;
            }

            if (siblingIndex > -1) {
                grid.setColumnWidth(thIndex, thExpectedWidth);
                grid.setColumnWidth(siblingIndex, siblingExpectedWidth);
            }

            int thWidthNow = getElementWidth(th);

            int siblingWidthNow;
            if (siblingIndex > -1)
                siblingWidthNow = getElementWidth(sibling);
            else
                siblingWidthNow = 0;

            if (thWidthNow > thExpectedWidth)
                grid.setColumnWidth(thIndex, 2 * thExpectedWidth - thWidthNow);
            if (siblingWidthNow > siblingExpectedWidth && siblingIndex > -1)
                grid.setColumnWidth(siblingIndex, 2 * siblingExpectedWidth - siblingWidthNow);
            this.startX = position;
        }

        /**
         * This method starts column resizing.
         *
         * @param event is an event.
         */
        protected void startResizing(Event event) {
            if ("e-resize".equalsIgnoreCase(DOM.getStyleAttribute(DOM.eventGetTarget(event), "cursor"))) {
                th = getTh(event);
                startX = getPositionX(event);
                currentX = startX;
                if (!isOverBorder(event, th))
                    th = null;
            }
        }

        /**
         * Gets element width.
         *
         * @param element is an element
         * @return width in pixels
         */
        protected int getElementWidth(Element element) {
            return DOM.getElementPropertyInt(element, "offsetWidth");
        }

        /**
         * Gets element height.
         *
         * @param element is an element
         * @return height in pizels.
         */
        protected int getElementHeight(Element element) {
            return DOM.getElementPropertyInt(element, "offsetHeight");
        }

        /**
         * Gets mouse X position.
         *
         * @param event is an event.
         * @return X position in pixels.
         */
        protected int getPositionY(Event event) {
            return DOM.eventGetClientY(event);
        }

        /**
         * Gets mouse Y position.
         *
         * @param event is an event.
         * @return Y position in pixels.
         */
        protected int getPositionX(Event event) {
            return DOM.eventGetClientX(event);
        }

        /**
         * This method looks for the TH element starting from the element which produced the event.
         *
         * @param event is an event.
         * @return a TH element or <code>null</code> if there is no such element.
         */
        protected Element getTh(Event event) {
            Element element = DOM.eventGetTarget(event);
            while (element != null && !DOM.getElementProperty(element, "tagName").equalsIgnoreCase("th"))
                element = DOM.getParent(element);
            return element;
        }

        /**
         * This method detects whether the cursor is over the border between columns.
         *
         * @param event is an event.
         * @param th is TH element.
         * @return a result of check.
         */
        protected boolean isOverBorder(Event event, Element th) {
            int position = getPositionX(event);
            int left = DOM.getAbsoluteLeft(th);
            int width = getElementWidth(th);
            int index = DOM.getChildIndex(DOM.getParent(th), th);

            return position <= left + 1 && index > 0
                   || position >= left + width - 1 && index < DOM.getChildCount(DOM.getParent(th)) - 1;
        }

        /**
         * Gets a link to the grid.
         *
         * @return a grid that initializes this listener.
         */
        public SimpleGrid getGrid() {
            return grid;
        }
    }

    /**
     * This timer is invoked every time when column resizing might happen.
     *
     * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
     */
    protected static class ResizeTimer extends Timer {
        /** resize listener that starts this timer */
        private ResizeListener listener;

        /**
         * Creates the timer.
         *
         * @param listener is a resize listener.
         */
        public ResizeTimer(ResizeListener listener) {
            this.listener = listener;
        }

        /**
         * See class docs.
         */
        public void run() {
            if (listener.th != null) {
                listener.resize();
            }
        }
    }
}