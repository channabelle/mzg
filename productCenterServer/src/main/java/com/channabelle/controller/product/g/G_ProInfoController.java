package com.channabelle.controller.product.g;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
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
import com.channabelle.model.product.ProDetail;
import com.channabelle.model.product.ProInfo;
import com.channabelle.model.product.ProMenu;
import com.channabelle.model.shop.ShopConfig;
import com.channabelle.service.impl.product.MenuServiceImpl;
import com.channabelle.service.impl.product.ProDetailServiceImpl;
import com.channabelle.service.impl.product.ProInfoServiceImpl;
import com.channabelle.service.impl.shop.ShopConfigServiceImpl;

@Controller
@RequestMapping("/g/product/proInfo")
public class G_ProInfoController extends BaseController {
	@Autowired
	ProInfoServiceImpl<ProInfo> proInfoService;

	@Autowired
	ShopConfigServiceImpl<ShopConfig> shopConfigService;

	@Autowired
	ProDetailServiceImpl<ProDetail> proDetailService;

	@Autowired
	MenuServiceImpl<ProMenu> menuService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public @ResponseBody Object info(@RequestParam(value = "pid", required = true) String pid) throws Exception {
		List<Criterion> criterions = new ArrayList<Criterion>();
		criterions.add(Restrictions.eq("p_uuid_pro_info", pid));

		ProInfo pro = proInfoService.hfindOne(ProInfo.class, criterions);
		if (null != pro) {
			pro.setPro_menus(proInfoService.getMenusByProInfo(pro));
			pro.setPro_detail(proDetailService.getProDetailByProInfoId(pro.getP_uuid_pro_info()));
		}

		return ControllerResult.success(pro);
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody Object list(
			@RequestParam(value = "miniProgram_appid", required = false) String miniProgram_appid,
			@RequestParam(value = "shopId", required = false) String shopId,
			@RequestParam(value = "menuId", required = false) String menuId,
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pagePerNumber", required = true) int pagePerNumber) throws ServiceException {

		Map<String, Object> map = new HashMap<String, Object>();
		List<Criterion> criterions = new ArrayList<Criterion>();
		List<String> uuid_shops = null;

		if (null != shopId) {
			criterions.add(Restrictions.eq("uuid_shop", shopId));
		} else if (null != miniProgram_appid) {
			uuid_shops = shopConfigService.getShopUuidsByNameAndValue(ShopConfig.CONFIG_MINI_PROGRAM_APPID[0],
					miniProgram_appid);
			criterions.add(Restrictions.in("uuid_shop", uuid_shops));
		}

		List<ProMenu> menus = menuService.getMenuByShopUuid(uuid_shops, true);
		String menuUuid = null;
		Map<String, Object> proInfos = null;
		if (null == menuId) {
			if (null != menus && 0 < menus.size()) {
				menuUuid = menus.get(0).getP_uuid_pro_menu();
			}
		} else {
			menuUuid = menuId;
		}
		if (null != menuUuid) {
			proInfos = proInfoService.getProInfosByMenuId(menuUuid, true, page, pagePerNumber);
		}

		map.put("menuUuid", menuUuid);
		map.put("menus", menus);
		map.put("proInfos", proInfos);

		return ControllerResult.success(map);
	}
}
