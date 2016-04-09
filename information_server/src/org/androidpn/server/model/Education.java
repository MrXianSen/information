package org.androidpn.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="education")
public class Education {
	
	@Id
	private String student_id ;
	
	// 学号
	@Column(name="student_number" , length=24 , unique=true)
	private String student_number;

	// 学校ID
	@Column(name="school_ID" , length=12 , unique=true)
	private String school_ID;

	// 学院ID
	@Column(name="college_ID" , length=12 , unique=true)
	private String college_ID;

	public String getStudent_id() {
		return student_id;
	}

	public void setStudent_id(String student_id) {
		this.student_id = student_id;
	}

	public String getStudent_number() {
		return student_number;
	}

	public void setStudent_number(String student_number) {
		this.student_number = student_number;
	}

	public String getSchool_ID() {
		return school_ID;
	}

	public void setSchool_ID(String school_ID) {
		this.school_ID = school_ID;
	}

	public String getCollege_ID() {
		return college_ID;
	}

	public void setCollege_ID(String college_ID) {
		this.college_ID = college_ID;
	}
}
