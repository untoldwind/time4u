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

package org.gwt.advanced.client.ui;

import org.gwt.advanced.client.datamodel.GridDataModel;
import org.gwt.advanced.client.ui.widget.GridPanel;

/**
 * This interface defines methods of the grid panel factory.<p>
 * Implementations of this interface are used in hierarchical grids to create expandable
 * subgrids.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.0.0
 */
public interface GridPanelFactory {
    /**
     * Creates an instance of the grid panel and initializes it.
     *
     * @param model is a model instance.
     * @return a newly created grid panel.
     */
    GridPanel create(GridDataModel model);

    /**
     * This method creates a data model.<p>
     * It's used by the hiearachical grid if there is no data model defined for the cell 
     * in the parent data model.
     *
     * @param parentRow is a parent model row.
     * @param parentModel is a parent model instance.
     *  
     * @return a data model.
     */
    GridDataModel create(int parentRow, GridDataModel parentModel);
}
