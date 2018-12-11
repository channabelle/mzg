package com.channabelle.model.user;

import com.channabelle.model.BaseBean;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "T_UserAddress")
public class UserAddress extends BaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6854100430016728114L;

	@Id
	@GeneratedValue(generator = "system_uuid")
	@GenericGenerator(name = "system_uuid", strategy = "uuid")
	@Column(name = "p_uuid_user_address")
	private String p_uuid_user_address;// 1）地址编号，主键

	@Column(name = "uuid_user")
	private String uuid_user;// 2）所属用户

	@Column(name = "address_type")
	private String address_type;// 3）地址类型

	@Column(name = "address_full")
	private String address_full;// 4）地址全写

	@Column(name = "contact_name")
	private String contact_name;// 5）联系人

	@Column(name = "contact_phone")
	private String contact_phone;// 6）联系人电话

	@Column(name = "is_default")
	private int is_default;// 7）是否默认地址（0：否，1：是）

	@Column(name = "cTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date cTime;// 100）创建时间

	@Column(name = "uTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date uTime;// 101）更新时间

	public String getP_uuid_user_address() {
		return p_uuid_user_address;
	}

	public void setP_uuid_user_address(String p_uuid_user_address) {
		this.p_uuid_user_address = p_uuid_user_address;
	}

	public String getUuid_user() {
		return uuid_user;
	}

	public void setUuid_user(String uuid_user) {
		this.uuid_user = uuid_user;
	}

	public String getAddress_type() {
		return address_type;
	}

	public void setAddress_type(String address_type) {
		this.address_type = address_type;
	}

	public String getAddress_full() {
		return address_full;
	}

	public void setAddress_full(String address_full) {
		this.address_full = address_full;
	}

	public String getContact_name() {
		return contact_name;
	}

	public void setContact_name(String contact_name) {
		this.contact_name = contact_name;
	}

	public String getContact_phone() {
		return contact_phone;
	}

	public void setContact_phone(String contact_phone) {
		this.contact_phone = contact_phone;
	}

	public int getIs_default() {
		return is_default;
	}

	public void setIs_default(int is_default) {
		this.is_default = is_default;
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