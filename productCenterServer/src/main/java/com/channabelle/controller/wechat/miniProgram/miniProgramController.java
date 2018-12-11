package com.channabelle.controller.wechat.miniProgram;

import com.channabelle.common.ControllerResult;
import com.channabelle.common.ServiceResult;
import com.channabelle.common.ServiceResult.Status;
import com.channabelle.common.exception.ServiceException;
import com.channabelle.common.utils.HttpClientUtil;
import com.channabelle.common.utils.JsonDateValueProcessor;
import com.channabelle.controller.BaseController;
import com.channabelle.model.shop.ShopConfig;
import com.channabelle.model.user.User;
import com.channabelle.service.impl.shop.ShopConfigServiceImpl;
import com.channabelle.service.impl.user.UserServiceImpl;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/wechat/miniProgram")
public class miniProgramController extends BaseController {
    Logger log = Logger.getLogger(miniProgramController.class);

    @Autowired
    UserServiceImpl<User> userService;

    @Autowired
    ShopConfigServiceImpl<ShopConfig> cService_UserConfig;

    @RequestMapping(value = "/test")
    public @ResponseBody
    Object test() {
        log.info("===== test =====");

        return null;
    }

    @RequestMapping(value = "/login")
    public @ResponseBody
    Object login(@RequestParam(value = "appid", required = true) String appid,
                 @RequestParam(value = "code", required = true) String code) throws ServiceException {
        log.info("===== login =====");

        String secret = cService_UserConfig.getValueByConfigName("MINI_PROGRAM_APPSECRET_" + appid);

        String url = String.format("https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code", appid, secret, code);

        String res = HttpClientUtil.executeGet(url);
        String openid = null;
        String unionid = null;
        if(null != res) {
            JSONObject resJson = JSONObject.fromObject(res, JsonDateValueProcessor.getJsonConfig());
            if(null != resJson && resJson.containsKey("openid")) {
                openid = resJson.getString("openid");
            }
            if(null != resJson && resJson.containsKey("unionid")) {
                unionid = resJson.getString("unionid");
            }
        }

        if(null != openid) {
            User u = userService.findUserByMiniProgram(appid, openid);
            User uu = null;
            if(null != u) {
                uu = userService.loginByUser(u, this.request, false);
            } else {
                uu = userService.registerByMiniProgram(appid, openid);
                uu = userService.loginByUser(uu, this.request, false);
            }
            return ControllerResult.success(uu);
        } else {
            return ControllerResult.setServiceResult(new ServiceResult(Status.Error, "openid", "为空"), null);
        }
    }
}