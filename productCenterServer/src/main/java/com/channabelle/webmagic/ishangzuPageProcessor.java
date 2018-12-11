package com.channabelle.webmagic;

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

public class ishangzuPageProcessor extends CommonPageProcessor {
	private static Logger log = Logger.getLogger(ishangzuPageProcessor.class);

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
		log.info("=== processDefaultPage ===");
		int cPage = Integer.parseInt(page.getUrl().regex("/p(\\d+)/").toString());
		String cUrl = page.getUrl().toString();
		log.info(String.format("page: %d, url: %s", cPage, cUrl));

		boolean notFound = page.getHtml().css(".demand-container").regex("暂时没有找到您想要的房子").match();
		List<Selectable> listItem = page.getHtml().css(".list-item").nodes();
		if (false == notFound && null != listItem && 0 < listItem.size()) {
			for (int k = 0; k < listItem.size(); k++) {
				Selectable item = listItem.get(k);

				String title = item.css(".list-item-intro span").regex("<span>(.*)</span>").toString();
				String price = item.css(".price").regex("<span class=\"price\">(\\d+)<i>").toString();
				String itemDetailUrl = item.css(".list-item-info a").regex(".*href=\"(\\S+)\"").toString();

				log.info(String.format("{\"index\": \"%d\", \"price\": \"%s\", \"title\": \"%s\", \"url\": \"%s\"}", k,
						price, title, itemDetailUrl));

				// 部分三：从页面发现后续的url地址来抓取
				page.addTargetRequest(itemDetailUrl);
			}

			// 添加下一页
			String nextPageUrl = cUrl.replace(String.format("/p%d/", cPage), String.format("/p%d/", cPage + 1));
			page.addTargetRequest(nextPageUrl);
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

		String price = page.getHtml().css(".house-info-wrap .price").regex("<sy>.*\\D+(\\d+).*</sy>").toString();
		if (true == StringUtil.isBlank(price)) {
			price = page.getHtml().css(".house-info-wrap .price").regex(">(.*)<span").toString();
		}
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

		log.info(String.format("[%s] house: %s", pageId,
				JSONObject.fromObject(h, JsonDateValueProcessor.getJsonConfig()).toString()));

		NormalTask.addQuery(NormalTask.HOUSE_RECORD, h);
	}

	public static void main(String[] args) throws Exception {
		log.info("=== START ===");
		String url = "https://sz.ishangzu.com/zufang/p1/?q=万科碧桂园";
		Spider.create(new ishangzuPageProcessor()).addUrl(url).thread(1).run();

		log.info("=== END ===");
	}

}