package org.androidpn.server.service.impl;

import java.util.List;

import org.androidpn.server.dao.InformationCommentDao;
import org.androidpn.server.model.InformationComment;
import org.androidpn.server.service.InformationCommentService;

public class InformationCommentServiceImpl implements InformationCommentService {
	
	private InformationCommentDao informationCommentDao;

	public InformationCommentDao getInformationCommentDao() {
		return informationCommentDao;
	}

	public void setInformationCommentDao(InformationCommentDao informationCommentDao) {
		this.informationCommentDao = informationCommentDao;
	}

	public void saveInformationComment(InformationComment informationComment) {
		informationCommentDao.saveInformationComment(informationComment);
	}

	public List<InformationComment> getInformationCommentsByInformationID(
			String informationID) {
		return informationCommentDao.getInformationCommentsByInformationID(informationID);
	}

	public void deleteInformationCommentByID(String id) {
		informationCommentDao.deleteInformationCommentByID(id);
	}

	public InformationComment getInformationCommentByID(String id) {
		return informationCommentDao.getInformationCommentByID(id);
	}

}
