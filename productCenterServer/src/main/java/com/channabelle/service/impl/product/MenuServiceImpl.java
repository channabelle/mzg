package com.channabelle.service.impl.product;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.channabelle.common.ServiceResult;
import com.channabelle.model.product.Menu_ProInfo;
import com.channabelle.model.product.ProMenu;
import com.channabelle.service.impl.BaseServiceImpl;

@Service
@Transactional
public class MenuServiceImpl<T> extends BaseServiceImpl<T> {
	Logger log = Logger.getLogger(MenuServiceImpl.class);

	public MenuServiceImpl() {
		log.info("MenuServiceImpl");
	}

	public ProMenu getFatherMenu(String id) {
		return (ProMenu) hDao.findOne(ProMenu.class, id);
	}

	public List<ProMenu> getMenuByShopUuid(List<String> uuids, boolean menu_status) {
		List<Criterion> criterions = new ArrayList<Criterion>();
		List<Order> orders = new ArrayList<Order>();

		if (null != uuids && 0 < uuids.size()) {
			criterions.add(Restrictions.in("uuid_shop", uuids));
			orders.add(Order.desc("order_number"));
		}

		if (true == menu_status) {
			criterions.add(Restrictions.eq("menu_status", 0));
		}

		return (List<ProMenu>) hDao.findAll(ProMenu.class, criterions, orders);
	}

	public int getChildrenMenuCount(String id) {
		List<Criterion> criterions = new ArrayList<Criterion>();
		criterions.add(Restrictions.eq("menu_father_uuid", id));

		return hDao.count(ProMenu.class, criterions);
	}

	public int getProCount(String menu_id) {
		List<Criterion> criterions = new ArrayList<Criterion>();
		criterions.add(Restrictions.eq("uuid_pro_menu", menu_id));

		return hDao.count(Menu_ProInfo.class, criterions);
	}

	public ServiceResult updateOrders(List<String> menu_uuids, String shop_uuid) {
		int length = menu_uuids.size();
		String whenThenStr = "";

		for (int m = 0; m < length; m++) {
			String ss = String.format("WHEN '%s' THEN %d ", menu_uuids.get(m), length - m);
			whenThenStr += ss;
		}

		String sql = String.format("UPDATE T_ProMenu m " + "set m.order_number = CASE m.p_uuid_pro_menu %s END "
				+ "WHERE m.uuid_shop = '%s'", whenThenStr, shop_uuid);

		return hDao.execQuery(sql);
	}
}