package com.channabelle.controller.wechat.util;

import com.channabelle.common.utils.HttpClientUtil;
import com.channabelle.common.utils.JsonDateValueProcessor;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.util.*;

public class WechatApiUtil {
    private static Logger log = Logger.getLogger(WechatApiUtil.class);

    public static final boolean USE_SANDBOX = false;
    public static final String WECHAT_DOMAIN = "https://api.weixin.qq.com";
    public static final String WEPAY_DOMAIN = "https://api.mch.weixin.qq.com";
    public static final String SERVER_DOMAIN = "http://www.szttxs.net/productCenterServer";

    /*
     * TEST START
     */
    private static final String Token_TEST = "test1q2w3e4r";
    private static final String appID_TEST = "wx6f58574f16d1fddb";
    private static final String appsecret_TEST = "84332cd9c1c259159b5ff31037f10eb6";
    public static String access_token_TEST = null;

    /*
     * ONLINE START
     */
    private static final String Token_ONLINE = "ttxs1q2w3e4r";
    public static final String appID_ONLINE = "wx9774d45c8ae1e817";
    private static final String appsecret_ONLINE = "c20cfed218b6d6bf7cceeef08e088cb6";
    public static String access_token_ONLINE = null;

    /*
     * WEPAY CONFIG START
     */
    public static final String wepay_CERTPATH_ONLINE = "/home/ttxs/project/ttxs_game/wxCert/apiclient_cert.p12";
    public static final String wepay_MchID = "1488216812";// IMPORTANT!!!
    public static final String wepay_KEY = (true == USE_SANDBOX) ? "e73f522799127dcfcd2a3278361e0d76" : "nE3GbzqmS69qYyZTkPAjYP3EmWNFUOJx";
    public static final String wepay_NOTIFY_URL_SANDBOX = SERVER_DOMAIN + "/wxpay/pay/notifySandbox";
    public static final String wepay_NOTIFY_URL_ONLINE = SERVER_DOMAIN + "/wxpay/pay/notifyOnline";

    static {
        try {
            if(null == access_token_TEST) {
                // refreshAccessToken(false);
            }
            if(null == access_token_ONLINE) {
                // refreshAccessToken(true);
            }
        } catch(Exception e) {
            log.error("WechatApiUtil Error: ", e);
        }
    }

    public static void refreshAccessToken(boolean isOnline) {
        if(false == isOnline) {
            access_token_TEST = WechatApiUtil.getAccessToken(false);
        } else {
            access_token_ONLINE = WechatApiUtil.getAccessToken(true);
        }
    }

    public static boolean checkToken(HttpServletRequest request, boolean isOnline) {
        // 验证接入
        final String token = (true == isOnline) ? Token_ONLINE : Token_TEST;
        String signature = request.getParameter("signature");
        String nonce = request.getParameter("nonce");
        String timestamp = request.getParameter("timestamp");
        List<String> list = new ArrayList<String>();
        if(StringUtils.isNotEmpty(nonce)) {
            list.add(nonce);
        }
        if(StringUtils.isNotEmpty(timestamp)) {
            list.add(timestamp);
        }
        if(StringUtils.isNotEmpty(token)) {
            list.add(token);
        }
        Collections.sort(list);
        String str = StringUtils.join(list, "");
        String key = DigestUtils.sha1Hex(str);
        return key.equals(signature);
    }

    public static String getAccessToken(boolean isOnline) {
        String accessToken = null;
        String appID = (true == isOnline) ? appID_ONLINE : appID_TEST;
        String appsecret = (true == isOnline) ? appsecret_ONLINE : appsecret_TEST;

        String url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", appID, appsecret);

        String res = HttpClientUtil.executeGet(url);
        if(null != res) {
            JSONObject resJson = JSONObject.fromObject(res, JsonDateValueProcessor.getJsonConfig());
            if(null != resJson && resJson.containsKey("access_token")) {
                accessToken = resJson.getString("access_token");
            }
        }
        return accessToken;
    }

    public static String getOpenIdFromCode(String code, boolean isOnline) {
        String openid = null;
        String appID = (true == isOnline) ? appID_ONLINE : appID_TEST;
        String appsecret = (true == isOnline) ? appsecret_ONLINE : appsecret_TEST;

        String url = String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code", appID, appsecret, code);

        String res = HttpClientUtil.executeGet(url);
        if(null != res) {
            JSONObject resJson = JSONObject.fromObject(res, JsonDateValueProcessor.getJsonConfig());
            if(null != resJson && resJson.containsKey("openid")) {
                openid = resJson.getString("openid");
            }
        }
        return openid;
    }

    public static void bindSessionAndOpenid(String fromUser, HttpSession session) {
        if(null != session) {
            session.setAttribute("openid", fromUser);
        }
    }

    public static String getOpenidFromSession(HttpSession session) {
        String openid = null;
        if(null != session && null != session.getAttribute("openid")) {
            openid = String.valueOf(session.getAttribute("openid"));
        }
        return openid;
    }

    public static String sendTextMsg(String touser, String content, boolean isOnline) {
        return sendTextMsg(touser, content, isOnline, true);
    }

    public static String sendTextMsg(String touser, String content, boolean isOnline, boolean retryIfTokenInvalid) {
        String token = (true == isOnline) ? access_token_ONLINE : access_token_TEST;
        String url = String.format("%s/cgi-bin/message/custom/send?access_token=%s", WECHAT_DOMAIN, token);

        JSONObject bodyJson = new JSONObject();
        bodyJson.put("touser", touser);
        bodyJson.put("msgtype", "text");
        JSONObject textJson = new JSONObject();
        textJson.put("content", content);
        bodyJson.put("text", textJson);

        String res = HttpClientUtil.executePost(url, bodyJson.toString());
        if(true == isTokenInvalid(res) && true == retryIfTokenInvalid) {
            refreshAccessToken(isOnline);
            res = sendTextMsg(touser, content, isOnline, false);
        }

        return res;
    }

    private static boolean isTokenInvalid(String res) {
        boolean isTokenInvalid = false;
        if(null != res) {
            JSONObject resJson = JSONObject.fromObject(res, JsonDateValueProcessor.getJsonConfig());
            if(null != resJson && resJson.containsKey("errcode")) {
                int errcode = resJson.getInt("errcode");
                if(40001 == errcode) {
                    isTokenInvalid = true;
                }
            }
        }
        return isTokenInvalid;
    }

    public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
        // 将解析结果存储在HashMap中
        Map<String, String> map = new LinkedHashMap<String, String>();
        // 从request中取得输入流
        InputStream inputStream = request.getInputStream();
        // 读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        // 得到xml根元素
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();

        // 遍历所有子节点
        for(Element e : elementList)
            map.put(e.getName(), e.getText());

        // 释放资源
        inputStream.close();
        inputStream = null;

        return map;
    }
}
