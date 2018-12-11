package com.channabelle.model.product;

import com.channabelle.model.BaseBean;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 产品目录
 */
@Entity
@Table(name = "T_ProMenu")
public class ProMenu extends BaseBean implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 8670818403539536011L;

	// 产品目录编号
	@Id
	@GeneratedValue(generator = "system_uuid")
	@GenericGenerator(name = "system_uuid", strategy = "uuid")
	@Column(name = "p_uuid_pro_menu")
	private String p_uuid_pro_menu;

	// 所属商铺
	@Column(name = "uuid_shop")
	private String uuid_shop;

	// 目录名称
	@Column(name = "menu_name")
	private String menu_name;

	// 上级目录编号
	@Column(name = "menu_father_uuid")
	private String menu_father_uuid;

	// 目录状态
	@Column(name = "menu_status")
	private int menu_status;// 0：上架，1：下架

	// 目录排序
	@Column(name = "order_number")
	private int order_number;

	// 创建时间
	@Column(name = "cTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date cTime;

	// 更新时间
	@Column(name = "uTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date uTime;

	public String getP_uuid_pro_menu() {
		return p_uuid_pro_menu;
	}

	public void setP_uuid_pro_menu(String p_uuid_pro_menu) {
		this.p_uuid_pro_menu = p_uuid_pro_menu;
	}

	public String getUuid_shop() {
		return uuid_shop;
	}

	public void setUuid_shop(String uuid_shop) {
		this.uuid_shop = uuid_shop;
	}

	public String getMenu_name() {
		return menu_name;
	}

	public void setMenu_name(String menu_name) {
		this.menu_name = menu_name;
	}

	public String getMenu_father_uuid() {
		return menu_father_uuid;
	}

	public void setMenu_father_uuid(String menu_father_uuid) {
		this.menu_father_uuid = menu_father_uuid;
	}

	public int getMenu_status() {
		return menu_status;
	}

	public void setMenu_status(int menu_status) {
		this.menu_status = menu_status;
	}

	public int getOrder_number() {
		return order_number;
	}

	public void setOrder_number(int order_number) {
		this.order_number = order_number;
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