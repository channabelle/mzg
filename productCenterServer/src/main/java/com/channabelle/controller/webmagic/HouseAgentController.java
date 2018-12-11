package com.channabelle.controller.webmagic;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.channabelle.common.ControllerResult;
import com.channabelle.controller.BaseController;
import com.channabelle.model.webmagic.houseAgent.House;
import com.channabelle.service.impl.CommonServiceImpl;
import com.channabelle.webmagic.ishangzuPageProcessor;
import com.channabelle.webmagic.lianjiaPageProcessor;

import us.codecraft.webmagic.Spider;

@Controller
@RequestMapping("/webmagic/houseAgent")
public class HouseAgentController extends BaseController {
	private Logger log = Logger.getLogger(HouseAgentController.class);
	@Autowired
	CommonServiceImpl<House> service;

	@RequestMapping(value = "houseList", method = RequestMethod.GET)
	public @ResponseBody Object houseList(@RequestParam(value = "platform", required = true) String platform,
			@RequestParam(value = "url", required = true) String url) {
		log.info("houseList ===> platform: " + platform + ", url: " + url);

		if ("ishangzu".equals(platform)) {
			Spider.create(new ishangzuPageProcessor()).addUrl(url).thread(1).run();
		} else if ("lianjia".equals(platform)) {
			Spider.create(new lianjiaPageProcessor()).addUrl(url).thread(1).run();
		}

		return ControllerResult.success(null);
	}
}
