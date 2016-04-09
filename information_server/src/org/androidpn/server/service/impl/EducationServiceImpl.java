package org.androidpn.server.service.impl;

import org.androidpn.server.dao.EducationDao;
import org.androidpn.server.model.Education;
import org.androidpn.server.service.EducationService;

public class EducationServiceImpl implements EducationService {

	private EducationDao educationDao ;
	
	
	public EducationDao getEducationDao() {
		return educationDao;
	}

	public void setEducationDao(EducationDao educationDao) {
		this.educationDao = educationDao;
	}

	public void saveEducation(Education education) {
		educationDao.saveEducation(education);
	}

	public Education getEducationByID(String id) {
		return educationDao.getEducationByID(id);
	}

}
