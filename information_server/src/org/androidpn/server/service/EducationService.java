package org.androidpn.server.service;

import org.androidpn.server.model.Education;

public interface EducationService {

	void saveEducation(Education education);

	Education getEducationByID(String id);

}
