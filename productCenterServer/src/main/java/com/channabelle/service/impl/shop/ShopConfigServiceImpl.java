package com.channabelle.service.impl.shop;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.channabelle.common.exception.ServiceException;
import com.channabelle.model.shop.ShopConfig;
import com.channabelle.service.impl.BaseServiceImpl;
import com.channabelle.service.impl.CommonServiceImpl;

@Service
@Transactional
public class ShopConfigServiceImpl<T> extends BaseServiceImpl<T> {
	Logger log = Logger.getLogger(ShopConfigServiceImpl.class);

	@Autowired
	CommonServiceImpl<ShopConfig> cService_UserConfig;

	public ShopConfigServiceImpl() {
		log.info("UserServiceImpl");
	}

	public String getValueByConfigName(String configName) throws ServiceException {
		String value = null;

		List<Criterion> criterions = new ArrayList<Criterion>();
		criterions.add(Restrictions.eq("config_name", configName));
		ShopConfig c = cService_UserConfig.hfindOne(ShopConfig.class, criterions);

		if (null != c) {
			value = c.getConfig_value();
		}
		return value;
	}

	public String getValueByUuidShopAndConfigName(String uid, String configName) throws ServiceException {
		String value = null;

		List<Criterion> criterions = new ArrayList<Criterion>();
		criterions.add(Restrictions.eq("uuid_shop", uid));
		criterions.add(Restrictions.eq("config_name", configName));
		ShopConfig c = cService_UserConfig.hfindOne(ShopConfig.class, criterions);

		if (null != c) {
			value = c.getConfig_value();
		}
		return value;
	}

	public List<ShopConfig> getConfigByNameAndValue(String configName, String configValue) {
		List<Criterion> criterions = new ArrayList<Criterion>();
		criterions.add(Restrictions.eq("config_name", configName));
		criterions.add(Restrictions.eq("config_value", configValue));
		List<ShopConfig> list = cService_UserConfig.hfindAll(ShopConfig.class, criterions);

		return list;
	}

	public List<String> getShopUuidsByNameAndValue(String configName, String configValue) {
		List<ShopConfig> lConfigs = this.getConfigByNameAndValue(configName, configValue);
		List<String> uuid_shops = new ArrayList<String>();

		if (null != lConfigs && lConfigs.size() > 0) {
			for (int k = 0; k < lConfigs.size(); k++) {
				uuid_shops.add(lConfigs.get(k).getUuid_shop());
			}
		}
		return uuid_shops;
	}

}