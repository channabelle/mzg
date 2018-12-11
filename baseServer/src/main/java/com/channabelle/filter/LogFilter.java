package com.channabelle.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.channabelle.common.ControllerResult;
import com.channabelle.model.HttpRecord;
import com.channabelle.task.NormalTask;

public class LogFilter implements Filter {
	private Logger log = Logger.getLogger(LogFilter.class);

	public LogFilter() {
		log.info("LogFilter");
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

		HttpRecord r = new HttpRecord();
		r.setIp(this.getIP(request));
		r.setMethod(request.getMethod());
		r.setUrl(request.getRequestURL().toString());
		r.setSession_id(request.getSession().getId());
		r.setcTime(new Date());
		long sTime = System.currentTimeMillis();

		String errorSerialNo = null;
		try {
			arg2.doFilter(request, response);
		} catch (IOException e) {
			errorSerialNo = String.valueOf(sTime);
			log.error("errorSerialNo: " + errorSerialNo, e);
		} catch (ServletException e) {
			errorSerialNo = String.valueOf(sTime);
			log.error("errorSerialNo: " + errorSerialNo, e);
		} finally {
			if (null != errorSerialNo) {
				r.setErrorSerialNo(errorSerialNo);

				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/json");

				PrintWriter out = response.getWriter();
				JSONObject json = JSONObject.fromObject(ControllerResult.systemError(errorSerialNo));
				out.write(json.toString());
			}

			double resTime = System.currentTimeMillis() - sTime;
			r.setResponseTime(resTime / 1000);
			r.setStatus(response.getStatus());

			NormalTask.addQuery(NormalTask.HTTP_RECORD, r);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		log.info(arg0.getFilterName() + "init");
	}

	private String getIP(HttpServletRequest request) {
		if (null == request.getHeader("x-forwarded-for")) {
			return request.getRemoteAddr();
		} else {
			return request.getHeader("x-forwarded-for");
		}
	}
}
