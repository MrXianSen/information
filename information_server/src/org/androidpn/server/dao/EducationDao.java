package org.androidpn.server.dao;

import org.androidpn.server.model.Education;

public interface EducationDao {
	
	void saveEducation(Education education) ;
	
	Education getEducationByID(String id) ;

}
