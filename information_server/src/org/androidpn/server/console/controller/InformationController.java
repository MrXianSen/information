package org.androidpn.server.console.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




import org.androidpn.server.model.Information;
import org.androidpn.server.service.InformationService;
import org.androidpn.server.service.ServiceLocator;
import org.json.JSONArray;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.albery.bean.PageBean;
import com.albery.data.DataToJson;

public class InformationController extends MultiActionController {

	//每页显示10条数据
	public static int PAGE_SIZE = 10 ;
	
	private InformationService informationService;


	public InformationController() {
		this.informationService = ServiceLocator.getInformationService();
	}
	/**
	 * 列出数据库中所有的数据
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		//创建一个PageBean对象
		PageBean pageBean = new PageBean() ;
		
		pageBean.setPc(Integer.parseInt(getPc(request)));
		pageBean.setTr(informationService.getTotalRecord());
		pageBean.setPs(PAGE_SIZE);
		
		
		// 获取数据库中的数据
		int start = (pageBean.getPc() - 1) * PAGE_SIZE ;
		List<Information> informations = informationService.
				getInformations(start, PAGE_SIZE);

		ModelAndView mvd = new ModelAndView();
		// 想页面中添加一个list集合
		mvd.addObject("informationList", informations);
		mvd.addObject("pageBean",pageBean) ;
		// 重定向页面
		mvd.setViewName("information/list");

		return mvd;
	}
	
	public String getPc(HttpServletRequest request)
	{
		//获取request域中的值
		String currPage = request.getParameter("currPage") ;
		if(currPage != null && !currPage.trim().isEmpty())
		{
			return currPage ;
		}
		return "1" ;
	}

	/**
	 * 添加信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView add(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		/*
		 * //获取标题 String title = ServletRequestUtils.getStringParameter(request,
		 * "title") ; //获取日期 String date =
		 * ServletRequestUtils.getStringParameter(request, "date") ; //获取信息类型
		 * String type = ServletRequestUtils.getStringParameter(request, "type")
		 * ; //获取信息内容 String content =
		 * ServletRequestUtils.getStringParameter(request, "content") ;
		 * //将获取的信息封装到Information中 information.setDate(date);
		 * information.setTitle(title); information.setType(type);
		 * information.setContent(content); //将分装好的information对象保存到数据库中
		 * informationService.saveInformation(information);
		 */
		// 定向页面
		ModelAndView mvd = new ModelAndView();
		mvd.setViewName("information/add");
		return mvd;
	}

	/**
	 * 填写完添加的信息之后
	 * 点击提交按钮触发的事件
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView addInformation(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		//获取ID
		String strID = ServletRequestUtils.getStringParameter(request, "id") ;
		
		// 获取标题
		String title = ServletRequestUtils.getStringParameter(request, "title");
		System.out.println("标题" + title);
		// 获取日期
		String date = ServletRequestUtils.getStringParameter(request, "date");
		System.out.println("日期" + date);
		// 获取信息类型
		String type = ServletRequestUtils.getStringParameter(request, "type");
		System.out.println("类型" + type);
		// 获取信息内容
		String content = ServletRequestUtils.getStringParameter(request,
				"content");
		System.out.println("内容" + content);
		// 获取时间
		String hour = ServletRequestUtils.getStringParameter(request, "hour");
		String minute = ServletRequestUtils.getStringParameter(request,
				"minute");
		Information information = new Information();
		if(strID.equals("-1"))
		{
			information.setID(null);
		}else{
			information.setID(strID);
		}
		information.setTitle(title);
		information.setDate(date);
		information.setType(type);
		information.setContent(content);
		information.setShows(content);
		information.setHour(hour);
		information.setMinute(minute);
		// 将分装好的information对象保存到数据库中
		informationService.saveInformation(information);

		// 定向页面
		ModelAndView mvd = new ModelAndView();
		mvd.setViewName("redirect:add.do");
		return mvd;
	}
	/**
	 * 点击删除连接的时候触发的动作
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView delete(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String idStr = ServletRequestUtils.getStringParameter(request, "id");
		Information information = informationService.findInformationById(idStr);
		informationService.deleteInformation(information);
		ModelAndView mvd = new ModelAndView();
		mvd.setViewName("redirect:information.do");
		return mvd;
	}

	/**
	 * 点击编辑链接的时候触发的动作
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView edit(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String idStr = ServletRequestUtils.getStringParameter(request, "id");
		Information information = informationService.findInformationById(idStr);
		ModelAndView mvd = new ModelAndView();
		mvd.addObject("information", information);
		mvd.setViewName("information/add");
		return mvd;
	}
	/**
	 * 点击推送链接的时候触发的动作
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView push(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String idStr = ServletRequestUtils.getStringParameter(request, "id");
		Information information = informationService.findInformationById(idStr);
		ModelAndView mvd = new ModelAndView();
		mvd.addObject("information", information);
		mvd.setViewName("information/list");
		return mvd;
	}
	/**
	 * 将数据库中的信息传进response中
	 * 通过此方法将信息传递到客户端
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void sendInformationsToClient(HttpServletRequest request,
			HttpServletResponse response)throws Exception{
		//获取当前页数
		int courrentPage = Integer.parseInt(request.getParameter("page")) ;
		//获取数据集合
		List<Information> informationList = informationService.
				getInformations((courrentPage-1) * PAGE_SIZE, PAGE_SIZE) ;
		//获取数据库中总数据数量
		for(Information info:informationList)
		{
			System.out.println("标题："+info.getTitle());
		}
		//将数据集合转换成json数据
		JSONArray jsonArrayData = DataToJson.toJson(informationList) ;
		byte[] jsonBytes = jsonArrayData.toString().getBytes("UTF-8") ;
		//写进response域中
		response.setCharacterEncoding("UTF-8");
		response.getOutputStream().write(jsonBytes);
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}
	
	/**
	 * 将数据库中的数据总数传进response中
	 * 通过此方法将信息传递到客户端
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void sendTotalRecordToClient(HttpServletRequest request,
			HttpServletResponse response)throws Exception{
		//获取数据库中总数据数量
		int totalRecord = informationService.getTotalRecord() ;
		System.out.println("总记录数："+totalRecord);
		StringBuilder tr = new StringBuilder() ;
		tr.append(totalRecord) ;
		byte[] trByte = tr.toString().getBytes("utf-8") ;
		response.setCharacterEncoding("UTF-8");
		response.getOutputStream().write(trByte);
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}
}
