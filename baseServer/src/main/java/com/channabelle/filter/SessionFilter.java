package com.channabelle.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.channabelle.common.ControllerResult;
import com.channabelle.common.ServiceResult;
import com.channabelle.common.ServiceResult.Status;
import com.channabelle.model.User;
import com.channabelle.service.impl.UserServiceImpl;

public class SessionFilter implements Filter {
	private Logger log = Logger.getLogger(SessionFilter.class);

	public SessionFilter() {
		log.info("SessionFilter");
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

		User user = UserServiceImpl.hasLogin(request);
		if (null == user) {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

			ServletOutputStream out = response.getOutputStream();
			ServiceResult res = new ServiceResult(Status.Info, 2101, "用户", "未登录");
			ControllerResult result = ControllerResult.setServiceResult(res, null);

			out.write(JSONObject.fromObject(result).toString().getBytes());
			out.flush();
			out.close();
		} else {
			arg2.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		log.info(arg0.getFilterName() + "init");
	}
}
