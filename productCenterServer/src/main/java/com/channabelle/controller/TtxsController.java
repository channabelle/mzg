package com.channabelle.controller;

import com.channabelle.common.ControllerResult;
import com.channabelle.common.ServiceResult;
import com.channabelle.common.exception.ServiceException;
import com.channabelle.controller.wechat.util.DoWXPay;
import com.channabelle.controller.wechat.util.WechatApiUtil;
import com.channabelle.controller.wechat.util.wePay.WXPayUtil;
import com.channabelle.model.WepayOrder;
import com.channabelle.model.user.User;
import com.channabelle.service.impl.WepayOrderServiceImpl;
import com.channabelle.service.impl.user.UserServiceImpl;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ttxs")
public class TtxsController extends BaseController {
    Logger log = Logger.getLogger(TtxsController.class);

    @Autowired
    UserServiceImpl<User> userService;

    @Autowired
    WepayOrderServiceImpl<WepayOrder> orderService;

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "/enroll", method = RequestMethod.POST)
    public @ResponseBody
    Object enrollSubmit(@RequestBody User u) {
        log.info("=== enrollSubmit ===");
        User user = userService.registerByPassword(u);

        // 天天向上特殊需求，只要提交，无论注册是否成功，默认都是登录，绑定当前user和session
        if(true) {
            try {
                userService.loginByAccountAndPassword(user, request, true);
            } catch(ServiceException e) {
                // TODO Auto-generated catch block
                log.error("", e);
            }
        }
        try {
            user = userService.findUserByAccount(user.getAccount());
        } catch(ServiceException e) {
            // TODO Auto-generated catch block
            log.error("", e);
        }

        HttpSession session = request.getSession(false);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("user", user);

        String openid = WechatApiUtil.getOpenidFromSession(session);

        /*
         * 本次活动规则，一个用户（宝宝名+电话）为唯一识别号，只能报名一次。 如果未成功支付报名费，直接生成新的订单
         */
        JSONObject jAttach = new JSONObject();
        jAttach.put("account", user.getAccount());
        String attach = jAttach.toString();
        List<WepayOrder> list = orderService.findSuccessPayedOrderByAttach(attach);
        if(null != list && 0 < list.size()) {
            map.put("orderList", list);
        } else {
            try {
                DoWXPay pay = new DoWXPay(WechatApiUtil.USE_SANDBOX);
                int fee = 1;
                HashMap<String, Object> data = new HashMap<String, Object>();
                data.put("openid", openid);
                data.put("total_fee", fee);
                if(null != attach) {
                    data.put("attach", attach);
                }
                Map<String, Object> order = pay.doUnifiedOrder(data);

                if(null != order && "SUCCESS".equals(order.get("return_code")) && "SUCCESS".equals(order.get("result_code"))) {

                    long cTime = WXPayUtil.getCurrentTimestampMs();

                    // 标准订单字段
                    Map<String, Object> orderSuccess = new HashMap<String, Object>();
                    orderSuccess.put("appId", order.get("appid"));
                    orderSuccess.put("timeStamp", String.valueOf(cTime / 1000));
                    orderSuccess.put("nonceStr", String.valueOf(WXPayUtil.generateNonceStr()));
                    orderSuccess.put("package", "prepay_id=" + String.valueOf(order.get("prepay_id")));
                    orderSuccess.put("signType", "MD5");
                    orderSuccess.put("paySign", pay.sign(orderSuccess));

                    // 追加字段
                    orderSuccess.put("orderStatus", "SUCCESS");
                    orderSuccess.put("fee", String.valueOf(fee));

                    // 微信端订单生成成功，创建本地订单
                    WepayOrder wOrder = new WepayOrder();
                    wOrder.setAppid(orderSuccess.get("appId").toString());
                    wOrder.setAttach(attach);
                    wOrder.setBody(data.get("body").toString());
                    wOrder.setcTime(new Date(cTime));
                    wOrder.setMch_id(data.get("mch_id").toString());
                    wOrder.setOpenid(data.get("openid").toString());
                    wOrder.setOut_trade_no(data.get("out_trade_no").toString());
                    wOrder.setPrepay_id(String.valueOf(order.get("prepay_id")));
                    wOrder.setTotal_fee(Integer.valueOf(data.get("total_fee").toString()));
                    wOrder.setTrade_state("NOTPAY");
                    wOrder.setTrade_type(data.get("trade_type").toString());
                    wOrder.setuTime(new Date(cTime));

                    ServiceResult sResult = orderService.create(wOrder);

                    // 微信订单和本地订单都创建成功，返回前端
                    if(ServiceResult.Status.Success == sResult.getStatus()) {
                        map.put("orderSuccess", orderSuccess);
                    } else {
                        map.put("orderFail", sResult);
                    }
                }
            } catch(Exception e) {
                // TODO Auto-generated catch block
                log.error("", e);
            }
        }
        log.info("===>");
        log.info(map);
        return ControllerResult.success(map);
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public @ResponseBody
    Object info(@RequestParam(value = "uid", required = false) String uid,
                @RequestParam(value = "account", required = false) String account) {

        log.info("info query by [uid = " + uid + " or account = " + account + "]");

        User user = null;
        if(null != uid) {
            user = userService.hfindOne(User.class, uid);
        } else if(null != account) {
            try {
                user = userService.findUserByAccount(account);
            } catch(ServiceException e) {
                // TODO Auto-generated catch block
                log.error("", e);
            }
        }

        return ControllerResult.success(user);
    }
}
