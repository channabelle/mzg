package com.channabelle.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class BaseController {
    private Logger log = Logger.getLogger(BaseController.class);

    @Autowired
    public HttpServletRequest request;

    // 统一捕获controller层的异常
    @ExceptionHandler
    public ModelAndView handleException(HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
                                        Exception e) {
        log.error("BaseController error", e);

        return null;
    }
}
