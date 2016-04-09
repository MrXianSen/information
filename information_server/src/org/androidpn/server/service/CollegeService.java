package org.androidpn.server.service;

import java.util.List;

import org.androidpn.server.model.College;

public interface CollegeService {
	
void saveCollege(College college) ;
	
	List<College> getColleges() ;

}
