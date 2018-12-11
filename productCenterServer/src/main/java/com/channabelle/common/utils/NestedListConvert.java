package com.channabelle.common.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NestedListConvert {
	public static List<Object> getTreeList(List<?> list, String pKey, String fatherKey, String childrenKey) {
		List<Object> array = new ArrayList<Object>();
		List<Object> leftList = new ArrayList<Object>();
		leftList.addAll(list);

		// 加根节点
		for (int k = list.size() - 1; k >= 0; k--) {
			Object obj = list.get(k);
			Map<String, Object> oMap = objectToMap(obj);
			if (null == oMap.get(fatherKey) || "".equals(oMap.get(fatherKey))) {

				// 加根节点下的子节点
				leftList.remove(k);
				addChild(oMap, pKey, leftList, fatherKey, childrenKey);
				array.add(oMap);
			}
		}
		Collections.reverse(array);

		return array;
	}

	public static List<Map<String, Object>> getChainList(List<?> list, String pKey, String fatherKey, String valueKey) {
		List<Map<String, Object>> array = new ArrayList<Map<String, Object>>();
		if (null != list && 0 < list.size()) {
			for (int k = 0; k < list.size(); k++) {
				Map<String, Object> map = getChain(list, pKey, fatherKey, list.get(k), valueKey);

				array.add(map);
			}
		}

		return array;
	}

	private static Map<String, Object> getChain(List<?> list, String pKey, String fatherKey, Object item,
			String valueKey) {
		Map<String, Object> map = new HashMap<String, Object>();

		Map<String, Object> iMap = objectToMap(item);
		String root_id = iMap.get(pKey).toString();
		String id_path = iMap.get(pKey).toString();
		String root_value = iMap.get(valueKey).toString();
		String value_path = iMap.get(valueKey).toString();

		Object f = getFather(list, pKey, fatherKey, item);
		while (null != f) {
			Map<String, Object> fMap = objectToMap(f);

			id_path = fMap.get(pKey).toString() + "-" + id_path;
			value_path = fMap.get(valueKey).toString() + "-" + value_path;

			root_id = fMap.get(pKey).toString();
			root_value = fMap.get(valueKey).toString();

			f = getFather(list, pKey, fatherKey, f);
		}
		map.put("value", item);
		map.put("root_id", root_id);
		map.put("id_path", id_path);
		map.put("root_value", root_value);
		map.put("value_path", value_path);

		return map;
	}

	private static Object getFather(List<?> list, String pKey, String fatherKey, Object item) {
		Object f = null;

		Map<String, Object> iMap = objectToMap(item);
		if (null == iMap.get(fatherKey) && "".equals(iMap.get(fatherKey))) {

		} else {
			if (null != list && 0 < list.size()) {
				for (int m = 0; m < list.size(); m++) {
					Object fObj = list.get(m);
					Map<String, Object> fMap = objectToMap(fObj);
					if (fMap.get(pKey).equals(iMap.get(fatherKey))) {
						f = fObj;
						break;
					}
				}
			}
		}

		return f;
	}

	private static void addChild(Map<String, Object> node, String pKey, List<?> list, String fatherKey,
			String childrenKey) {
		List<Object> leftList = new ArrayList<Object>();
		leftList.addAll(list);

		List<Map<String, Object>> childrenArray = new ArrayList<Map<String, Object>>();
		for (int k = list.size() - 1; k >= 0; k--) {
			Object obj = list.get(k);
			Map<String, Object> oMap = objectToMap(obj);

			if (true == node.get(pKey).equals(oMap.get(fatherKey))) {
				leftList.remove(k);
				addChild(oMap, pKey, leftList, fatherKey, childrenKey);
				childrenArray.add(oMap);
			}
		}
		node.put(childrenKey, childrenArray);
	}

	private static Map<String, Object> objectToMap(Object obj) {
		if (obj == null) {
			return null;
		}

		Map<String, Object> map = new HashMap<String, Object>();

		Field[] declaredFields = obj.getClass().getDeclaredFields();
		try {
			for (Field field : declaredFields) {
				field.setAccessible(true);
				map.put(field.getName(), field.get(obj));
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return map;
	}
}
