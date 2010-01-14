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
 * This class implements lazy loadable tree row.
 * 
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.4.0
 */
public class LazyTreeGridRow extends TreeGridRow implements LazyLoadable {
    /** total row count of the subtree */
    private int totalRowCount;

    /**
     * Always throws <code>UnsupportedOperationException</code>.
     */
    public LazyTreeGridRow() {
    }

    /**
     * Creates an instnace of this class and initilizes the internal fields.
     *
     * @param model is a grid data model that contains this row.
     */
    protected LazyTreeGridRow(Composite model) {
        super(model);
    }

    /** {@inheritDoc} */
    public void setTotalRowCount(int totalRowCount) {
        this.totalRowCount = totalRowCount;
    }

    /**
     * Getter for property 'totalRowCount'.
     *
     * @return Value for property 'totalRowCount'.
     */
    public int getTotalRowCount() {
        return totalRowCount;
    }
}
