/**
 * $RCSfile$
 * $Revision: 10204 $
 * $Date: 2008-04-11 15:44:25 -0700 (Fri, 11 Apr 2008) $
 *
 * Copyright (C) 2004-2008 Jive Software. All rights reserved.
 *
 * This software is published under the terms of the GNU Public License (GPL),
 * a copy of which is included in this distribution, or a commercial license
 * agreement with Jive.
 */

package org.jivesoftware.util;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Dispatches property events. Each event has a {@link EventType type}
 * and optional parameters, as follows:<p>
 *
 * <table border="1">
 * <tr><th>Event Type</th><th>Extra Params</th></tr>
 * <tr><td>{@link EventType#property_set property_set}</td><td>A param named <tt>value</tt> that
 *      has the value of the property set.</td></tr>
 * <tr><td>{@link EventType#property_deleted property_deleted}</td><td><i>None</i></td></tr>
 * <tr><td>{@link EventType#xml_property_set xml_property_set}</td><td>A param named <tt>value</tt> that
 *      has the value of the property set.</td></tr>
 * <tr><td>{@link EventType#xml_property_deleted xml_property_deleted}</td><td><i>None</i></td></tr>
 * </table>
 *
 * @author Matt Tucker
 */
public class PropertyEventDispatcher {

    private static Set<PropertyEventListener> listeners =
            new CopyOnWriteArraySet<PropertyEventListener>();

    private PropertyEventDispatcher() {
        // Not instantiable.
    }

    /**
     * Registers a listener to receive events.
     *
     * @param listener the listener.
     */
    public static void addListener(PropertyEventListener listener) {
        if (listener == null) {
            throw new NullPointerException();
        }
        listeners.add(listener);
    }

    /**
     * Unregisters a listener to receive events.
     *
     * @param listener the listener.
     */
    public static void removeListener(PropertyEventListener listener) {
        listeners.remove(listener);
    }

    /**
     * Dispatches an event to all listeners.
     *
     * @param property the property.
     * @param eventType the event type.
     * @param params event parameters.
     */
    public static void dispatchEvent(String property, EventType eventType, Map<String, Object> params) {
        for (PropertyEventListener listener : listeners) {
            try {
                switch (eventType) {
                    case property_set: {
                        listener.propertySet(property, params);
                        break;
                    }
                    case property_deleted: {
                        listener.propertyDeleted(property, params);
                        break;
                    }
                    case xml_property_set: {
                        listener.xmlPropertySet(property, params);
                        break;
                    }
                    case xml_property_deleted: {
                        listener.xmlPropertyDeleted(property, params);
                        break;
                    }
                    default:
                        break;
                }
            }
            catch (Exception e) {
                // Log.error(e);
                e.printStackTrace();
            }
        }
    }

    /**
     * Represents valid event types.
     */
    public enum EventType {

        /**
         * A property was set.
         */
        property_set,

        /**
         * A property was deleted.
         */
        property_deleted,

        /**
         * An XML property was set.
         */
        xml_property_set,

        /**
         * An XML property was deleted.
         */
        xml_property_deleted;
    }
}