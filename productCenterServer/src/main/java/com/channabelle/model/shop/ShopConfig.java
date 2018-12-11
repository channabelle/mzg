package com.channabelle.model.shop;

import com.channabelle.model.BaseBean;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "T_Shop_Config")
public class ShopConfig extends BaseBean implements Serializable {
    public static final String[] CONFIG_MINI_PROGRAM_APPID = {"MINI_PROGRAM_APPID", "微信小程序APPID"};
    public static final String[] CONFIG_TECENT_SecretId = {"TECENT_SecretId", "腾讯云SecretId"};
    public static final String[] CONFIG_TECENT_SecretKey = {"TECENT_SecretKey", "腾讯云SecretKey"};
    public static final String[] CONFIG_COS_REGION = {"COS_REGION", "存储桶区域"};
    public static final String[] CONFIG_COS_NAME = {"COS_NAME", "存储桶名称"};
    public static final String[] CONFIG_PRO_IMG_ROOT = {"CONFIG_PRO_IMG_ROOT", "存储桶-商品图片-根目录"};

    /**
     *
     */
    private static final long serialVersionUID = 1431680056857164937L;

    @Id
    @GeneratedValue(generator = "system_uuid")
    @GenericGenerator(name = "system_uuid", strategy = "uuid")
    @Column(name = "p_uuid_shop_config")
    private String p_uuid_shop_config;// 1）配置编号，主键

    @Column(name = "uuid_shop")
    private String uuid_shop;// 2）商铺编号

    @Column(name = "config_name")
    private String config_name;// 3）配置名称

    @Column(name = "config_value")
    private String config_value;// 4）配置值

    @Column(name = "config_remark")
    private String config_remark;// 5）配置备注

    @Column(name = "cTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cTime;// 100）注册时间

    @Column(name = "uTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date uTime;// 101）更新时间

    public String getP_uuid_shop_config() {
        return p_uuid_shop_config;
    }

    public void setP_uuid_shop_config(String p_uuid_shop_config) {
        this.p_uuid_shop_config = p_uuid_shop_config;
    }

    public String getUuid_shop() {
        return uuid_shop;
    }

    public void setUuid_shop(String uuid_shop) {
        this.uuid_shop = uuid_shop;
    }

    public String getConfig_name() {
        return config_name;
    }

    public void setConfig_name(String config_name) {
        this.config_name = config_name;
    }

    public String getConfig_value() {
        return config_value;
    }

    public void setConfig_value(String config_value) {
        this.config_value = config_value;
    }

    public String getConfig_remark() {
        return config_remark;
    }

    public void setConfig_remark(String config_remark) {
        this.config_remark = config_remark;
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