package org.androidpn.server.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 信息评论数据类
 * @author Albery
 *
 */

@Entity
@Table(name = "informationComment")
public class InformationComment {

	//评论id
	@Id
	private String id ;
	
	//信息id
	@Column(name = "informationId", length = 64, nullable = false)
	private String informationId ;
	
	@Column(name="UserName", length = 64, nullable = false)
	private String UserName ;
	
	
	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getUserId() {
		return UserId;
	}

	public void setUserId(String userId) {
		UserId = userId;
	}

	@Column(name = "UserId", length = 64, nullable = false)
	private String UserId;
	
	//评论内容
	@Column(name = "content", length = 256, nullable = false)
	private String content ;

	public String getId() {
		return id;
	}

	public void setId() {
		String id = UUID.randomUUID().toString() ;
		id.replace("-", "") ;
		this.id = id;
	}

	public String getInformationId() {
		return informationId;
	}

	public void setInformationId(String informationId) {
		this.informationId = informationId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}  
}
