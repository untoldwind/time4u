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

import org.gwt.advanced.client.ui.widget.EditableGrid;

/**
 * This is a cell factory designed especially to construct expandable cells.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.0.0
 */
public class ExpandableCellFactory extends DefaultGridCellFactory {
    /**
     * Creates an instance of the factory.
     *
     * @param grid is a grid instance.
     */
    public ExpandableCellFactory (EditableGrid grid) {
        super(grid);
    }

    /** {@inheritDoc} */
    public GridCell create(int row, int column, Object content) {
        if (content instanceof GridCell) {
            ExpandableCell cell = new ExpandableCellImpl();
            prepareCell(cell, row, column, content);
            return cell;
        } else
            return super.create(row, column, content);
    }
}
