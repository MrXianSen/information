package org.androidpn.server.service.impl;

import java.util.List;

import org.androidpn.server.dao.SchoolDao;
import org.androidpn.server.model.School;
import org.androidpn.server.service.SchoolService;

public class SchoolServiceImpl implements SchoolService {
	
	private SchoolDao schoolDao ;


	public SchoolDao getSchoolDao() {
		return schoolDao;
	}

	public void setSchoolDao(SchoolDao schoolDao) {
		this.schoolDao = schoolDao;
	}

	public List<School> getSchools() {
		return schoolDao.getSchools();
	}

	public void saveSchool(School school) {
		schoolDao.saveSchool(school);
	}

}
