package org.androidpn.server.dao.hibernate;

import java.util.List;

import org.androidpn.server.dao.CollegeDao;
import org.androidpn.server.model.College;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class CollegeDaoHibernate extends HibernateDaoSupport implements CollegeDao
{

	public void saveCollege(College college) {
		getHibernateTemplate().saveOrUpdate(college);
		getHibernateTemplate().flush();
	}

	public List<College> getColleges() {
		
		@SuppressWarnings("unchecked")
		List<College> list = getHibernateTemplate()
				.find("from College") ;
		return list;
	}

}
