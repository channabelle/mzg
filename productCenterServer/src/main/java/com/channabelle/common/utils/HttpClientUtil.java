package com.channabelle.common.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HttpClientUtil {
    private static Logger log = Logger.getLogger(HttpClientUtil.class);
    private static HttpClient client = null;
    private static MultiThreadedHttpConnectionManager conn_manager = null;
    private static HttpConnectionManagerParams cmanager_params = null;

    static {
        if(null == client) {
            init();
        }
    }

    private static void init() {
        if(conn_manager == null)
            conn_manager = new MultiThreadedHttpConnectionManager();
        if(cmanager_params == null)
            cmanager_params = new HttpConnectionManagerParams();
        cmanager_params.setDefaultMaxConnectionsPerHost(10);
        cmanager_params.setMaxTotalConnections(20);
        cmanager_params.setConnectionTimeout(6000);
        conn_manager.setParams(cmanager_params);
        if(client == null)
            client = new HttpClient(conn_manager);
    }

    public static String executePost(String uri, String requestBody) {
        log.info("executePost uri: " + uri + ", body: " + requestBody);

        PostMethod method = null;
        try {
            method = new PostMethod(uri);
            method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
            method.getParams().setParameter(HttpMethodParams.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.102 Safari/537.36");
            if(null != requestBody) {
                RequestEntity reqEntity = new StringRequestEntity(requestBody, "application/json", "UTF-8");
                method.setRequestEntity(reqEntity);
            }
            int status = client.executeMethod(method);
            if(status == 405) {
                method.releaseConnection();
                return executeGet(uri);
            }
            String res = method.getResponseBodyAsString();
            log.info(String.format("executePost resStatus: %d, resBody: %s", status, res));
            if(status == 200) {
                return res;
            } else {
                return null;
            }
        } catch(Exception e) {
            log.error("", e);
        } finally {
            if(method != null) {
                method.releaseConnection();
            }
        }
        return null;
    }

    public static String executePostXML(String uri, String requestBody) {
        log.info(">>>>>>>>>>>>>>>>>>>>>> executePostXML RESQUEST START");
        log.info("uri: " + uri + ", body: " + requestBody);
        log.info(">>>>>>>>>>>>>>>>>>>>>> executePostXML RESQUEST   END");

        PostMethod method = null;
        try {
            method = new PostMethod(uri);
            method.setRequestHeader("Content-Type", "text/plain;charset=UTF-8");
            method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
            method.getParams().setParameter(HttpMethodParams.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.102 Safari/537.36");
            if(null != requestBody) {
                RequestEntity reqEntity = new StringRequestEntity(requestBody, "text/plain", "UTF-8");
                method.setRequestEntity(reqEntity);
            }
            int status = client.executeMethod(method);
            if(status == 405) {
                method.releaseConnection();
                return executeGet(uri);
            }
            String res = method.getResponseBodyAsString();

            log.info("executePostXML RESPONSE START >>>>>>>>>>>>>>>>>>>>>>");
            log.info(String.format("resStatus: %d, resBody: %s ", status, res));
            log.info("executePostXML RESPONSE   END >>>>>>>>>>>>>>>>>>>>>>");
            if(status == 200) {
                return res;
            } else {
                return null;
            }
        } catch(Exception e) {
            log.error("", e);
        } finally {
            if(method != null) {
                method.releaseConnection();
            }
        }
        return null;
    }

    public static String executeGet(String uri) {
        log.info("executeGet uri: " + uri);

        GetMethod method = null;
        try {
            method = new GetMethod(uri);
            method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
            method.getParams().setParameter(HttpMethodParams.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.102 Safari/537.36");
            int status = client.executeMethod(method);
            String res = method.getResponseBodyAsString();
            log.info(String.format("executeGet resStatus: %d, resBody: %s", status, res));
            if(status == 200) {
                return res;
            } else {
                return null;
            }
        } catch(Exception e) {
            log.error("executeGet Error", e);
        } finally {
            if(null != method) {
                method.releaseConnection();
            }
        }
        return null;
    }

    public static String executePostFormData(String url, Map<String, Object> params) throws IOException {
        System.out.println("<=== executePostFormData url: " + url);
        log.info("<=== executePostFormData url: " + url);
        String body = null;

        // 创建httpclient对象
        CloseableHttpClient client = HttpClients.createDefault();
        // 创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);

        // 装填参数
        List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
        Iterator<String> i = params.keySet().iterator();
        while(i.hasNext()) {
            String key = i.next();
            String value = String.valueOf(params.get(key));
            nvps.add(new BasicNameValuePair(key, value));
            System.out.println("<=== executePostFormData params: [" + key + ", " + value + "]");
            log.info("<=== executePostFormData params: [" + key + ", " + value + "]");
        }

        // 设置参数到请求对象中
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));

        // 设置header信息
        // 指定报文头【Content-type】、【User-Agent】
        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
        httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        // 执行请求操作，并拿到结果（同步阻塞）
        CloseableHttpResponse response = client.execute(httpPost);
        // 获取结果实体
        HttpEntity entity = response.getEntity();
        if(entity != null) {
            // 按指定编码转换结果实体为String类型
            body = EntityUtils.toString(entity, "UTF-8");
        }
        EntityUtils.consume(entity);
        // 释放链接
        response.close();

        log.info("===> executePostFormData result body: " + body);
        System.out.println("===> executePostFormData result body: " + body);
        return body;
    }
}
