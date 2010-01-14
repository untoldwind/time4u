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
 * This is a cell implementation for <code>Double</code> numbers.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.0.0
 */
public class DoubleCell extends NumberCell {
    /** Constructs a new DoubleCell. */
    public DoubleCell() {
        super(REAL_PATTERN, Double.MAX_VALUE, -Double.MAX_VALUE);
    }

    /** {@inheritDoc} */
    protected Number convertToNumber(String text) {
        return Double.valueOf(text);
    }
}
