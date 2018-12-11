package com.channabelle.common;

import java.io.Serializable;
import java.util.HashMap;

public class ControllerResult implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 2161686205170378216L;
    private int status;
    private HashMap<String, Object> message;
    private Object data;

    public ControllerResult(int status, HashMap<String, Object> message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static ControllerResult success(Object data) {
        HashMap<String, Object> message = new HashMap<String, Object>();
        message.put("title", "系统消息");
        message.put("code", ServiceResult.ERROR_CODE_DEFAULT);
        message.put("content", "成功");
        return new ControllerResult(2000, message, data);
    }

    public static ControllerResult systemError(Object data) {
        ControllerResult controllerResult = ControllerResult.setServiceResult((new ServiceResult()).systemError(), data);
        return controllerResult;
    }

    public static ControllerResult setServiceResult(ServiceResult sR, Object data) {
        ControllerResult res = null;
        HashMap<String, Object> message = new HashMap<String, Object>();

        switch(sR.getStatus()) {
            case Success:
                res = ControllerResult.success(data);
                break;
            case Info:
                message.put("title", "系统提示");
                message.put("code", sR.getErrorCode());
                message.put("content", sR.getErrorKey() + sR.getErrorValue());
                res = new ControllerResult(2001, message, data);
                break;
            case Warning:
                message.put("title", "注意");
                message.put("code", sR.getErrorCode());
                message.put("content", sR.getErrorKey() + sR.getErrorValue());
                res = new ControllerResult(2002, message, data);
                break;
            case Error:
                message.put("title", "系统错误");
                message.put("code", sR.getErrorCode());
                message.put("content", sR.getErrorKey() + sR.getErrorValue());
                res = new ControllerResult(2003, message, data);
                break;
        }

        return res;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public HashMap<String, Object> getMessage() {
        return message;
    }

    public void setMessage(HashMap<String, Object> message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
