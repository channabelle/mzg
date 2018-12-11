package com.channabelle.model.order;

import java.io.Serializable;
import java.util.List;

import com.channabelle.model.BaseBean;
import com.channabelle.model.user.UserAddress;

/**
 * 用户订单创建
 */
public class UserOrderCreate extends BaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4230837596479754841L;

	private List<String> shoppingCarUuids;
	private UserAddress userAddress;
	private String uuid_shop;
	private String remark;

	public List<String> getShoppingCarUuids() {
		return shoppingCarUuids;
	}

	public void setShoppingCarUuids(List<String> shoppingCarUuids) {
		this.shoppingCarUuids = shoppingCarUuids;
	}

	public UserAddress getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(UserAddress userAddress) {
		this.userAddress = userAddress;
	}

	public String getUuid_shop() {
		return uuid_shop;
	}

	public void setUuid_shop(String uuid_shop) {
		this.uuid_shop = uuid_shop;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}