package org.androidpn.server.service;

import java.util.List;

import org.androidpn.server.model.Student;

public interface StudentService {

	void saveStudent(Student student);

	List<Student> getStduents();

	Student getStudent(String id);
	
	Student getStudent(String username, String password);
}
