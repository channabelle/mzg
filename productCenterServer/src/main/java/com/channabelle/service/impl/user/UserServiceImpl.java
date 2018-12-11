package com.channabelle.service.impl.user;

import com.channabelle.common.ServiceResult;
import com.channabelle.common.exception.ServiceException;
import com.channabelle.common.utils.JsonDateValueProcessor;
import com.channabelle.common.utils.MD5;
import com.channabelle.common.utils.RedisUtil;
import com.channabelle.model.shop.Shop;
import com.channabelle.model.user.User;
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
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl<T> extends BaseServiceImpl<T> {
    Logger log = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    CommonServiceImpl<Shop> shopService;

    public UserServiceImpl() {
        log.info("UserServiceImpl");
    }

    private void doLogin(User user, HttpServletRequest req, boolean fromBrowser) {

        // 区分从浏览器来的还是客户端来的
        if(fromBrowser) {
            HttpSession session = req.getSession(true);
            session.setAttribute(this.LOGIN_SESSION_KEY, user);

            session.setMaxInactiveInterval(24 * 3600);
        } else {
            JSONObject session = new JSONObject();
            session.put(this.LOGIN_SESSION_KEY, JSONObject.fromObject(user, JsonDateValueProcessor.getJsonConfig()));
            String token = (new RedisUtil()).addValue(session.toString());
            user.setToken(token);
        }
    }

    public User loginByAccountAndPassword(User u, HttpServletRequest req, boolean fromBrowser) throws ServiceException {
        User user = this.findUserByAccountAndPassword(u.getAccount(), u.getPassword());

        if(null != user) {
            doLogin(user, req, fromBrowser);
        }
        return user;
    }

    public User loginByUser(User user, HttpServletRequest req, boolean fromBrowser) {
        doLogin(user, req, fromBrowser);

        return user;
    }


    public static User getSession_User(HttpServletRequest req) {
        User user = null;
        String token = req.getHeader("token");

        if(null == token) {
            HttpSession session = req.getSession(false);
            if(null != session) {
                Object ob = session.getAttribute(UserServiceImpl.LOGIN_SESSION_KEY);
                if(ob instanceof User) {
                    user = (User) ob;
                }
            }
        } else {
            String tokenValue = (new RedisUtil()).getValue(token);
            JSONObject session = null;

            if(null != tokenValue && null != (session = JSONObject.fromObject(tokenValue, JsonDateValueProcessor.getJsonConfig()))) {
                Object ob = JSONObject.toBean(session.getJSONObject(UserServiceImpl.LOGIN_SESSION_KEY), User.class);
                if(ob instanceof User) {
                    user = (User) ob;
                }
            }
        }
        return user;
    }

    private void doRegister(User u) {
        Date time = new Date();
        u.setcTime(time);
        u.setuTime(time);
        hDao.save((T) u);
        return;
    }

    public User registerByPassword(User u) {
        u.setPassword((new MD5()).getMD5ofStr(u.getPassword()));
        doRegister(u);

        return u;
    }

    public User registerByMiniProgram(String appid, String openid) {
        User u = new User();
        u.setMiniProgram_appid(appid);
        u.setMiniProgram_openid(openid);
        u.setAccount(appid + "-" + openid);

        doRegister(u);
        return u;
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

    public User findUserByAccount(String account) throws ServiceException {
        List<Criterion> criterions = new ArrayList<Criterion>();
        criterions.add(Restrictions.eq("account", account));

        return (User) hDao.findOne(User.class, criterions);
    }

    public User findUserByMiniProgram(String appid, String openid) throws ServiceException {
        List<Criterion> criterions = new ArrayList<Criterion>();
        criterions.add(Restrictions.eq("miniProgram_appid", appid));
        criterions.add(Restrictions.eq("miniProgram_openid", openid));

        return (User) hDao.findOne(User.class, criterions);
    }
}