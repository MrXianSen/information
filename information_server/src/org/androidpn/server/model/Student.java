package org.androidpn.server.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 注册用户类
 * 此类的实体包含用户的基本信息
 * @author albery
 *
 */
@Entity
@Table(name="student")
public class Student {
	
	//学生ID
	@Id
	private String ID ;
	
	//学生姓名
	@Column(name="name" , length=16 , nullable=false , unique=true)
	private String name ;
	
	//姓名
	@Column(name="gender", length=1, nullable=false)
	private String gender;
	
	//密码
	@Column(name="password" , length=16 , nullable=false)
	private String password ;
	
	@Column(name="school", length=64)
	private String school;

	@Column(name="sign", length=256)
	private String sign;
	
	//头像图标链接
	@Column(name="icon_url" , length=64 , nullable=false)
	private String icon_url ;

	public String getID() {
		return ID;
	}

	public void setID(String inputId) {
		if (inputId == null) {
			String id = UUID.randomUUID().toString();
			id.replace("-", "");
			this.ID = id;
		}
		else{
			this.ID = inputId ;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon_url() {
		return icon_url;
	}

	public void setIcon_url(String icon_url) {
		this.icon_url = icon_url;
	}
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}
	
	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
}
