package com.channabelle.controller.wechat;

import com.channabelle.common.ControllerResult;
import com.channabelle.controller.BaseController;
import com.channabelle.controller.wechat.util.WechatApiUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Controller
@RequestMapping("/wechat")
public class wxController extends BaseController {
    Logger log = Logger.getLogger(wxController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @RequestMapping(value = "/wxTest")
    public @ResponseBody
    Object wxTest() {
        log.info("===== wxTest =====");

        processWxMessage(false);
        return null;
    }

    @RequestMapping(value = "/wxOnline")
    public @ResponseBody
    Object wxOnline() {
        log.info("===== wxOnline =====");
        processWxMessage(true);
        return null;
    }

    @RequestMapping(value = "/openidText", method = RequestMethod.GET)
    public @ResponseBody
    Object openidText(@RequestParam(value = "code", required = true) String code) throws Exception {
        log.info("===== openidText =====");
        log.info("<=== code: " + code);

        String openid = WechatApiUtil.getOpenidFromSession(request.getSession(false));
        if(null == openid) {
            openid = WechatApiUtil.getOpenIdFromCode(code, true);
            if(null != openid) {
                WechatApiUtil.bindSessionAndOpenid(openid, request.getSession(true));
            }
        }

        log.info("===> openid: " + code);
        return ControllerResult.success(openid);
    }

    @RequestMapping(value = "/openidOnline", method = RequestMethod.GET)
    public @ResponseBody
    Object openidOnline(@RequestParam(value = "code", required = true) String code) throws Exception {
        log.info("===== openidOnline =====");
        log.info("<=== code: " + code);

        String openid = WechatApiUtil.getOpenidFromSession(request.getSession(false));
        if(null == openid) {
            openid = WechatApiUtil.getOpenIdFromCode(code, true);
            if(null != openid) {
                WechatApiUtil.bindSessionAndOpenid(openid, request.getSession(true));
            }
        }

        log.info("===> openid: " + openid);
        return ControllerResult.success(openid);
    }

    private Object processWxMessage(boolean isOnline) {
        String method = request.getMethod();

        if(method.equalsIgnoreCase("get")) {
            // 验证接入
            if(true == WechatApiUtil.checkToken(request, isOnline)) {
                String echostr = request.getParameter("echostr");

                log.info("echostr: " + echostr);

                response.setContentType("application/json;charset=UTF-8");
                PrintWriter out;
                try {
                    out = response.getWriter();
                    out.print(echostr);
                    out.flush();
                    out.close();
                } catch(IOException e) {
                    // TODO Auto-generated catch block
                    log.error("processWxMessage Error: ", e);
                }
            }
        } else if(method.equalsIgnoreCase("post")) {
            // 接收消息
            if(false == WechatApiUtil.checkToken(request, isOnline)) {
                log.error("WechatApiUtil.checkAccession faile: " + request);
                return null;
            }

            // xml请求解析
            Map<String, String> requestMap = null;
            try {
                requestMap = WechatApiUtil.parseXml(request);
                log.info(requestMap.toString());

            } catch(Exception e) {
                // TODO Auto-generated catch block
                log.error("parseXml Error: ", e);
            }

            if(null != requestMap) {
                String ToUserName = requestMap.get("ToUserName"); // 开发者微信号
                String FromUserName = requestMap.get("FromUserName"); // 发送方帐号（一个OpenID）
                String MsgType = requestMap.get("MsgType"); // 消息类型

                if("text".equals(MsgType)) {
                    processWxTextMessage(requestMap.get("Content"), ToUserName, FromUserName, isOnline);
                } else if("event".equals(MsgType)) {
                    processWxEventMessage(requestMap.get("Event"), ToUserName, FromUserName, isOnline);
                }
            }
        }

        return null;
    }

    private void processWxTextMessage(String content, String ToUserName, String FromUserName, boolean isOnline) {
        if("我要报名".equals(content)) {
            WechatApiUtil.sendTextMsg(FromUserName, "您好，报名时间9.10-10.10，还未开始哦", isOnline);
        }
    }

    private void processWxEventMessage(String event, String ToUserName, String FromUserName, boolean isOnline) {
        // 关注公众号
        if("subscribe".equals(event)) {
            WechatApiUtil.bindSessionAndOpenid(FromUserName, request.getSession(true));
        }
    }
}