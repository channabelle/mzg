package com.channabelle.model.product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
 * 产品信息
 */
@Entity
@Table(name = "T_ProInfo")
public class ProInfo extends BaseBean implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3745670799812800024L;

	// 产品编号
	@Id
	@GeneratedValue(generator = "system_uuid")
	@GenericGenerator(name = "system_uuid", strategy = "uuid")
	@Column(name = "p_uuid_pro_info")
	private String p_uuid_pro_info;

	// 所属商铺（** T_ProMenu）
	@Column(name = "uuid_shop")
	private String uuid_shop;

	// 所属商铺产品编号（** T_ProMenu）
	@Column(name = "uuid_shop_pro")
	private String uuid_shop_pro;

	// 产品标题-全
	@Column(name = "pro_title_full")
	private String pro_title_full;

	// 产品标题-简
	@Column(name = "pro_title_short")
	private String pro_title_short;

	// 产品封面
	@Column(name = "pro_cover_img_url")
	private String pro_cover_img_url;

	// 标价
	@Column(name = "pro_price")
	private double pro_price;

	// 最终折扣价格
	@Transient
	private double pro_price_with_discount;

	// 商家推荐度
	@Column(name = "recommended_rank")
	private int recommended_rank;

	// 折扣
	@Column(name = "pro_discount")
	private double pro_discount;

	// 折扣有效期-起始
	@Column(name = "pro_discount_sTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date pro_discount_sTime;

	// 折扣有效期-终止
	@Column(name = "pro_discount_eTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date pro_discount_eTime;

	// 产品有效期-起始-是否无限
	@Column(name = "valid_sTime_unlimited")
	private int valid_sTime_unlimited;// 0：否，1：是

	// 产品有效期-起始
	@Column(name = "valid_sTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date valid_sTime;

	// 产品有效期-终止-是否无限
	@Column(name = "valid_eTime_unlimited")
	private int valid_eTime_unlimited;// 0：否，1：是

	// 产品有效期-终止
	@Column(name = "valid_eTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date valid_eTime;

	// 产品状态
	@Column(name = "pro_status")
	private int pro_status;// 0：可售，1：下架

	// 产品创建时间
	@Column(name = "cTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date cTime;

	// 产品更新时间
	@Column(name = "uTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date uTime;

	// 产品总量是否无限
	@Column(name = "pro_total_quantity_unlimited")
	private int pro_total_quantity_unlimited;// 0：否，1：是

	// 产品总量
	@Column(name = "pro_total_quantity")
	private int pro_total_quantity;

	// 产品余量
	@Column(name = "pro_left_quantity")
	private int pro_left_quantity;

	// 对应目录
	@Transient
	private List<ProMenu> pro_menus;

	// 产品详情
	@Transient
	private ProDetail pro_detail;

	public String getP_uuid_pro_info() {
		return p_uuid_pro_info;
	}

	public void setP_uuid_pro_info(String p_uuid_pro_info) {
		this.p_uuid_pro_info = p_uuid_pro_info;
	}

	public String getUuid_shop() {
		return uuid_shop;
	}

	public void setUuid_shop(String uuid_shop) {
		this.uuid_shop = uuid_shop;
	}

	public String getUuid_shop_pro() {
		return uuid_shop_pro;
	}

	public void setUuid_shop_pro(String uuid_shop_pro) {
		this.uuid_shop_pro = uuid_shop_pro;
	}

	public String getPro_title_full() {
		return pro_title_full;
	}

	public void setPro_title_full(String pro_title_full) {
		this.pro_title_full = pro_title_full;
	}

	public String getPro_title_short() {
		return pro_title_short;
	}

	public void setPro_title_short(String pro_title_short) {
		this.pro_title_short = pro_title_short;
	}

	public String getPro_cover_img_url() {
		return pro_cover_img_url;
	}

	public void setPro_cover_img_url(String pro_cover_img_url) {
		this.pro_cover_img_url = pro_cover_img_url;
	}

	public double getPro_price_with_discount() {
		double d = pro_price * pro_discount / 10;
		pro_price_with_discount = (new BigDecimal(d)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return pro_price_with_discount;
	}

	public double getPro_price() {
		return pro_price;
	}

	public void setPro_price(double pro_price) {
		this.pro_price = pro_price;
	}

	public int getRecommended_rank() {
		return recommended_rank;
	}

	public void setRecommended_rank(int recommended_rank) {
		this.recommended_rank = recommended_rank;
	}

	public double getPro_discount() {
		return pro_discount;
	}

	public void setPro_discount(double pro_discount) {
		this.pro_discount = pro_discount;
	}

	public Date getPro_discount_sTime() {
		return pro_discount_sTime;
	}

	public void setPro_discount_sTime(Date pro_discount_sTime) {
		this.pro_discount_sTime = pro_discount_sTime;
	}

	public Date getPro_discount_eTime() {
		return pro_discount_eTime;
	}

	public void setPro_discount_eTime(Date pro_discount_eTime) {
		this.pro_discount_eTime = pro_discount_eTime;
	}

	public Date getValid_sTime() {
		return valid_sTime;
	}

	public void setValid_sTime(Date valid_sTime) {
		this.valid_sTime = valid_sTime;
	}

	public Date getValid_eTime() {
		return valid_eTime;
	}

	public void setValid_eTime(Date valid_eTime) {
		this.valid_eTime = valid_eTime;
	}

	public int getPro_status() {
		return pro_status;
	}

	public void setPro_status(int pro_status) {
		this.pro_status = pro_status;
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

	public int getPro_total_quantity() {
		return pro_total_quantity;
	}

	public void setPro_total_quantity(int pro_total_quantity) {
		this.pro_total_quantity = pro_total_quantity;
	}

	public int getPro_left_quantity() {
		return pro_left_quantity;
	}

	public void setPro_left_quantity(int pro_left_quantity) {
		this.pro_left_quantity = pro_left_quantity;
	}

	public List<ProMenu> getPro_menus() {
		return pro_menus;
	}

	public void setPro_menus(List<ProMenu> pro_menus) {
		this.pro_menus = pro_menus;
	}

	public int getValid_sTime_unlimited() {
		return valid_sTime_unlimited;
	}

	public void setValid_sTime_unlimited(int valid_sTime_unlimited) {
		this.valid_sTime_unlimited = valid_sTime_unlimited;
	}

	public int getValid_eTime_unlimited() {
		return valid_eTime_unlimited;
	}

	public void setValid_eTime_unlimited(int valid_eTime_unlimited) {
		this.valid_eTime_unlimited = valid_eTime_unlimited;
	}

	public int getPro_total_quantity_unlimited() {
		return pro_total_quantity_unlimited;
	}

	public void setPro_total_quantity_unlimited(int pro_total_quantity_unlimited) {
		this.pro_total_quantity_unlimited = pro_total_quantity_unlimited;
	}

	public ProDetail getPro_detail() {
		return pro_detail;
	}

	public void setPro_detail(ProDetail pro_detail) {
		this.pro_detail = pro_detail;
	}

}