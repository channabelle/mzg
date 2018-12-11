package com.channabelle.dao.impl.hibernate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.channabelle.common.ServiceResult;
import com.channabelle.common.exception.SQLException;
import com.channabelle.common.exception.ServiceException;
import com.channabelle.common.utils.MyReflect;

@Transactional
@Repository(value = "hBaseDaoImpl")
public class hBaseDaoImpl<T> {
	Logger log = Logger.getLogger(hBaseDaoImpl.class);

	@Resource(name = "hibernateSqlSessionFactory_productCenter")
	private SessionFactory sessionFactory_productCenter;
	private Session session_productCenter = null;

	@Resource(name = "hibernateSqlSessionFactory_houseAgent")
	private SessionFactory sessionFactory_houseAgent;
	private Session session_houseAgent = null;

	private final int BATCH_NUM = 50;

	public Session getCurrentSession(String sessionName) {
		if ("productCenter".equals(sessionName)) {
			if (null == this.session_productCenter) {

				this.session_productCenter = this.sessionFactory_productCenter.openSession();
			}
			return this.session_productCenter;
		} else if ("houseAgent".equals(sessionName)) {
			if (null == this.session_houseAgent) {

				this.session_houseAgent = this.sessionFactory_houseAgent.openSession();
			}
			return this.session_houseAgent;
		}
		return null;
	}

	public Session getCurrentSession() {
		return this.getCurrentSession("productCenter");
	}

	public void closeCurrentSession(String sessionName) {
		if ("productCenter".equals(sessionName)) {
			if (null != this.session_productCenter) {
				this.session_productCenter.close();
				this.session_productCenter = null;
			}
		} else if ("houseAgent".equals(sessionName)) {
			if (null == this.session_houseAgent) {
				this.session_houseAgent.close();
				this.session_houseAgent = null;
			}
		}
	}

	public void closeCurrentSession() {
		this.closeCurrentSession("productCenter");
	}

	public List<T> findAll(Class<?> clazz) {
		Query query = this.getCurrentSession().createQuery("from " + clazz.getSimpleName());
		return query.list();
	}

	public List<T> findAll(Class<?> clazz, List<Criterion> criterions) {
		return this.findAll(clazz, criterions, null);
	}

	public List<T> findAll(Class<?> clazz, List<Criterion> criterions, List<Order> orders) {
		Map<String, Object> map = this.findAll(clazz, criterions, orders, 1, Integer.MAX_VALUE);
		return (List<T>) map.get("list");
	}

	public Map<String, Object> findAll(Class<?> clazz, List<Criterion> criterions, List<Order> orders, int current_page,
			int number) {
		Criteria cri = this.getCurrentSession().createCriteria(clazz);
		if (null != criterions) {
			for (int k = 0; k < criterions.size(); k++) {
				cri.add(criterions.get(k));
			}
		}

		if (null != orders) {
			for (int n = 0; n < orders.size(); n++) {
				cri.addOrder(orders.get(n));
			}
		}

		int count = this.count(clazz, criterions);
		cri.setFirstResult((current_page - 1) * number);
		cri.setMaxResults(number);
		List<T> list = cri.list();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("count", count);
		map.put("total_page", (0 == count % number) ? count / number : 1 + (count / number));
		map.put("current_page", current_page);
		map.put("list", list);
		return map;
	}

	public int count(Class<?> clazz, List<Criterion> criterions) {
		Criteria cri = this.getCurrentSession().createCriteria(clazz);
		if (null != criterions) {
			for (int k = 0; k < criterions.size(); k++) {
				cri.add(criterions.get(k));
			}
		}

		cri.setProjection(Projections.rowCount());
		int count = (Integer) Integer.parseInt(cri.uniqueResult().toString());

		return count;
	}

	public T findOne(Class<?> clazz, Serializable id) {

		return (T) this.getCurrentSession().get(clazz, id);
	}

	public T findOne(Class<?> clazz, List<Criterion> criterions) throws ServiceException {
		Criteria cri = this.getCurrentSession().createCriteria(clazz);
		for (int k = 0; k < criterions.size(); k++) {
			cri.add(criterions.get(k));
		}
		List<T> list = cri.list();

		if (null == list || 0 == list.size()) {
			return null;
		} else {
			if (1 == list.size()) {
				return list.get(0);
			} else {
				throw new ServiceException("No more than 1 result is expected");
			}
		}
	}

	public ServiceResult save(T entity) {
		return save(entity, "productCenter");
	}

	public ServiceResult save(T entity, String sessionName) {
		ServiceResult res = null;

		Transaction tx = getCurrentSession(sessionName).beginTransaction();
		try {
			getCurrentSession(sessionName).clear();
			getCurrentSession(sessionName).save(entity);
			getCurrentSession(sessionName).flush();
			tx.commit();
			res = new ServiceResult();
		} catch (Exception error) {
			res = SQLException.handle(error);
			tx.rollback();
		} finally {
			this.closeCurrentSession(sessionName);
		}
		return res;
	}

	public void saveInTask(T entity, Session session) {
		session.clear();
		session.save(entity);
		session.flush();
	}

	public ServiceResult update(T entity) {
		ServiceResult res = null;

		Transaction tx = getCurrentSession().beginTransaction();
		try {
			getCurrentSession().clear();
			getCurrentSession().update(entity);
			getCurrentSession().flush();
			tx.commit();
			res = new ServiceResult();
		} catch (Exception error) {
			res = SQLException.handle(error);
			tx.rollback();
		} finally {
			this.closeCurrentSession();
		}
		return res;
	}

