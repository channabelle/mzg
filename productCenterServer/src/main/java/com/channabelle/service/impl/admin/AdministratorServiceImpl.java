package com.channabelle.service.impl.admin;

import com.channabelle.common.exception.ServiceException;
import com.channabelle.common.utils.JsonDateValueProcessor;
import com.channabelle.common.utils.MD5;
import com.channabelle.common.utils.RedisUtil;
import com.channabelle.model.admin.Administrator;
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
import java.util.List;

@Service
@Transactional
public class AdministratorServiceImpl<T> extends BaseServiceImpl<T> {
    Logger log = Logger.getLogger(AdministratorServiceImpl.class);

    @Autowired
    CommonServiceImpl<Administrator> service;

    public AdministratorServiceImpl() {
        log.info("AdministratorServiceImpl");
    }

    public static Administrator getSession_Administrator(HttpServletRequest req) {
        Administrator u = null;
        String token = req.getHeader("token");

        if(null == token) {
            HttpSession session = req.getSession(false);
            if(null != session) {
                Object ob = session.getAttribute(AdministratorServiceImpl.LOGIN_SESSION_KEY);
                if(ob instanceof Administrator) {
                    u = (Administrator) ob;
                }
            }
        } else {
            String tokenValue = (new RedisUtil()).getValue(token);
            JSONObject session = null;
            if(null != tokenValue) {
                session = JSONObject.fromObject(tokenValue, JsonDateValueProcessor.getJsonConfig());
            }

            if(null != session) {
                Object ob = JSONObject.toBean(session.getJSONObject(AdministratorServiceImpl.LOGIN_SESSION_KEY), Administrator.class);
                if(ob instanceof Administrator) {
                    u = (Administrator) ob;
                }
            }
        }
        return u;
    }

    private void doLogin(Administrator u, HttpServletRequest req, boolean fromBrowser) {
        // 区分从浏览器来的还是客户端来的
        if(true == fromBrowser) {
            HttpSession session = req.getSession(true);
            session.setAttribute(this.LOGIN_SESSION_KEY, u);
            session.setMaxInactiveInterval(24 * 3600);
        } else {
            JSONObject session = new JSONObject();
            session.put(this.LOGIN_SESSION_KEY, JSONObject.fromObject(u, JsonDateValueProcessor.getJsonConfig()));
            String token = (new RedisUtil()).addValue(session.toString());
            u.setToken(token);
        }
    }

    public Administrator loginByAccountAndPassword(Administrator u, HttpServletRequest req, boolean fromBrowser)
            throws ServiceException {
        Administrator m = this.findAdministratorByAccountAndPassword(u.getAccount(), u.getPassword());

        if(null != m) {
            doLogin(m, req, fromBrowser);
        }
        return m;
    }

    private Administrator findAdministratorByAccountAndPassword(String account, String password)
            throws ServiceException {
        List<Criterion> criterions = new ArrayList<Criterion>();
        criterions.add(Restrictions.eq("account", account));
        criterions.add(Restrictions.eq("password", (new MD5()).getMD5ofStr(password)));

        return (Administrator) hDao.findOne(Administrator.class, criterions);
    }

}