package com.channabelle.service.impl.shop;

import com.channabelle.common.exception.ServiceException;
import com.channabelle.common.utils.JsonDateValueProcessor;
import com.channabelle.common.utils.MD5;
import com.channabelle.common.utils.RedisUtil;
import com.channabelle.model.shop.Shop;
import com.channabelle.model.shop.ShopManager;
import com.channabelle.service.impl.BaseServiceImpl;
import com.channabelle.service.impl.CommonServiceImpl;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ShopManagerServiceImpl<T> extends BaseServiceImpl<T> {
	Logger log = Logger.getLogger(ShopManagerServiceImpl.class);

	@Autowired
	CommonServiceImpl<Shop> shopService;

	public ShopManagerServiceImpl() {
		log.info("UserServiceImpl");
	}

	private void doLogin(ShopManager sm, HttpServletRequest req, boolean fromBrowser) {
		// 区分从浏览器来的还是客户端来的
		if (true == fromBrowser) {
			HttpSession session = req.getSession(true);
			session.setAttribute(this.LOGIN_SESSION_KEY, sm);
			session.setMaxInactiveInterval(24 * 3600);
		} else {
			JSONObject session = new JSONObject();
			session.put(this.LOGIN_SESSION_KEY, JSONObject.fromObject(sm, JsonDateValueProcessor.getJsonConfig()));
			String token = (new RedisUtil()).addValue(session.toString());
			sm.setToken(token);
		}
	}

	public ShopManager loginByAccountAndPassword(ShopManager sm, HttpServletRequest req, boolean fromBrowser)
			throws ServiceException {
		ShopManager m = this.findShopManagerByAccountAndPassword(sm.getAccount(), sm.getPassword());

		if (null != m) {
			doLogin(m, req, fromBrowser);
		}
		return m;
	}

	private ShopManager findShopManagerByAccountAndPassword(String account, String password) throws ServiceException {
		List<Criterion> criterions = new ArrayList<Criterion>();
		criterions.add(Restrictions.eq("account", account));
		criterions.add(Restrictions.eq("password", (new MD5()).getMD5ofStr(password)));

		return (ShopManager) hDao.findOne(ShopManager.class, criterions);
	}

	public static ShopManager getSession_ShopManager(HttpServletRequest req) {
		ShopManager sm = null;
		String token = req.getHeader("token");

		if (null == token) {
			HttpSession session = req.getSession(false);
			if (null != session) {
				Object ob = session.getAttribute(ShopManagerServiceImpl.LOGIN_SESSION_KEY);
				if (ob instanceof ShopManager) {
					sm = (ShopManager) ob;
				}
			}
		} else {
			String tokenValue = (new RedisUtil()).getValue(token);
			JSONObject session = null;

			if (null != tokenValue
					&& null != (session = JSONObject.fromObject(tokenValue, JsonDateValueProcessor.getJsonConfig()))) {
				Object ob = JSONObject.toBean(session.getJSONObject(ShopManagerServiceImpl.LOGIN_SESSION_KEY),
						ShopManager.class);
				if (ob instanceof ShopManager) {
					sm = (ShopManager) ob;
				}
			}
		}
		return sm;
	}
}