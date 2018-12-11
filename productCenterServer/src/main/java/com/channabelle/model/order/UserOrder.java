package com.channabelle.model.order;

import java.io.Serializable;
import java.text.SimpleDateFormat;
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

/**
 * 用户订单
 */
@Entity
@Table(name = "T_UserOrder")
public class UserOrder extends BaseBean implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -855407382566221207L;

	// 订单编号
	@Id
	@GeneratedValue(generator = "system_uuid")
	@GenericGenerator(name = "system_uuid", strategy = "uuid")
	@Column(name = "p_uuid_user_order")
	private String p_uuid_user_order;

	// 下单用户编号
	@Column(name = "uuid_user")
	private String uuid_user;

	// 所属商铺
	@Column(name = "uuid_shop")
	private String uuid_shop;

	// 订单总额
	@Column(name = "order_amount")
	private double order_amount;

	// 订单总件数
	@Column(name = "order_total_num")
	private double order_total_num;

	// 订单名称
	@Column(name = "order_name")
	private String order_name;

	// 4）订单支付状态（1：待支付，2：支付中，3：已支付，4：已退款, 5：已取消）
	@Column(name = "order_pay_status")
	private int order_pay_status;

	// 4.1）订单支付状态文字描述（1：待支付，2：支付中，3：已支付，4：已退款, 5：已取消）
	@Transient
	private String order_pay_status_str;

	// 5）支付方式（1：在线支付，2：货到付款）
	@Column(name = "order_pay_mode")
	private int order_pay_mode;

	// 5.1）支付方式文字（1：在线支付，2：货到付款）
	@Transient
	private String order_pay_mode_str;

	// 6）支付渠道（1：现金，2：POS，3：微信，4：支付宝）
	@Column(name = "order_pay_channel")
	private int order_pay_channel;

	// 6.1）支付渠道文字（1：现金，2：POS，3：微信，4：支付宝）
	@Transient
	private String order_pay_channel_str;

	// 7）订单备注
	@Column(name = "user_order_remark")
	private String user_order_remark;

	@Column(name = "address_full")
	private String address_full;// 8）地址全写

	@Column(name = "contact_name")
	private String contact_name;// 9）联系人

	@Column(name = "contact_phone")
	private String contact_phone;// 10）联系人电话

	// 创建时间
	@Column(name = "cTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date cTime;

	@Transient
	private String cTime_date_str;
	@Transient
	private String cTime_time_str;

	// 更新时间
	@Column(name = "uTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date uTime;

	@Transient
	private String uTime_date_str;
	@Transient
	private String uTime_time_str;

	// 支付时间
	@Column(name = "payTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date payTime;

	@Transient
	private String payTime_date_str;
	@Transient
	private String payTime_time_str;

	public String getcTime_date_str() {
		cTime_date_str = (new SimpleDateFormat("yyyy-MM-dd")).format(cTime);
		return cTime_date_str;
	}

	public String getcTime_time_str() {
		cTime_time_str = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(cTime);
		return cTime_time_str;
	}

	public String getuTime_date_str() {
		uTime_date_str = (new SimpleDateFormat("yyyy-MM-dd")).format(uTime);
		return uTime_date_str;
	}

	public String getuTime_time_str() {
		uTime_time_str = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(uTime);
		return uTime_time_str;
	}

	public String getpayTime_date_str() {
		if (null != payTime) {
			payTime_date_str = (new SimpleDateFormat("yyyy-MM-dd")).format(payTime);
		}
		return payTime_date_str;
	}

	public String getpayTime_time_str() {
		if (null != payTime) {
			payTime_time_str = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(payTime);
		}
		return payTime_time_str;
	}

	public String getP_uuid_user_order() {
		return p_uuid_user_order;
	}

	public void setP_uuid_user_order(String p_uuid_user_order) {
		this.p_uuid_user_order = p_uuid_user_order;
	}

	public String getUuid_user() {
		return uuid_user;
	}

	public String getUuid_shop() {
		return uuid_shop;
	}

	public void setUuid_shop(String uuid_shop) {
		this.uuid_shop = uuid_shop;
	}

	public void setUuid_user(String uuid_user) {
		this.uuid_user = uuid_user;
	}

	public Date getcTime() {
		return cTime;
	}

	public void setcTime(java.util.Date cTime) {
		this.cTime = cTime;
	}

	public Date getuTime() {
		return uTime;
	}

	public void setuTime(java.util.Date uTime) {
		this.uTime = uTime;
	}

	public double getOrder_amount() {
		return order_amount;
	}

	public void setOrder_amount(double order_amount) {
		this.order_amount = order_amount;
	}

	public int getOrder_pay_status() {
		return order_pay_status;
	}

	public void setOrder_pay_status(int order_pay_status) {
		this.order_pay_status = order_pay_status;
	}

	public int getOrder_pay_mode() {
		return order_pay_mode;
	}

	public void setOrder_pay_mode(int order_pay_mode) {
		this.order_pay_mode = order_pay_mode;
	}

	public int getOrder_pay_channel() {
		return order_pay_channel;
	}

	public void setOrder_pay_channel(int order_pay_channel) {
		this.order_pay_channel = order_pay_channel;
	}

	public String getUser_order_remark() {
		return user_order_remark;
	}

	public void setUser_order_remark(String user_order_remark) {
		this.user_order_remark = user_order_remark;
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

	public double getOrder_total_num() {
		return order_total_num;
	}

	public void setOrder_total_num(double order_total_num) {
		this.order_total_num = order_total_num;
	}

	public String getOrder_name() {
		return order_name;
	}

	public void setOrder_name(String order_name) {
		this.order_name = order_name;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	// 4.1）订单支付状态文字描述（1：待支付，2：支付中，3：已支付，4：已退款, 5：已取消）
	public String getOrder_pay_status_str() {
		switch (order_pay_status) {
		case 0:
			order_pay_status_str = "初始";
			break;
		case 1:
			order_pay_status_str = "待支付";
			break;
		case 2:
			order_pay_status_str = "支付中";
			break;
		case 3:
			order_pay_status_str = "已支付";
			break;
		case 4:
			order_pay_status_str = "已退款";
			break;
		case 5:
			order_pay_status_str = "已取消";
			break;
		}

		return order_pay_status_str;
	}

	// 5.2）支付方式（1：在线支付，2：货到付款）
	public String getOrder_pay_mode_str() {
		switch (order_pay_mode) {
		case 1:
			order_pay_mode_str = "在线支付";
			break;
		case 2:
			order_pay_mode_str = "货到付款";
			break;
		default:
			order_pay_mode_str = "其他";
			break;
		}

		return order_pay_mode_str;
	}

	// 6.2）支付渠道（1：现金，2：POS，3：微信，4：支付宝）
	public String getOrder_pay_channel_str() {
		switch (order_pay_channel) {
		case 1:
			order_pay_channel_str = "现金";
			break;
		case 2:
			order_pay_channel_str = "POS";
			break;
		case 3:
			order_pay_channel_str = "微信";
			break;
		case 4:
			order_pay_channel_str = "支付宝";
			break;
		default:
			order_pay_channel_str = "其他";
			break;
		}

		return order_pay_channel_str;
	}

}