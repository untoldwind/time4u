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

package org.gwt.advanced.client.datamodel;

/**
 * This interface describes pageable object (normally a row set of the grid).<p>
 * Implementations of this interface are usually used by pagers.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.0.0
 */
public interface Pageable {
    /**
     * This method sets a page size.
     *
     * @param pageSize is a page size.
     */
    void setPageSize(int pageSize);

    /**
     * This method sets the current page number.
     *
     * @param currentPageNumber is a current page number.
     *
     * @throws IllegalArgumentException if current page number less then zero or greater then
     *                                  actual number of pages.
     */
    void setCurrentPageNumber(int currentPageNumber) throws IllegalArgumentException;

    /**
     * This method returns a number of existing pages.
     *
     * @return a total pages number.
     */
    int getTotalPagesNumber();

    /**
     * This method returns a start page number.
     *
     * @return is a start page number.
     */
    int getStartPage();

    /**
     * This method returns an end page number.
     *
     * @return is an end page number.
     */
    int getEndPage();

    /**
     * This method gets the number of pages links to be displayed.
     *
     * @return is a number of pages.
     */
    int getDisplayedPages ();

    /**
     * This method sets the number of pages links to be displayed.
     *
     * @param displayedPages is a number of pages.
     */
    void setDisplayedPages (int displayedPages);

    /**
     * This method returns a page size.
     *
     * @return a page size (number of displayable rows).
     */
    int getPageSize ();

    /**
     * This method gets a current page number.
     *
     * @return a current page number.
     */
    int getCurrentPageNumber ();
}
