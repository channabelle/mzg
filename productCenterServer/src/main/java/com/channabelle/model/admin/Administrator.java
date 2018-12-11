package com.channabelle.model.admin;

import com.channabelle.model.BaseBean;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "T_Administrator")
public class Administrator extends BaseBean {

    /**
     *
     */
    private static final long serialVersionUID = -16737176544765153L;

    @Id
    @GeneratedValue(generator = "system_uuid")
    @GenericGenerator(name = "system_uuid", strategy = "uuid")
    @Column(name = "p_uuid_administrator")
    private String p_uuid_administrator;// 商铺管理员编号

    @Column(name = "account")
    private String account;// 2）用户名

    @Column(name = "realname")
    private String realname;// 3）真实姓名

    @Column(name = "password")
    private String password;// 6）密码（MD5加密）

    @Column(name = "phone")
    private String phone;// 7）手机号码

    @Column(name = "email")
    private String email;// 8）邮箱

    @Column(name = "remark")
    private String remark;// 10）备注

    @Column(name = "status")
    private int status;// 11）账号状态（-1：锁定，1：可用）

    @Column(name = "cTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cTime;// 100）注册时间

    @Column(name = "uTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date uTime;// 101）更新时间

    @Transient
    private String token; // 登录凭证

    public String getP_uuid_administrator() {
        return p_uuid_administrator;
    }

    public void setP_uuid_administrator(String p_uuid_administrator) {
        this.p_uuid_administrator = p_uuid_administrator;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
