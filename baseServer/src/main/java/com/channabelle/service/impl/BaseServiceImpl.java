package com.channabelle.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.channabelle.common.ServiceResult;
import com.channabelle.common.exception.ServiceException;
import com.channabelle.dao.impl.hibernate.hBaseDaoImpl;

@Service
@Transactional
public class BaseServiceImpl<T> {
	private Logger log = Logger.getLogger(BaseServiceImpl.class);

	@Autowired
	@Resource(name = "hBaseDaoImpl")
	hBaseDaoImpl<T> hDao;

	public List<Object> execSQL(String sql) {
		return hDao.SQLQuery(sql);
	}

	public List<T> hfindAll(Class<T> clazz) {
		List<T> list = hDao.findAll(clazz);
		return list;
	}

	public T hfindOne(Class<T> clazz, String id) {
		return hDao.findOne(clazz, id);
	}

	public T hfindOne(Class<T> clazz, List<Criterion> criterions) throws ServiceException {
		return hDao.findOne(clazz, criterions);
	}

	public ServiceResult hAddOne(T one) {
		hDao.save(one);
		return hDao.save(one);
	}

	public ServiceResult hUpdateOne(T one) {
		return hDao.update(one);
	}

	public ServiceResult hPatchUpdateOne(T one, Class<T> clazz, String id) {
		return hDao.patchUpdate(one, clazz, id);
	}

	public ServiceResult hDeleteOne(T one) {
		return hDao.delete(one);
	}

	public ServiceResult hAddList(List<T> list) {
		return hDao.batchSave(list);
	}

	public ServiceResult hDeleteList(List<T> list) {
		return hDao.batchDelete(list);
	}
}