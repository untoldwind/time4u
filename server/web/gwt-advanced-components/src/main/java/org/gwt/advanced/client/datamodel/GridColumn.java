package org.gwt.advanced.client.datamodel;

/**
 * This is a grid column implementation.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.4.0
 */
public class GridColumn implements IndexedEntity {
    /** column name */
    private String name;
    /** column index */
    private int index;
    /** parent model reference */
    private Editable model;

    /**
     * Creates an instance of this class and associates it with the parent model.
     *
     * @param model is a grid model.
     */
    protected GridColumn(Editable model) {
        this.model = model;
    }

    /**
     * Getter for property 'name'.
     *
     * @return Value for property 'name'.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for property 'name'.
     *
     * @param name Value to set for property 'name'.
     */
    public void setName(String name) {
        this.name = name;
    }

    /** {@inheritDoc} */
    public int getIndex() {
        return index;
    }

    /** {@inheritDoc} */
    public void setIndex(int index) {
        this.index = index;
    }

    /** {@inheritDoc} */
    public Object[] getData() throws IllegalStateException {
        if (getIndex() > model.getTotalColumnCount() - 1 || getIndex() < 0)
            throw new IllegalStateException("Column index is out of total column count range");
        
        Object[][] data = model.getData();
        Object[] result = new Object[model.getTotalRowCount()];
        for (int i = 0; i < data.length; i++) {
            Object[] row = data[i];
            if (row != null)
                result[i] = row[getIndex()];
        }
        return result;
    }

    /**
     * Getter for property 'model'.
     *
     * @return Value for property 'model'.
     */
    protected Editable getModel() {
        return model;
    }
}
