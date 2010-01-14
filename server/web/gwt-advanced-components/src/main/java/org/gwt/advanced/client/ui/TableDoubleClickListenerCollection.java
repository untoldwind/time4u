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

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This collection assumes that it contains the {@link org.gwt.advanced.client.ui.TableDoubleClickListener}s
 * only.<p/>
 * It's able to fire the double click event and invoke these listeners on demand.
 * 
 * @author Sergey Skladchikov
 * @since 1.4.9
 */
public class TableDoubleClickListenerCollection extends ArrayList {
    /**
     * This method fires the double click event and invokes all the listeners
     * stored in this collection.
     *
     * @param sender is a sender table.
     * @param row is a row number.
     * @param cell is a cell number.
     */
    public void fireCellDoubleClicked(SourcesTableDoubleClickEvents sender, int row, int cell) {
        for (Iterator iterator = this.iterator(); iterator.hasNext();) {
            TableDoubleClickListener listener = (TableDoubleClickListener) iterator.next();
            try {
                listener.onCellDoubleClick(sender, row, cell);
            } catch (Throwable t) {
                //continue event dispatching whatever happened
            }
        }
    }
}
