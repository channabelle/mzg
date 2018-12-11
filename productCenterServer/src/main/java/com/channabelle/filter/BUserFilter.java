package com.channabelle.filter;

import com.channabelle.common.ControllerResult;
import com.channabelle.common.ServiceResult;
import com.channabelle.common.ServiceResult.Status;
import com.channabelle.common.utils.JsonDateValueProcessor;
import com.channabelle.model.HttpRecord;
import com.channabelle.model.shop.ShopManager;
import com.channabelle.service.impl.shop.ShopManagerServiceImpl;
import com.channabelle.service.impl.user.UserServiceImpl;

import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BUserFilter implements Filter {
	private Logger log = Logger.getLogger(BUserFilter.class);

	public BUserFilter() {
		log.info("BUserFilter");
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

		ShopManager sm = ShopManagerServiceImpl.getSession_ShopManager(request);
		if (null == sm) {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

			ServletOutputStream out = response.getOutputStream();
			ServiceResult res = new ServiceResult(Status.Info, ServiceResult.ERROR_CODE_B_NOT_LOGIN, "B端用户", "未登录");
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
				r.setUser_id(sm.getP_uuid_shop_manager());
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
