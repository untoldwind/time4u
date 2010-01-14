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

import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.TableListener;
import org.gwt.advanced.client.ui.widget.GridPanel;

/**
 * This is an interface of grid event managers.<p/>
 * All classes which handle events produced by the grid must implement it.<br/>
 * Usually you won't have to implement this interface directly. Extend
 * {@link org.gwt.advanced.client.ui.widget.DefaultGridEventManager} or
 * {@link org.gwt.advanced.client.ui.widget.HierarchicalGridEventManager} instead. 
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.3.0
 */
public interface GridEventManager extends FocusListener, TableListener, KeyboardListener {
    /**
     * This method dispatches events and performs actions related to a concrete combinations of
     * keys.
     *
     * @param panel is a grid panel that invokes the manager.
     * @param keyCode is a key code.
     * @param modifiers is a list of modifiers defined in <code>KeyboardListener</code>.
     */
    void dispatch(GridPanel panel, char keyCode, int modifiers);
}
