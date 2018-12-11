package com.channabelle.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.channabelle.common.ServiceResult;
import com.channabelle.common.exception.SQLException;
import com.channabelle.common.exception.ServiceException;
import com.channabelle.common.utils.RedisUtil;
import com.channabelle.dao.impl.hibernate.hBaseDaoImpl;
import com.channabelle.model.HttpRecord;

@Service
@Transactional
public class BaseServiceImpl<T> {
	private Logger log = Logger.getLogger(BaseServiceImpl.class);
	public static final String LOGIN_SESSION_KEY = "loginSession";

	@Autowired
	@Resource(name = "hBaseDaoImpl")
	protected hBaseDaoImpl<T> hDao;

	public List<Object> querySQL(String sql) {
		return hDao.sqlQuery(sql);
	}

	public ServiceResult execSQL(String sql) {
		return hDao.execQuery(sql);
	}

	public void execSQLInTask(String sql, Session session) {
		hDao.execQueryInTask(sql, session);
	}

	public int hCount(Class<T> clazz, List<Criterion> criterions) {
		return hDao.count(clazz, criterions);
	}

	public List<T> hfindAll(Class<T> clazz) {
		List<T> list = hDao.findAll(clazz);
		return list;
	}

	public Map<String, Object> hfindAll(Class<T> clazz, List<Criterion> criterions, int current_page, int number) {
		return this.hfindAll(clazz, criterions, null, current_page, number);
	}

	public Map<String, Object> hfindAll(Class<T> clazz, List<Criterion> criterions, List<Order> orders,
			int current_page, int number) {
		Map<String, Object> map = hDao.findAll(clazz, criterions, orders, current_page, number);
		return map;
	}

	public List<T> hfindAll(Class<T> clazz, List<Criterion> criterions) {
		List<T> list = hDao.findAll(clazz, criterions);
		return list;
	}

	public T hfindOne(Class<T> clazz, String id) {
		return hDao.findOne(clazz, id);
	}

	public T hfindOne(Class<T> clazz, List<Criterion> criterions) throws ServiceException {
		return hDao.findOne(clazz, criterions);
	}

	public ServiceResult hAddOne(T one) {
		return hDao.save(one);
	}

	public ServiceResult hAddOne(T one, String sessionName) {
		return hDao.save(one, sessionName);
	}

	public void hAddOneInTask(T one, Session session) throws Exception {
		hDao.saveInTask(one, session);
	}

	public ServiceResult hUpdateOne(T one) {
		return hDao.update(one);
	}

	public void hUpdateOneInTask(T one, Session session) {
		hDao.updateInTask(one, session);
	}

	public ServiceResult hPatchUpdateOne(T one, Class<T> clazz, String id) {
		return hDao.patchUpdate(one, clazz, id);
	}

	public void hPatchUpdateOneInTask(T one, Class<T> clazz, String id, Session session) throws Exception {
		hDao.patchUpdateInTask(one, clazz, id, session);
	}

	public ServiceResult hDeleteOne(T one) {
		return hDao.delete(one);
	}

	public void hDeleteOneInTask(T one, Session session) {
		hDao.deleteInTask(one, session);
	}

	public ServiceResult hAddList(List<T> list) {
		return hDao.batchSave(list, "productCenter");
	}

	public ServiceResult hAddList(List<T> list, String sessionName) {
		return hDao.batchSave(list, sessionName);
	}

	public void hAddListInTask(List<T> list, Session session) {
		hDao.batchSaveInTask(list, session);
	}

	public ServiceResult hDeleteList(List<T> list) {
		return hDao.batchDelete(list);
	}

	public void hDeleteListInTask(List<T> list, Session session) {
		hDao.batchDeleteInTask(list, session);
	}

	public Session getSession() {
		return hDao.getCurrentSession();
	}

	public void closeSession() {
		hDao.closeCurrentSession();
	}

	public ServiceResult doTransaction(TranscationTask task) {
		ServiceResult sRes = new ServiceResult();
		Session session = this.getSession();
		Transaction tx = session.beginTransaction();
		try {
			task.run(session);
			tx.commit();
		} catch (Exception error) {
			sRes = SQLException.handle(error);
			tx.rollback();
		} finally {
			this.closeSession();
		}

		return sRes;
	}

	public abstract interface TranscationTask {
		public abstract void run(Session session) throws Exception;
	}

	public void logout(HttpServletRequest req, boolean fromBrowser) {
		if (true == fromBrowser) {
			HttpSession session = req.getSession(true);
			if (null != session) {
				session.setAttribute(this.LOGIN_SESSION_KEY, null);
			}
		} else {
			String token = req.getHeader("token");
			(new RedisUtil()).delKey(token);
		}
	}

	public static HttpRecord getHttpRecord(HttpServletRequest req) {
		HttpRecord record = null;
		Object r = req.getAttribute("HttpRecord");
		if (r instanceof HttpRecord && null != r) {
			record = (HttpRecord) r;
		}
		return record;
	}
}