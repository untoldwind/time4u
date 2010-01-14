package org.gwt.advanced.client.ui.widget.border;

/**
 * This interface describes a border factory that is used by some components to create borders.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.4.6
 */
public interface BorderFactory {
    /**
     * This method always creates a new instance of border.
     *
     * @return a new border widget.
     */
    Border create();
}
