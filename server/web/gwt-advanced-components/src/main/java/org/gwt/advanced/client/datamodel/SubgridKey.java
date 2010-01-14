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
 * This class is a subgrid key.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.0.0
 */
public class SubgridKey {
    /**
     * row identifier
     */
    private String rowIdentifier;
    /**
     * column number
     */
    private int columnNumber;

    /**
     * Creates an instance of this class.
     *
     * @param rowIdentifier    is a row identifier.
     * @param columnNumber is a column number.
     */
    public SubgridKey (String rowIdentifier, int columnNumber) {
        this.rowIdentifier = rowIdentifier;
        this.columnNumber = columnNumber;
    }

    /**
     * Getter for property 'rowIdentifier'.
     *
     * @return Value for property 'rowIdentifier'.
     */
    public String getRowIdentifier () {
        return rowIdentifier;
    }

    /**
     * Getter for property 'columnNumber'.
     *
     * @return Value for property 'columnNumber'.
     */
    public int getColumnNumber () {
        return columnNumber;
    }

    /** {@inheritDoc} */
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null) return false;

        SubgridKey that = (SubgridKey) o;

        return columnNumber == that.columnNumber && !(rowIdentifier != null
                ? !rowIdentifier.equals(that.rowIdentifier)
                : that.rowIdentifier != null);
    }

    /** {@inheritDoc} */
    public int hashCode () {
        int result;
        result = (rowIdentifier != null ? rowIdentifier.hashCode() : 0);
        result = 31 * result + columnNumber;
        return result;
    }
}