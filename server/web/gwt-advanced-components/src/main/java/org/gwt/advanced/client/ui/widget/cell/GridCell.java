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

package org.gwt.advanced.client.ui.widget.cell;

import com.google.gwt.user.client.ui.FlexTable;

/**
 * This is a grid cell interface.<p>
 * All cell implementations must implement it.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.0.0
 */
public interface GridCell {
    /**
     * This method gets a cell value.
     *
     * @return a cell value.
     */
    Object getValue();

    /**
     * This method sets a cell value.
     *
     * @param value is a cell value.
     */
    void setValue(Object value);

    /**
     * This method specifies position of the cell in the grid (but doen't add it!).
     *
     * @param row is a row number.
     * @param column is a column number.
     */
    void setPosition(int row, int column);

    /**
     * This method gets a cell row number.
     *
     * @return a row number.
     */
    int getRow();

    /**
     * This method gets a cell column number.
     *
     * @return a column number.
     */
    int getColumn();

    /**
     * This method shows the cell.
     *
     * @param active is a flag meaning whether the cell must be shown as active.
     */
    void displayActive(boolean active);

    /**
     * This method sets the grid instance.
     *
     * @param grid is a grid instance.
     */
    void setGrid(FlexTable grid);

    /**
     * This method gets a grid instance.
     *
     * @return a grid instance.
     */
    FlexTable getGrid();

    /**
     * This method sets focus to the cell.
     *
     * @param focus is a flag meaning whether the focus must be set or lost.
     */
    void setFocus(boolean focus);

    /**
     * This method checks whether the cell is active.
     *
     * @return <code>true</code> if the cell is active.
     */
    boolean isActive();

    /**
     * This method returns a new value entered while the cell is active.<p>
     * If the cell is passive, it is an equivalent of the {@link #getValue()} method.
     *
     * @return is a new value.
     */
    Object getNewValue();

    /**
     * This method compares values in this cell and in the specified cell.<p/>
     * NOTE: it can return a result not correlating with the result of the <code>equals</code>
     * method invocation.
     *
     * @param value is a value to be compared.
     * @return <code>true</code> if values are equal.
     */
    boolean valueEqual (Object value);
}
