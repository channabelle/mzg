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
@Table(name = "T_User")
public class User extends BaseBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1008490398253635529L;

	@Id
	@GeneratedValue(generator = "system_uuid")
	@GenericGenerator(name = "system_uuid", strategy = "uuid")
	@Column(name = "p_uuid_user")
	private String uuid_user;

	@Column(name = "account")
	private String account;

	@Column(name = "password")
	private String password;

	@Column(name = "cTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date cTime;

	@Column(name = "uTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date uTime;

	public User() {

	}

	public User(String uuid_user) {
		this.uuid_user = uuid_user;
	}

	public String getUuid_user() {
		return uuid_user;
	}

	public void setUuid_user(String uuid_user) {
		this.uuid_user = uuid_user;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getcTime() {
		return cTime;
	}

	public void setcTime(Date cTime) {
		this.cTime = cTime;
	}

	public Date getuTime() {
		return uTime;
	}

	public void setuTime(Date uTime) {
		this.uTime = uTime;
	}

}