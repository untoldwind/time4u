package org.gwt.advanced.client.datamodel;

/**
 * This is a basic implementation of the grid pager model.
 * 
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.4.0
 */
public class GridPagerModel implements Pageable {
    /** grid data model that uses the pager model */
    private GridDataModel model;
    /** page size */
    private int pageSize = 20;
    /** current page number */
    private int currentPageNumber = 0;
    /** displayed pages number */
    private int displayedPages = 10;

    /**
     * Creates an instance of this class and assigns this model with the grid model.
     *
     * @param model is a grid data model instance.
     */
    public GridPagerModel(GridDataModel model) {
        this.model = model;
    }

    /** {@inheritDoc} */
    public void setPageSize (int pageSize) {
        if (pageSize < 0)
            throw new IllegalArgumentException("Page size must be greater then zero");

        this.pageSize = pageSize;
    }

    /** {@inheritDoc} */
    public void setCurrentPageNumber (int currentPageNumber) throws IllegalArgumentException {
        if (currentPageNumber < 0)
            throw new IllegalArgumentException("Page number must be greater then zero");
        if (!getModel().isEmpty() && getTotalPagesNumber() <= currentPageNumber)
            throw new IllegalArgumentException("Page number must be less then total pages number");

        this.currentPageNumber = currentPageNumber;
    }

    /** {@inheritDoc} */
    public int getDisplayedPages () {
        return displayedPages;
    }

    /** {@inheritDoc} */
    public void setDisplayedPages (int displayedPages) {
        this.displayedPages = displayedPages;
    }

    /** {@inheritDoc} */
    public int getTotalPagesNumber () {
        int rowCount = getModel().getTotalRowCount();
        int pageSize = getPageSize();
        int result = rowCount / pageSize;

        return result + (result * pageSize == rowCount ? 0 : 1);
    }

    /** {@inheritDoc} */
    public int getStartPage () {
        int startPage = 0;

        if (getCurrentPageNumber() >= getDisplayedPages())
            startPage = getCurrentPageNumber() / getDisplayedPages() * getDisplayedPages();

        return startPage;
    }

    /** {@inheritDoc} */
    public int getEndPage () {
        int endPage;

        if (getStartPage() + getDisplayedPages() > getTotalPagesNumber() )
            endPage = getTotalPagesNumber() - 1;
        else
            endPage = getStartPage() + getDisplayedPages() - 1;

        if (endPage < 0)
            endPage = 0;

        return endPage;
    }

    /** {@inheritDoc} */
    public int getPageSize () {
        return pageSize;
    }

    /** {@inheritDoc} */
    public int getCurrentPageNumber () {
        return currentPageNumber;
    }

    /**
     * Getter for property 'model'.
     *
     * @return Value for property 'model'.
     */
    public GridDataModel getModel() {
        return model;
    }
}
