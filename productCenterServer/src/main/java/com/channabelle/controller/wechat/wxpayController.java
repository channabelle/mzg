package com.channabelle.controller.wechat;

import com.channabelle.common.ControllerResult;
import com.channabelle.controller.BaseController;
import com.channabelle.controller.wechat.util.DoWXPay;
import com.channabelle.controller.wechat.util.WechatApiUtil;
import com.channabelle.model.WepayOrder;
import com.channabelle.service.impl.WepayOrderServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping("/wxpay")
public class wxpayController extends BaseController {
    Logger log = Logger.getLogger(wxpayController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    WepayOrderServiceImpl<WepayOrder> orderService;

    @RequestMapping(value = "/sandbox/sign", method = RequestMethod.GET)
    public @ResponseBody
    Object sandboxSign() throws Exception {
        log.info("===== sandboxSign =====");

        DoWXPay pay = new DoWXPay(WechatApiUtil.USE_SANDBOX);
        String res = pay.getSandboxSign();

        return ControllerResult.success(res);
    }

    @RequestMapping(value = "/pay/notifySandbox")
    public @ResponseBody
    void payNotifySandbox() throws Exception {
        log.info("===== payNotifySandbox =====");
        processPayNotify();
        return;
    }

    @RequestMapping(value = "/pay/notifyOnline")
    public @ResponseBody
    void payNotifyOnline() throws Exception {
        log.info("===== payNotifyOnline =====");
        processPayNotify();
        return;
    }

    private void processPayNotify() throws Exception {
        Map<String, String> requestMap = WechatApiUtil.parseXml(request);
        log.info(requestMap);

        // 处理订单
        if("SUCCESS".equals(requestMap.get("return_code")) && "SUCCESS".equals(requestMap.get("result_code"))) {
            WepayOrder order = orderService.hfindOne(WepayOrder.class, requestMap.get("out_trade_no"));
            // 初步核实下订单信息
            if(null != order && null != order.getAttach() && order.getAttach().equals(requestMap.get("attach")) && null != order.getOpenid() && order.getOpenid().equals(requestMap.get("openid"))) {
                order.setTransaction_id(requestMap.get("transaction_id"));

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                Date uTime = sdf.parse(requestMap.get("time_end"));
                order.setuTime(uTime);

                order.setTrade_state("SUCCESS");

                orderService.update(order);
            } else {
                log.error("支付回调订单与发起时信息不匹配, 原订单: ");
                log.error(order);
            }
        }

        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out;
        try {
            out = response.getWriter();
            out.print("<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>");
            out.flush();
            out.close();
        } catch(IOException e) {
            // TODO Auto-generated catch block
            log.error("processPayNotify Error: ", e);
        }
    }
}