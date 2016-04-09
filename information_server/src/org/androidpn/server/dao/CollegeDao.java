package org.androidpn.server.dao;

import java.util.List;

import org.androidpn.server.model.College;

public interface CollegeDao {
	
	void saveCollege(College college) ;
	
	List<College> getColleges() ;

}
