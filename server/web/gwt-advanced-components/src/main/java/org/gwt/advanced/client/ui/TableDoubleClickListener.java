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

/**
 * This interface describes listeners which must be invoked on double click
 * event.<p/>
 * Any table that is able register the listeners and invoke them should implement
 * the {@link SourcesTableDoubleClickEvents} inteface.
 * 
 * @author Sergey Skladchikov
 * @since 1.4.9
 */
public interface TableDoubleClickListener {
    /**
     * This method is invoked if the double click event occurs in one of
     * sender cells.
     *
     * @param sender is a sender table.
     * @param row is a row number.
     * @param cell is a cell number.
     */
    void onCellDoubleClick(SourcesTableDoubleClickEvents sender, int row, int cell);
}
