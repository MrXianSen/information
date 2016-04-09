package org.androidpn.server.dao;


import java.util.List;

import org.androidpn.server.model.Notification;

/*
 * NotifiationDao层
 * 提供对数据库操作
 */
public interface NotificationDao {
	
	public void saveNotification(Notification notification) ;
	
	public List<Notification> findNotificatoinsByUsername(String username) ;
	
	public void deleteNotigication(Notification notification) ;
	
	public void deleteNotificationByUUid(String uuid) ;

}
