package com.channabelle.controller.product.b;

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
import com.channabelle.model.product.ProDetail;
import com.channabelle.model.product.ProInfo;
import com.channabelle.model.shop.ShopManager;
import com.channabelle.service.impl.product.ProDetailServiceImpl;
import com.channabelle.service.impl.product.ProInfoServiceImpl;
import com.channabelle.service.impl.shop.ShopManagerServiceImpl;

@Controller
@RequestMapping("/b/product/proInfo")
public class BProInfoController extends BaseController {
	private Logger log = Logger.getLogger(BProInfoController.class);

	@Autowired
	ProInfoServiceImpl<ProInfo> proInfoService;

	@Autowired
	ProDetailServiceImpl<ProDetail> proDetailService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public @ResponseBody Object info(@RequestParam(value = "pid", required = true) String pid) throws Exception {
		ShopManager sm = ShopManagerServiceImpl.getSession_ShopManager(this.request);

		List<Criterion> criterions = new ArrayList<Criterion>();
		criterions.add(Restrictions.eq("uuid_shop", sm.getUuid_shop()));
		criterions.add(Restrictions.eq("p_uuid_pro_info", pid));

		ProInfo pro = proInfoService.hfindOne(ProInfo.class, criterions);
		if (null != pro) {
			pro.setPro_menus(proInfoService.getMenusByProInfo(pro));
			pro.setPro_detail(proDetailService.getProDetailByProInfoId(pro.getP_uuid_pro_info()));
		}

		return ControllerResult.success(pro);
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody Object list(@RequestParam(value = "menuId", required = false) String menuId,
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pagePerNumber", required = true) int pagePerNumber,
			@RequestParam(value = "proName", required = false) String proName,
			@RequestParam(value = "cTime_start", required = false) String cTime_start,
			@RequestParam(value = "cTime_end", required = false) String cTime_end) throws Exception {

		List<ProInfo> list = null;
		ShopManager sm = ShopManagerServiceImpl.getSession_ShopManager(this.request);

		List<Criterion> criterions = new ArrayList<Criterion>();
		criterions.add(Restrictions.eq("uuid_shop", sm.getUuid_shop()));

		if (null != proName && 0 < proName.length()) {
			criterions.add(Restrictions.or(Restrictions.ilike("pro_title_short", proName, MatchMode.ANYWHERE),
					Restrictions.ilike("pro_title_full", proName, MatchMode.ANYWHERE)));
		}
		if (null != cTime_start && 0 < cTime_start.length()) {
			criterions.add(Restrictions.ge("cTime", (new SimpleDateFormat("yyyy-MM-dd")).parse(cTime_start)));
		}
		if (null != cTime_end && 0 < cTime_end.length()) {
			criterions.add(Restrictions.le("cTime", (new SimpleDateFormat("yyyy-MM-dd")).parse(cTime_end)));
		}

		List<Order> orders = new ArrayList<Order>();
		orders.add(Order.desc("recommended_rank"));
		orders.add(Order.desc("uTime"));

		if (null == menuId) {
			Map<String, Object> map = proInfoService.hfindAll(ProInfo.class, criterions, orders, page, pagePerNumber);
			return ControllerResult.success(map);
		} else {
			list = proInfoService.getProInfosByMenuId(menuId, false);
			return ControllerResult.success(list);
		}
	}

	@RequestMapping(value = "/patchUpdate", method = RequestMethod.POST)
	public @ResponseBody Object patchUpdate(@RequestBody ProInfo proInfo) {
		ServiceResult sRes = proInfoService.patchUpdateProInfoAndMenu(proInfo);
		return ControllerResult.setServiceResult(sRes, proInfo);
	}

	@RequestMapping(value = "/orderUpdate/{uuid}/{order}", method = RequestMethod.GET)
	public @ResponseBody Object orderUpdate(@PathVariable String uuid, @PathVariable int order) {
		ShopManager sm = ShopManagerServiceImpl.getSession_ShopManager(this.request);
		ProInfo pro = proInfoService.hfindOne(ProInfo.class, uuid);
		ServiceResult sRes = null;

		if (null != pro) {
			if (false == pro.getUuid_shop().equals(sm.getUuid_shop())) {
				sRes = new ServiceResult(Status.Error, "商品" + uuid, "不属于该商户");
				return ControllerResult.setServiceResult(sRes, null);
			}

			pro.setRecommended_rank(order);
		}

		sRes = proInfoService.patchUpdateProInfoAndMenu(pro);
		return ControllerResult.setServiceResult(sRes, pro);
	}

	@RequestMapping(value = "/statusUpdate/{uuid}/{status}", method = RequestMethod.GET)
	public @ResponseBody Object statusUpdate(@PathVariable String uuid, @PathVariable int status) {
		ShopManager sm = ShopManagerServiceImpl.getSession_ShopManager(this.request);
		ProInfo pro = proInfoService.hfindOne(ProInfo.class, uuid);
		ServiceResult sRes = null;

		if (null != pro) {
			if (false == pro.getUuid_shop().equals(sm.getUuid_shop())) {
				sRes = new ServiceResult(Status.Error, "商品" + uuid, "不属于该商户");
				return ControllerResult.setServiceResult(sRes, null);
			}

			pro.setPro_status(status);
		}

		sRes = proInfoService.patchUpdateProInfoAndMenu(pro);
		return ControllerResult.setServiceResult(sRes, pro);
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public @ResponseBody Object create(@RequestBody ProInfo proInfo) throws Exception {
		ShopManager sm = ShopManagerServiceImpl.getSession_ShopManager(this.request);
		proInfo.setUuid_shop(sm.getUuid_shop());

		ServiceResult sRes = proInfoService.saveProInfoAndMenu(proInfo);
		return ControllerResult.setServiceResult(sRes, proInfo);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody Object delete(@RequestBody ProInfo proInfo) throws Exception {
		ServiceResult sRes = proInfoService.deleteProInfoAndMenu(proInfo);
		return ControllerResult.setServiceResult(sRes, proInfo);
	}
}
