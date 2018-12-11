package com.channabelle.model.user;

import com.channabelle.model.BaseBean;
import com.channabelle.model.product.ProInfo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "T_ShoppingCar")
public class ShoppingCar extends BaseBean implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -2851870343229486111L;

	/**
	 *
	 */

	@Id
	@GeneratedValue(generator = "system_uuid")
	@GenericGenerator(name = "system_uuid", strategy = "uuid")
	@Column(name = "p_uuid_shopping_car")
	private String p_uuid_shopping_car;

	@Column(name = "uuid_user")
	private String uuid_user;

	@Column(name = "uuid_pro_info")
	private String uuid_pro_info;

	// 数量
	@Column(name = "amount")
	private int amount;

	// 是否选择结算
	@Column(name = "checked")
	private String checked;

	// 添加或减少数量
	@Transient
	private int amountChangeNum;

	@Column(name = "cTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date cTime;// 创建时间

	@Column(name = "uTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date uTime;// 更新时间

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "uuid_pro_info", insertable = false, updatable = false)
	private ProInfo proInfo;

	@Transient
	private double amount_pro_price;

	@Transient
	private double amount_pro_price_with_discount;

	public String getP_uuid_shopping_car() {
		return p_uuid_shopping_car;
	}

	public void setP_uuid_shopping_car(String p_uuid_shopping_car) {
		this.p_uuid_shopping_car = p_uuid_shopping_car;
	}

	public String getUuid_user() {
		return uuid_user;
	}

	public void setUuid_user(String uuid_user) {
		this.uuid_user = uuid_user;
	}

	public String getUuid_pro_info() {
		return uuid_pro_info;
	}

	public void setUuid_pro_info(String uuid_pro_info) {
		this.uuid_pro_info = uuid_pro_info;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getAmountChangeNum() {
		return amountChangeNum;
	}

	public void setAmountChangeNum(int amountChangeNum) {
		this.amountChangeNum = amountChangeNum;
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

	public ProInfo getProInfo() {
		return proInfo;
	}

	public void setProInfo(ProInfo proInfo) {
		this.proInfo = proInfo;
	}

	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}

	public double getAmount_pro_price() {
		if (null != proInfo) {
			amount_pro_price = amount * proInfo.getPro_price();
			amount_pro_price = (new BigDecimal(amount_pro_price)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}

		return amount_pro_price;
	}

	public double getAmount_pro_price_with_discount() {
		if (null != proInfo) {
			amount_pro_price_with_discount = amount * proInfo.getPro_price_with_discount();
			amount_pro_price_with_discount = (new BigDecimal(amount_pro_price_with_discount))
					.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		return amount_pro_price_with_discount;
	}

}