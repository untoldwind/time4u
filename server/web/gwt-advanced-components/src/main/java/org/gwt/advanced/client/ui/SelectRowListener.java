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

import org.gwt.advanced.client.ui.widget.EditableGrid;

/**
 * This interface describes a listeners that handles row selection events.<p/>
 * The instance of this interface implementation will be invoked every time when
 * row selection is changed. If you like just to do handling on cell selection / edit
 * you must use {@link org.gwt.advanced.client.ui.EditCellListener}.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.1.0
 */
public interface SelectRowListener {
    /**
     * This method is invoked every time when row selection is done.
     *
     * @param grid is a grid where the row has been selected.
     * @param row is a selected row number.
     */
    void onSelect(EditableGrid grid, int row);
}