	public void updateInTask(T entity, Session session) {
		session.clear();
		session.update(entity);
		session.flush();
	}

	public ServiceResult patchUpdate(T entity, Class<?> clazz, Serializable id) {
		ServiceResult res = null;
		T oldEntity = this.findOne(clazz, id);

		Transaction tx = getCurrentSession().beginTransaction();
		try {
			getCurrentSession().clear();

			/*
			 * some fields need update even if they are null
			 */
			if (null != oldEntity) {
				MyReflect<T> myReflect = new MyReflect();
				entity = myReflect.merge(entity, oldEntity, clazz, PATCH_UPDATE_MERGE_WHITE_LIST);
			}

			getCurrentSession().update(entity);
			getCurrentSession().flush();
			tx.commit();
			res = new ServiceResult();
		} catch (Exception error) {
			res = SQLException.handle(error);
			tx.rollback();
		} finally {
			this.closeCurrentSession();
		}
		return res;
	}

	public void patchUpdateInTask(T entity, Class<?> clazz, Serializable id, Session session) throws Exception {
		T oldEntity = this.findOne(clazz, id);
		session.clear();
		/*
		 * some fields need update even if they are null
		 */
		if (null != oldEntity) {
			MyReflect<T> myReflect = new MyReflect();
			entity = myReflect.merge(entity, oldEntity, clazz, PATCH_UPDATE_MERGE_WHITE_LIST);
		}

		session.update(entity);
		session.flush();
	}

	public ServiceResult delete(T entity) {
		ServiceResult res = null;

		Transaction tx = getCurrentSession().beginTransaction();
		try {
			getCurrentSession().clear();
			getCurrentSession().delete(entity);
			getCurrentSession().flush();
			tx.commit();
			res = new ServiceResult();
		} catch (Exception error) {
			res = SQLException.handle(error);
			tx.rollback();
		} finally {
			this.closeCurrentSession();
		}
		return res;
	}

	public void deleteInTask(T entity, Session session) {
		session.clear();
		session.delete(entity);
		session.flush();
	}

	public ServiceResult batchSave(List<T> list, String sessionName) {
		ServiceResult res = null;

		Transaction tx = getCurrentSession(sessionName).beginTransaction();
		try {
			getCurrentSession(sessionName).clear();
			for (int k = 0; k < list.size(); k++) {
				getCurrentSession(sessionName).save(list.get(k));
				if (0 == k % BATCH_NUM) {
					getCurrentSession(sessionName).flush();
					getCurrentSession(sessionName).clear();
				}
			}
			getCurrentSession(sessionName).flush();
			tx.commit();
			res = new ServiceResult();
		} catch (Exception error) {
			res = SQLException.handle(error);
			tx.rollback();
		} finally {
			this.closeCurrentSession(sessionName);
		}
		return res;
	}

	public void batchSaveInTask(List<T> list, Session session) {
		session.clear();
		for (int k = 0; k < list.size(); k++) {
			session.save(list.get(k));
			if (0 == k % BATCH_NUM) {
				session.flush();
				session.clear();
			}
		}
		session.flush();
	}

	public ServiceResult batchDelete(List<T> list) {
		ServiceResult res = null;

		Transaction tx = getCurrentSession().beginTransaction();
		try {
			getCurrentSession().clear();
			for (int k = 0; k < list.size(); k++) {
				getCurrentSession().delete(list.get(k));
				if (0 == k % BATCH_NUM) {
					getCurrentSession().flush();
					getCurrentSession().clear();
				}
			}
			getCurrentSession().flush();
			tx.commit();
			res = new ServiceResult();
		} catch (Exception error) {
			res = SQLException.handle(error);
			tx.rollback();
		} finally {
			this.closeCurrentSession();
		}
		return res;
	}

	public void batchDeleteInTask(List<T> list, Session session) {
		session.clear();
		for (int k = 0; k < list.size(); k++) {
			session.delete(list.get(k));
			if (0 == k % BATCH_NUM) {
				session.flush();
				session.clear();
			}
		}
		session.flush();
	}

	public List sqlQuery(String sql) {
		return this.getCurrentSession().createSQLQuery(sql).list();
	}

	public ServiceResult execQuery(String sql) {
		log.info("execQuery: " + sql);

		ServiceResult res = null;
		Transaction tx = getCurrentSession().beginTransaction();
		try {
			getCurrentSession().clear();
			getCurrentSession().createSQLQuery(sql).executeUpdate();
			getCurrentSession().flush();
			tx.commit();
			res = new ServiceResult();
		} catch (Exception error) {
			res = SQLException.handle(error);
			tx.rollback();
		} finally {
			this.closeCurrentSession();
		}
		return res;
	}

	public void execQueryInTask(String sql, Session session) {
		log.info("execQuery: " + sql);

		session.clear();
		session.createSQLQuery(sql).executeUpdate();
		session.flush();
	}

	private static final HashMap<String, Object> PATCH_UPDATE_MERGE_WHITE_LIST = new HashMap<String, Object>();

	static {
		PATCH_UPDATE_MERGE_WHITE_LIST.put("setuTime", null);
	}
}
