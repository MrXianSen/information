package org.androidpn.server.console.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.service.StudentService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.androidpn.server.model.FeedBackStudent;
import org.androidpn.server.model.FeedBackMsg;
import org.androidpn.server.model.Student;

/**
 * 学生对象的页面的controllor
 * 
 * @author albery 日期：2015.8.27
 * 
 */
public class StudentController extends MultiActionController {
	// 服务层对象
	private StudentService studentService;

	public StudentController() {
		this.studentService = ServiceLocator.getStudentService();
	}

	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mvd = new ModelAndView();
		mvd.setViewName("student/list");
		return mvd;
	}

	/*******************************************************
	 * 判断用户是否存在
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 ******************************************************/
	private FeedBackStudent isUserExisted(String username, String password) {
		Student student = studentService.getStudent(username, password);
		FeedBackStudent feedBack = new FeedBackStudent();
		if (student == null) {
			feedBack.code = FeedBackMsg.USER_NOT_EXIST;
			feedBack.msg = "用户不存在";
			feedBack.student = new Student();
		} else {
			feedBack.code = FeedBackMsg.USER_EXIST;
			feedBack.msg = "用户存在";
			feedBack.student = student;
		}
		return feedBack;
	}

	/*******************************************************
	 * 判断用户是否存在
	 * 
	 * @param stuID
	 *            用户ID
	 * @return 存在true，否则false
	 *******************************************************/
	private Student isUserExisted(String stuID) {
		Student student = studentService.getStudent(stuID);
		if (student != null)
			return student;
		return null;
	}

	/*******************************************************
	 * 添加数据
	 * 
	 * @param request
	 *            HTTP请求对象
	 * @param response
	 *            HTTP响应对象
	 * @throws Exception
	 *             抛出的异常
	 *******************************************************/
	public void addStudent(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String strIcon = request.getParameter("icon");
		String strUsername = request.getParameter("username");
		String strGender = request.getParameter("gender");
		String strPassword = request.getParameter("pwd");
		String strSchool = request.getParameter("school");
		String strSign = "";
		FeedBackStudent sendToClientMsg = new FeedBackStudent();
		FeedBackStudent feedBack = isUserExisted(strUsername, strPassword);
		if (feedBack.code == FeedBackMsg.USER_NOT_EXIST) {
			Student student = new Student();
			student.setID(null);
			student.setGender(strGender);
			student.setIcon_url(strIcon);
			student.setName(strUsername);
			student.setPassword(strPassword);
			student.setSchool(strSchool);
			student.setSign(strSign);
			/* 调用服务添加数据 */
			studentService.saveStudent(student);
			/* 封装反馈数据 */
			sendToClientMsg.code = FeedBackMsg.INSERT_DONE;
			sendToClientMsg.msg = "添加成功";
			System.out.println(student.getID());
			sendToClientMsg.student = student;
		} else {
			sendToClientMsg = feedBack;
		}
		String responseMsg = sendToClientMsg.toJson();
		response.setCharacterEncoding("UTF-8");
		response.getOutputStream().write(responseMsg.getBytes("UTF-8"));
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}

	/********************************************************
	 * 客户端用户登录
	 * 
	 * @param request
	 *            HTTP请求对象
	 * @param response
	 *            HTTP响应对象
	 * @throws Exception
	 *             抛出的异常
	 ********************************************************/
	public void login(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String username = request.getParameter("username");
		String password = request.getParameter("pwd");
		FeedBackStudent feedBack = isUserExisted(username, password);
		String responseMsg = feedBack.toJson();
		response.setCharacterEncoding("UTF-8");
		response.getOutputStream().write(responseMsg.getBytes("UTF-8"));
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}

	/*********************************************************
	 * 修改用户信息
	 * 
	 * @param request
	 *            HTTP请求对象
	 * @param response
	 *            HTTP响应对象
	 * @throws Exception
	 *             抛出的异常
	 *********************************************************/
	public void modifyStudentInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int code = 0;
		String msg = "";
		String stuID = request.getParameter("stuID");
		String stuHead = request.getParameter("stuHead");
		String stuName = request.getParameter("stuName");
		String stuGender = request.getParameter("stuGender");
		String stuSchool = request.getParameter("stuSchool");
		String stuSign = request.getParameter("stuSign");
		Student student = isUserExisted(stuID);
		if (student != null) {
			student.setGender(stuGender);
			student.setName(stuName);
			student.setSchool(stuSchool);
			student.setSign(stuSign);
			student.setIcon_url(stuHead);
			studentService.saveStudent(student);
			code = 1;
			msg = "修改成功";
		} else {
			code = 0;
			msg = "修改失败";
		}
		String json = "{\"Code\":\"" + code + "\",\"Msg\":\"" + msg + "\"}";
		response.setCharacterEncoding("UTF-8");
		response.getOutputStream().write(json.getBytes("UTF-8"));
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}

	/*********************************************************
	 * 修改密码
	 * 
	 * @param request
	 *            HTTP请求对象
	 * @param response
	 *            HTTP响应对象
	 * @throws Exception
	 *             抛出的异常
	 *********************************************************/
	public void modifyPassword(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int code = 0;
		String msg = "";
		String stuID = request.getParameter("stuID");
		String oldPwd = request.getParameter("oldPwd");
		String newPwd = request.getParameter("newPwd");
		Student student = isUserExisted(stuID);
		if (oldPwd.equals(student.getPassword()) && student != null) {
			student.setPassword(newPwd);
			studentService.saveStudent(student);
			code = 1;
			msg = "修改成功";
		} else {
			code = 0;
			msg = "修改失败";
		}
		String json = "{\"Code\":\"" + code + "\",\"Msg\":\"" + msg + "\"}";
		response.setCharacterEncoding("UTF-8");
		response.getOutputStream().write(json.getBytes("UTF-8"));
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}
}
