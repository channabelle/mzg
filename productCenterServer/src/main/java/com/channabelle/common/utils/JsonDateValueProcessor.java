package com.channabelle.common.utils;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.util.JSONUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class JsonDateValueProcessor implements JsonValueProcessor {
    private String datePattern = "yyyy-MM-dd HH:mm:ss";

    public static JsonConfig getJsonConfig() {
        JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[]{"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"}));

        JsonConfig config = new JsonConfig();
        config.registerJsonValueProcessor(Date.class, new JsonDateValueProcessor());

        return config;
    }

    public JsonDateValueProcessor() {
        super();
    }

    public JsonDateValueProcessor(String format) {
        super();
        this.datePattern = format;
    }

    @Override
    public Object processArrayValue(Object value, JsonConfig jsonConfig) {
        return process(value);
    }

    @Override
    public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
        return process(value);
    }

    private Object process(Object value) {
        try {
            if(value instanceof Date) {
                SimpleDateFormat sdf = new SimpleDateFormat(datePattern, Locale.UK);
                return sdf.format((Date) value);
            }
            return value == null ? "" : value.toString();
        } catch(Exception e) {
            return "";
        }

    }

    public String getDatePattern() {
        return datePattern;
    }

    public void setDatePattern(String pDatePattern) {
        datePattern = pDatePattern;
    }
}