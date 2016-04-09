/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidpn.client;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

/**
 * This class parses incoming IQ packets to NotificationIQ objects.
 * 解析到达的IQ
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class NotificationIQProvider implements IQProvider {

    public NotificationIQProvider() {
    }

    @Override
    public IQ parseIQ(XmlPullParser parser) throws Exception {

        //<id>1d06ac0a</id><apiKey>1234567890</apiKey><title>测试数据</title><message>测试数据内容</message><uri></uri>
        //<hour>10</hour><minute>0</minute><date>2015-05-05</date><type>宣讲会</type></notification></iq>
        NotificationIQ notification = new NotificationIQ();
        for (boolean done = false; !done; ) {
            int eventType = parser.next();
            if (eventType == 2) {
                //解析到达的数据并且将其设置到notification中保存起来
                if ("id".equals(parser.getName())) {    //id
                    notification.setId(parser.nextText());
                }
                if ("apiKey".equals(parser.getName())) {    //apikey
                    notification.setApiKey(parser.nextText());
                }
                if ("title".equals(parser.getName())) {        //title
                    notification.setTitle(parser.nextText());
                }
                if ("message".equals(parser.getName())) {    //message
                    notification.setMessage(parser.nextText());
                }
                if ("uri".equals(parser.getName())) {    //uri
                    notification.setUri(parser.nextText());
                }
                if ("hour".equals(parser.getName()))        //hour
                {
                    notification.setHour(parser.nextText());
                }
                if ("minute".equals(parser.getName()))    //minute
                {
                    notification.setMinute(parser.nextText());
                }
                if ("date".equals(parser.getName()))        //date
                {
                    notification.setDate(parser.nextText());
                }
                if ("type".equals(parser.getName()))        //type
                {
                    notification.setType(parser.nextText());
                }
            } else if (eventType == 3
                    && "notification".equals(parser.getName())) {
                done = true;
            }
        }

        return notification;
    }

}
