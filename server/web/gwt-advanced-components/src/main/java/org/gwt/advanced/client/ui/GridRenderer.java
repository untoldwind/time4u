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

package org.gwt.advanced.client.ui;

import org.gwt.advanced.client.datamodel.GridDataModel;

/**
 * This is an interface that describes cell renderers.<p/>
 * All renderers applied by the grids must implement it.<br/>
 * If you like to develop your own renderer you can implement this interface but
 * it will be better if you extend {@link org.gwt.advanced.client.ui.widget.DefaultGridRenderer}
 * or {@link org.gwt.advanced.client.ui.widget.HierarchicalGridRenderer}.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.3.0
 */
public interface GridRenderer {
    /**
     * This method renders column headers.
     *
     * @param headers is a list of header values to be rendered.
     */
    void drawHeaders(Object[] headers);

    /**
     * This method renders grid content.
     *
     * @param model is a model to be applied.
     */
    void drawContent(GridDataModel model);

    /**
     * This method renders a particular row.
     *
     * @param data is a row data array.
     * @param row is a row number.
     */
    void drawRow(Object[] data, int row);

    /**
     * This method draws a particular cell.
     *
     * @param data is a cell data.
     * @param row is a row number.
     * @param column is a column number.
     * @param active is a falg that indicates that the cell must be active.
     */
    void drawCell(Object data, int row, int column, boolean active);

    /**
     * This method converts grid row number to model row number.
     *
     * @param row is a row number.
     * @return a row number.
     */
    int getModelRow(int row);

    /**
     * Gets a grid row number on the displayed page by a model row number.
     *
     * @param modelRow is a model row number.
     *
     * @return a grid row number.
     */
    int getRowByModelRow(int modelRow);

    /**
     * This method draws a column.
     *
     * @param data is a column data to draw.
     * @param column is a column number
     * @param overwrite is a flag that means whether it's required to overwrite the column cells.
     */
    void drawColumn(Object[] data, int column, boolean overwrite);
}
