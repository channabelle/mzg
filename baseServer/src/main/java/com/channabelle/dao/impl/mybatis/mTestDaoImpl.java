package com.channabelle.dao.impl.mybatis;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class mTestDaoImpl extends mBaseDaoImpl {
	Logger log = Logger.getLogger(mTestDaoImpl.class);

	public Object findOne() {
		log.info("findOne");
		return this.getSession().selectOne("AppleMapper.findOne");
	}
}
