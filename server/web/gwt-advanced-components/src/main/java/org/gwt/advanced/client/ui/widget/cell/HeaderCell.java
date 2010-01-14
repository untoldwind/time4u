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

package org.gwt.advanced.client.ui.widget.cell;

/**
 * This is a header cell interface.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.0.0
 */
public interface HeaderCell extends GridCell {
    /**
     * This method sets sortable flag value.
     *
     * @param sortable is a sortable flag value.
     */
    void setSortable (boolean sortable);

    /**
     * This method sets asceding sort flag value.
     *
     * @param ascending is an ascending sort value.
     */
    void setAscending (boolean ascending);

    /**
     * This method sets sorted flag value.
     *
     * @param sorted is a sorted flag value.
     */
    void setSorted (boolean sorted);

    /**
     * This method checks whether the column is sorted ascending.
     *
     * @return <code>true</code> if the sort order is ascending.
     */
    boolean isAscending();

    /**
     * This method checks whether the column is sortable.
     *
     * @return <code>true</code> if the column is sortable.
     */
    boolean isSortable();

    /**
     * This method checks whether the column is sorted.
     *
     * @return <code>true</code> if the column is sorted.
     */
    boolean isSorted();

    /**
     * This method changes the current sort order or sets the current column as sorted.
     */
    void sort();
}
