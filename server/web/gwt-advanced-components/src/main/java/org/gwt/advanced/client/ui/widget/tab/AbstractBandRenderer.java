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
import com.google.gwt.user.client.ui.Widget;
import org.gwt.advanced.client.ui.widget.AdvancedTabPanel;
import org.gwt.advanced.client.ui.widget.border.Border;
import org.gwt.advanced.client.ui.widget.border.BorderLine;

/**
 * This is an anbstract tabs band renderer that implements the main cycle of rendering<p/>
 * However it doesn't "know" how to create tabs and separators betwenn them.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.4.6
 */
public abstract class AbstractBandRenderer implements TabBandRenderer {
    /**
     * This method build the band that is represneted as a <code>FlexTable</code>.
     *
     * @param panel the tab panel that invokes this method.
     * @return a resulting table.
     */
    public Widget render(AdvancedTabPanel panel) {
        FlexTable result = new FlexTable();
        result.setCellSpacing(0);
        result.setCellPadding(0);
        result.setStyleName("tabs-" + panel.getPosition().getName());
        result.addTableListener(new TabEventListener(panel));

        int count = 0;
        for (int i = 0; i < panel.count(); i++) {
            count = createEmpty(result, count, "separator");
            Border border = panel.getTabBorderFactory().create();
            border.setVisible(false, getHiddenLine());
            border.setWidget(panel.getTab(i));
            count = createTab(panel, result, count, i, border);
        }

        createEmpty(result, count, "last-empty");
        panel.getContentBorder().setVisible(false, getHiddenLine().getOpposite());
        return result;
    }

    /**
     * This method should create a tab content and decorate it.<p/>
     * It also inserts the tab into the specified layout table.
     *
     * @param panel is a target tabs panel widget.
     * @param result is a resulting table.
     * @param count is a current number of the table cell.
     * @param index is a current tab number (different to the cell number).
     * @param border is a tab border to be placed into the cell.
     *
     * @return next cell number.
     */
    protected abstract int createTab(AdvancedTabPanel panel, FlexTable result, int count, int index, Border border);

    /**
     * Gets a tab border line to hide.
     *
     * @return a tab border line.
     */
    protected abstract BorderLine getHiddenLine();

    /**
     * Creates an empty table cell.
     *
     * @param result is a resulting table.
     * @param count is a current cell number.
     * @param styleName is a style name to be applied to the cell.
     *
     * @return next cell number.
     */
    protected abstract int createEmpty(FlexTable result, int count, String styleName);
}
