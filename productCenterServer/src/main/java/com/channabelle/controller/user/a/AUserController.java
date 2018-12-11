package com.channabelle.controller.user.a;

import com.channabelle.common.ControllerResult;
import com.channabelle.common.ServiceResult;
import com.channabelle.common.exception.ServiceException;
import com.channabelle.controller.BaseController;
import com.channabelle.model.user.User;
import com.channabelle.service.impl.user.UserServiceImpl;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/a/user")
public class AUserController extends BaseController {
	private Logger log = Logger.getLogger(AUserController.class);

	@Autowired
	UserServiceImpl<User> service;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody Object list(@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pagePerNumber", required = true) int pagePerNumber) {

		Map<String, Object> map = null;
		List<Criterion> criterion = new ArrayList<Criterion>();

		List<Order> orders = new ArrayList<Order>();
		orders.add(Order.desc("cTime"));

		map = service.hfindAll(User.class, criterion, orders, page, pagePerNumber);
		return ControllerResult.success(map);
	}

	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public @ResponseBody Object info(@RequestParam(value = "uid", required = false) String uid,
			@RequestParam(value = "account", required = false) String account) {

		User user = null;
		if (null != uid) {
			user = service.hfindOne(User.class, uid);
		} else if (null != account) {
			try {
				user = service.findUserByAccount(account);
			} catch (ServiceException e) {
				log.error("", e);
			}
		}

		return ControllerResult.success(user);
	}

	@RequestMapping(value = "/update", method = RequestMethod.PATCH)
	public @ResponseBody Object patchUpdateUser(@RequestBody User user) {
		ServiceResult sRes = service.hPatchUpdateOne(user, User.class, user.getUuid_user());
		return ControllerResult.setServiceResult(sRes, service.hfindOne(User.class, user.getUuid_user()));
	}
}
