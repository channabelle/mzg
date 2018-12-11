package com.channabelle.service.impl.shoppingCar;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.channabelle.common.ServiceResult;
import com.channabelle.common.exception.ServiceException;
import com.channabelle.model.user.ShoppingCar;
import com.channabelle.service.impl.BaseServiceImpl;
import com.channabelle.service.impl.CommonServiceImpl;

@Service
@Transactional
public class ShoppingCarServiceImpl<T> extends BaseServiceImpl<T> {
	Logger log = Logger.getLogger(ShoppingCarServiceImpl.class);

	@Autowired
	CommonServiceImpl<ShoppingCar> shoppingCarService;

	public ShoppingCarServiceImpl() {
		log.info("ShoppingCarServiceImpl");
	}

	public ServiceResult handleShoppingCar(List<ShoppingCar> list, String action) {
		ServiceResult result = null;
		String sql = null;
		String whereStr = null;
		if (null != list) {
			for (int m = 0; m < list.size(); m++) {
				if (0 == m) {
					whereStr = "('" + list.get(m).getP_uuid_shopping_car() + "'";
				} else {
					whereStr = whereStr + ", '" + list.get(m).getP_uuid_shopping_car() + "'";
				}
			}
			if (null != whereStr) {
				whereStr = whereStr + ")";
			}
		}

		if (null != whereStr) {
			if ("check".equals(action)) {
				sql = "update T_ShoppingCar sc set sc.checked = 'checked' where sc.p_uuid_shopping_car in " + whereStr;
			} else if ("uncheck".equals(action)) {
				sql = "update T_ShoppingCar sc set sc.checked = '' where p_uuid_shopping_car in " + whereStr;
			}
			result = shoppingCarService.execSQL(sql);
		}

		return result;
	}

	public List<ShoppingCar> listByUserUuidAndProInfo(String uuid, String uuid_pro_info) {
		List<Criterion> criterions = new ArrayList<Criterion>();
		criterions.add(Restrictions.eq("uuid_user", uuid));
		if (null != uuid_pro_info) {
			criterions.add(Restrictions.eq("uuid_pro_info", uuid_pro_info));
		}
		return shoppingCarService.hfindAll(ShoppingCar.class, criterions);
	}

	public Map<String, Object> getSummaryByShoppingCarList(List<ShoppingCar> list) {
		Map<String, Object> map = new HashMap<String, Object>();
		int totalNum = 0;
		double totalMoney = 0;
		double totalMoney_withDiscount = 0;

		if (null != list) {
			for (int m = 0; m < list.size(); m++) {
				if ("checked".equals(list.get(m).getChecked())) {
					totalNum = totalNum + list.get(m).getAmount();
					totalMoney = totalMoney + list.get(m).getAmount() * list.get(m).getProInfo().getPro_price();
					totalMoney_withDiscount = totalMoney_withDiscount
							+ list.get(m).getAmount() * list.get(m).getProInfo().getPro_price_with_discount();
				}
			}
		}

		map.put("totalNum", totalNum);
		map.put("totalMoney", (new BigDecimal(totalMoney)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		map.put("totalMoney_withDiscount",
				(new BigDecimal(totalMoney_withDiscount)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
		return map;
	}

	public ShoppingCar getByUserUuidAndProInfo(String uuid, String pro_infoID) throws ServiceException {
		List<Criterion> criterions = new ArrayList<Criterion>();
		criterions.add(Restrictions.eq("uuid_user", uuid));
		criterions.add(Restrictions.eq("uuid_pro_info", pro_infoID));
		return shoppingCarService.hfindOne(ShoppingCar.class, criterions);
	}

	public List<ShoppingCar> getByUserUuidAndShoppingCarUuids(String user_uuid, List<String> uuids) {
		List<Criterion> criterions = new ArrayList<Criterion>();
		criterions.add(Restrictions.eq("uuid_user", user_uuid));
		criterions.add(Restrictions.in("p_uuid_shopping_car", uuids));
		return shoppingCarService.hfindAll(ShoppingCar.class, criterions);
	}
}