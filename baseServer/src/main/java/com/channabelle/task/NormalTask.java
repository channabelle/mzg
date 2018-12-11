package com.channabelle.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.channabelle.model.HttpRecord;
import com.channabelle.service.impl.BaseServiceImpl;

@Component
public class NormalTask implements ApplicationContextAware {
	private Logger log = Logger.getLogger(BaseServiceImpl.class);
	private ApplicationContext appContext;
	private BaseServiceImpl<HttpRecord> hrService;

	private BaseServiceImpl<HttpRecord> getHrService() {
		if (null == hrService) {
			hrService = (BaseServiceImpl<HttpRecord>) appContext.getBean("baseServiceImpl");
		}
		return hrService;
	}

	public static final String HTTP_RECORD = "HttpRecord";
	private static Map<String, List<?>> taskQuery = new HashMap<String, List<?>>();
	static {
		taskQuery.put(HTTP_RECORD, new ArrayList<HttpRecord>());
	}

	public static List getAndCleanQuery(String query) {
		List list = taskQuery.get(query);
		taskQuery.put(query, new ArrayList());
		return list;
	}

	public static void addQuery(String query, Object o) {
		List list = taskQuery.get(query);
		list.add(o);
	}

	// "0 15 10 ? * *" 每天上午10:15触发
	// "0 15 10 * * ?" 每天上午10:15触发
	// "0 15 10 * * ? *" 每天上午10:15触发
	// "0 15 10 * * ? 2005" 2005年的每天上午10:15触发
	// "0 * 14 * * ?" 在每天下午2点到下午2:59期间的每1分钟触发
	// "0 0/5 14 * * ?" 在每天下午2点到下午2:55期间的每5分钟触发
	// "0 0/5 14,18 * * ?" 在每天下午2点到2:55期间和下午6点到6:55期间的每5分钟触发
	// "0 0-5 14 * * ?" 在每天下午2点到下午2:05期间的每1分钟触发
	// "0 10,44 14 ? 3 WED" 每年三月的星期三的下午2:10和2:44触发
	// "0 15 10 ? * MON-FRI" 周一至周五的上午10:15触发
	// "0 15 10 15 * ?" 每月15日上午10:15触发
	// "0 15 10 L * ?" 每月最后一日的上午10:15触发
	// "0 15 10 ? * 6L" 每月的最后一个星期五上午10:15触发
	// "0 15 10 ? * 6L 2002-2005" 2002年至2005年的每月的最后一个星期五上午10:15触发
	// "0 15 10 ? * 6#3" 每月的第三个星期五上午10:15触发

	@Scheduled(cron = "0/30 * * * * *")
	public void filterLogToDB() {
		List list = getAndCleanQuery(HTTP_RECORD);
		if (null != list && 0 < list.size()) {
			this.getHrService().hAddList(list);
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		// TODO Auto-generated method stub
		appContext = arg0;
	}
}
