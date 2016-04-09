package org.androidpn.server.service.impl;

import java.util.List;

import org.androidpn.server.dao.NotificationDao;
import org.androidpn.server.model.Notification;
import org.androidpn.server.service.NotificationService;

/**
 * NotifcationService实现
 * 调用NotifcationDao层中的方法
 * @author Administrator
 *
 */
public class NotificationServiceImpl implements NotificationService {
	
	private NotificationDao notificationDao ;
	
	public NotificationDao getNotificationDao() {
		return notificationDao;
	}

	public void setNotificationDao(NotificationDao notificationDao) {
		this.notificationDao = notificationDao;
	}

	public void saveNotification(Notification notification) {
		notificationDao.saveNotification(notification);
	}

	public List<Notification> findNotificatoinsByUsername(String username) {
		return notificationDao.findNotificatoinsByUsername(username);
	}

	public void deleteNotigication(Notification notification) {
		notificationDao.deleteNotigication(notification);
	}

	public void deleteNotificationByUUid(String uuid) {
		notificationDao.deleteNotificationByUUid(uuid);
	}
}
