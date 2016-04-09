package org.androidpn.server.dao.hibernate;

import java.util.List;

import org.androidpn.server.dao.InformationCommentDao;
import org.androidpn.server.model.InformationComment;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class InformationCommentDaoHibernate extends HibernateDaoSupport
		implements InformationCommentDao {

	public void saveInformationComment(InformationComment informationComment) {
		getHibernateTemplate().saveOrUpdate(informationComment);
		getHibernateTemplate().flush();
	}

	public List<InformationComment> getInformationCommentsByInformationID(
			String informationID) {
		@SuppressWarnings("unchecked")
		List<InformationComment> list = getHibernateTemplate().
				find("from InformationComment where informationId=?",informationID) ;
		if(list != null && list.size() > 0)
		{
			return list ;
		}
		return null;
	}

	public void deleteInformationCommentByID(String id) {
		InformationComment informationComment = getInformationCommentByID(id) ;
		if(informationComment != null)
		{
			getHibernateTemplate().delete(informationComment);
		}
	}

	public InformationComment getInformationCommentByID(String id) {
		@SuppressWarnings("unchecked")
		List<InformationComment> list = getHibernateTemplate()
				.find("from InformationComment where id=?", id) ;
		InformationComment informationComment ;
		if(list != null && list.size() > 0)
		{
			informationComment = list.get(0) ;
			return informationComment ;
		}
		return null;
	}
}
