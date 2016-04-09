package org.androidpn.server.dao;

import java.util.List;

import org.androidpn.server.model.Student;

public interface StudentDao {
	
	void saveStudent(Student student) ;
	
	List<Student> getStduents() ;
	
	Student getStudent(String id) ;
	
	Student getStudent(String username, String password);
}
