package com.channabelle.common.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.regex.Matcher;

import org.apache.log4j.Logger;

public class MyReflect<T> {
	Logger log = Logger.getLogger(MyReflect.class);

	public T merge(T newEntity, T oldEntity, Class<?> clazz) throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		return merge(newEntity, oldEntity, clazz, new HashMap<String, Object>());
	}

	public T merge(T newEntity, T oldEntity, Class<?> clazz, HashMap<String, Object> whiteList)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method[] methods = clazz.getMethods();

		if (null != whiteList && 0 < whiteList.size()) {
			for (int k = 0; k < methods.length; k++) {
				if (true == whiteList.containsKey(methods[k].getName())) {
					Object value = whiteList.get(methods[k].getName());
					methods[k].invoke(oldEntity, value);
				}
			}
		}
		for (int k = 0; k < methods.length; k++) {
			Method sMethod = getSetMethodIfValueNotNull(methods, methods[k], newEntity);
			if (null != sMethod) {
				Object value = methods[k].invoke(newEntity);
				sMethod.invoke(oldEntity, value);
			}
		}
		return oldEntity;
	}

	private Method getSetMethodIfValueNotNull(Method[] methods, Method mGet, Object obj) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Method r = null;
		String getMethodName = mGet.getName();
		Matcher match = MyPattern.grep(getMethodName, "get(\\w+)");
		if (true == match.find()) {
			String setMethodName = "set" + match.group(1);
			Method setMethod = this.getMethod(setMethodName, methods);

			Object getMethodValue = mGet.invoke(obj);
			if (null != setMethod && null != getMethodValue) {
				r = setMethod;
			}
		}
		return r;
	}

	private Method getMethod(String name, Method[] methods) {
		for (int k = 0; k < methods.length; k++) {
			if (methods[k].getName().equals(name)) {
				return methods[k];
			}
		}
		return null;
	}
}
