package org.gwt.advanced.client.datamodel;

/**
 * This interface describes an indexed enity, i.e. any object that has unique numeric index.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.4.0
 */
public interface IndexedEntity {
    /**
     * Gets an unique index of the object.
     *
     * @return an index value.
     */
    int getIndex();

    /**
     * Sets an unique index of the object.
     *
     * @param index is an index value.
     */
    void setIndex(int index);

    /**
     * This method gets data of this row.
     *
     * @return a data array.
     */
    Object[] getData();
}
