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
 * This is a listener interface for all {@link org.gwt.advanced.client.datamodel.EditableGridDataModel}
 * events.<p/>
 * To register a listener you must invoke the
 * {@link Editable#addListener(EditableModelListener)} method.
 * However you don't have to do it since this kind of lusteners is used by widgets. So use the listeners
 * attached to these widgets. 
 *
 * @author Sergey Skladchikov
 * @since 1.4.7
 */
public interface EditableModelListener {
    /**
     * This method is invoked on any data model change event.
     *
     * @param event is an event to be handled by the listeners.
     */
    void onModelEvent(EditableModelEvent event);
}
