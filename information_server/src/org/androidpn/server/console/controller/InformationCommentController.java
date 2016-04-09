package org.androidpn.server.console.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.androidpn.server.model.InformationComment;
import org.androidpn.server.service.InformationCommentService;
import org.androidpn.server.service.ServiceLocator;
import org.json.JSONArray;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.albery.data.DataToJson;

public class InformationCommentController extends MultiActionController {

	private InformationCommentService informationCommentService;

	public InformationCommentController() {
		this.informationCommentService = ServiceLocator
				.getInformationCommentService();
	}

	/**
	 * 添加评论信息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void addComment(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String userID = request.getParameter("userID");
		String username = request.getParameter("userName");
		String informationID = request.getParameter("infoID");
		String content = request.getParameter("content");
		InformationComment comment = new InformationComment();
		comment.setId();
		comment.setUserId(userID);
		comment.setInformationId(informationID);
		comment.setUserName(username);
		comment.setContent(content);
		informationCommentService.saveInformationComment(comment);
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("{\"code\":\"1\",\"msg\":\"添加成功\"}");
		System.out.println(strBuilder.toString());
		response.setCharacterEncoding("UTF-8");
		response.getOutputStream().write(
				strBuilder.toString().getBytes("UTF-8"));
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}

	public void commentList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String infoID = request.getParameter("infoID");
		List<InformationComment> list = informationCommentService
				.getInformationCommentsByInformationID(infoID);
		if (list != null && list.size() > 0) {
			JSONArray jsonArray = DataToJson.commentToJson(list);
			byte[] jsonBytes = jsonArray.toString().getBytes("UTF-8");
			// 写进response域中
			response.setCharacterEncoding("UTF-8");
			response.getOutputStream().write(jsonBytes);
			response.getOutputStream().flush();
			response.getOutputStream().close();
		}
		else
		{
			String msg = "[{\"code\":\"0\",\"msg\":\"无数据\"}]";
			response.setCharacterEncoding("UTF-8");
			response.getOutputStream().write(msg.getBytes("utf-8"));
			response.getOutputStream().flush();
			response.getOutputStream().close();
		}
	}
}