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
 * This is a tree index implementation for the tree grid data models.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.4.0
 */
public class TreeGridRow extends GridRow implements Pageable {
    /** parent index */
    private TreeGridRow parent;
    /** expanded node flag */
    private boolean expanded;
    /** row owner, i.e. grid model that contains the row */
    private Composite model;
    /** pageable delegate */
    private Pageable pageable;
    /** pager enabled flag */
    private boolean pagerEnabled;

    /**
     * Always throws <code>UnsupportedOperationException</code>.
     */
    public TreeGridRow() {
        throw new UnsupportedOperationException("Never use this constructor!");
    }

    /**
     * Creates an instnace of this class and initilizes the internal fields.
     *
     * @param model is a grid data model that contains this row.
     */
    protected TreeGridRow(final Composite model) {
        super();
        setModel(model);
        final TreeGridRow row = this;
        setPageable(new GridPagerModel(new SimpleGridDataModel(null) {
            public int getTotalRowCount() {
                return model.getTotalRowCount(row);
            }
        }));
    }

    /**
     * Getter for property 'parent'.
     *
     * @return Value for property 'parent'.
     */
    public TreeGridRow getParent() {
        return parent;
    }

    /**
     * Setter for property 'parent'.
     *
     * @param parent Value to set for property 'parent'.
     */
    public void setParent(TreeGridRow parent) {
        this.parent = parent;
    }

    /**
     * Gets a level number of this row trying to iterate through all the parents.
     *
     * @return a level number. <code>0</code> if it's a root row.
     */
    public int getLevel() {
        TreeGridRow parent = getParent();
        int count = 0;
        while(parent != null) {
            count++;
            parent = parent.getParent();
        }
        return count;
    }

    /**
     * Getter for property 'expanded'.
     *
     * @return Value for property 'expanded'.
     */
    public boolean isExpanded() {
        return expanded;
    }

    /**
     * Setter for property 'expanded'.
     *
     * @param expanded Value to set for property 'expanded'.
     */
    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    /**
     * Getter for property 'pagerEnabled'.
     *
     * @return Value for property 'pagerEnabled'.
     */
    public boolean isPagerEnabled() {
        return pagerEnabled;
    }

    /**
     * Setter for property 'pagerEnabled'.
     *
     * @param pagerEnabled Value to set for property 'pagerEnabled'.
     */
    public void setPagerEnabled(boolean pagerEnabled) {
        this.pagerEnabled = pagerEnabled;
    }

    /** {@inheritDoc} */
    public void setCurrentPageNumber(int currentPageNumber) throws IllegalArgumentException {
        getPageable().setCurrentPageNumber(currentPageNumber);
    }

    /** {@inheritDoc} */
    public int getTotalPagesNumber() {
        return getPageable().getTotalPagesNumber();
    }

    /** {@inheritDoc} */
    public int getStartPage () {
        return getPageable().getStartPage();
    }

    /** {@inheritDoc} */
    public int getEndPage () {
        return getPageable().getEndPage();
    }

    /** {@inheritDoc} */
    public int getPageSize () {
        return getPageable().getPageSize();
    }

    /** {@inheritDoc} */
    public int getDisplayedPages() {
        return getPageable().getDisplayedPages();
    }

    /** {@inheritDoc} */
    public void setDisplayedPages(int displayedPages) {
        getPageable().setDisplayedPages(displayedPages);
    }

    /** {@inheritDoc} */
    public int getCurrentPageNumber() {
        return getPageable().getCurrentPageNumber();
    }

    /** {@inheritDoc} */
    public void setPageSize (int pageSize) {
        getPageable().setPageSize(pageSize);
    }

    /**
     * Getter for property 'model'.
     *
     * @return Value for property 'model'.
     */
    protected GridDataModel getModel() {
        return model;
    }

    /**
     * Setter for property 'model'.
     *
     * @param model Value to set for property 'model'.
     */
    protected void setModel(Composite model) {
        this.model = model;
    }

    /**
     * Getter for property 'pageable'.
     *
     * @return Value for property 'pageable'.
     */
    protected Pageable getPageable() {
        return pageable;
    }

    /**
     * Setter for property 'pageable'.
     *
     * @param pageable Value to set for property 'pageable'.
     */
    protected void setPageable(Pageable pageable) {
        if (pageable != null)
            this.pageable = pageable;
    }
}
