package org.androidpn.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 关于学校的实体类
 * 此类的实体包含学校的基本信息
 * @author albery
 *
 */
@Entity
@Table(name="school")
public class School {
	
	//学校ID
	@Id
	private String school_ID ;
	
	//学校名称
	@Column(name="name" , length=16 , unique=false , nullable=false)
	private String name ;

	public String getSchool_ID() {
		return school_ID;
	}

	public void setSchool_ID(String school_ID) {
		this.school_ID = school_ID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
