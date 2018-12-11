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
@Table(name = "T_Menu_ProInfo")
public class Menu_ProInfo extends BaseBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8358522845318101855L;

    // 编号
    @Id
    @GeneratedValue(generator = "system_uuid")
    @GenericGenerator(name = "system_uuid", strategy = "uuid")
    @Column(name = "p_uuid_menu_proInfo")
    private String p_uuid_menu_proInfo;

    // 目录编号
    @Column(name = "uuid_pro_menu")
    private String uuid_pro_menu;

    // 产品编号
    @Column(name = "uuid_pro_info")
    private String uuid_pro_info;

    // 创建时间
    @Column(name = "cTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cTime;

    // 更新时间
    @Column(name = "uTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date uTime;

    public String getP_uuid_menu_proInfo() {
        return p_uuid_menu_proInfo;
    }

    public void setP_uuid_menu_proInfo(String p_uuid_menu_proInfo) {
        this.p_uuid_menu_proInfo = p_uuid_menu_proInfo;
    }

    public String getUuid_pro_menu() {
        return uuid_pro_menu;
    }

    public void setUuid_pro_menu(String uuid_pro_menu) {
        this.uuid_pro_menu = uuid_pro_menu;
    }

    public String getUuid_pro_info() {
        return uuid_pro_info;
    }

    public void setUuid_pro_info(String uuid_pro_info) {
        this.uuid_pro_info = uuid_pro_info;
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