package com.channabelle.service.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TestServiceImpl<T> extends BaseServiceImpl<T> {
    Logger log = Logger.getLogger(TestServiceImpl.class);

    public TestServiceImpl() {
        log.info("TestServiceImpl");
    }
}