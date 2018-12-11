package com.channabelle.controller.shoppingCar.c;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.channabelle.common.ControllerResult;
import com.channabelle.common.ServiceResult;
import com.channabelle.common.ServiceResult.Status;
import com.channabelle.common.exception.ServiceException;
import com.channabelle.controller.BaseController;
import com.channabelle.model.user.ShoppingCar;
import com.channabelle.model.user.User;
import com.channabelle.service.impl.shoppingCar.ShoppingCarServiceImpl;
import com.channabelle.service.impl.user.UserServiceImpl;

@Controller
@RequestMapping("/c/shoppingCar")
public class ShoppingCarController extends BaseController {
	private Logger log = Logger.getLogger(ShoppingCarController.class);
	@Autowired
	ShoppingCarServiceImpl<ShoppingCar> shoppingCarService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody Object list() {
		List<ShoppingCar> list = null;
		Map<String, Object> summary = null;

		User user = UserServiceImpl.getSession_User(this.request);
		if (null != user) {
			list = shoppingCarService.listByUserUuidAndProInfo(user.getUuid_user(), null);
			summary = shoppingCarService.getSummaryByShoppingCarList(list);
		}
		Map<String, Object> mapRes = new HashMap<String, Object>();
		mapRes.put("summary", summary);
		mapRes.put("list", list);
		return ControllerResult.success(mapRes);
	}

	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public @ResponseBody Object list(@RequestBody List<String> uuids) {
		List<ShoppingCar> list = null;
		Map<String, Object> summary = null;

		User user = UserServiceImpl.getSession_User(this.request);
		if (null != user) {
			list = shoppingCarService.getByUserUuidAndShoppingCarUuids(user.getUuid_user(), uuids);
			summary = shoppingCarService.getSummaryByShoppingCarList(list);
		}

		Map<String, Object> mapRes = new HashMap<String, Object>();
		mapRes.put("summary", summary);
		mapRes.put("list", list);

		return ControllerResult.success(mapRes);
	}

	@RequestMapping(value = "/count", method = RequestMethod.GET)
	public @ResponseBody Object count() {
		User user = UserServiceImpl.getSession_User(this.request);
		Map<String, Object> map = null;
		if (null != user) {
			List<ShoppingCar> list = shoppingCarService.listByUserUuidAndProInfo(user.getUuid_user(), null);
			map = shoppingCarService.getSummaryByShoppingCarList(list);
		}
		return ControllerResult.success(map);
	}

	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public @ResponseBody Object addOrUpdate(@RequestBody ShoppingCar shoppingCar) throws ServiceException {
		log.info("=== addOrUpdate ===");

		ServiceResult res = null;
		Map<String, Object> map = null;
		List<ShoppingCar> list = null;
		int nAmount = shoppingCar.getAmount();
		int changeNum = shoppingCar.getAmountChangeNum();

		User user = UserServiceImpl.getSession_User(this.request);
		if (null != user) {
			shoppingCar.setUuid_user(user.getUuid_user());

			ShoppingCar ss = shoppingCarService.getByUserUuidAndProInfo(shoppingCar.getUuid_user(),
					shoppingCar.getUuid_pro_info());

			if (null != ss) {
				shoppingCar.setP_uuid_shopping_car(ss.getP_uuid_shopping_car());
				int setAmount = 0;
				if (0 != changeNum) {
					setAmount = ss.getAmount() + changeNum;
				} else {
					setAmount = nAmount;
				}

				shoppingCar.setAmount(setAmount);
				if (0 == setAmount) {
					res = shoppingCarService.hDeleteOne(shoppingCar);
				} else {
					res = shoppingCarService.hPatchUpdateOne(shoppingCar, ShoppingCar.class,
							shoppingCar.getP_uuid_shopping_car());

				}
			} else {
				if (0 != changeNum) {
					shoppingCar.setAmount(changeNum);
				}
				res = shoppingCarService.hAddOne(shoppingCar);
			}
			list = shoppingCarService.listByUserUuidAndProInfo(user.getUuid_user(), null);
			map = shoppingCarService.getSummaryByShoppingCarList(list);
		}

		Map<String, Object> mapRes = new HashMap<String, Object>();
		mapRes.put("summary", map);
		mapRes.put("list", list);

		return ControllerResult.setServiceResult(res, mapRes);
	}

	@RequestMapping(value = "/delete/{uuid}", method = RequestMethod.POST)
	public @ResponseBody Object delete(@PathVariable String uuid) {
		ServiceResult res = new ServiceResult();
		User user = UserServiceImpl.getSession_User(this.request);
		if (null != user) {
			ShoppingCar ss = shoppingCarService.hfindOne(ShoppingCar.class, uuid);

			if (null != ss) {
				if (false == ss.getUuid_user().equals(user.getUuid_user())) {
					res = new ServiceResult(Status.Error, "购物车记录" + uuid, "不属于该用户");
					return ControllerResult.setServiceResult(res, null);
				}

				res = shoppingCarService.hDeleteOne(ss);
			}
		}
		return ControllerResult.setServiceResult(res, null);
	}

	@RequestMapping(value = "/checkOrUncheck/{action}", method = RequestMethod.POST)
	public @ResponseBody Object checkOrUncheck(@PathVariable String action) {
		ServiceResult res = new ServiceResult();
		Map<String, Object> mapRes = new HashMap<String, Object>();

		User user = UserServiceImpl.getSession_User(this.request);
		if (null != user) {
			List<ShoppingCar> sCars = shoppingCarService.listByUserUuidAndProInfo(user.getUuid_user(), null);

			res = shoppingCarService.handleShoppingCar(sCars, action);

			List<ShoppingCar> list = shoppingCarService.listByUserUuidAndProInfo(user.getUuid_user(), null);
			Map<String, Object> map = shoppingCarService.getSummaryByShoppingCarList(list);

			mapRes.put("summary", map);
			mapRes.put("list", list);
		}

		return ControllerResult.setServiceResult(res, mapRes);
	}

}
