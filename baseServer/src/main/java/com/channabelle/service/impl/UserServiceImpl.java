package com.channabelle.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.channabelle.common.ServiceResult;
import com.channabelle.common.exception.ServiceException;
import com.channabelle.common.utils.MD5;
import com.channabelle.model.User;

@Service
@Transactional
public class UserServiceImpl<T> extends BaseServiceImpl<T> {
	Logger log = Logger.getLogger(UserServiceImpl.class);

	public UserServiceImpl() {
		log.info("UserServiceImpl");
	}

	public User login(User u, HttpServletRequest req) throws ServiceException {
		User user = this.findUserByAccountAndPassword(u.getAccount(), u.getPassword());

		HttpSession session = req.getSession(true);
		if (null != user) {
			session.setAttribute("user", user);
			session.setMaxInactiveInterval(500);
		}
		return user;
	}

	public void logout(HttpServletRequest req) {
		HttpSession session = req.getSession(true);
		if (null != session) {
			session.setAttribute("user", null);
		}
	}

	public static User hasLogin(HttpServletRequest req) {
		User user = null;
		HttpSession session = req.getSession(false);
		if (null != session) {
			user = (User) session.getAttribute("user");
		}
		return user;
	}

	public ServiceResult register(User u) {
		u.setPassword((new MD5()).getMD5ofStr(u.getPassword()));

		Date time = new Date();
		u.setcTime(time);
		u.setuTime(time);
		return hDao.save((T) u);
	}

	public ServiceResult deleteUser(User u) {
		return hDao.delete((T) u);
	}

	public ServiceResult updateUser(User u) {
		return hDao.patchUpdate((T) u, User.class, u.getUuid_user());
	}

	private User findUserByAccountAndPassword(String account, String password) throws ServiceException {
		List<Criterion> criterions = new ArrayList<Criterion>();
		criterions.add(Restrictions.eq("account", account));
		criterions.add(Restrictions.eq("password", (new MD5()).getMD5ofStr(password)));

		return (User) hDao.findOne(User.class, criterions);
	}
}