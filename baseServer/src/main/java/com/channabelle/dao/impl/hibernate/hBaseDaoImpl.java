package com.channabelle.dao.impl.hibernate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
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

	@Resource(name = "hibernateSqlSessionFactory")
	private SessionFactory sessionFactory;
	private Session session = null;

	private final int BATCH_NUM = 50;

	// @Resource(name = "hibernateTemplate")
	// private HibernateTemplate hTemplate;

	protected Session getCurrentSession() {
		if (null == session) {
			session = sessionFactory.openSession();
		}
		return session;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	protected void closeCurrentSession() {
		if (null != session) {
			session.close();
			session = null;
		}
	}

	public List<T> findAll(Class<?> clazz) {
		Query query = this.getCurrentSession().createQuery("from " + clazz.getSimpleName());
		return query.list();
	}

	public List<T> findAll(Class<?> clazz, List<Criterion> criterions) {
		Criteria cri = this.getCurrentSession().createCriteria(clazz);
		for (int k = 0; k < criterions.size(); k++) {
			cri.add(criterions.get(k));
		}
		return cri.list();
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
		ServiceResult res = null;

		Transaction tx = getCurrentSession().beginTransaction();
		try {
			getCurrentSession().clear();
			getCurrentSession().save(entity);
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

	public ServiceResult batchSave(List<T> list) {
		ServiceResult res = null;

		Transaction tx = getCurrentSession().beginTransaction();
		try {
			getCurrentSession().clear();
			for (int k = 0; k < list.size(); k++) {
				getCurrentSession().save(list.get(k));
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

	public List SQLQuery(String sql) {
		return this.getCurrentSession().createSQLQuery(sql).list();
	}

	private static final HashMap<String, Object> PATCH_UPDATE_MERGE_WHITE_LIST = new HashMap<String, Object>();
	static {
		PATCH_UPDATE_MERGE_WHITE_LIST.put("setuTime", null);
	}
}
