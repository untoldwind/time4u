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

import com.google.gwt.core.client.GWT;

import java.util.*;

/**
 * This is an implementation of the data model interface for the ComboBox widget.
 * 
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.2.0
 */
public class ComboBoxDataModel implements ListDataModel {
    /** a list of item IDs where each item is instance of <code>String</code> */
    private List itemIds = new ArrayList();
    /** a map of items where each item is pair of <code>String</code> ID and <code>Object</code> value */
    private Map items = new HashMap();
    /** a selected item ID */
    private String selectedId;
    /** {@link org.gwt.advanced.client.datamodel.ListModelListener}s */
    private List listeners = new ArrayList();

    /** {@inheritDoc} */
    public void add(String id, Object item) {
        List ids = getItemIds();
        if (!ids.contains(id))
            ids.add(id);
        getItems().put(id, item);

        fireEvent(new ListModelEvent(this, id, getItemIds().indexOf(id), ListModelEvent.ADD_ITEM));
    }

    /** {@inheritDoc} */
    public void add(int index, String id, Object item) {
        List ids = getItemIds();
        index = getValidIndex(index);

        if (!ids.contains(id))
            ids.add(index, id);

        add(id, item);
    }

    /** {@inheritDoc} */
    public Object get(String id) {
        return getItems().get(id);
    }

    /** {@inheritDoc} */
    public Object get(int index) {
        if (isIndexValid(index))
            return get((String) getItemIds().get(index));
        else
            return null;
    }

    /** {@inheritDoc} */
    public void remove(String id) {
        int index = getItemIds().indexOf(id);
        getItemIds().remove(id);
        getItems().remove(id);

        fireEvent(new ListModelEvent(this, id, index, ListModelEvent.REMOVE_ITEM));
    }

    /** {@inheritDoc} */
    public void remove(int index) {
        if (isIndexValid(index))
            remove((String) getItemIds().get(index));
    }

    /** {@inheritDoc} */
    public String getSelectedId() {
        return selectedId;
    }

    /** {@inheritDoc} */
    public int getSelectedIndex() {
        return getItemIds().indexOf(getSelectedId());
    }

    /** {@inheritDoc} */
    public Object getSelected() {
        return getItems().get(getSelectedId());
    }

    /** {@inheritDoc} */
    public void setSelectedId(String id) {
        this.selectedId = id;

        fireEvent(new ListModelEvent(this, id, getSelectedIndex(), ListModelEvent.SELECT_ITEM));
    }

    /** {@inheritDoc} */
    public void setSelectedIndex(int index) {
        if (index < 0) {
            selectedId = null;
            return;
        }
        List ids = getItemIds();
        if (ids.size() > 0)
            setSelectedId((String) ids.get(index));
    }

    /** {@inheritDoc} */
    public void clear() {
        itemIds.clear();

        fireEvent(new ListModelEvent(this, ListModelEvent.CLEAN));
    }

    /** {@inheritDoc} */
    public boolean isEmpty() {
        return itemIds.isEmpty();
    }

    /** {@inheritDoc} */
    public int getCount() {
        return itemIds.size();
    }

    /**
     * This method registers a list data model listener.
     *
     * @param listener is a listener to be invoked on any event.
     */
    public void addListModelListener(ListModelListener listener) {
        removeListModelListener(listener);
        listeners.add(listener);
    }

    /**
     * This method unregisters the specified listener.
     *
     * @param listener is a listener to be unregistered.
     */
    public void removeListModelListener(ListModelListener listener) {
        listeners.remove(listener);
    }

    /**
     * This method fires the specified event and invokes the listeners.
     *
     * @param event is an event to fire.
     */
    protected void fireEvent(ListModelEvent event) {
        for (Iterator iterator = listeners.iterator(); iterator.hasNext();) {
            ListModelListener listener = (ListModelListener) iterator.next();
            try {
                listener.onModelEvent(event);
            } catch (Throwable t) {
                GWT.log("Unknown listener error", t);
            }
        }
    }

    /**
     * Getter for property 'itemIds'.
     *
     * @return Value for property 'itemIds'.
     */
    protected List getItemIds() {
        return itemIds;
    }

    /**
     * Getter for property 'items'.
     *
     * @return Value for property 'items'.
     */
    protected Map getItems() {
        return items;
    }

    /**
     * This method checks whether the specified index is valid.
     *
     * @param index is an index value to check.
     * @return <code>true</code> if the index is valid.
     */
    protected boolean isIndexValid(int index) {
        return getItemIds().size() > index && index >= 0;
    }

    /**
     * This method calculates a valid index value taking into account the following rule:
     * if the index < 0, it returns 0;
     * if the index > then {@link #getItemIds()} size, it returns {@link #getItemIds()} size.
     *
     * @param invalidIndex is an index.
     * @return a valid index value.
     */
    protected int getValidIndex(int invalidIndex) {
        List ids = getItemIds();

        if (invalidIndex < 0)
            invalidIndex = 0;
        if (invalidIndex > ids.size())
            invalidIndex = ids.size();
        return invalidIndex;
    }
}
