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
 * This model event extends the super event and additionally describes evet types specific for the
 * {@link org.gwt.advanced.client.datamodel.Hierarchical} data models.
 *
 * @author Sergey Skladchikov
 */
public class HierarchicalModelEvent extends EditableModelEvent {
    /** on subgrid attach */
    public static final EventType ADD_SUBGRID = new EventType();
    /** on subgrid detach */
    public static final EventType REMOVE_SUBGRID = new EventType();
    /** on expand cell */
    public static final EventType CELL_EXPANDED = new EventType();
    /** on collapse cell */
    public static final EventType CELL_COLLAPSED = new EventType();

    /**
     * Creates an instance of this class invoking the same constructor of the super class.<p/>
     * See {@link org.gwt.advanced.client.datamodel.EditableModelEvent} also. 
     *
     * @param eventType is an event type.
     * @param row is a row number.
     * @param column is a column number.
     */
    public HierarchicalModelEvent(EventType eventType, int row, int column) {
        super(eventType, row, column);
    }
}
