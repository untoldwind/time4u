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

import com.google.gwt.user.client.ui.Widget;

import java.util.Date;

/**
 * This is a calendar listener.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.0.0
 */
public interface CalendarListener {
    /**
     * This method is invoked when a user chooses a date.
     *
     * @param sender is a calendar which sent the event.
     * @param oldValue is an old date value.
     */
    void onChange(Widget sender, Date oldValue);

    /**
     * This method is invoked on cancel.
     *
     * @param sender is a calendar which sent the event.
     */
    void onCancel(Widget sender);
}
