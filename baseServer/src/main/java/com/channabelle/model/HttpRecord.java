package com.channabelle.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "T_Http_Record")
public class HttpRecord extends BaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4496362568975828001L;

	@Id
	@GeneratedValue(generator = "system_uuid")
	@GenericGenerator(name = "system_uuid", strategy = "uuid")
	@Column(name = "p_uuid")
	private String uuid;

	@Column(name = "session_id")
	private String session_id;

	@Column(name = "ip")
	private String ip;

	@Column(name = "url")
	private String url;

	@Column(name = "method")
	private String method;

	@Column(name = "status")
	private int status;

	@Column(name = "error_serialNo")
	private String errorSerialNo;

	@Column(name = "cTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date cTime;

	@Column(name = "response_time")
	private double responseTime;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getSession_id() {
		return session_id;
	}

	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Date getcTime() {
		return cTime;
	}

	public void setcTime(Date cTime) {
		this.cTime = cTime;
	}

	public double getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(double responseTime) {
		this.responseTime = responseTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getErrorSerialNo() {
		return errorSerialNo;
	}

	public void setErrorSerialNo(String errorSerialNo) {
		this.errorSerialNo = errorSerialNo;
	}

}