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
 * This is a model event that is sent by the {@link org.gwt.advanced.client.datamodel.ListDataModel} implementations
 * if anything is changed in encapsulated data.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.4.9
 */
public class ListModelEvent {
    /** item added */
    public static final EventType ADD_ITEM = new EventType();
    /** item removed */
    public static final EventType REMOVE_ITEM = new EventType();
    /** all data removed */
    public static final EventType CLEAN = new EventType();
    /** item selected */
    public static final EventType SELECT_ITEM = new EventType();

    /** it's a data model that produced the event */
    private ListDataModel source;
    /** related item ID */
    private String itemId = null;
    /** related item index */
    private int itemIndex = -1;
    /** type of the event */
    private EventType type;

    /**
     * Creates an instance of this class.<p/>
     * Mostly applicable for types like {@link #CLEAN} which don't require to specify a concrete item.
     *
     * @param source is a data model produced this event.
     * @param type is an event type.
     */
    public ListModelEvent(ListDataModel source, EventType type) {
        this.source = source;
        this.type = type;
    }

    /**
     * Creates an instance of this class.<p/>
     * Mostly applicable for types like {@link #ADD_ITEM}, {@link #REMOVE_ITEM} or {@link #SELECT_ITEM}
     * which require to specify a concrete item.
     *
     * @param source is a data model produced this event.
     * @param itemId is a related item ID.
     * @param itemIndex is a related item index (usauly index of the item identified with <code>itemId</code>.
     * @param type is an event type.
     */
    public ListModelEvent(ListDataModel source, String itemId, int itemIndex, EventType type) {
        this.source = source;
        this.itemId = itemId;
        this.itemIndex = itemIndex;
        this.type = type;
    }

    /**
     * Gets an event source data model.
     *
     * @return a list data model.
     */
    public ListDataModel getSource() {
        return source;
    }

    /**
     * Gets a related item ID.<p/>
     * Might be equal to <code>null</code> for those events which are not related to a concrete item.
     *
     * @return an item ID.
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * Gets a related item index.<p/>
     * Might be equal to <code>-1</code> for those events which are not related to a concrete item.
     *
     * @return an item ID.
     */
    public int getItemIndex() {
        return itemIndex;
    }

    /**
     * Gets an event type that specifies what exactly happened.
     *
     * @return an event type.
     */
    public EventType getType() {
        return type;
    }

    /**
     * Event type class<p/>
     * It's in use because older versions of GWT don't support enums.
     */
    protected static class EventType {
        /** See class docs */
        protected EventType() {
        }
    }
}
