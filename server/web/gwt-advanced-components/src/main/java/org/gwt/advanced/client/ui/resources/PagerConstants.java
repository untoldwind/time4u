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

package org.gwt.advanced.client.ui.resources;

import com.google.gwt.i18n.client.Messages;

/**
 * This is a resource bundle interface for the {@link org.gwt.advanced.client.ui.widget.Pager}.
 *
 * @author Sergey Skladchikov
 * @since 1.4.6
 */
public interface PagerConstants extends Messages {
    /**
     * Method getPreviousPage returns the previousPage message of this PagerConstants object.
     *
     * @return the previousPage (type String) of this PagerConstants object.
     */
    String getPreviousPage();
    /**
     * Method getNextPage returns the nextPage message of this PagerConstants object.
     *
     * @return the nextPage (type String) of this PagerConstants object.
     */
    String getNextPage();
    /**
     * Method getJumpTo returns the jumpTo message of this PagerConstants object.
     *
     * @return the jumpTo (type String) of this PagerConstants object.
     */
    String getJumpTo();
    /**
     * Method getDisplayPage returns the displayPage message of this PagerConstants object.
     *
     * @return the displayPage (type String) of this PagerConstants object.
     */
    String getDisplayPage();
    /**
     * Method getTotalCount returns a total page count message.
     *
     * @param value is a number of pages.
     * @return String is a message text with the injected value. 
     */
    String getTotalCount(String value);
}
