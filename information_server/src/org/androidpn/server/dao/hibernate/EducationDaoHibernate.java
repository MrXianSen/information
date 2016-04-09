package org.androidpn.server.dao.hibernate;

import java.util.List;

import org.androidpn.server.dao.EducationDao;
import org.androidpn.server.model.Education;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class EducationDaoHibernate extends HibernateDaoSupport implements
		EducationDao {

	public void saveEducation(Education education) {
		
		getHibernateTemplate().saveOrUpdate(education);
		getHibernateTemplate().flush();

	}

	public Education getEducationByID(String id) {
		
		@SuppressWarnings("unchecked")
		List<Education> list = getHibernateTemplate()
				.find("from Education where ID=?",id) ;
		if(list != null)
		{
			return list.get(0) ;
		}
		return null; 
	}

}
