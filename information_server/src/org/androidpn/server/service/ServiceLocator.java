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
package org.androidpn.server.service;

import org.androidpn.server.xmpp.XmppServer;

/** 
 * This is a helper class to look up service objects.
 *
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class ServiceLocator {

    public static String USER_SERVICE = "userService";
    
    public static String NOTIFICATION_SERVICE = "notificationService" ;
    
    public static String INFORMATION_SERIVCE = "informationService" ;
    
    public static String INFORMATION_COMMENT_SERIVCE = "informationCommentService" ;
    
    public static String COLLEGE_SERVICE = "collegeService" ;
    
    public static String STUDENT_SERVICE = "studentService" ;
    
    public static String SCHOOL_SERVICE = "schoolService" ;
    
    public static String EDUCATION_SERVICE = "educationService" ;
    
    public static String Comment_SERVICE = "commentService" ;

    /**
     * Generic method to obtain a service object for a given name. 
     * 
     * @param name the service bean name
     * @return
     */
    public static Object getService(String name) {
        return XmppServer.getInstance().getBean(name);
    }

    /**
     * Obtains the user service.
     * 
     * @return the user service
     */
    public static UserService getUserService() {
        return (UserService) XmppServer.getInstance().getBean(USER_SERVICE);
    }
    
    /*
     * 获取notificationService实例
     */
    public static NotificationService getNotificationService()
    {
    	return (NotificationService) XmppServer.getInstance().getBean(NOTIFICATION_SERVICE) ;
    }
    
    /*
     * 获取informationService实例
     */
    public static InformationService getInformationService()
    {
    	return (InformationService) XmppServer.getInstance().getBean(INFORMATION_SERIVCE) ;
    }
    
    public static InformationCommentService getInformationCommentService(){
    	return (InformationCommentService) XmppServer.getInstance().getBean(INFORMATION_COMMENT_SERIVCE) ;
    }
    
    public static CollegeService getCollegeService(){
    	return (CollegeService) XmppServer.getInstance().getBean(COLLEGE_SERVICE) ;
    }
    
    public static StudentService getStudentService()
    {
    	return (StudentService) XmppServer.getInstance().getBean(STUDENT_SERVICE) ;
    }
    
    public static SchoolService getSchoolService(){
    	return (SchoolService) XmppServer.getInstance().getBean(SCHOOL_SERVICE) ;
    }
}
