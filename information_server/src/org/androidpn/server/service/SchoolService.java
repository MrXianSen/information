package org.androidpn.server.service;

import java.util.List;

import org.androidpn.server.model.School;

public interface SchoolService {

	void saveSchool(School school);

	List<School> getSchools();

}
