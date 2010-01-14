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

import org.gwt.advanced.client.ui.widget.cell.GridCell;

/**
 * This interface describes edit cell listeners.<p>
 * Use it to validate entered values.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.0.0
 */
public interface EditCellListener {
    /**
     * This method is invoked on start edit (cell activation) event.
     * 
     * @param cell is a cell widget.
     *
     * @return <code>true</code> if edit operation can be started. Otherwise the cell
     *         won't be activated. 
     */
    boolean onStartEdit(GridCell cell);

    /**
     * This method is invoked on finish edit (cell passivation) event.<p>
     * Listeners can also show alert messages if the cell value is invalid.
     *
     * @param cell is a cell widget.
     * @param newValue is a new value to be checked.
     *
     * @return <code>true</code> if edit operation can be finished. Otherwise the cell
     *         won't be passivated.
     */
    boolean onFinishEdit(GridCell cell, Object newValue);
}
