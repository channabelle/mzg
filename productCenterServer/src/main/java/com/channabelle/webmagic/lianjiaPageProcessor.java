package com.channabelle.webmagic;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.helper.StringUtil;

import com.channabelle.common.utils.JsonDateValueProcessor;
import com.channabelle.model.webmagic.houseAgent.House;
import com.channabelle.task.NormalTask;

import net.sf.json.JSONObject;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.selector.Selectable;

public class lianjiaPageProcessor extends CommonPageProcessor {
	private static Logger log = Logger.getLogger(lianjiaPageProcessor.class);

	private static void printLog(Object info) {
		log.info(info);
		// System.out.println(info);
	}

	@Override
	// process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
	public void process(Page page) {
		try {
			if (true == page.getUrl().regex("https://su.lianjia.com/zufang/\\w+.html").match()) {
				processItemPage(page);
			} else {
				processDefaultPage(page);
			}
		} catch (Exception e) {
			log.error("", e);
		}

	}

	private void processDefaultPage(Page page) throws Exception {
		printLog("=== processDefaultPage ===");
		int cPage = Integer.parseInt(page.getUrl().regex("pg(\\d+)rs").toString());
		String cUrl = page.getUrl().toString();
		printLog(String.format("page: %d, url: %s", cPage, cUrl));

		List<Selectable> listItem = page.getHtml().css(".info-panel").nodes();
		if (null != listItem && 0 < listItem.size()) {
			for (int k = 0; k < listItem.size(); k++) {
				Selectable item = listItem.get(k);

				String title = item.css("h2 a").regex(">(.*)</a>").toString();
				String price = item.css(".price .num").regex(">(\\d+)</span>").toString();
				String itemDetailUrl = item.css("h2 a").regex("href=\"(\\S+html)\"").toString();

				printLog(String.format("{\"index\": \"%d\", \"price\": \"%s\", \"title\": \"%s\", \"url\": \"%s\"}", k,
						price, title, itemDetailUrl));

				// 部分三：从页面发现后续的url地址来抓取
				page.addTargetRequest(itemDetailUrl);
			}
			// 添加下一页
			String nextPageUrl = cUrl.replace(String.format("pg%drs", cPage), String.format("pg%drs", cPage + 1));
			page.addTargetRequest(nextPageUrl);
		}

	}

	private void processItemPage(Page page) throws Exception {
		String pageId = page.getUrl().regex("https://su.lianjia.com/zufang/(\\w+)\\.html").toString();

		House h = new House();
		h.setPlatform("lianjia");

		String url = page.getUrl().toString();
		h.setUrl(url);

		List<Selectable> crumbsNodes = page.getHtml().css(".fl.l-txt a").nodes();
		String crumbs = "";
		for (int k = 0; k < crumbsNodes.size(); k++) {
			Selectable crumbNode = crumbsNodes.get(k);

			String c = crumbNode.regex(">(\\S+)</a>").toString();
			if (0 == k) {
				crumbs = c;
			} else {
				crumbs += "/" + c;
			}
		}
		h.setCrumbs(crumbs);

		String houseTitle = page.getHtml().css(".title .main").regex(">(.*)<").toString();
		h.setHouseTitle(houseTitle);

		String houseSubTitle = page.getHtml().css(".title .sub").regex(">(.*)<").toString();
		h.setHouseSubTitle(houseSubTitle);

		String price = page.getHtml().css(".price .total").regex(">(.*)</span>").toString();
		if (false == StringUtil.isBlank(price)) {
			h.setPrice(Double.parseDouble(price.toString()));
		} else {
			h.setPrice(-1);
		}

		String priceUnit = page.getHtml().css(".price .unit").regex("<span>([^span]+)</span>").toString();
		h.setPriceUnit(priceUnit);

		h.setHouseRentType(null);
		h.setHouseFloor(null);

		String houseArea = page.getHtml().css(".zf-room").regex("面积：</i>(.*)平米</p>").toString();
		h.setHouseArea(Double.parseDouble(houseArea.toString()));

		h.setHouseAreaUnit("平米");

		String houseOrientation = page.getHtml().css(".zf-room").regex("房屋朝向：</i>([^/p]+)</p>").toString();
		h.setHouseOrientation(houseOrientation);

		h.setHouseDecoration(null);
		h.setHousePaymentType(null);

		String houseAddress = page.getHtml().css(".zf-room").regex("位置：</i>.*>([^>]+)</a>").toString();
		h.setHouseAddress(houseAddress);

		String houseNo = page.getHtml().css(".houseRecord .houseNum").regex("链家编号\\：(\\d+)</span>").toString();
		h.setHouseNo(houseNo);

		h.setHouseDecorationFinishTime(null);

		String houseAgentName = page.getHtml().css(".brokerName .name").regex(">(.*)</a>").toString();
		h.setHouseAgentName(houseAgentName);

		h.setHouseAgentShop(null);
		h.setHouseAgentPhone(null);

		printLog(String.format("[%s] house: %s", pageId,
				JSONObject.fromObject(h, JsonDateValueProcessor.getJsonConfig()).toString()));

		NormalTask.addQuery(NormalTask.HOUSE_RECORD, h);
	}

	public static void main(String[] args) throws Exception {
		printLog("=== START ===");

		String url = "https://su.lianjia.com/zufang/pg1rs" + URLEncoder.encode("万科碧桂园/", "utf-8");
		Spider.create(new lianjiaPageProcessor()).addUrl(url).thread(1).run();

		printLog("=== END ===");
	}
}