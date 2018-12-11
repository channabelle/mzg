package com.channabelle.controller.user.b;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.channabelle.common.ControllerResult;
import com.channabelle.common.exception.ServiceException;
import com.channabelle.controller.BaseController;
import com.channabelle.model.shop.ShopConfig;
import com.channabelle.model.shop.ShopManager;
import com.channabelle.model.user.User;
import com.channabelle.service.impl.shop.ShopConfigServiceImpl;
import com.channabelle.service.impl.shop.ShopManagerServiceImpl;
import com.channabelle.service.impl.user.UserServiceImpl;

@Controller
@RequestMapping("/b/user")
public class BUserController extends BaseController {
	private Logger log = Logger.getLogger(BUserController.class);

	@Autowired
	UserServiceImpl<User> userService;

	@Autowired
	ShopConfigServiceImpl<ShopConfig> shopConfigService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody Object list(@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pagePerNumber", required = true) int pagePerNumber,
			@RequestParam(value = "uName", required = false) String uName,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "cTime_start", required = false) String cTime_start,
			@RequestParam(value = "cTime_end", required = false) String cTime_end) throws Exception {

		ShopManager sm = ShopManagerServiceImpl.getSession_ShopManager(this.request);
		String miniProgram_appid = shopConfigService.getValueByUuidShopAndConfigName(sm.getUuid_shop(),
				ShopConfig.CONFIG_MINI_PROGRAM_APPID[0]);

		Map<String, Object> map = null;
		List<Criterion> criterion = new ArrayList<Criterion>();
		criterion.add(Restrictions.eq("miniProgram_appid", miniProgram_appid));

		if (null != uName && 0 < uName.length()) {
			criterion.add(Restrictions.or(Restrictions.ilike("account", uName, MatchMode.ANYWHERE),
					Restrictions.ilike("realname", uName, MatchMode.ANYWHERE)));
		}
		if (null != phone && 0 < phone.length()) {
			criterion.add(Restrictions.ilike("phone", phone, MatchMode.ANYWHERE));
		}
		if (null != cTime_start && 0 < cTime_start.length()) {
			criterion.add(Restrictions.ge("cTime", (new SimpleDateFormat("yyyy-MM-dd")).parse(cTime_start)));
		}
		if (null != cTime_end && 0 < cTime_end.length()) {
			criterion.add(Restrictions.le("cTime", (new SimpleDateFormat("yyyy-MM-dd")).parse(cTime_end)));
		}

		List<Order> orders = new ArrayList<Order>();
		orders.add(Order.desc("cTime"));

		map = userService.hfindAll(User.class, criterion, orders, page, pagePerNumber);
		return ControllerResult.success(map);
	}

	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public @ResponseBody Object info(@RequestParam(value = "uid", required = false) String uid,
			@RequestParam(value = "account", required = false) String account) {

		User user = null;
		if (null != uid) {
			user = userService.hfindOne(User.class, uid);
		} else if (null != account) {
			try {
				user = userService.findUserByAccount(account);
			} catch (ServiceException e) {
				log.error("", e);
			}
		}

		return ControllerResult.success(user);
	}
}
