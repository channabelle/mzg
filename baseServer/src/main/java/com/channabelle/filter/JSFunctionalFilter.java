package com.channabelle.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/*
 * 场景：任何客户端多次请求间，客户端参数回传的需求。
 */
public class JSFunctionalFilter implements Filter {
	private Logger log = Logger.getLogger(JSFunctionalFilter.class);

	public JSFunctionalFilter() {
		log.info("JSFunctionalFilter");
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		log.info("destroy");
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException,
			ServletException {
		log.info("doFilter");

		// TODO Auto-generated method stub
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;

		response.addHeader("Additional-Data", request.getHeader("Additional-Data"));
		arg2.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		log.info(arg0.getFilterName() + "init");
	}
}
