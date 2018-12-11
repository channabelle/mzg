package com.channabelle.service.impl.product;

import com.channabelle.model.product.ProDetail;
import com.channabelle.service.impl.BaseServiceImpl;
import com.channabelle.service.impl.CommonServiceImpl;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ProDetailServiceImpl<T> extends BaseServiceImpl<T> {
    Logger log = Logger.getLogger(ProDetailServiceImpl.class);

    @Autowired
    CommonServiceImpl<ProDetail> proDetailService;

    public ProDetailServiceImpl() {
        log.info("ProDetailServiceImpl");
    }

    public ProDetail getProDetailByProInfoId(String proInfoId) throws Exception {
        List<Criterion> criterions = new ArrayList<Criterion>();
        criterions.add(Restrictions.eq("uuid_pro_info", proInfoId));
        ProDetail pDetail = proDetailService.hfindOne(ProDetail.class, criterions);

        return pDetail;
    }
}