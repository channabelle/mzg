package com.channabelle.service.impl;

import com.channabelle.common.ServiceResult;
import com.channabelle.model.WepayOrder;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class WepayOrderServiceImpl<T> extends BaseServiceImpl<T> {
    Logger log = Logger.getLogger(WepayOrderServiceImpl.class);

    public WepayOrderServiceImpl() {
        log.info("WepayOrderServiceImpl");
    }

    public ServiceResult create(T order) {
        return hDao.save(order);
    }

    public ServiceResult update(WepayOrder order) {
        return hDao.patchUpdate((T) order, WepayOrder.class, order.getOut_trade_no());
    }

    public List<WepayOrder> findSuccessPayedOrderByAttach(String attach) {
        List<Criterion> criterions = new ArrayList<Criterion>();
        criterions.add(Restrictions.eq("trade_state", "SUCCESS"));
        criterions.add(Restrictions.eq("attach", attach));

        return (List<WepayOrder>) hDao.findAll(WepayOrder.class, criterions);
    }
}