package com.channabelle.model.product;

import com.channabelle.model.BaseBean;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 产品详情
 */
@Entity
@Table(name = "T_ProDetail")
public class ProDetail extends BaseBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8456439758492322355L;

    // 产品详情编号
    @Id
    @GeneratedValue(generator = "system_uuid")
    @GenericGenerator(name = "system_uuid", strategy = "uuid")
    @Column(name = "p_uuid_pro_detail")
    private String p_uuid_pro_detail;

    // 产品编号
    @Column(name = "uuid_pro_info")
    private String uuid_pro_info;

    // 产品详细介绍-全
    @Column(name = "pro_detail_full_h5")
    private String pro_detail_full_h5;

    // 产品详细介绍图片-列表
    @Column(name = "pro_detail_img_url_list")
    private String pro_detail_img_url_list;

    // 创建时间
    @Column(name = "cTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cTime;

    // 更新时间
    @Column(name = "uTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date uTime;

    public String getP_uuid_pro_detail() {
        return p_uuid_pro_detail;
    }

    public void setP_uuid_pro_detail(String p_uuid_pro_detail) {
        this.p_uuid_pro_detail = p_uuid_pro_detail;
    }

    public String getPro_detail_full_h5() {
        return pro_detail_full_h5;
    }

    public void setPro_detail_full_h5(String pro_detail_full_h5) {
        this.pro_detail_full_h5 = pro_detail_full_h5;
    }

    public String getPro_detail_img_url_list() {
        return pro_detail_img_url_list;
    }

    public void setPro_detail_img_url_list(String pro_detail_img_url_list) {
        this.pro_detail_img_url_list = pro_detail_img_url_list;
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