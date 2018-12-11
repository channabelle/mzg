package com.channabelle.model.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.channabelle.model.BaseBean;
import com.channabelle.model.product.ProInfo;

/**
 * 子订单
 */
@Entity
@Table(name = "T_UserSubOrder")
public class UserSubOrder extends BaseBean implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -855407382566221208L;

	// 子订单编号
	@Id
	@GeneratedValue(generator = "system_uuid")
	@GenericGenerator(name = "system_uuid", strategy = "uuid")
	@Column(name = "p_uuid_sub_order")
	private String p_uuid_sub_order;

	// 下单用户编号
	@Column(name = "uuid_user")
	private String uuid_user;

	// 订单编号（父订单）
	@Column(name = "uuid_user_order")
	private String uuid_user_order;

	// 订单产品编号
	@Column(name = "uuid_pro_info")
	private String uuid_pro_info;

	// 所属商铺
	@Column(name = "uuid_shop")
	private String uuid_shop;

	// 产品
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "uuid_pro_info", insertable = false, updatable = false)
	private ProInfo proInfo;

	// 订单产品名称（形成订单后的名称，产品再次被编辑后，不改变订单中的名称）
	@Column(name = "user_order_pro_name")
	private String user_order_pro_name;

	// 订单产品价格
	@Column(name = "user_order_price")
	private double user_order_price;

	// 订单产品折扣
	@Column(name = "user_order_discount")
	private double user_order_discount;

	// 最终折扣价格
	@Transient
	private double user_order_price_with_discount;

	// 订单订购数量
	@Column(name = "user_order_num")
	private int user_order_num;

	// 创建时间
	@Column(name = "cTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date cTime;

	// 更新时间
	@Column(name = "uTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date uTime;

	public String getP_uuid_sub_order() {
		return p_uuid_sub_order;
	}

	public void setP_uuid_sub_order(String p_uuid_sub_order) {
		this.p_uuid_sub_order = p_uuid_sub_order;
	}

	public String getUuid_user() {
		return uuid_user;
	}

	public void setUuid_user(String uuid_user) {
		this.uuid_user = uuid_user;
	}

	public String getUuid_user_order() {
		return uuid_user_order;
	}

	public void setUuid_user_order(String uuid_user_order) {
		this.uuid_user_order = uuid_user_order;
	}

	public String getUuid_shop() {
		return uuid_shop;
	}

	public void setUuid_shop(String uuid_shop) {
		this.uuid_shop = uuid_shop;
	}

	public String getUser_order_pro_name() {
		return user_order_pro_name;
	}

	public void setUser_order_pro_name(String user_order_pro_name) {
		this.user_order_pro_name = user_order_pro_name;
	}

	public double getUser_order_price() {
		return user_order_price;
	}

	public void setUser_order_price(double user_order_price) {
		this.user_order_price = user_order_price;
	}

	public double getUser_order_discount() {
		return user_order_discount;
	}

	public void setUser_order_discount(double user_order_discount) {
		this.user_order_discount = user_order_discount;
	}

	public int getUser_order_num() {
		return user_order_num;
	}

	public void setUser_order_num(int user_order_num) {
		this.user_order_num = user_order_num;
	}

	public java.util.Date getcTime() {
		return cTime;
	}

	public void setcTime(java.util.Date cTime) {
		this.cTime = cTime;
	}

	public java.util.Date getuTime() {
		return uTime;
	}

	public void setuTime(java.util.Date uTime) {
		this.uTime = uTime;
	}

	public String getUuid_pro_info() {
		return uuid_pro_info;
	}

	public void setUuid_pro_info(String uuid_pro_info) {
		this.uuid_pro_info = uuid_pro_info;
	}

	public double getUser_order_price_with_discount() {
		user_order_price_with_discount = user_order_price * user_order_discount / 10;
		user_order_price_with_discount = (new BigDecimal(user_order_price_with_discount))
				.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return user_order_price_with_discount;
	}

	public ProInfo getProInfo() {
		return proInfo;
	}

	public void setProInfo(ProInfo proInfo) {
		this.proInfo = proInfo;
	}

}