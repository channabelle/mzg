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

import org.apache.log4j.Logger;

import com.channabelle.common.ControllerResult;
import com.channabelle.common.ServiceResult;
import com.channabelle.common.ServiceResult.Status;
import com.channabelle.common.utils.JsonDateValueProcessor;
import com.channabelle.model.HttpRecord;
import com.channabelle.model.user.User;
import com.channabelle.service.impl.user.UserServiceImpl;

import net.sf.json.JSONObject;

public class CUserFilter implements Filter {
	private Logger log = Logger.getLogger(CUserFilter.class);

	public CUserFilter() {
		log.info("CUserFilter");
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		log.info("destroy");
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
		// log.info("doFilter");

		// TODO Auto-generated method stub
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;

		User user = UserServiceImpl.getSession_User(request);
		if (null == user) {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

			ServletOutputStream out = response.getOutputStream();
			ServiceResult res = new ServiceResult(Status.Info, ServiceResult.ERROR_CODE_C_NOT_LOGIN, "C端用户", "未登录");
			ControllerResult result = ControllerResult.setServiceResult(res, null);

			String jsonResultStr = JSONObject.fromObject(result, JsonDateValueProcessor.getJsonConfig()).toString();
			log.info(String.format("doFilter method: %s, url: %s, reason: %s", request.getMethod(),
					request.getRequestURL().toString(), jsonResultStr));

			out.write(jsonResultStr.getBytes());
			out.flush();
			out.close();
		} else {
			HttpRecord r = UserServiceImpl.getHttpRecord(request);
			if (null != r) {
				r.setUser_id(user.getUuid_user());
			}

			arg2.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		log.info(arg0.getFilterName() + "init");
	}
}
