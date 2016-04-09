/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.androidpn.server.xmpp.push;

import java.util.List;
import java.util.Random;
import java.util.Set;

import org.androidpn.server.model.Notification;
import org.androidpn.server.model.User;
import org.androidpn.server.service.NotificationService;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.service.UserNotFoundException;
import org.androidpn.server.service.UserService;
import org.androidpn.server.xmpp.session.ClientSession;
import org.androidpn.server.xmpp.session.SessionManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;
import org.xmpp.packet.IQ;

/** 
 * This class is to manage sending the notifications to the users.  
 * 这个类来管理发送推送消息给用户
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class NotificationManager {

    private static final String NOTIFICATION_NAMESPACE = "androidpn:iq:notification";

    private final Log log = LogFactory.getLog(getClass());

    private SessionManager sessionManager;
    
    private NotificationService notificationService ;
    
    private UserService userService ;

    /**
     * Constructor.
     */
    public NotificationManager() {
    	//
        sessionManager = SessionManager.getInstance();
        notificationService = ServiceLocator.getNotificationService() ;
        userService = ServiceLocator.getUserService() ;
    }

    /**
     * Broadcasts a newly created notification message to all connected users.
     * 
     * @param apiKey the API key
     * @param title the title
     * @param message the message details
     * @param uri the uri
     */
	public void sendBroadcast(String apiKey, String hour, String minute,
			String date, String type, String title, String message,
			String uri) {
		log.debug("sendBroadcast()...");
		// 获取数据库中的所有用户
		List<User> list = userService.getUsers();
		for (User user : list) {
			Random random = new Random();
			String id = Integer.toHexString(random.nextInt());
			//将页面获取的数据封装成一个IQ
			IQ notificationIQ = createNotificationIQ(id, hour, minute, date, type, apiKey, title,
					message, uri);
			// 通过用户名获取客户和服务器的会话
			ClientSession session = sessionManager.getSession(user
					.getUsername());
			// 如果会话不为空，且该用户在线就将消息发送给该用户
			if (session != null && session.getPresence().isAvailable()) {
				notificationIQ.setTo(session.getAddress());
				session.deliver(notificationIQ);
			}
			// 保存这条信息
			saveNotification(id, hour, minute, date, type, apiKey, user.getUsername(), title, message,
					uri);
		}
	}

    /**
     * Sends a newly created notification message to the specific user.
     * 
     * @param apiKey the API key
     * @param title the title
     * @param message the message details
     * @param uri the uri
     */
    public void sendNotifcationToUser(String apiKey, String hour, String minute,
    		String date, String type, String username,
            String title, String message, String uri, boolean shouldSave) {
        log.debug("sendNotifcationToUser()...");
        Random random = new Random();
        String id = Integer.toHexString(random.nextInt());
        IQ notificationIQ = createNotificationIQ(id, hour, minute, date, type, apiKey, title, message, uri);
        ClientSession session = sessionManager.getSession(username);
        if (session != null) {
            if (session.getPresence().isAvailable()) {
                notificationIQ.setTo(session.getAddress());
                session.deliver(notificationIQ);
            }
        }
		try {
			User user = userService.getUserByUsername(username);
			if (user != null && shouldSave) {
				saveNotification(id, hour, minute, date, type, apiKey, username, title, message, uri);
			}
		} catch (UserNotFoundException e) {
			e.printStackTrace();
		}
	}
    
    /**
     * 通过别名推送消息
     * 
     */
	public void sendNotificationByAlias(String apiKey, String hour, String minute,
			String date, String type, String alias,
            String title, String message, String uri, boolean shouldSave) {
		//获取用户名
		String username = sessionManager.getUsernameByAlias(alias) ;
		System.out.println("客户机名称："+username);
		if(username != null){
			sendNotifcationToUser(apiKey, hour, minute, date, type, username, title, message, uri, shouldSave);
		}
	}
	
	/*
	 * 通过别名推送消息
	 */
	public void sendNotificationByTag(String apiKey, String hour, String minute,
			String date, String type, String tag,
            String title, String message, String uri, boolean shouldSave)
	{
		Set<String> usernameSet = sessionManager.getUsersByTag(tag) ;
		if(usernameSet != null && !usernameSet.isEmpty()){
			for(String username : usernameSet)
			{
				sendNotifcationToUser(apiKey,hour, minute, date, type, username, title, message, uri, shouldSave);
			}
		}
	}
    
    public void saveNotification(String uuid, String hour, String minute,
    		String date, String type, String apiKey, String username,
            String title, String message, String uri)
    {
    	Notification notification = new Notification() ;
    	notification.setApiKey(apiKey);
    	notification.setMessage(message);
    	notification.setTitle(title);
    	notification.setUri(uri);
    	notification.setUsername(username);
    	notification.setUuid(uuid);
    	notification.setDate(date);
    	notification.setType(type);
    	notification.setHour(hour);
    	notification.setMinute(minute);
    	notificationService.saveNotification(notification);
    }

    /**
     * Creates a new notification IQ and returns it.
     */
    private IQ createNotificationIQ(String id, String hour, String minute, 
    		String date, String type, String apiKey, String title,
            String message, String uri) {
        // String id = String.valueOf(System.currentTimeMillis());

        Element notification = DocumentHelper.createElement(QName.get(
                "notification", NOTIFICATION_NAMESPACE));
        notification.addElement("id").setText(id);
        notification.addElement("apiKey").setText(apiKey);
        notification.addElement("title").setText(title);
        notification.addElement("message").setText(message);
        notification.addElement("uri").setText(uri);
        notification.addElement("hour").setText(hour);
        notification.addElement("minute").setText(minute);
        notification.addElement("date").setText(date);
        notification.addElement("type").setText(type);

        IQ iq = new IQ();
        iq.setType(IQ.Type.set);
        iq.setChildElement(notification);

        return iq;
    }
}
