package com.channabelle.dao.impl.mybatis;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class mBaseDaoImpl {
	private @Resource(name = "mybatisSqlSessionTemplate")
	SqlSessionTemplate session;

	public SqlSessionTemplate getSession() {
		return session;
	}
}
