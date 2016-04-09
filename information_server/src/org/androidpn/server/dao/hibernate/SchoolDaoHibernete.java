package org.androidpn.server.dao.hibernate;

import java.util.List;

import org.androidpn.server.dao.SchoolDao;
import org.androidpn.server.model.School;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class SchoolDaoHibernete extends HibernateDaoSupport implements
		SchoolDao {

	public void saveSchool(School school) {
		
		getHibernateTemplate().saveOrUpdate(school);
		getHibernateTemplate().flush() ;

	}

	public List<School> getSchools() {
		
		@SuppressWarnings("unchecked")
		List<School> list = getHibernateTemplate()
				.find("from School") ;
		return list;
	}

}
