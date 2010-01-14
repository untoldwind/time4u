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
 * This is a list data model extention that is used by the
 * {@link org.gwt.advanced.client.datamodel.SuggestionBoxDataModel} for the same purposes but proides
 * additional event types.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.4.9
 */
public class SuggestionModelEvent extends ListModelEvent {
    /** expression value changed */
    public static final EventType EXPRESSION_CHANGED = new EventType();
    /** related expression value*/
    private String expression;

    /**
     * Creates an instance of this class.
     * This constructor is applicable for events which must contain data related to
     * expressions
     *
     * @param source is a data model that produced the event.
     * @param type is an event type.
     * @param expression is an expression value.
     */
    public SuggestionModelEvent(ListDataModel source, EventType type, String expression) {
        super(source, type);
        this.expression = expression;
    }

    /**
     * Gets an expression value.<p/>
     * Might be equal to <code>null</code> if the event is not related to expression changes. 
     *
     * @return an expresion value.
     */
    public String getExpression() {
        return expression;
    }
}
