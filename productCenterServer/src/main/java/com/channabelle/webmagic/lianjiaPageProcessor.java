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

		List<Selectable> listItem = page.getHtml().css(".content__list--item").nodes();
		if (null != listItem && 0 < listItem.size()) {
			for (int k = 0; k < listItem.size(); k++) {
				Selectable item = listItem.get(k);

				String title = item.css(".content__list--item--title a").regex(">(.*)</a>").toString();
				String price = item.css(".content__list--item-price em").regex(">(\\d+)</em>").toString();
				String itemDetailUrl = item.css(".content__list--item--title a").regex("href=\"(\\S+html)\"")
						.toString();

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

		String houseTitle = page.getHtml().css(".content__title").regex(">(.*)<").toString();
		h.setHouseTitle(houseTitle);

		h.setHouseSubTitle(null);

		String price = page.getHtml().css(".content__aside--title span").regex(">(.*)</span>").toString();
		if (false == StringUtil.isBlank(price)) {
			h.setPrice(Double.parseDouble(price.toString()));
		} else {
			h.setPrice(-1);
		}

		String priceUnit = page.getHtml().css(".content__aside--title").regex("</span>([^span]+)</p>").toString();
		h.setPriceUnit(priceUnit);

		h.setHouseRentType(null);
		h.setHouseFloor(null);

		String houseArea = page.getHtml().css(".content__article__table").regex("<i class=\"area\"></i>(\\d+).*</span>")
				.toString();
		if (false == StringUtil.isBlank(houseArea)) {
			h.setHouseArea(Double.parseDouble(houseArea.toString()));
		} else {
			h.setHouseArea(-1);
		}

		h.setHouseAreaUnit("平米");

		String houseOrientation = page.getHtml().css(".content__article__table")
				.regex("<i class=\"orient\"></i>(.*)</span>").toString();
		h.setHouseOrientation(houseOrientation);

		h.setHouseDecoration(null);
		h.setHousePaymentType(null);
		h.setHouseAddress(null);

		String houseNo = page.getHtml().css(".house_code").regex("房源编号\\：(\\w+)</i>").toString();
		h.setHouseNo(houseNo);

		h.setHouseDecorationFinishTime(null);
		h.setHouseAgentName(null);
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