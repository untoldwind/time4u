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

package org.gwt.advanced.client.util;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a stack data structure implementation for GWT.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.4.0
 */
public class Stack {
    /** values storage */
    private List values = new ArrayList();

    /**
     * Adds a new value at the end of this stack.
     *
     * @param value is a value to be added.
     */
    public void add(Object value) {
        values.add(value);
    }

    /**
     * Adds all the values at the end of this stack.
     *
     * @param values is a list of values.
     */
    public void add(List values) {
        this.values.add(values);
    }

    /**
     * Gets the last element of the stack.
     *
     * @return a last element. <code>null</code> if there are no elements.
     */
    public Object get() {
        int size = size();
        if (size > 0)
            return values.get(size - 1);
        return null;
    }

    /**
     * Gets the last element of the stack and removes it.
     *
     * @return a last element. <code>null</code> if there are no elements.
     */
    public Object getAndRemove() {
        Object result = get();
        remove();
        return result;
    }

    /**
     * Removes the last element of the stack.
     */
    public void remove() {
        int size = size();
        if (size > 0)
            values.remove(size - 1);
    }

    /**
     * Clears the stack.
     */
    public void clear() {
        values.clear();
    }

    /**
     * Returns stack size value.
     *
     * @return a size value.
     */
    public int size() {
        return values.size();
    }
}
