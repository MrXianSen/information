/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package org.androidpn.server.console.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.androidpn.server.model.Information;
import org.androidpn.server.service.InformationService;
import org.androidpn.server.service.ServiceLocator;
import org.androidpn.server.util.Config;
import org.androidpn.server.xmpp.push.NotificationManager;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/** 
 * A controller class to process the notification related requests.  
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class NotificationController extends MultiActionController {

    private NotificationManager notificationManager;
    
    public InformationService informationService ;

    public NotificationController() {
        notificationManager = new NotificationManager();
        informationService = ServiceLocator.getInformationService() ;
    }

    public ModelAndView list(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();
        // mav.addObject("list", null);
        mav.setViewName("notification/form");	//返回的是一个逻辑名
        return mav;
    }

    public ModelAndView send(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String broadcast = ServletRequestUtils.getStringParameter(request,
                "broadcast", "0");
        String username = ServletRequestUtils.getStringParameter(request,
                "username");
        String title = ServletRequestUtils.getStringParameter(request, "title");
        String message = ServletRequestUtils.getStringParameter(request,
                "message");
        String alias = ServletRequestUtils.getStringParameter(request, "alias") ;
        String type = ServletRequestUtils.getStringParameter(request, "type") ;
        String date = ServletRequestUtils.getStringParameter(request, "date") ;
        String uri = ServletRequestUtils.getStringParameter(request, "uri");
        String hour = ServletRequestUtils.getStringParameter(request, "hour") ;
        String minute = ServletRequestUtils.getStringParameter(request, "minute") ;

        String apiKey = Config.getString("apiKey", "");
        logger.debug("apiKey=" + apiKey);

        if (broadcast.equals("0")) {
            notificationManager.sendBroadcast(apiKey, hour, minute, date, type, title, message, uri);
        } else if(broadcast.equals("1")){
            notificationManager.sendNotifcationToUser(apiKey, hour, minute, date, type, username, title,
                    message, uri, true);
        } else if(broadcast.equals("2")){
        	notificationManager.sendNotificationByAlias(apiKey, hour, minute, date, type, alias, title, 
        			message, uri, false);
        }

        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:notification.do");
        return mav;
    }
    
    public ModelAndView push(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	
    	String idStr = ServletRequestUtils.getStringParameter(request, "id") ;
    	Information information = informationService.findInformationById(idStr) ;
    	
    	ModelAndView mvd = new ModelAndView() ;
    	//将information对象传入到jsp页面中
    	mvd.addObject("information",information) ;
    	mvd.setViewName("notification/form");
    	return mvd ;
    }

}
