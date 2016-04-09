package org.androidpn.server.service.impl;

import java.util.List;

import org.androidpn.server.dao.StudentDao;
import org.androidpn.server.model.Student;
import org.androidpn.server.service.StudentService;

public class StudentServiceImpl implements StudentService {

	private StudentDao studentDao;


	public StudentDao getStudentDao() {
		return studentDao;
	}

	public void setStudentDao(StudentDao studentDao) {
		this.studentDao = studentDao;
	}

	public void saveStudent(Student student) {
		
		studentDao.saveStudent(student);
	}

	public List<Student> getStduents() {
		return studentDao.getStduents();
	}

	public Student getStudent(String id) {
		return studentDao.getStudent(id);
	}

	public Student getStudent(String username, String password) {
		return studentDao.getStudent(username, password);
	}
	
}
