package com.actec.bsms.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;

/**
 * 控制器支持类
 * 
 * @author zhangst
 * @version 2017-1-12
 */
public abstract class BaseController {

	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 成功对象
	 */
	protected static String successResult = "success";

	/**
	 * 失败对象
	 */
	protected static String failResult = "failure";

	@Context
	protected HttpServletResponse response;
	@Context
	protected HttpServletRequest request;
	@Context
	protected HttpSession session;

}
