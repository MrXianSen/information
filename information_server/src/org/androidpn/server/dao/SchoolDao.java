package org.androidpn.server.dao;

import java.util.List;

import org.androidpn.server.model.School;

public interface SchoolDao {
	
	void saveSchool(School school) ;
	
	List<School> getSchools() ;

}
