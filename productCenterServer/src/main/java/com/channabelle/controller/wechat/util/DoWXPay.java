package com.channabelle.controller.wechat.util;

import com.channabelle.common.utils.HttpClientUtil;
import com.channabelle.controller.wechat.util.wePay.WXPay;
import com.channabelle.controller.wechat.util.wePay.WXPayConfigImpl;
import com.channabelle.controller.wechat.util.wePay.WXPayConstants;
import com.channabelle.controller.wechat.util.wePay.WXPayConstants.SignType;
import com.channabelle.controller.wechat.util.wePay.WXPayUtil;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class DoWXPay {
    Logger log = Logger.getLogger(DoWXPay.class);

    private WXPay wxpay;
    private WXPayConfigImpl config;
    private boolean useSandbox;

    public DoWXPay(final boolean useSandbox) throws Exception {
        config = WXPayConfigImpl.getInstance();
        wxpay = new WXPay(config, null, true, useSandbox);
        this.useSandbox = useSandbox;
    }

    /*
     * 获取沙箱密钥
     */
    public String getSandboxSign() throws Exception {
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("mch_id", config.getMchID());
        data.put("nonce_str", WXPayUtil.generateNonceStr());
        data.put("sign", this.sign(data));

        String resPonse = HttpClientUtil.executePostXML(WechatApiUtil.WEPAY_DOMAIN + "/sandboxnew/pay/getsignkey", WXPayUtil.mapToXml(data));
        log.info(resPonse);

        return resPonse;
    }

    public String sign(final Map<String, Object> data) throws Exception {
        return WXPayUtil.generateSignature(data, config.getKey(), this.wxpay.signType);
    }

    /**
     * 公众号支付 下单
     */
    public Map<String, Object> doUnifiedOrder(HashMap<String, Object> data) throws Exception {
        Map<String, Object> resultMap = null;
        data.put("body", "亲子运动会-报名费");
        data.put("out_trade_no", WXPayUtil.generateNonceStr());
        data.put("device_info", "WEB");
        data.put("fee_type", "CNY");
        data.put("spbill_create_ip", "116.62.191.200");

        String notifyUrl = (true == this.useSandbox) ? WechatApiUtil.wepay_NOTIFY_URL_SANDBOX : WechatApiUtil.wepay_NOTIFY_URL_ONLINE;
        data.put("notify_url", notifyUrl);
        data.put("trade_type", "JSAPI");

        String url = (true == this.useSandbox) ? WXPayConstants.SANDBOX_UNIFIEDORDER_URL_SUFFIX : WXPayConstants.UNIFIEDORDER_URL_SUFFIX;

        Map<String, Object> resData = this.fillOrderRequestData(data);

        String resPonse = HttpClientUtil.executePostXML(WechatApiUtil.WEPAY_DOMAIN + url, WXPayUtil.mapToXml(resData));

        if(null != resPonse) {
            resultMap = WXPayUtil.xmlToMap(resPonse);
        }

        return resultMap;
    }

    /**
     * 向 Map 中添加 appid、mch_id、nonce_str、sign_type、sign <br>
     * 该函数适用于商户适用于统一下单等接口，不适用于红包、代金券接口
     *
     * @param reqData
     *
     * @return
     *
     * @throws Exception
     */
    private Map<String, Object> fillOrderRequestData(Map<String, Object> reqData) throws Exception {
        reqData.put("appid", config.getAppID());
        reqData.put("mch_id", config.getMchID());
        reqData.put("nonce_str", WXPayUtil.generateUUID());
        if(SignType.MD5.equals(this.wxpay.signType)) {
            reqData.put("sign_type", WXPayConstants.MD5);
        } else if(SignType.HMACSHA256.equals(this.wxpay.signType)) {
            reqData.put("sign_type", WXPayConstants.HMACSHA256);
        }
        reqData.put("sign", WXPayUtil.generateSignature(reqData, config.getKey(), this.wxpay.signType));
        return reqData;
    }
}
