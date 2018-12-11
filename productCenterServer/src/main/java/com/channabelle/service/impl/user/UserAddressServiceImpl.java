package com.channabelle.service.impl.user;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.channabelle.common.ServiceResult;
import com.channabelle.model.user.UserAddress;
import com.channabelle.service.impl.BaseServiceImpl;

@Service
@Transactional
public class UserAddressServiceImpl<T> extends BaseServiceImpl<T> {
	Logger log = Logger.getLogger(UserAddressServiceImpl.class);

	public UserAddressServiceImpl() {
		log.info("UserAddressServiceImpl");
	}

	public List<UserAddress> listByUser(String user_uuid) {
		List<Criterion> criterions = new ArrayList<Criterion>();
		criterions.add(Restrictions.eq("uuid_user", user_uuid));

		return (List<UserAddress>) hDao.findAll(UserAddress.class, criterions);
	}

	// TBD...
	public ServiceResult setDefaultAddress(String user_uuid, String address_uuid) {
		return this.doTransaction(new TranscationTask() {
			public void run(Session session) throws Exception {

			}
		});
	}
}