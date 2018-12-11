package com.channabelle.controller.wechat.util.wePay;

import com.channabelle.controller.wechat.util.WechatApiUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class WXPayConfigImpl extends WXPayConfig {

    private byte[] certData;
    private static WXPayConfigImpl INSTANCE;

    private WXPayConfigImpl() throws Exception {
        String certPath = WechatApiUtil.wepay_CERTPATH_ONLINE;
        File file = new File(certPath);
        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int) file.length()];
        certStream.read(this.certData);
        certStream.close();
    }

    public static WXPayConfigImpl getInstance() throws Exception {
        if(INSTANCE == null) {
            synchronized(WXPayConfigImpl.class) {
                if(INSTANCE == null) {
                    INSTANCE = new WXPayConfigImpl();
                }
            }
        }
        return INSTANCE;
    }

    public String getAppID() {
        // 微信支付测试也必须为线上appID
        return WechatApiUtil.appID_ONLINE;
    }

    public String getMchID() {
        return WechatApiUtil.wepay_MchID;
    }

    public String getKey() {
        return WechatApiUtil.wepay_KEY;
    }

    public InputStream getCertStream() {
        ByteArrayInputStream certBis;
        certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    public int getHttpConnectTimeoutMs() {
        return 2000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }

    IWXPayDomain getWXPayDomain() {
        return WXPayDomainSimpleImpl.instance();
    }

    public String getPrimaryDomain() {
        return "api.mch.weixin.qq.com";
    }

    public String getAlternateDomain() {
        return "api2.mch.weixin.qq.com";
    }

    public int getReportWorkerNum() {
        return 1;
    }

    public int getReportBatchSize() {
        return 2;
    }
}
