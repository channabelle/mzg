package com.channabelle.controller.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.channabelle.common.ControllerResult;
import com.channabelle.common.ServiceResult;
import com.channabelle.controller.BaseController;
import com.channabelle.model.User;
import com.channabelle.service.impl.TestServiceImpl;
import com.channabelle.service.impl.UserServiceImpl;

@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {
	@Autowired
	TestServiceImpl<Object> testService;

	@Autowired
	UserServiceImpl<User> userService;

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
	Object updateApple(@RequestBody User user) {
		ServiceResult sRes = userService.updateUser(user);
		return ControllerResult.setServiceResult(sRes, userService.hfindOne(User.class, user.getUuid_user()));
	}
}
