package com.albery.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.androidpn.server.model.Information;
import org.androidpn.server.model.InformationComment;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 将数据封装成JSON格式
 * 
 * @author Albery
 * 
 */
public class DataToJson {

	/**
	 * 将传入的数据对象封装成JSON数据
	 * 
	 * @param informations
	 * @return JSONArray对象
	 */
	public static JSONArray toJson(List<Information> informations) {
		boolean isLastOne = true;
		JSONArray jsonArray = null;
		StringBuilder jsonStr = new StringBuilder();
		jsonStr.append("[");
		for (Information information : informations) {
			if (isLastOne) {
				jsonStr.append("{");
				jsonStr.append("\"id\":");
				jsonStr.append("\"" + information.getID() + "\",");
				jsonStr.append("\"title\":");
				jsonStr.append("\"" + information.getTitle() + "\",");
				jsonStr.append("\"date\":");
				jsonStr.append("\"" + information.getDate()+" "+information.getTime() + "\",");
				jsonStr.append("\"type\":");
				jsonStr.append("\"" + information.getType() + "\",");
				jsonStr.append("\"content\":");
				jsonStr.append("\"" + information.getContent() + "\"");
				jsonStr.append("}");
				isLastOne = false;
				continue;
			}
			jsonStr.append(",{");
			jsonStr.append("\"id\":");
			jsonStr.append("\"" + information.getID() + "\",");
			jsonStr.append("\"title\":");
			jsonStr.append("\"" + information.getTitle() + "\",");
			jsonStr.append("\"date\":");
			jsonStr.append("\"" + information.getDate()+" "+information.getTime() + "\",");
			jsonStr.append("\"type\":");
			jsonStr.append("\"" + information.getType() + "\",");
			jsonStr.append("\"content\":");
			jsonStr.append("\"" + information.getContent() + "\"");
			jsonStr.append("}");
		}
		jsonStr.append("]");
		jsonArray = new JSONArray(jsonStr.toString());
		return jsonArray;
	}
	public static JSONArray commentToJson(List<InformationComment> comments)
	{
		boolean isLastOne = true;
		JSONArray jsonArray = null;
		StringBuilder jsonStr = new StringBuilder();
		jsonStr.append("[");
		if(comments == null && comments.size() <= 0) return null;
		for (InformationComment comment : comments) {
			if (isLastOne) {
				jsonStr.append("{\"id\":\"");
				jsonStr.append(comment.getId());
				jsonStr.append("\",\"infoId\":\"");
				jsonStr.append(comment.getInformationId());
				jsonStr.append("\",\"userId\":\"");
				jsonStr.append(comment.getUserId());
				jsonStr.append("\",\"userName\":\"");
				jsonStr.append(comment.getUserName());
				jsonStr.append("\",\"content\":\"");
				jsonStr.append(comment.getContent());
				jsonStr.append("\"}");
				isLastOne = false;
				continue;
			}
			jsonStr.append(",{\"id\":\"");
			jsonStr.append(comment.getId());
			jsonStr.append("\",\"infoId\":\"");
			jsonStr.append(comment.getInformationId());
			jsonStr.append("\",\"userId\":\"");
			jsonStr.append(comment.getUserId());
			jsonStr.append("\",\"userName\":\"");
			jsonStr.append(comment.getUserName());
			jsonStr.append("\",\"content\":\"");
			jsonStr.append(comment.getContent());
			jsonStr.append("\"}");
		}
		jsonStr.append("]");
		jsonArray = new JSONArray(jsonStr.toString());
		return jsonArray;
	}
}
