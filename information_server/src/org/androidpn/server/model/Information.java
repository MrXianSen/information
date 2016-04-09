package org.androidpn.server.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 信息类
 * @author Albery
 *
 */

@Entity
@Table(name = "information")
public class Information {
	
	@Id
    private String id;
	
	@Column(name = "title", length = 64, nullable = false)
	private String title ;
	
	@Column(name = "date", updatable = true)
	private String date ;
	
	@Column(name = "hour", nullable=false, length=16)
	private String hour ;
	
	@Column(name = "minute", nullable=false, length=16)
	private String minute ;
	
	@Column(name = "type", length = 16, nullable = false)
	private String type ;
	
	@Column(name = "content", nullable = false, length=5000)
	private String content ;
	
	@Column(name = "shows", nullable = false, length=100)
	private String shows ;

	public String getShows() {
		return shows;
	}

	public void setShows(String content) {
		StringBuilder builder = new StringBuilder() ;
		if(content.length() > 100)
		{
			builder.append(content.substring(0, 40)) ;
			builder.append("......") ;
			builder.append(content.substring(content.length() - 40, 
					content.length())) ;
		}
		else
		{
			builder.append(content) ;
		}
		this.shows = builder.toString();
	}

	public void setID(String inputId)
	{
		if (inputId == null) {
			String id = UUID.randomUUID().toString();
			id.replace("-", "");
			this.id = id;
		}
		else{
			this.id = inputId ;
		}
	}
	
	public String getID()
	{
		return id ;
	}
	
	public void setDate(String date)
	{
		this.date = date ;
	}
	
	public String getDate()
	{
		return date ;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return this.hour + ":" + this.minute ;
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
