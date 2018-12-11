package com.channabelle.model.user;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.channabelle.model.BaseBean;
import com.fasterxml.jackson.annotation.JsonFormat;

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
	private String uuid_user;// 1）用户编号，主键

	@Column(name = "account")
	private String account;// 2）用户名

	@Column(name = "realname")
	private String realname;// 3）真实姓名

	@Column(name = "id_card")
	private String id_card;// 4）身份证号码

	@Column(name = "sex")
	private int sex;// 5）性别（0：未定，1：男，2：女）

	@Column(name = "password")
	private String password;// 6）密码（MD5加密）

	@Column(name = "phone")
	private String phone;// 7）手机号码

	@Column(name = "email")
	private String email;// 8）邮箱

	@Column(name = "u_tag_list")
	private String tag_list;// 9）标签列表

	@Column(name = "remark")
	private String remark;// 10）备注

	@Column(name = "status")
	private int status;// 11）账号状态（-1：锁定，1：可用）

	@Column(name = "miniProgram_appid")
	private String miniProgram_appid;// 13）微信小程序APPID

	@Column(name = "miniProgram_openid")
	private String miniProgram_openid;// 14）微信小程序OPENID

	@Column(name = "cTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date cTime;// 100）注册时间

	@Column(name = "uTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date uTime;// 101）更新时间

	@Transient
	private String token; // 登录凭证

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

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getId_card() {
		return id_card;
	}

	public void setId_card(String id_card) {
		this.id_card = id_card;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTag_list() {
		return tag_list;
	}

	public void setTag_list(String tag_list) {
		this.tag_list = tag_list;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMiniProgram_appid() {
		return miniProgram_appid;
	}

	public void setMiniProgram_appid(String miniProgram_appid) {
		this.miniProgram_appid = miniProgram_appid;
	}

	public String getMiniProgram_openid() {
		return miniProgram_openid;
	}

	public void setMiniProgram_openid(String miniProgram_openid) {
		this.miniProgram_openid = miniProgram_openid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}