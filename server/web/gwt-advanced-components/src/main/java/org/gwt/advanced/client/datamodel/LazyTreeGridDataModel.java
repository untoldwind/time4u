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
 * This class implements lazy loadable tree grid data model.
 * 
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.4.0
 */
public class LazyTreeGridDataModel extends TreeGridDataModel implements LazyLoadableComposite {
    /**
     * Creates an instance of this class and initializes data putting into lazy loadable delegate.
     *
     * @param data is a data set to put.
     */
    public LazyTreeGridDataModel(Object[][] data) {
        super(data);
        setDelegate(new DelegateLazyGridDataModel(data, this));
    }

    /**
     * Creates an inistanec of this class and synchronizes data using the handler.
     *
     * @param handler is a handler to be used for synchronization.
     */
    public LazyTreeGridDataModel(TreeDataModelCallbackHandler handler) {
        super(handler);
        setDelegate(new DelegateLazyGridDataModel(handler));
    }

    /** {@inheritDoc} */
    public int getTotalRowCount(TreeGridRow parent) {
        if (parent == null)
            return getDelegate().getTotalRowCount();
        return ((LazyTreeGridRow) parent).getTotalRowCount();
    }

    /** {@inheritDoc} */
    public void setTotalRowCount(int totalRowCount) {
        ((LazyLoadable)getDelegate()).setTotalRowCount(totalRowCount);
    }

    /** {@inheritDoc} */
    public void setTotalRowCount(TreeGridRow gridRow, int totalRowCount) {
        if (gridRow == null) {
            setTotalRowCount(totalRowCount);
            return;
        }

        if (gridRow instanceof LazyLoadable)
            ((LazyLoadable) gridRow).setTotalRowCount(totalRowCount);
    }

    /** {@inheritDoc} */
    public int addRow (TreeGridRow parent, Object[] row) throws IllegalArgumentException {
        int index = super.addRow(parent, row);
        setTotalRowCount(parent, getTotalRowCount(parent) + 1);
        return index;
    }

    /** {@inheritDoc} */
    public void update(TreeGridRow parent, Object[][] children) {
        super.update(parent, children);
        if (children != null)
            setTotalRowCount(parent, getTotalRowCount(parent) + children.length);    
    }

    /** {@inheritDoc} */
    public void removeRow (TreeGridRow parent, int rowNumber) throws IllegalArgumentException {
        super.removeRow(parent, rowNumber - getStartRow());
        setTotalRowCount(parent, getTotalRowCount(parent) - 1);
    }

    /** {@inheritDoc} */
    public int getEndRow (TreeGridRow parent) {
        if (!parent.isPagerEnabled())
            return getTotalRowCount(parent) - 1;
        
        if (getTotalRowCount(parent) > super.getTotalRowCount(parent)) {
            return getStartRow(parent) + Math.min(getPageSize(parent) - 1, super.getTotalRowCount(parent) - 1);
        } else
            return super.getEndRow(parent);
    }

    /** {@inheritDoc} */
    public void removeAll (TreeGridRow parent) {
        super.removeAll(parent);
        setTotalRowCount(parent, 0);
    }

    /**
     * This is a delegate class for lazy tree rows creation.
     *
     * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
     */
    protected class DelegateLazyGridDataModel extends LazyGridDataModel {
        /** source of model events */
        private LazyTreeGridDataModel source;

        /**
         * Creates a new instance of the delegate.
         *
         * @param data is a data to store in the delegate.
         * @param source is a soirce of model events.
         */
        public DelegateLazyGridDataModel(Object[][] data, LazyTreeGridDataModel source) {
            super(data);
            this.source = source;
        }

        /**
         * Creates a new instance of the class.
         *
         * @param handler is a handler required for synchronization.
         */
        protected DelegateLazyGridDataModel(DataModelCallbackHandler handler) {
            super(handler);
        }

        /**
         * Overriden to create lazy rows.
         *
         * @param columnCount is a column count.
         * @return a lazy tree row instance.
         */
        protected GridRow createGridRow(int columnCount) {
            return new LazyTreeGridRow(getThisModel());
        }

        /** {@inheritDoc} */
        protected void prepareEvent(EditableModelEvent event) {
            event.setSource(source);
        }

        /** {@inheritDoc} */
        protected EditableModelEvent createEvent(EditableModelEvent.EventType eventType) {
            return new CompositeModelEvent(eventType, null);
        }

        /** {@inheritDoc} */
        protected EditableModelEvent createEvent(EditableModelEvent.EventType eventType, int row, int column) {
            CompositeModelEvent event = new CompositeModelEvent(eventType, null, row);
            event.setColumn(column);
            return event;
        }
    }
}
