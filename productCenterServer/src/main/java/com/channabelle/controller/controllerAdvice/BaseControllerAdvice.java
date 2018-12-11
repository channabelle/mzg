package com.channabelle.controller.controllerAdvice;

import com.channabelle.common.utils.JsonDateValueProcessor;
import com.channabelle.model.HttpRecord;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class BaseControllerAdvice {
    Logger log = Logger.getLogger(BaseControllerAdvice.class);

    @Autowired
    private HttpServletRequest request;

    /**
     * 应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器
     *
     * @param binder
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // log.info("initBinder");

        HttpRecord record = this.getHttpRecord();
        if(null != record) {
            if(true == JSONUtils.isArray(binder.getTarget())) {
                JSONArray jArray = JSONArray.fromObject(binder.getTarget(), JsonDateValueProcessor.getJsonConfig());
                record.setReq_content(jArray.toString());
            } else {
                JSONObject jBody = JSONObject.fromObject(binder.getTarget(), JsonDateValueProcessor.getJsonConfig());
                record.setReq_content(jBody.toString());
            }

        }
    }

    /**
     * 把值绑定到Model中，使全局@RequestMapping可以获取到该值
     *
     * @param model
     */
    @ModelAttribute
    public void addAttributes(Model model) {
        // log.info("addAttributes");

    }

    /**
     * 全局异常捕捉处理
     *
     * @param ex
     *
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public void errorHandler(Exception ex) throws Exception {

        String errorSerialNo = String.valueOf(System.currentTimeMillis());
        log.error("errorHandler errorSerialNo: " + errorSerialNo, ex);

        HttpRecord record = this.getHttpRecord();
        if(null != record) {
            record.setErrorSerialNo(errorSerialNo);
        }
        throw new Exception(ex);
    }

    private HttpRecord getHttpRecord() {
        HttpRecord record = null;
        Object r = request.getAttribute("HttpRecord");
        if(r instanceof HttpRecord && null != r) {
            record = (HttpRecord) r;
        }
        return record;
    }
}
