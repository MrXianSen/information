package org.androidpn.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 此类是学院的实体类
 * 此类的实体对象包含学院的基本信息
 * @author albery
 *
 */

@Entity
@Table(name="college")
public class College {
	
	//学院ID
	@Id
	private String college_ID ;
	
	//学院名称
	@Column(name="name" , length=16 , nullable=false)
	private String name ;
	
	public String getCollege_ID() {
		return college_ID;
	}

	public void setCollege_ID(String college_ID) {
		this.college_ID = college_ID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
