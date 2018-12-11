package com.channabelle.model.shop;

import com.channabelle.model.BaseBean;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "T_Shop")
public class Shop extends BaseBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -6412195862454321502L;

    @Id
    @GeneratedValue(generator = "system_uuid")
    @GenericGenerator(name = "system_uuid", strategy = "uuid")
    @Column(name = "p_uuid_shop")
    private String p_uuid_shop;// 商铺编号

    @Column(name = "shop_name_full")
    private String shop_name_full;// 商铺全称

    @Column(name = "shop_name_short")
    private String shop_name_short;// 商铺简称

    @Column(name = "shop_address")
    private String shop_address;// 商铺地址

    @Column(name = "shop_phone")
    private String shop_phone;// 商铺联系电话

    @Column(name = "shop_contact")
    private String shop_contact;// 商铺联系人

    @Column(name = "shop_contact_phone")
    private String shop_contact_phone;// 商铺联系人电话

    @Column(name = "shop_business_licence")
    private String shop_business_licence;// 商铺营业执照

    @Column(name = "cTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cTime;// 商铺创建时间

    @Column(name = "uTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date uTime;// 商铺更新时间

    @Column(name = "shop_status")
    private int shop_status;// 商铺状态

    public String getP_uuid_shop() {
        return p_uuid_shop;
    }

    public void setP_uuid_shop(String p_uuid_shop) {
        this.p_uuid_shop = p_uuid_shop;
    }

    public String getShop_name_full() {
        return shop_name_full;
    }

    public void setShop_name_full(String shop_name_full) {
        this.shop_name_full = shop_name_full;
    }

    public String getShop_name_short() {
        return shop_name_short;
    }

    public void setShop_name_short(String shop_name_short) {
        this.shop_name_short = shop_name_short;
    }

    public String getShop_address() {
        return shop_address;
    }

    public void setShop_address(String shop_address) {
        this.shop_address = shop_address;
    }

    public String getShop_phone() {
        return shop_phone;
    }

    public void setShop_phone(String shop_phone) {
        this.shop_phone = shop_phone;
    }

    public String getShop_contact() {
        return shop_contact;
    }

    public void setShop_contact(String shop_contact) {
        this.shop_contact = shop_contact;
    }

    public String getShop_contact_phone() {
        return shop_contact_phone;
    }

    public void setShop_contact_phone(String shop_contact_phone) {
        this.shop_contact_phone = shop_contact_phone;
    }

    public String getShop_business_licence() {
        return shop_business_licence;
    }

    public void setShop_business_licence(String shop_business_licence) {
        this.shop_business_licence = shop_business_licence;
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

    public int getShop_status() {
        return shop_status;
    }

    public void setShop_status(int shop_status) {
        this.shop_status = shop_status;
    }
}