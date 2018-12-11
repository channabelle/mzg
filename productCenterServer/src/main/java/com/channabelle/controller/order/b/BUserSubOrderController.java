package com.channabelle.controller.order.b;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.channabelle.common.ControllerResult;
import com.channabelle.controller.BaseController;
import com.channabelle.model.order.UserSubOrder;
import com.channabelle.model.shop.ShopManager;
import com.channabelle.service.impl.order.UserSubOrderServiceImpl;
import com.channabelle.service.impl.shop.ShopManagerServiceImpl;

@Controller
@RequestMapping("/b/subOrder")
public class BUserSubOrderController extends BaseController {
	Logger log = Logger.getLogger(BUserSubOrderController.class);

	@Autowired
	UserSubOrderServiceImpl<UserSubOrder> subOrderService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody Object list(@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pagePerNumber", required = true) int pagePerNumber) {
		ShopManager sm = ShopManagerServiceImpl.getSession_ShopManager(this.request);
		Map<String, Object> orders = subOrderService.listByShopUuid(sm.getUuid_shop(), page, pagePerNumber);
		return ControllerResult.success(orders);
	}

}
