package org.androidpn.server.service;

import java.util.List;

import org.androidpn.server.model.Notification;
/**
 * Service层
 * NotificationService接口
 * 连接dao层和业务逻辑层
 * 代码基本和NotificationDao层的相同
 * @author Albery
 *
 */
public interface NotificationService {
	
	public void saveNotification(Notification notification) ;
	
	public List<Notification> findNotificatoinsByUsername(String username) ;
	
	public void deleteNotigication(Notification notification) ;
	
	public void deleteNotificationByUUid(String uuid) ;

}
