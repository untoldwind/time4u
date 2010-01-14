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

package org.gwt.advanced.client.ui.widget.tab;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import org.gwt.advanced.client.ui.widget.AdvancedTabPanel;

/**
 * This listener changes the currently selected tab when a user clicks on it.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.4.6
 */
public class TabEventListener implements TableListener {
    /** target tabs panel */
    private AdvancedTabPanel panel;

    /**
     * Creates an instance of this class and initializes internal variables.
     *
     * @param panel is a targer tabs panel.
     */
    public TabEventListener(AdvancedTabPanel panel) {
        this.panel = panel;
    }

    /**
     * See class docs.
     */
    public void onCellClicked(SourcesTableEvents sender, int row, int cell) {
        FlexTable tabs = (FlexTable) sender;
        String name = tabs.getCellFormatter().getStyleName(row, cell);

        if (name != null && name.indexOf("unselected") != -1)
            panel.setSelected(Math.max(row / 2, cell / 2));
    }
}