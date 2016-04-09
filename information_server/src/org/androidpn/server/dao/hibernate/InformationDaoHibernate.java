package org.androidpn.server.dao.hibernate;

import java.util.List;

import org.androidpn.server.dao.InformationDao;
import org.androidpn.server.model.Information;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class InformationDaoHibernate extends HibernateDaoSupport implements
		InformationDao {

	public void saveInformation(Information information) {

		getHibernateTemplate().saveOrUpdate(information);
		getHibernateTemplate().flush();

	}

	public List<Information> findInformationByType(String type) {
		@SuppressWarnings("unchecked")
		List<Information> list = getHibernateTemplate().find(
				"from Information where type=?", type);

		if (list != null && list.size() > 0) {
			return list ;
		}

		return null;
	}

	public List<Information> findInformationByTitle(String title) {
		@SuppressWarnings("unchecked")
		List<Information> list = getHibernateTemplate().find(
				"from Information where title=?", title);

		if (list != null && list.size() > 0) {
			return list ;
		}

		return null;
	}

	public List<Information> getInformations() {
		@SuppressWarnings("unchecked")
		List<Information> list = getHibernateTemplate().find(
				"from Information i order by i.date");

		if (list != null && list.size() > 0) {
			return list ;
		}

		return null;
	}

	public void deleteInformation(Information information) {
		getHibernateTemplate().delete(information);
	}

	public Information findInformationById(String id) {
		
		Information information = null;
		
		@SuppressWarnings("unchecked")
		List<Information> list = getHibernateTemplate().find(
				"from Information where id=?",id) ;
		information = list.get(0) ;
		return information;
	}

	//主要适用于传递数据到客户端
	@SuppressWarnings("unchecked")
	public List<Information> getInformations(int start, int count) {
		List<Information> list = null ;
		//使用HQL语句获取数据库中指定数目的数据
		int totalRecord = this.getTotalRecord() ;
		if (totalRecord > start + count) {	//数据库中总数据数量是否足够
			list = getHibernateTemplate().find(
					"from Information i order by i.date desc").subList(start,
					start + count);
		}
		else
		{
			list = getHibernateTemplate().find(
					"from Information i order by i.date desc").subList(start,
					totalRecord);
		}
		
		return list;
	}

	public int getTotalRecord() {
		@SuppressWarnings("unchecked")
		List<Information> list = getHibernateTemplate().find(
				"from Information i order by i.date");
		return list.size();
	}

}
