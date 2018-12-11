package com.channabelle.common;

import java.io.Serializable;

public class ServiceResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3618749614155554984L;

	public enum Status {
		Success, Error, Warning, Info
	};

	private Status status;
	private int errorCode;
	private String errorKey;
	private String errorValue;

	public final static int DEFAULT_ERROR_CODE = 0;

	public ServiceResult(Status status, String errorKey, String errorValue) {
		this.errorCode = DEFAULT_ERROR_CODE;
		this.status = status;
		this.errorKey = errorKey;
		this.errorValue = errorValue;
	}

	public ServiceResult(Status status, int errorCode, String errorKey, String errorValue) {
		this(status, errorKey, errorValue);
		this.errorCode = errorCode;
	}

	public ServiceResult() {
		this(Status.Success, null, null);
	}

	public ServiceResult systemError() {
		return new ServiceResult(Status.Error, "系统", "内部错误，请联系客服");
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getErrorKey() {
		return errorKey;
	}

	public void setErrorKey(String errorKey) {
		this.errorKey = errorKey;
	}

	public String getErrorValue() {
		return errorValue;
	}

	public void setErrorValue(String errorValue) {
		this.errorValue = errorValue;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

}
