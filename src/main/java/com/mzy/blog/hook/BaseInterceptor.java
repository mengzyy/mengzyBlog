/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.mzy.blog.hook;


import com.mzy.blog.hook.interceptor.InterceptorHookManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * 基础拦截器 - 向 request 中添加一些基础变量
 * 
 * @author langhsu
 * 
 */
@Component
public class BaseInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private InterceptorHookManager interceptorHookManager;

	//这个应该是web请求前的预处理
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		interceptorHookManager.preHandle(request, response, handler);
		return true;
	}
	//postHandle会在请求完成后，在视图被渲染前被调用
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

		request.setAttribute("base", request.getContextPath());
		interceptorHookManager.postHandle(request,response,handler,modelAndView);
	}

	//afterCompletion在视图被渲染后被调用
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		super.afterCompletion(request, response, handler, ex);
		interceptorHookManager.afterCompletion(request, response, handler, ex);
	}

	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		super.afterConcurrentHandlingStarted(request, response, handler);
		interceptorHookManager.afterConcurrentHandlingStarted(request, response, handler);
	}

}
