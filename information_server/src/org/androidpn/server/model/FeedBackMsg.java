package org.androidpn.server.model;

public enum FeedBackMsg {
	/*------------注册-------------*/
	USER_EXIST,			//用户存在
	USER_NOT_EXIST,		//用户不存在
	INPUT_ERR,			//输入错误
	INSERT_DONE,		//添加成功
	INSERT_ERR,			//添加失败
	/*------------登录-------------*/
	LGONIN_DONE,		//登录失败
	LOGIN_ERR,			//登录成功
}
