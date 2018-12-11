package com.channabelle.controller.user.c;

import com.channabelle.common.ControllerResult;
import com.channabelle.common.ServiceResult;
import com.channabelle.common.ServiceResult.Status;
import com.channabelle.controller.BaseController;
import com.channabelle.model.user.User;
import com.channabelle.service.impl.user.UserServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/c/user")
public class CUserController extends BaseController {
	private Logger log = Logger.getLogger(CUserController.class);

	@Autowired
	UserServiceImpl<User> service;

	@RequestMapping(value = "/update", method = RequestMethod.PATCH)
	public @ResponseBody Object patchUpdateUser(@RequestBody User user) {
		User u = UserServiceImpl.getSession_User(this.request);

		if (false == u.getUuid_user().equals(user.getUuid_user())) {
			return ControllerResult.setServiceResult(new ServiceResult(Status.Error, "当前用户与修改用户编号", "不一致"), null);
		}
		ServiceResult sRes = service.hPatchUpdateOne(user, User.class, user.getUuid_user());
		return ControllerResult.setServiceResult(sRes, service.hfindOne(User.class, user.getUuid_user()));
	}

	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public @ResponseBody Object info() {
		User u = UserServiceImpl.getSession_User(this.request);
		User user = service.hfindOne(User.class, u.getUuid_user());

		return ControllerResult.success(user);
	}
}
