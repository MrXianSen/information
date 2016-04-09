package org.androidpn.server.dao.hibernate;

import java.util.List;

import org.androidpn.server.dao.NotificationDao;
import org.androidpn.server.model.Notification;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/*
 * 继承HibernateDaoSupport
 * 调用getHibernateTeplate通过这个方法来实现对数据库的访问操作
 * 实现了NotificationDao接口
 */
public class NotificationDaoHibernate extends HibernateDaoSupport 
	implements NotificationDao{

	/**
	 * 保存推送信息
	 */
	public void saveNotification(Notification notification) {
		getHibernateTemplate().saveOrUpdate(notification);
		getHibernateTemplate().flush();
	}
	
	/**
	 * 通过用户名查询推送信息
	 */
	public List<Notification> findNotificatoinsByUsername(String username) {
		@SuppressWarnings("unchecked")
		//使用HQL语言
		List<Notification> notifications = getHibernateTemplate().find(
				"from Notification where username=?",username) ;
		if(notifications != null && notifications.size() > 0)
		{
			return notifications ;
		}
		return null ;
	}

	/**
	 * 删除推送信息
	 */
	public void deleteNotigication(Notification notification) {
		getHibernateTemplate().delete(notification);
	}

	/**
	 * 通过UUID删除推送信息
	 */
	public void deleteNotificationByUUid(String uuid) {
		@SuppressWarnings("unchecked")
		List<Notification> list = getHibernateTemplate().find(
				"from Notification where uuid=?",uuid) ;
		if(list!=null && list.size() > 0)
		{
			Notification notification = list.get(0) ;
			deleteNotigication(notification);
		}
	}

}
