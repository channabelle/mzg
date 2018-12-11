package com.channabelle.controller.order.b;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.channabelle.common.ControllerResult;
import com.channabelle.common.ServiceResult;
import com.channabelle.common.ServiceResult.Status;
import com.channabelle.controller.BaseController;
import com.channabelle.model.order.UserOrder;
import com.channabelle.model.order.UserSubOrder;
import com.channabelle.model.shop.ShopManager;
import com.channabelle.service.impl.CommonServiceImpl;
import com.channabelle.service.impl.order.UserOrderServiceImpl;
import com.channabelle.service.impl.shop.ShopManagerServiceImpl;

@Controller
@RequestMapping("/b/order")
public class BUserOrderController extends BaseController {
	Logger log = Logger.getLogger(BUserOrderController.class);

	@Autowired
	UserOrderServiceImpl<UserOrder> service;

	@Autowired
	CommonServiceImpl<UserSubOrder> subOrderService;

	@Autowired
	private HttpServletRequest request;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody Object list(@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pagePerNumber", required = true) int pagePerNumber,
			@RequestParam(value = "orderName", required = false) String orderName,
			@RequestParam(value = "cTime_start", required = false) String cTime_start,
			@RequestParam(value = "cTime_end", required = false) String cTime_end) throws Exception {

		ShopManager sm = ShopManagerServiceImpl.getSession_ShopManager(this.request);

		Map<String, Object> map = null;
		if (null != sm) {
			List<Criterion> criterions = new ArrayList<Criterion>();
			criterions.add(Restrictions.eq("uuid_shop", sm.getUuid_shop()));

			if (null != orderName && 0 < orderName.length()) {
				criterions.add(Restrictions.ilike("order_name", orderName, MatchMode.ANYWHERE));
			}
			if (null != cTime_start && 0 < cTime_start.length()) {
				criterions.add(Restrictions.ge("cTime", (new SimpleDateFormat("yyyy-MM-dd")).parse(cTime_start)));
			}
			if (null != cTime_end && 0 < cTime_end.length()) {
				criterions.add(Restrictions.le("cTime", (new SimpleDateFormat("yyyy-MM-dd")).parse(cTime_end)));
			}

			List<Order> orders = new ArrayList<Order>();
			orders.add(Order.desc("cTime"));

			map = service.hfindAll(UserOrder.class, criterions, orders, page, pagePerNumber);
		}
		return ControllerResult.success(map);
	}

	@RequestMapping(value = "/info/{uuid}", method = RequestMethod.GET)
	public @ResponseBody Object info(@PathVariable String uuid) {
		ShopManager sm = ShopManagerServiceImpl.getSession_ShopManager(this.request);
		UserOrder uo = service.hfindOne(UserOrder.class, uuid);

		if (false == sm.getUuid_shop().equals(uo.getUuid_shop())) {
			return ControllerResult.setServiceResult(new ServiceResult(Status.Error, "当前商户与订单所属商户", "不一致"), null);
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
