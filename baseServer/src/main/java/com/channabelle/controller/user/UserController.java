package com.channabelle.controller.user;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.channabelle.common.ControllerResult;
import com.channabelle.common.ServiceResult;
import com.channabelle.common.ServiceResult.Status;
import com.channabelle.common.exception.ServiceException;
import com.channabelle.controller.BaseController;
import com.channabelle.model.User;
import com.channabelle.service.impl.UserServiceImpl;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
	Logger log = Logger.getLogger(UserController.class);

	@Autowired
	UserServiceImpl<User> service;

	@Autowired
	private HttpServletRequest request;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody
	Object login(@RequestBody User user) {
		User userLogin = null;

		try {
			userLogin = service.login(user, request);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			log.error("", e);
			return ControllerResult.systemError(userLogin);
		}
		if (null == userLogin) {
			ServiceResult res = new ServiceResult(Status.Info, "用户名或密码", "错误");
			return ControllerResult.setServiceResult(res, userLogin);
		} else {
			return ControllerResult.success(userLogin);
		}
	}

	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public @ResponseBody
	Object logout() {
		service.logout(request);

		return ControllerResult.success(null);
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public @ResponseBody
	Object register(@RequestBody User user) {
		ServiceResult sRes = service.register(user);
		return ControllerResult.setServiceResult(sRes, service.hfindOne(User.class, user.getUuid_user()));
	}
}
