package com.channabelle.controller.product.b;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.channabelle.common.ControllerResult;
import com.channabelle.common.ServiceResult;
import com.channabelle.common.ServiceResult.Status;
import com.channabelle.common.utils.NestedListConvert;
import com.channabelle.controller.BaseController;
import com.channabelle.model.product.ProInfo;
import com.channabelle.model.product.ProMenu;
import com.channabelle.model.shop.ShopManager;
import com.channabelle.service.impl.product.MenuServiceImpl;
import com.channabelle.service.impl.product.ProInfoServiceImpl;
import com.channabelle.service.impl.shop.ShopManagerServiceImpl;

@Controller
@RequestMapping(value = { "/b/product/proMenu" })
public class BProMenuController extends BaseController {
	@Autowired
	MenuServiceImpl<ProMenu> cService_ProMenu;

	@Autowired
	ProInfoServiceImpl<ProInfo> proInfoService;

	// 获取目录详情及所属产品信息
	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public @ResponseBody Object info(@RequestParam(value = "menu_uuid", required = true) String menu_uuid) {
		ServiceResult sRes = null;
		Map<String, Object> map = new HashMap<String, Object>();

		ShopManager sm = ShopManagerServiceImpl.getSession_ShopManager(this.request);

		ProMenu menu = cService_ProMenu.hfindOne(ProMenu.class, menu_uuid);
		if (null != menu) {
			if (false == menu.getUuid_shop().equals(sm.getUuid_shop())) {
				sRes = new ServiceResult(Status.Error, "目录" + menu_uuid, "不属于该商户");
				return ControllerResult.setServiceResult(sRes, null);
			}

			List<Criterion> criterions = new ArrayList<Criterion>();
			criterions.add(Restrictions.eq("uuid_shop", sm.getUuid_shop()));
			List<ProInfo> proinfo_list = proInfoService.getProInfosByMenuId(menu.getP_uuid_pro_menu(), false);

			map.put("menu", menu);
			map.put("proinfo_list", proinfo_list);
		}

		return ControllerResult.success(map);
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody Object list(@RequestParam(value = "fMenuId", required = false) String fMenuId,
			@RequestParam(value = "menu_status", required = false) String menu_status) {
		List<ProMenu> menus = null;
		ShopManager sm = ShopManagerServiceImpl.getSession_ShopManager(this.request);

		List<Criterion> criterions = new ArrayList<Criterion>();
		criterions.add(Restrictions.eq("uuid_shop", sm.getUuid_shop()));

		if (null != fMenuId) {
			if (true == "".equals(fMenuId)) {
				criterions.add(Restrictions.or(Restrictions.isNull("menu_father_uuid"),
						Restrictions.eq("menu_father_uuid", "")));
			} else {
				criterions.add(Restrictions.eq("menu_father_uuid", fMenuId));
			}
		}

		if (null != menu_status) {
			String[] status_s = menu_status.split(",");
			List<Integer> status_i = new ArrayList<Integer>();
			for (int k = 0; k < status_s.length; k++) {
				status_i.add(Integer.parseInt(status_s[k]));
			}
			criterions.add(Restrictions.in("menu_status", status_i));
		}

		menus = cService_ProMenu.hfindAll(ProMenu.class, criterions);
		List<Map<String, Object>> chainMenus = NestedListConvert.getChainList(menus, "p_uuid_pro_menu",
				"menu_father_uuid", "menu_name");

		Collections.sort(chainMenus, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				return o1.get("id_path").toString().compareTo(o2.get("id_path").toString());
			}
		});

