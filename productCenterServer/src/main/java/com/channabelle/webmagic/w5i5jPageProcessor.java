package com.channabelle.webmagic;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.log4j.Logger;

import com.channabelle.common.utils.JsonDateValueProcessor;
import com.channabelle.model.webmagic.houseAgent.House;
import com.channabelle.task.NormalTask;

import net.sf.json.JSONObject;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.selector.Selectable;

public class w5i5jPageProcessor extends CommonPageProcessor {
	private static Logger log = Logger.getLogger(w5i5jPageProcessor.class);

	private static void printLog(Object info) {
		log.info(info);
		System.out.println(info);
	}

	@Override
	// process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
	public void process(Page page) {
		try {
			if (true == page.getUrl().regex("https://sz.ishangzu.com/zufang/\\w+\\.html").match()) {
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

		List<Selectable> listItem = page.getHtml().css(".listCon").nodes();
		for (int k = 0; k < listItem.size(); k++) {
			Selectable item = listItem.get(k);

			String title = item.css(".listTit a").regex(">(.*)</a>").toString();
			String price = item.css(".jia .redC").regex("<strong>(\\d+)</strong>").toString();
			String itemDetailUrl = "";// item.css(".listTit a").regex("<a href=\"(.*)\"").toString();

			printLog(String.format("{\"index\": \"%d\", \"price\": \"%s\", \"title\": \"%s\", \"url\": \"%s\"}", k,
					price, title, itemDetailUrl));

			// 部分三：从页面发现后续的url地址来抓取
			page.addTargetRequest(itemDetailUrl);
		}
	}

	private void processItemPage(Page page) throws Exception {
		String pageId = page.getUrl().regex("https://sz.ishangzu.com/zufang/(\\w+)\\.html").toString();

		House h = new House();
		h.setPlatform("ishangzu");

		String url = page.getUrl().toString();
		h.setUrl(url);

		List<Selectable> crumbsNodes = page.getHtml().css(".crumbs div a").nodes();
		String crumbs = "";
		for (int k = 0; k < crumbsNodes.size(); k++) {
			Selectable crumbNode = crumbsNodes.get(k);

			String c = crumbNode.regex(".*>(\\S+)[\\s+\\|<]").toString();
			if (0 == k) {
				crumbs = c;
			} else {
				crumbs += "/" + c;
			}
		}
		h.setCrumbs(crumbs);

		String houseTitle = page.getHtml().css(".house-info-name h1").regex(">(.*)<").toString();
		h.setHouseTitle(houseTitle);

		String houseSubTitle = page.getHtml().css(".house-info-name h3").regex(">(.*)<").toString();
		h.setHouseSubTitle(houseSubTitle);

		String price = page.getHtml().css(".house-info-wrap .price").regex(">(.*)<span").toString();
		h.setPrice(Double.parseDouble(price.toString()));

		String priceUnit = page.getHtml().css(".house-info-wrap .price").regex("<span>(.*)</span>").toString();
		h.setPriceUnit(priceUnit);

		String houseRentType = page.getHtml().css(".house-attr-c a").regex("<p>(.*)</p>").toString();
		h.setHouseRentType(houseRentType);

		String houseFloor = page.getHtml().css(".house-attr-c span").regex("<span>(.*)</span>").toString();
		h.setHouseFloor(houseFloor);

		String houseArea = page.getHtml().css(".house-attr-c.attr-center p").regex("<p>(\\d+).*</p>").toString();
		h.setHouseArea(Double.parseDouble(houseArea.toString()));

		String houseAreaUnit = page.getHtml().css(".house-attr-c.attr-center p").regex("<p>\\d+(.*)</p>").toString();
		h.setHouseAreaUnit(houseAreaUnit);

		String houseOrientation = page.getHtml().css(".house-attr-c.attr-right p").regex("<p>(.*)</p>").toString();
		h.setHouseOrientation(houseOrientation);

		String houseDecoration = page.getHtml().css(".house-attr-c.attr-right span").regex("<span>(.*)</span>")
				.toString();
		h.setHouseDecoration(houseDecoration);

		String housePaymentType = page.getHtml().css(".bank-pay-dev").regex(">(.*)<").toString();
		h.setHousePaymentType(housePaymentType);

		String houseAddress = page.getHtml().css(".house-address-name").regex(".*title=\"(.*)\" class").toString();
		h.setHouseAddress(houseAddress);

		String houseNo = page.getHtml().css(".house-info-wrap.border-info-top").regex("房源编号.*<span>(\\w+-\\d+)</span>")
				.toString();
		h.setHouseNo(houseNo);

		String houseDecorationFinishTime = page.getHtml().css(".house-info-wrap.border-info-top")
				.regex("装修交房日期.*<span>(\\d+/\\d+/\\d+)</span>").toString();
		h.setHouseDecorationFinishTime((new SimpleDateFormat("yyyy/MM/dd")).parse(houseDecorationFinishTime));

		String houseAgentName = page.getHtml().css(".person-name").regex(">(\\S+)<span>").toString();
		h.setHouseAgentName(houseAgentName);

		String houseAgentShop = page.getHtml().css(".person-name").regex("<span>(.*)</span>").toString();
		h.setHouseAgentShop(houseAgentShop);

		String houseAgentPhone = page.getHtml().css(".person-intro").regex("data-content=\"(\\d+)\"").toString();
		h.setHouseAgentPhone(houseAgentPhone);

		printLog(String.format("[%s] house: %s", pageId,
				JSONObject.fromObject(h, JsonDateValueProcessor.getJsonConfig()).toString()));

		NormalTask.addQuery(NormalTask.HOUSE_RECORD, h);
	}

	public static void main(String[] args) throws Exception {
		printLog("=== START ===");

		String url = "https://sz.5i5j.com/zufang/" + URLEncoder.encode("_万科碧桂园?zn=万科碧桂园", "utf-8");
		Spider.create(new w5i5jPageProcessor()).addUrl(
				"https://sz.5i5j.com/zufang/90009155.html")
				.thread(1).run();

		printLog("=== END ===");
	}

}