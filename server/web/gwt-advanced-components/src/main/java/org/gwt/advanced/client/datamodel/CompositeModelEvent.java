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
 * This is a model event that describes types for all events produced by composite models.<p/>
 * Additionally to the basic fields of events it adds a link to the parent row.
 *
 * @author Sergey Skladchikov
 */
public class CompositeModelEvent extends EditableModelEvent {
    /** on remove a subtree */
    public static final EventType CLEAN_SUBTREE = new EventType();
    
    /** parent row */
    private TreeGridRow parent;

    /**
     * Creates an instance of this class and initilizes mandatory fields.
     *
     * @param eventType is an event type.
     * @param parent is a parent row link.
     */
    public CompositeModelEvent(EventType eventType, TreeGridRow parent) {
        super(eventType);
        this.parent = parent;
    }

    /**
     * Creates an instance of this class and initilizes internal fields.
     *
     * @param eventType is an event type.
     * @param parent is a parent row link.
     * @param row is a number of the row that produced an event.
     */
    public CompositeModelEvent(EventType eventType, TreeGridRow parent, int row) {
        this(eventType, parent);
        setRow(row);
    }

    /**
     * Gets a link to a parent row.
     *
     * @return a link to a parent row.
     */
    public TreeGridRow getParent() {
        return parent;
    }
}
