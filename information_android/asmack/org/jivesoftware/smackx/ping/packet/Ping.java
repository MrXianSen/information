/**
 * Copyright 2012 Florian Schmaus
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

package org.jivesoftware.smackx.ping.packet;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smackx.ping.PingManager;

public class Ping extends IQ {

    public Ping() {
    }

    public Ping(String from, String to) {
        setTo(to);
        setFrom(from);
        setType(IQ.Type.GET);
        setPacketID(getPacketID());
    }

    @Override
    public String getChildElementXML() {
        return "<" + PingManager.ELEMENT + " xmlns=\'" + PingManager.NAMESPACE + "\' />";
    }

}
