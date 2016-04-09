/**
 * $RCSfile$
 * $Revision$
 * $Date$
 * <p>
 * Copyright 2003-2007 Jive Software.
 * <p>
 * All rights reserved. Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jivesoftware.smackx;

/**
 *
 * Default implementation of the MessageEventRequestListener interface.<p>
 *
 * This class automatically sends a delivered notification to the sender of the message 
 * if the sender has requested to be notified when the message is delivered. 
 *
 * @author Gaston Dombiak
 */
public class DefaultMessageEventRequestListener implements MessageEventRequestListener {

    @Override
    public void deliveredNotificationRequested(String from, String packetID,
                                               MessageEventManager messageEventManager) {
        // Send to the message's sender that the message has been delivered
        messageEventManager.sendDeliveredNotification(from, packetID);
    }

    @Override
    public void displayedNotificationRequested(String from, String packetID,
                                               MessageEventManager messageEventManager) {
    }

    @Override
    public void composingNotificationRequested(String from, String packetID,
                                               MessageEventManager messageEventManager) {
    }

    @Override
    public void offlineNotificationRequested(String from, String packetID,
                                             MessageEventManager messageEventManager) {
    }
}
