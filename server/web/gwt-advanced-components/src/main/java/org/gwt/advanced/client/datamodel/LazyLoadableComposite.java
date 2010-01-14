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
 * This interface describes lazy loadable composites.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.4.0
 */
public interface LazyLoadableComposite extends LazyLoadable {
    /**
     * Sets a total gridRow count of the subtree rows.
     *
     * @param gridRow is a parent gridRow.
     * @param totalRowCount is a total gridRow count of the subtree rows.
     */
    void setTotalRowCount(TreeGridRow gridRow, int totalRowCount);
}
