package com.channabelle.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "T_Wepay_Order")
public class WepayOrder extends BaseBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -9098670997409683340L;

    @Id
    @Column(name = "p_out_trade_no")
    private String out_trade_no;// 1）微信商户订单号，主键

    @Column(name = "appid")
    private String appid;// 2）公众账号ID

    @Column(name = "mch_id")
    private String mch_id;// 3）商户号

    @Column(name = "body")
    private String body;// 4）商品简单描述

    @Column(name = "attach")
    private String attach;// 5）商品附加描述，在查询API和支付通知中原样返回

    @Column(name = "total_fee")
    private int total_fee;// 6）商品单价（单位：分）

    // 7）交易状态：
    // SUCCESS—支付成功/
    // REFUND—转入退款/
    // NOTPAY—未支付/
    // CLOSED—已关闭/
    // REVOKED—已撤销（刷卡支付）/
    // USERPAYING--用户支付中/
    // PAYERROR--支付失败(其他原因，如银行返回失败)
    @Column(name = "trade_state")
    private String trade_state;

    @Column(name = "trade_state_desc")
    private String trade_state_desc;// 8）对当前查询订单状态的描述和下一步操作的指引

    @Column(name = "trade_type")
    private String trade_type;// 9）交易方式，JSAPI，NATIVE，APP等

    @Column(name = "openid")
    private String openid;// 10）交易用户标识

    @Column(name = "transaction_id")
    private String transaction_id;// 11）微信支付订单号，和用户对账

    @Column(name = "prepay_id")
    private String prepay_id;// 12）微信生成的预支付会话标识，用于后续接口调用中使用，该值有效期为2小时

    @Column(name = "cTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cTime;// 100）注册时间

    @Column(name = "uTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date uTime;// 101）更新时间

    public WepayOrder() {

    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public int getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(int total_fee) {
        this.total_fee = total_fee;
    }

    public String getTrade_state() {
        return trade_state;
    }

    public void setTrade_state(String trade_state) {
        this.trade_state = trade_state;
    }

    public String getTrade_state_desc() {
        return trade_state_desc;
    }

    public void setTrade_state_desc(String trade_state_desc) {
        this.trade_state_desc = trade_state_desc;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
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