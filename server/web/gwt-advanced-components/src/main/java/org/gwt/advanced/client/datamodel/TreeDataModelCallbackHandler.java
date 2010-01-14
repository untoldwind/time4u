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
 * This is an interface descrining callback handlers for the tree grid data model.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.4.0
 */
public interface TreeDataModelCallbackHandler extends DataModelCallbackHandler {
    /**
     * This method synchronizes the subtree rows (but not the parent row!) data with any data source.<p/>
     * If the parent row is <code>null</code> it must invoke the {@link #synchronize(GridDataModel)} method.
     * If there are expanded child rows of the parent their children must be synchronized recursively as well.<br/>
     * Note also that you must use {@link org.gwt.advanced.client.ui.widget.GridPanel#unlock(TreeGridRow)} method
     * instead of the {@link org.gwt.advanced.client.ui.widget.GridPanel#unlock()} to improve perfomance.<br/>
     *
     * See also {@link #synchronize(GridDataModel)} for details about the synchronization process.
     *
     * @param parent is a parent row of the subtree.
     * @param model is a data model.
     */
    void synchronize(LazyTreeGridRow parent, Composite model);
}
