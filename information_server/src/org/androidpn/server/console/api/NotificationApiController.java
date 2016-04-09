//
//  NotificationApiController.java
//  FeOA
//
//  Created by LuTH on 2012-4-1.
//  Copyright 2012 flyrise. All rights reserved.
//

package org.androidpn.server.console.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.androidpn.server.util.Config;
import org.androidpn.server.xmpp.push.NotificationManager;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class NotificationApiController extends MultiActionController {

	private NotificationManager notificationManager;

	public NotificationApiController() {
		notificationManager = new NotificationManager();
	}

	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		// mav.addObject("list", null);
		mav.setViewName("notification/form");
		return mav;
	}

	public void send(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String broadcast = ServletRequestUtils.getStringParameter(request,
				"broadcast", "Y");
		String username = ServletRequestUtils.getStringParameter(request,
				"username");
		String title = ServletRequestUtils.getStringParameter(request, "title");
		String message = ServletRequestUtils.getStringParameter(request,
				"message");
		String uri = ServletRequestUtils.getStringParameter(request, "uri");
		String date = ServletRequestUtils.getStringParameter(request, "date") ;
		String type = ServletRequestUtils.getStringParameter(request, "type") ;
		String hour = ServletRequestUtils.getStringParameter(request, "hour") ;
		String minute = ServletRequestUtils.getStringParameter(request, "minute") ;
		String apiKey = Config.getString("apiKey", "");
		logger.debug("apiKey=" + apiKey);

		if (broadcast.equalsIgnoreCase("Y")) {
			notificationManager.sendBroadcast(apiKey, hour, minute, date, type, title, message, uri);
		} else {
			notificationManager.sendNotifcationToUser(apiKey, hour, minute, date, type, username, title,
					message, uri, true);
		}
		
		response.getWriter().print("{\"result\":\"0\",\"description\":\"success\"}");
//		ModelAndView mav = new ModelAndView();
//		mav.setViewName("redirect:notification.do");
//		return mav;
	}

}
