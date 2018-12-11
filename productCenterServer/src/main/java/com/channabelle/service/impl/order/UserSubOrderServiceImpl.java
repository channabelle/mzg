package com.channabelle.service.impl.order;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.channabelle.model.order.UserSubOrder;
import com.channabelle.service.impl.BaseServiceImpl;

@Service
@Transactional
public class UserSubOrderServiceImpl<T> extends BaseServiceImpl<T> {
	Logger log = Logger.getLogger(UserSubOrderServiceImpl.class);

	public UserSubOrderServiceImpl() {
		log.info("UserSubOrderServiceImpl");
	}

	public Map<String, Object> listByShopUuid(String shopUuid, int cPage, int perNumber) {
		List<Criterion> criterions = new ArrayList<Criterion>();
		criterions.add(Restrictions.eq("uuid_shop", shopUuid));

		List<Order> orders = new ArrayList<Order>();
		orders.add(Order.desc("cTime"));

		return hDao.findAll(UserSubOrder.class, criterions, orders, cPage, perNumber);
	}
}