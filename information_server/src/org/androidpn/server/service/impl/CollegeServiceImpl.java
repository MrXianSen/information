package org.androidpn.server.service.impl;

import java.util.List;

import org.androidpn.server.dao.CollegeDao;
import org.androidpn.server.model.College;
import org.androidpn.server.service.CollegeService;

public class CollegeServiceImpl implements CollegeService {

	private CollegeDao collegeDao ;
	
	
	public CollegeDao getCollegeDao() {
		return collegeDao;
	}

	public void setCollegeDao(CollegeDao collegeDao) {
		this.collegeDao = collegeDao;
	}

	public void saveCollege(College college) {

		collegeDao.saveCollege(college);
	}

	public List<College> getColleges() {
		return collegeDao.getColleges();
	}

}
