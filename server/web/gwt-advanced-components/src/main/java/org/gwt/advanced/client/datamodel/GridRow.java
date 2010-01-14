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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * This is a list that has an unique identifier.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.4.0
 */
public class GridRow implements IndexedEntity {
    /** list identifier */
    private String identifier;
    /** this index number */
    private int index;
    /** delegate list */
    private List delegate;

    /**
     * Constructs a new GridRow.
     *
     * @param initialCapacity is an initial capacity value.
     */
    protected GridRow(int initialCapacity) {
        this.delegate =  new ArrayList(initialCapacity);
        this.identifier = generateUniqueString();
    }

    /** Constructs a new GridRow. */
    protected GridRow() {
        this.delegate = new ArrayList();
        this.identifier = generateUniqueString();
    }

    /**
     * Constructs a new GridRow.
     *
     * @param c is an initial collection.
     */
    protected GridRow(Collection c) {
        this.delegate = new ArrayList(c);
        this.identifier = generateUniqueString();
    }

    /**
     * Getter for property 'identifier'.
     *
     * @return Value for property 'identifier'.
     */
    public String getIdentifier () {
        return identifier;
    }

    /**
     * This method gets data of this row.
     *
     * @return a data array.
     */
    public Object[] getData() {
        return getDelegate().toArray(new Object[getDelegate().size()]);
    }
    
    /**
     * This method sets data into the row.
     *
     * @param data is row data.
     */
    public void setData(Object[] data) {
        getDelegate().clear();
        getDelegate().addAll(Arrays.asList(data));
    }

    /**
     * Setter for property 'identifier'.
     *
     * @param identifier Value to set for property 'identifier'.
     */
    protected void setIdentifier (String identifier) {
        this.identifier = identifier;
    }

    /**
     * This method egnerates an unique string.
     *
     * @return is a nunique string.
     */
    protected String generateUniqueString() {
        StringBuffer result = new StringBuffer(String.valueOf(System.currentTimeMillis()));
        result.append(Math.random());
        return result.toString();
    }

    /** {@inheritDoc} */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!super.equals(o)) return false;

        GridRow that = (GridRow) o;

        return !(identifier != null ? !identifier.equals(that.identifier) : that.identifier != null);
    }

    /** {@inheritDoc} */
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (identifier != null ? identifier.hashCode() : 0);
        return result;
    }

    /** {@inheritDoc} */
    public int getIndex() {
        return index;
    }

    /** {@inheritDoc} */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Getter for property 'delegate'.
     *
     * @return Value for property 'delegate'.
     */
    protected List getDelegate() {
        return delegate;
    }

    /**
     * Adds a new cell value at the end of row.
     *
     * @param cell is a cell value to add.
     */
    protected void add(Object cell) {
        getDelegate().add(cell);
    }

    /**
     * This method adds a new value before the specified cell.
     *
     * @param beforeCell is a cell index.
     * @param data is cell data to be added.
     */
    protected void add(int beforeCell, Object data) {
        getDelegate().add(beforeCell, data);
    }

    /**
     * This method gets data placed in the specified cell.
     *
     * @param index a cell index.
     * @return cell data.
     */
    protected Object get(int index) {
        return getDelegate().get(index);
    }

    /**
     * Removes specified cell.
     *
     * @param cellNumber is a cell index.
     */
    protected void remove(int cellNumber) {
        getDelegate().remove(cellNumber);
    }

    /**
     * Sets the specified data to the cell.
     *
     * @param column is a cell index.
     * @param data is data to be set.
     */
    protected void set(int column, Object data) {
        getDelegate().set(column, data);
    }
}
