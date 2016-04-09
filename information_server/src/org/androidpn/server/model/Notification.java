package org.androidpn.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 推送信息类
 * 使用到了ORM对象关系映射
 * @author Albery
 *
 */
@Entity
@Table(name = "notification")
public class Notification {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long id ;
	
	@Column(name = "api_key", length = 64)
	private String apiKey ;
	
	@Column(name = "username", nullable = false, length = 64)
	private String username ;
	
	@Column(name = "title", nullable = false, length = 64)
	private String title ;
	
	@Column(name = "type", nullable = false, length = 64)
	private String type ;
	
	@Column(name = "date", nullable = false, length = 12)
	private String date ;
	
	@Column(name = "hour", nullable = false, length = 12)
	private String hour ;
	
	@Column(name = "minute", nullable = false, length = 12)
	private String minute ;
	
	@Column(name = "message", nullable = false, length = 1000)
	private String message ;
	
	@Column(name = "uri", length = 256)
	private String uri ;
	
	@Column(name = "uuid", length = 64, nullable=false, unique=true)
	private String uuid ;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public String getMinute() {
		return minute;
	}

	public void setMinute(String minute) {
		this.minute = minute;
	}
	
}
