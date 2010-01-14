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

import com.google.gwt.user.client.ui.Widget;
import org.gwt.advanced.client.ui.widget.AdvancedTabPanel;

/**
 * This is a basic interface for all tab band renderers.<p/>
 * It's invoked by the {@link org.gwt.advanced.client.ui.widget.AdvancedTabPanel} every time the band must
 * be rendered.
 *
 * @author Sergey Skladchikov
 * @since 1.4.6
 */
public interface TabBandRenderer {
    /**
     * This method creates a band widget.
     *
     * @param panel the tab panel that invokes this method.
     * @return a resulting band.
     */
    Widget render(AdvancedTabPanel panel);
}