		return ControllerResult.success(chainMenus);
	}

	// 生成层级结构的返回结果
	@RequestMapping(value = "/tree", method = RequestMethod.GET)
	public @ResponseBody Object tree() {
		List<ProMenu> menus = null;
		ShopManager sm = ShopManagerServiceImpl.getSession_ShopManager(this.request);

		List<String> list = new ArrayList<String>();
		list.add(sm.getUuid_shop());
		menus = cService_ProMenu.getMenuByShopUuid(list, false);

		List<Object> treeMenus = NestedListConvert.getTreeList(menus, "p_uuid_pro_menu", "menu_father_uuid",
				"children");

		return ControllerResult.success(treeMenus);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public @ResponseBody Object create(@RequestBody ProMenu proMenu) {
		ServiceResult sRes = null;

		ShopManager sm = ShopManagerServiceImpl.getSession_ShopManager(this.request);

		proMenu.setUuid_shop(sm.getUuid_shop());
		String fUid = proMenu.getMenu_father_uuid();
		if (null != fUid) {
			ProMenu fMenu = cService_ProMenu.getFatherMenu(fUid);
			if (null == fMenu) {
				sRes = new ServiceResult(Status.Warning, "父目录" + fUid, "不存在");
				return ControllerResult.setServiceResult(sRes, null);
			} else if (false == fMenu.getUuid_shop().equals(sm.getUuid_shop())) {
				sRes = new ServiceResult(Status.Error, "父目录" + fUid, "不属于该商铺");
				return ControllerResult.setServiceResult(sRes, null);
			}
		}

		sRes = cService_ProMenu.hAddOne(proMenu);
		return ControllerResult.setServiceResult(sRes, proMenu);
	}

	@RequestMapping(value = "/patchUpdate", method = RequestMethod.POST)
	public @ResponseBody Object patchUpdate(@RequestBody ProMenu proMenu) {
		ServiceResult sRes = null;
		String fUid = proMenu.getMenu_father_uuid();
		ShopManager sm = ShopManagerServiceImpl.getSession_ShopManager(this.request);

		proMenu.setUuid_shop(sm.getUuid_shop());
		if (null != fUid) {
			ProMenu fMenu = cService_ProMenu.getFatherMenu(fUid);
			if (null != fMenu && false == fMenu.getUuid_shop().equals(sm.getUuid_shop())) {
				sRes = new ServiceResult(Status.Error, "父目录" + fUid, "不属于该商铺");
				return ControllerResult.setServiceResult(sRes, null);
			}
		}

		ProMenu old = cService_ProMenu.hfindOne(ProMenu.class, proMenu.getP_uuid_pro_menu());
		if (-1 == proMenu.getMenu_status() && null != old) {
			proMenu.setMenu_status(old.getMenu_status());
		}
		if (-1 == proMenu.getOrder_number() && null != old) {
			proMenu.setOrder_number(old.getOrder_number());
		}

		sRes = cService_ProMenu.hPatchUpdateOne(proMenu, ProMenu.class, proMenu.getP_uuid_pro_menu());
		return ControllerResult.setServiceResult(sRes, proMenu);
	}

	@RequestMapping(value = "/updateOrder", method = RequestMethod.POST)
	public @ResponseBody Object updateOrder(@RequestBody List<String> uuids) {
		ServiceResult sRes = null;
		ShopManager sm = ShopManagerServiceImpl.getSession_ShopManager(this.request);

		sRes = cService_ProMenu.updateOrders(uuids, sm.getUuid_shop());
		List<ProMenu> list = cService_ProMenu.getMenuByShopUuid(uuids, false);
		return ControllerResult.setServiceResult(sRes, list);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody Object delete(@RequestBody ProMenu proMenu) {
		ServiceResult sRes = null;

		ShopManager sm = ShopManagerServiceImpl.getSession_ShopManager(this.request);

		String id = proMenu.getP_uuid_pro_menu();
		if (null != id) {
			ProMenu p = cService_ProMenu.hfindOne(ProMenu.class, id);
			if (null != p && false == p.getUuid_shop().equals(sm.getUuid_shop())) {
				sRes = new ServiceResult(Status.Error, "目录" + id, "不属于该商户，不可删除");
				return ControllerResult.setServiceResult(sRes, null);
			}

			if (0 < cService_ProMenu.getChildrenMenuCount(id)) {
				sRes = new ServiceResult(Status.Warning, "目录" + id, "存在子目录，不可删除");
				return ControllerResult.setServiceResult(sRes, null);
			}
			if (0 < cService_ProMenu.getProCount(id)) {
				sRes = new ServiceResult(Status.Warning, "目录" + id, "存在商品，不可删除");
				return ControllerResult.setServiceResult(sRes, null);
			}
		}

		sRes = cService_ProMenu.hDeleteOne(proMenu);
		return ControllerResult.setServiceResult(sRes, null);
	}

}
