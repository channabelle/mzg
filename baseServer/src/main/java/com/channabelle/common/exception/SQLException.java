package com.channabelle.common.exception;

import java.util.regex.Matcher;

import org.apache.log4j.Logger;

import com.channabelle.common.ServiceResult;
import com.channabelle.common.ServiceResult.Status;
import com.channabelle.common.utils.MyPattern;

public class SQLException {
	private static Logger log = Logger.getLogger(SQLException.class);

	public static ServiceResult handle(Exception error) {
		ServiceResult res = (new ServiceResult()).systemError();
		Throwable cause = getRootCause(error);
		String eMes = cause.getMessage();

		int m = 0;
		while (m++ < 1) {
			if (cause instanceof org.hibernate.StaleStateException) {
				// Batch update returned unexpected row count from update [0];
				// actual row count: 0; expected: 1
				if (eMes.contains("update") && eMes.contains("actual row count: 0; expected: 1")) {
					res = new ServiceResult(Status.Info, "数据更新", "操作对象不存在");
					break;
				}
			} else if (cause instanceof org.hibernate.TransientObjectException) {
				// The given object has a null identifier:
				// com.channabelle.model.User
				if (eMes.contains("given object has a null identifier")) {
					res = new ServiceResult(Status.Info, "数据更新", "未指定操作对象编号");
					break;
				}
			} else if (cause instanceof com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException) {
				Matcher match = null;
				// Duplicate entry 'XXX' for key 'name_UNIQUE'
				match = MyPattern.grep(eMes, "Duplicate entry '(\\S+)' for key '(\\S+)'");
				if (true == match.find()) {
					res = new ServiceResult(Status.Info, String.format("%s", match.group(1)), "已存在，不可重复");
					break;
				}
				// Column 'account' cannot be null
				match = MyPattern.grep(eMes, "Column '(\\S+)' cannot be null");
				if (true == match.find()) {
					res = new ServiceResult(Status.Info, String.format("%s", match.group(1)), "不能为空");
					break;
				}
			}
			break;
		}

		if (Status.Error == res.getStatus()) {
			log.error("SQLException", error);
		} else {
			log.warn("SQLException: " + cause);
		}
		return res;
	}

	private static Throwable getRootCause(Exception e) {
		Throwable cause = e;
		if (null != e && null != e.getCause()) {
			cause = e.getCause();
			while (true) {
				if (null == cause.getCause()) {
					return cause;
				} else {
					cause = cause.getCause();
				}
			}
		}
		return cause;
	}
}
