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

package org.jivesoftware.smackx.provider;

import java.util.ArrayList;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smackx.*;
import org.jivesoftware.smackx.packet.*;
import org.xmlpull.v1.XmlPullParser;

/**
 *
 * The RosterExchangeProvider parses RosterExchange packets.
 *
 * @author Gaston Dombiak
 */
public class RosterExchangeProvider implements PacketExtensionProvider {

    /**
     * Creates a new RosterExchangeProvider.
     * ProviderManager requires that every PacketExtensionProvider has a public, no-argument constructor
     */
    public RosterExchangeProvider() {
    }

    /**
     * Parses a RosterExchange packet (extension sub-packet).
     *
     * @param parser the XML parser, positioned at the starting element of the extension.
     * @return a PacketExtension.
     * @throws Exception if a parsing error occurs.
     */
    @Override
    public PacketExtension parseExtension(XmlPullParser parser) throws Exception {

        RosterExchange rosterExchange = new RosterExchange();
        boolean done = false;
        RemoteRosterEntry remoteRosterEntry = null;
        String jid = "";
        String name = "";
        ArrayList<String> groupsName = new ArrayList<String>();
        while (!done) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.getName().equals("item")) {
                    // Reset this variable since they are optional for each item
                    groupsName = new ArrayList<String>();
                    // Initialize the variables from the parsed XML
                    jid = parser.getAttributeValue("", "jid");
                    name = parser.getAttributeValue("", "name");
                }
                if (parser.getName().equals("group")) {
                    groupsName.add(parser.nextText());
                }
            } else if (eventType == XmlPullParser.END_TAG) {
                if (parser.getName().equals("item")) {
                    // Create packet.
                    remoteRosterEntry = new RemoteRosterEntry(jid, name, groupsName.toArray(new String[groupsName.size()]));
                    rosterExchange.addRosterEntry(remoteRosterEntry);
                }
                if (parser.getName().equals("x")) {
                    done = true;
                }
            }
        }

        return rosterExchange;

    }

}
