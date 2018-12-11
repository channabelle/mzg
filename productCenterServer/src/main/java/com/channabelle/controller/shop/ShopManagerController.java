package com.channabelle.controller.shop;

import com.channabelle.common.ControllerResult;
import com.channabelle.common.ServiceResult;
import com.channabelle.common.ServiceResult.Status;
import com.channabelle.common.exception.ServiceException;
import com.channabelle.controller.BaseController;
import com.channabelle.model.shop.ShopManager;
import com.channabelle.service.impl.shop.ShopManagerServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/shopManager")
public class ShopManagerController extends BaseController {
    private Logger log = Logger.getLogger(ShopManagerController.class);

    @Autowired
    ShopManagerServiceImpl<ShopManager> shopManagerService;

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody
    Object login(@RequestBody ShopManager shopManager) {
        ShopManager sm = null;

        try {
            sm = shopManagerService.loginByAccountAndPassword(shopManager, request, true);
        } catch(ServiceException e) {
            log.error("", e);
            return ControllerResult.systemError(sm);
        }
        if(null == sm) {
            ServiceResult res = new ServiceResult(Status.Info, "用户名或密码", "错误");
            return ControllerResult.setServiceResult(res, sm);
        } else {
            return ControllerResult.success(sm);
        }
    }

}
