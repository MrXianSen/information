package org.androidpn.server.model;

public class FeedBackStudent {
	public FeedBackMsg code ;
	public String msg;
	public Student student;
	public String toJson()
	{
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("{\"code\":");
		stringBuilder.append("\"");
		stringBuilder.append(code);
		stringBuilder.append("\"");
		stringBuilder.append(",");
		stringBuilder.append("\"msg\":");
		stringBuilder.append("\"");
		stringBuilder.append(msg);
		stringBuilder.append("\"");
		stringBuilder.append(",\"stuID\":\"");
		stringBuilder.append(student.getID());
		stringBuilder.append("\",\"stuName\":\"");
		stringBuilder.append(student.getName());
		stringBuilder.append("\",\"stuGender\":\"");
		stringBuilder.append(student.getGender());
		stringBuilder.append("\",\"stuSchool\":\"");
		stringBuilder.append(student.getSchool());
		stringBuilder.append("\",\"stuIcon\":\"");
		stringBuilder.append(student.getIcon_url());
		stringBuilder.append("\",\"stuSign\":\"");
		stringBuilder.append(student.getSign());
		stringBuilder.append("\",\"stuPwd\":\"");
		stringBuilder.append(student.getPassword());
		stringBuilder.append("\"}");
		return stringBuilder.toString();
	}
}
