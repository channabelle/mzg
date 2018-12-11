package com.channabelle.controller.order.c;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.channabelle.common.ControllerResult;
import com.channabelle.common.ServiceResult;
import com.channabelle.common.ServiceResult.Status;
import com.channabelle.controller.BaseController;
import com.channabelle.model.order.UserOrder;
import com.channabelle.model.order.UserOrderCreate;
import com.channabelle.model.order.UserSubOrder;
import com.channabelle.model.user.User;
import com.channabelle.service.impl.CommonServiceImpl;
import com.channabelle.service.impl.order.UserOrderServiceImpl;
import com.channabelle.service.impl.user.UserServiceImpl;

@Controller
@RequestMapping("/c/order")
public class CUserOrderController extends BaseController {
	Logger log = Logger.getLogger(CUserOrderController.class);

	@Autowired
	UserOrderServiceImpl<UserOrder> service;

	@Autowired
	CommonServiceImpl<UserSubOrder> subOrderService;

	@Autowired
	private HttpServletRequest request;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody Object list(@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pagePerNumber", required = true) int pagePerNumber) {
		User user = UserServiceImpl.getSession_User(this.request);
		Map<String, Object> map = null;
		if (null != user) {
			List<Criterion> criterions = new ArrayList<Criterion>();
			criterions.add(Restrictions.eq("uuid_user", user.getUuid_user()));

			List<Order> orders = new ArrayList<Order>();
			orders.add(Order.desc("cTime"));

			map = service.hfindAll(UserOrder.class, criterions, orders, page, pagePerNumber);
		}
		return ControllerResult.success(map);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public @ResponseBody Object create(@RequestBody UserOrderCreate uoc) {
		User user = UserServiceImpl.getSession_User(this.request);
		ServiceResult res = null;
		UserOrder userOrder = new UserOrder();
		if (null != user) {
			res = service.createUserOrderFromShoppingCar(user, uoc, userOrder, uoc.getUuid_shop());
		}

		return ControllerResult.setServiceResult(res, userOrder.getP_uuid_user_order());
	}

	@RequestMapping(value = "/update/{uuid}/{action}", method = RequestMethod.POST)
	public @ResponseBody Object update(@PathVariable String uuid, @PathVariable String action) {
		User user = UserServiceImpl.getSession_User(this.request);
		UserOrder uo = service.hfindOne(UserOrder.class, uuid);

		if (false == user.getUuid_user().equals(uo.getUuid_user())) {
			return ControllerResult.setServiceResult(new ServiceResult(Status.Error, "当前用户与修改用户编号", "不一致"), null);
		}

		if ("cancel".equals(action)) {
			uo.setOrder_pay_status(5);
			service.hUpdateOne(uo);
		}

		return ControllerResult.success(uo);
	}

	@RequestMapping(value = "/info/{uuid}", method = RequestMethod.GET)
	public @ResponseBody Object info(@PathVariable String uuid) {
		User user = UserServiceImpl.getSession_User(this.request);
		UserOrder uo = service.hfindOne(UserOrder.class, uuid);

		if (false == user.getUuid_user().equals(uo.getUuid_user())) {
			return ControllerResult.setServiceResult(new ServiceResult(Status.Error, "当前用户与订单所属用户", "不一致"), null);
		}

		List<Criterion> criterions = new ArrayList<Criterion>();
		criterions.add(Restrictions.eq("uuid_user_order", uo.getP_uuid_user_order()));
		List<UserSubOrder> subOrders = subOrderService.hfindAll(UserSubOrder.class, criterions);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userOrder", uo);
		map.put("userSubOrder", subOrders);

		return ControllerResult.success(map);
	}
}
