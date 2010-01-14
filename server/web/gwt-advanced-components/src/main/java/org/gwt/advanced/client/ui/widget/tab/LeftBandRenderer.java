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
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import org.gwt.advanced.client.ui.widget.AdvancedTabPanel;
import org.gwt.advanced.client.ui.widget.border.Border;
import org.gwt.advanced.client.ui.widget.border.BorderLine;
import org.gwt.advanced.client.util.ThemeHelper;

/**
 * This is implementation of the tabs band for the left position.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.4.6
 */
public class LeftBandRenderer extends AbstractBandRenderer {
    /** {@inheritDoc} */
    protected int createTab(AdvancedTabPanel panel, FlexTable result, int count, int index, Border border) {
        HTMLTable.CellFormatter formatter = result.getCellFormatter();
        result.setWidget(count, 0, (Widget) border);

        if (panel.getSelected() == index)
            formatter.setStyleName(count++, 0, "selected");
        else
            formatter.setStyleName(count++, 0, "unselected");
        return count;
    }

    /** {@inheritDoc} */
    protected BorderLine getHiddenLine() {
        return BorderLine.RIGHT;
    }

    /** {@inheritDoc} */
    protected int createEmpty(FlexTable result, int count, String styleName) {
        HTMLTable.CellFormatter formatter = result.getCellFormatter();
        result.setWidget(count, 0, new Image(ThemeHelper.getInstance().getFullResourceName("advanced/images/single.gif")));
        formatter.setStyleName(count++, 0, styleName);
        return count;
    }
}
