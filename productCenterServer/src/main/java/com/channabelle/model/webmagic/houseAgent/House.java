package com.channabelle.model.webmagic.houseAgent;

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

import com.channabelle.model.BaseBean;

@Entity
@Table(name = "T_House")
public class House extends BaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2492459576650043771L;

	@Id
	@GeneratedValue(generator = "system_uuid")
	@GenericGenerator(name = "system_uuid", strategy = "uuid")
	@Column(name = "p_uuid")
	private String uuid;

	@Column(name = "platform")
	private String platform;

	@Column(name = "url")
	private String url;

	@Column(name = "crumbs")
	private String crumbs;

	@Column(name = "houseTitle")
	private String houseTitle;

	@Column(name = "houseSubTitle")
	private String houseSubTitle;

	@Column(name = "price")
	private double price;

	@Column(name = "priceUnit")
	private String priceUnit;

	@Column(name = "houseRentType")
	private String houseRentType;

	@Column(name = "houseFloor")
	private String houseFloor;

	@Column(name = "houseArea")
	private double houseArea;

	@Column(name = "houseAreaUnit")
	private String houseAreaUnit;

	@Column(name = "houseOrientation")
	private String houseOrientation;

	@Column(name = "houseDecoration")
	private String houseDecoration;

	@Column(name = "housePaymentType")
	private String housePaymentType;

	@Column(name = "houseAddress")
	private String houseAddress;

	@Column(name = "houseNo")
	private String houseNo;

	@Column(name = "houseDecorationFinishTime")
	@Temporal(TemporalType.DATE)
	private Date houseDecorationFinishTime;

	@Column(name = "houseAgentName")
	private String houseAgentName;

	@Column(name = "houseAgentShop")
	private String houseAgentShop;

	@Column(name = "houseAgentPhone")
	private String houseAgentPhone;

	@Column(name = "addTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date addTime;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCrumbs() {
		return crumbs;
	}

	public void setCrumbs(String crumbs) {
		this.crumbs = crumbs;
	}

	public String getHouseTitle() {
		return houseTitle;
	}

	public void setHouseTitle(String houseTitle) {
		this.houseTitle = houseTitle;
	}

	public String getHouseSubTitle() {
		return houseSubTitle;
	}

	public void setHouseSubTitle(String houseSubTitle) {
		this.houseSubTitle = houseSubTitle;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getPriceUnit() {
		return priceUnit;
	}

	public void setPriceUnit(String priceUnit) {
		this.priceUnit = priceUnit;
	}

	public String getHouseRentType() {
		return houseRentType;
	}

	public void setHouseRentType(String houseRentType) {
		this.houseRentType = houseRentType;
	}

	public String getHouseFloor() {
		return houseFloor;
	}

	public void setHouseFloor(String houseFloor) {
		this.houseFloor = houseFloor;
	}

	public double getHouseArea() {
		return houseArea;
	}

	public void setHouseArea(double houseArea) {
		this.houseArea = houseArea;
	}

	public String getHouseAreaUnit() {
		return houseAreaUnit;
	}

	public void setHouseAreaUnit(String houseAreaUnit) {
		this.houseAreaUnit = houseAreaUnit;
	}

	public String getHouseOrientation() {
		return houseOrientation;
	}

	public void setHouseOrientation(String houseOrientation) {
		this.houseOrientation = houseOrientation;
	}

	public String getHouseDecoration() {
		return houseDecoration;
	}

	public void setHouseDecoration(String houseDecoration) {
		this.houseDecoration = houseDecoration;
	}

	public String getHousePaymentType() {
		return housePaymentType;
	}

	public void setHousePaymentType(String housePaymentType) {
		this.housePaymentType = housePaymentType;
	}

	public String getHouseAddress() {
		return houseAddress;
	}

	public void setHouseAddress(String houseAddress) {
		this.houseAddress = houseAddress;
	}

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public Date getHouseDecorationFinishTime() {
		return houseDecorationFinishTime;
	}

	public void setHouseDecorationFinishTime(Date houseDecorationFinishTime) {
		this.houseDecorationFinishTime = houseDecorationFinishTime;
	}

	public String getHouseAgentName() {
		return houseAgentName;
	}

	public void setHouseAgentName(String houseAgentName) {
		this.houseAgentName = houseAgentName;
	}

	public String getHouseAgentShop() {
		return houseAgentShop;
	}

	public void setHouseAgentShop(String houseAgentShop) {
		this.houseAgentShop = houseAgentShop;
	}

	public String getHouseAgentPhone() {
		return houseAgentPhone;
	}

	public void setHouseAgentPhone(String houseAgentPhone) {
		this.houseAgentPhone = houseAgentPhone;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

}
