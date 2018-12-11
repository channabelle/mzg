package com.channabelle.controller.admin;

import com.channabelle.common.ControllerResult;
import com.channabelle.common.ServiceResult;
import com.channabelle.common.ServiceResult.Status;
import com.channabelle.common.exception.ServiceException;
import com.channabelle.controller.BaseController;
import com.channabelle.model.admin.Administrator;
import com.channabelle.model.user.User;
import com.channabelle.service.impl.TestServiceImpl;
import com.channabelle.service.impl.admin.AdministratorServiceImpl;
import com.channabelle.service.impl.user.UserServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/administrator")
public class AdministratorController extends BaseController {
    Logger log = Logger.getLogger(AdministratorController.class);

    @Autowired
    TestServiceImpl<Object> testService;

    @Autowired
    UserServiceImpl<User> userService;

    @Autowired
    AdministratorServiceImpl<Administrator> administratorService;

    @RequestMapping(value = "/sql", method = RequestMethod.POST)
    public @ResponseBody
    Object runSql(@RequestBody String sql) {
        return ControllerResult.success(testService.execSQL(sql));
    }

    @RequestMapping(value = "/user", method = RequestMethod.DELETE)
    public @ResponseBody
    Object deleteUser(@RequestBody User user) {
        return ControllerResult.success(userService.deleteUser(user));
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public @ResponseBody
    Object listUser() {
        List<User> apples = userService.hfindAll(User.class);
        return ControllerResult.success(apples);
    }

    @RequestMapping(value = "/user", method = RequestMethod.PATCH)
    public @ResponseBody
    Object updateUser(@RequestBody User user) {
        ServiceResult sRes = userService.updateUser(user);
        return ControllerResult.setServiceResult(sRes, userService.hfindOne(User.class, user.getUuid_user()));
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody
    Object login(@RequestBody Administrator a) {
        Administrator u = null;

        try {
            u = administratorService.loginByAccountAndPassword(a, request, true);
        } catch(ServiceException e) {
            // TODO Auto-generated catch block
            log.error("", e);
            return ControllerResult.systemError(u);
        }
        if(null == u) {
            ServiceResult res = new ServiceResult(Status.Info, "用户名或密码", "错误");
            return ControllerResult.setServiceResult(res, u);
        } else {
            return ControllerResult.success(u);
        }
    }
}
