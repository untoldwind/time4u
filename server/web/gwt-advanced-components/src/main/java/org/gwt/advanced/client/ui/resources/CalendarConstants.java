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

import com.google.gwt.i18n.client.Constants;

/**
 * This interface describes some localized constant values for the {@link org.gwt.advanced.client.ui.widget.Calendar}
 * and {@link org.gwt.advanced.client.util.DateHelper}.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.0.0
 */
public interface CalendarConstants extends Constants {
    /**
     * Returns the first day of week (0-6).
     *
     * @return the first day of week.
     */
    String firstDayOfWeek();

    /**
     * Returns today word.
     *
     * @return a today word.
     */
    String today();

    /**
     * Returns the sun word.
     *
     * @return a sun word.
     */
    String sun();

    /**
     * Returns the mon word.
     *
     * @return a mon word.
     */
    String mon();

    /**
     * Returns the tue word.
     *
     * @return a tue word.
     */
    String tue();

    /**
     * Returns the wed word.
     *
     * @return a wed word.
     */
    String wed();

    /**
     * Returns the thu word.
     *
     * @return a thu word.
     */
    String thu();

    /**
     * Returns the fri word.
     *
     * @return a fri word.
     */
    String fri();

    /**
     * Returns the sat word.
     *
     * @return a sat word.
     */
    String sat();

    /**
     * Returns the January word.
     *
     * @return a January word.
     */
    String january();

    /**
     * Returns the February word.
     *
     * @return a February word.
     */
    String february();

    /**
     * Returns the March word.
     *
     * @return a March word.
     */
    String march();

    /**
     * Returns the April word.
     *
     * @return a April word.
     */
    String april();

    /**
     * Returns the May word.
     *
     * @return a May word.
     */
    String may();

    /**
     * Returns the June word.
     *
     * @return a June word.
     */
    String june();

    /**
     * Returns the July word.
     *
     * @return a July word.
     */
    String july();

    /**
     * Returns the August word.
     *
     * @return a August word.
     */
    String august();

    /**
     * Returns the September word.
     *
     * @return a September word.
     */
    String september();

    /**
     * Returns the October word.
     *
     * @return a October word.
     */
    String october();

    /**
     * Returns the November word.
     *
     * @return a November word.
     */
    String november();

    /**
     * Returns the December word.
     *
     * @return a December word.
     */
    String december();

    /**
     * Returns the hours cycle basis (12 or 24).
     *
     * @return a hours cycle basis value.
     */
    String hoursCircleBasis();

    /**
     * Specifies the date and time format for the date picker.
     *
     * @return a format applicable for the <code>DateTimeFormat</code>
     */
    String dateTimeFormat();

    /**
     * Specifies the date format only for the date picker.
     *
     * @return a format applicable for the <code>DateTimeFormat</code>
     */
    String dateFormat();
}
