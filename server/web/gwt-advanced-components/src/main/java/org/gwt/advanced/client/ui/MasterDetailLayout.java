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

import org.gwt.advanced.client.ui.widget.GridPanel;

/**
 * This interface describes an abstract master-detail layout where dependent grid panels
 * must be placed in.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.1.0
 */
public interface MasterDetailLayout extends Resizable {
    /**
     * This method adds a grid panel into the master-detail panel automatically choosing a cell to put it in.
     *
     * @param panel is a grid panel to be placed in.
     * @param parent is a parent grid panel.
     * @param caption is a grid panel caption (optional parameter).
     * @return <code>true</code> if the panel has been placed into the master-detail panel.
     */
    boolean addGridPanel(GridPanel panel, GridPanel parent, String caption);

    /**
     * This method removes a grid panel from this master-detail panel automatically finding it in cells.<p/>
     * Child grid panels will be removed as well.
     *
     * @param panel is a panel to be removed.
     * @return <code>true</code> is the widget has been found and removed.
     */
    boolean removeGridPanel(GridPanel panel);
}
