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
 * This interface describes a list data model.<p/> Each list data model should implement it.
 * The interface provides sevaral methods for list items manipulation. Each list item is a pair of
 * unique <code>String</code> ID and an object to be displayed in the list.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.2.0
 */
public interface ListDataModel {
    /**
     * This method adds a new item in the list.
     *
     * @param id an unique ID of the item. If there is an item with the same ID, this method replaces it with
     *           a new value.
     * @param item is an item to be placed into the list.
     */
    void add(String id, Object item);

    /**
     * This method adds a new item into the specified position.<p/>
     * If the index < 0 it adds a new item into the 0 position. If the index > number of existing items it adds an item
     * into the end of this list.
     *
     * @param index is an index value.
     * @param id is an item ID.
     * @param item is an item.
     */
    void add(int index, String id, Object item);

    /**
     * This method returns an item by its ID.
     *
     * @param id is an item ID.
     *
     * @return an item.
     */
    Object get(String id);

    /**
     * This method returns an item by its index.
     *
     * @param index is an item index.
     *
     * @return an item.
     */
    Object get(int index);

    /**
     * This method removes the specified item.
     *
     * @param id is an item ID.
     */
    void remove(String id);

    /**
     * This method removes an item specified by its index.
     *
     * @param index is an item index.
     */
    void remove(int index);

    /**
     * This method gets a selected item ID.
     *
     * @return an item ID.
     */
    String getSelectedId();

    /**
     * This method gets a selected item index.
     *
     * @return an item index.
     */
    int getSelectedIndex();

    /**
     * This method returns a selected item.
     *
     * @return a selected item.
     */
    Object getSelected();

    /**
     * This method sets a currently selected item specifying it by ID.
     *
     * @param id is an item ID.
     */
    void setSelectedId(String id);

    /**
     * This method sets a currently selected item specifying it by the index.<p/>
     * If the index < 0 it deselects any item. If the index > then list size, it selects the last item. 
     *
     * @param index is an item index.
     */
    void setSelectedIndex(int index);

    /**
     * This methdo clears the list of items.
     */
    void clear();

    /**
     * This method returns <code>true</code> if the list of items is empty.
     *
     * @return a result of check.
     */
    boolean isEmpty();

    /**
     * Returns a number of items in the list.
     *
     * @return a number of items.
     */
    int getCount();
}
