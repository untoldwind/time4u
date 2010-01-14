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
 * This is an inteface that must be implements by any table that is able
 * to handle double click events.<p/>
 * Other classes can implement the {@link TableDoubleClickListener}
 * interface to catch the events.<p/>
 * One of implementations is {@link org.gwt.advanced.client.ui.widget.AdvancedFlexTable}.
 * 
 * @author Sergey Skladchikov
 * @since 1.4.9
 */
public interface SourcesTableDoubleClickEvents {
    /**
     * Registers the specified listener thta will be invoked on double click event.
     *
     * @param listener is a listener to register.
     */
    void addDoubleClickListener(TableDoubleClickListener listener);

    /**
     * Removes (unregisters) the specified listener.
     *
     * @param listener is a listener to remove.
     */
    void removeDoubleClickListener(TableDoubleClickListener listener);
}
