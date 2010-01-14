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
 * This object represents a labeled icon item for the conbo box model.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.2.0
 */
public class IconItem {
    /** an icon image name */
    private String imageName;
    /** an icon label */
    private String label;

    /**
     * Creates an instance of this class and initilizes internal fields.
     *
     * @param imageName is an image name.
     * @param label is a label.
     */
    public IconItem(String imageName, String label) {
        this.imageName = imageName;
        this.label = label;
    }

    /**
     * Getter for property 'imageName'.
     *
     * @return Value for property 'imageName'.
     */
    public String getImageName() {
        return imageName;
    }

    /**
     * Getter for property 'label'.
     *
     * @return Value for property 'label'.
     */
    public String getLabel() {
        return label;
    }
}
