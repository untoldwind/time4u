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
 * This is a callback handler to be invoked on one row draw event.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 */
public interface GridRowDrawCallbackHandler {
    /**
     * This method is invoked before the row is drawn.
     *
     * @param row is a row number.
     * @param pageSize is a page size.
     * @param grid is a grid instance.
     * @param rowData an array of row data.
     *
     * @return <code>false</code> if the row must be skipped.
     */
    boolean beforeDraw(int row, int pageSize, EditableGrid grid, Object[] rowData);

    /**
     * This method is invoked after the row is drawn.
     *
     * @param row is a row number.
     * @param pageSize is a page size.
     * @param grid is a grid instance.
     * @param rowData an array of row data.
     *
     * @return <code>false</code> if drawing must be stopped when the row is drawn.
     */
    boolean afterDraw(int row, int pageSize, EditableGrid grid, Object[] rowData);
}