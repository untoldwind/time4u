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
 * This interface describes expandable cell methods.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.0.0
 */
public interface ExpandableCell extends GridCell {
    /**
     * This method checks whether the cell is expanded.
     *
     * @return a result of check.
     */
    boolean isExpanded();

    /**
     * This method sets an expanded flag value for the cell.
     *
     * @param expanded is an expanded falg value.
     */
    void setExpanded(boolean expanded);

    /**
     * This method checks whether the expanded cell is a leaf.
     *
     * @return a result of check.
     */
    boolean isLeaf();

    /**
     * This method sets a leaf flag value.
     *
     * @param leaf a leaf flag value.
     */
    void setLeaf(boolean leaf);
}
