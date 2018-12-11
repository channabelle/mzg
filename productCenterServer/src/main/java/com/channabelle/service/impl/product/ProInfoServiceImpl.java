package com.channabelle.service.impl.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.channabelle.common.ServiceResult;
import com.channabelle.model.product.Menu_ProInfo;
import com.channabelle.model.product.ProInfo;
import com.channabelle.model.product.ProMenu;
import com.channabelle.service.impl.BaseServiceImpl;
import com.channabelle.service.impl.CommonServiceImpl;

@Service
@Transactional
public class ProInfoServiceImpl<T> extends BaseServiceImpl<T> {
	Logger log = Logger.getLogger(ProInfoServiceImpl.class);

	public ProInfoServiceImpl() {
		log.info("ProInfoServiceImpl");
	}

	@Autowired
	CommonServiceImpl<ProInfo> proInfoService;
	@Autowired
	CommonServiceImpl<Menu_ProInfo> menu_ProInfoService;
	@Autowired
	CommonServiceImpl<ProMenu> proMenuService;

	public List<ProInfo> getProInfosByMenuId(String menuId, boolean pro_status) {
		Map<String, Object> map = this.getProInfosByMenuId(menuId, pro_status, 1, Integer.MAX_VALUE);
		List<ProInfo> list = null;
		if (null != map) {
			list = (List<ProInfo>) map.get("list");
		}
		return list;
	}

	public Map<String, Object> getProInfosByMenuId(String menuId, boolean pro_status, int page, int pagePerNumber) {
		Map<String, Object> map = null;

		List<Criterion> criterions = new ArrayList<Criterion>();

		criterions.add(Restrictions.eq("uuid_pro_menu", menuId));
		List<Menu_ProInfo> m_p = menu_ProInfoService.hfindAll(Menu_ProInfo.class, criterions);

		if (null != m_p && 0 < m_p.size()) {
			List<String> ids = new ArrayList<String>();
			for (int k = 0; k < m_p.size(); k++) {
				ids.add(m_p.get(k).getUuid_pro_info());
			}

			List<Criterion> criterionsInfo = new ArrayList<Criterion>();
			criterionsInfo.add(Restrictions.in("p_uuid_pro_info", ids));
			if (true == pro_status) {
				criterionsInfo.add(Restrictions.eq("pro_status", 0));
			}

			List<Order> orders = new ArrayList<Order>();
			orders.add(Order.desc("recommended_rank"));
			orders.add(Order.desc("uTime"));

			map = proInfoService.hfindAll(ProInfo.class, criterionsInfo, orders, page, pagePerNumber);
		}

		return map;
	}

	public List<ProMenu> getMenusByProInfo(ProInfo proInfo) {
		List<ProMenu> l = null;

		List<Criterion> criterionsMenu_ProInfo = new ArrayList<Criterion>();
		criterionsMenu_ProInfo.add(Restrictions.eq("uuid_pro_info", proInfo.getP_uuid_pro_info()));
		List<Menu_ProInfo> mList = menu_ProInfoService.hfindAll(Menu_ProInfo.class, criterionsMenu_ProInfo);

		if (null != mList && mList.size() > 0) {
			List<String> ids = new ArrayList<String>();
			for (int k = 0; k < mList.size(); k++) {
				ids.add(mList.get(k).getUuid_pro_menu());
			}

			List<Criterion> criterionsProMenu = new ArrayList<Criterion>();
			criterionsProMenu.add(Restrictions.in("p_uuid_pro_menu", ids));

			l = proMenuService.hfindAll(ProMenu.class, criterionsProMenu);
		}
		return l;
	}

	public ServiceResult saveProInfoAndMenu(final ProInfo proInfo) {
		ServiceResult sRes = new ServiceResult();

		/*
		 * 有多个数据表保存操作的service，必须调用事务
		 */
		sRes = this.doTransaction(new TranscationTask() {
			public void run(Session session) throws Exception {
				proInfoService.hAddOneInTask(proInfo, session);

				List<Menu_ProInfo> menus_toSave = new ArrayList<Menu_ProInfo>();
				List<ProMenu> proMenus = proInfo.getPro_menus();
				if (null != proMenus) {
					for (int k = 0; k < proMenus.size(); k++) {
						Menu_ProInfo m = new Menu_ProInfo();
						m.setUuid_pro_info(proInfo.getP_uuid_pro_info());
						m.setUuid_pro_menu(proMenus.get(k).getP_uuid_pro_menu());

						menus_toSave.add(m);
					}
				}
				menu_ProInfoService.hAddListInTask(menus_toSave, session);
			}
		});

		return sRes;
	}

	public ServiceResult patchUpdateProInfoAndMenu(final ProInfo proInfo) {
		ServiceResult sRes = new ServiceResult();

		/*
		 * 有多个数据表保存操作的service，必须调用事务
		 */
		sRes = this.doTransaction(new TranscationTask() {
			public void run(Session session) throws Exception {
				// 更新产品信息
				proInfoService.hPatchUpdateOneInTask(proInfo, ProInfo.class, proInfo.getP_uuid_pro_info(), session);

				// 更新目录信息
				List<ProMenu> proMenus = proInfo.getPro_menus();
				if (null != proMenus && proMenus.size() > 0) {
					// 清除原目录对应关系
					List<Criterion> criterions = new ArrayList<Criterion>();
					criterions.add(Restrictions.eq("uuid_pro_info", proInfo.getP_uuid_pro_info()));
					List<Menu_ProInfo> l = menu_ProInfoService.hfindAll(Menu_ProInfo.class, criterions);
					menu_ProInfoService.hDeleteListInTask(l, session);

					// 保存新对应关系
					List<Menu_ProInfo> menus_toSave = new ArrayList<Menu_ProInfo>();
					for (int k = 0; k < proMenus.size(); k++) {
						Menu_ProInfo m = new Menu_ProInfo();
						m.setUuid_pro_info(proInfo.getP_uuid_pro_info());
						m.setUuid_pro_menu(proMenus.get(k).getP_uuid_pro_menu());

						menus_toSave.add(m);
					}
					menu_ProInfoService.hAddListInTask(menus_toSave, session);
				}
			}
		});

		return sRes;
	}

	public ServiceResult deleteProInfoAndMenu(final ProInfo proInfo) {
		ServiceResult sRes = new ServiceResult();

		/*
		 * 有多个数据表保存操作的service，必须调用事务
		 */
		sRes = this.doTransaction(new TranscationTask() {
			@Override
			public void run(Session session) throws Exception {
				// 清除原目录对应关系
				List<Criterion> criterions = new ArrayList<Criterion>();
				criterions.add(Restrictions.eq("uuid_pro_info", proInfo.getP_uuid_pro_info()));
				List<Menu_ProInfo> l = menu_ProInfoService.hfindAll(Menu_ProInfo.class, criterions);
				menu_ProInfoService.hDeleteListInTask(l, session);

				// 清除原目录对应关系
				proInfoService.hDeleteOneInTask(proInfo, session);
			}
		});

		return sRes;
	}

	public void modifyProQuantityInTask(String proUuid, String quantityType, int changeQuantity, Session session) {
		String sql = String.format("update T_ProInfo p set p.%s = p.%s + %d where p.p_uuid_pro_info = '%s';",
				quantityType, quantityType, changeQuantity, proUuid);

		proInfoService.execSQLInTask(sql, session);
	}
}