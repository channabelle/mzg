package com.channabelle.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.channabelle.common.ControllerResult;
import com.channabelle.common.ServiceResult;
import com.channabelle.model.Apple;
import com.channabelle.service.impl.TestServiceImpl;

@Controller
@RequestMapping("/test")
public class TestController extends BaseController {
	Logger log = Logger.getLogger(TestController.class);
	@Autowired
	TestServiceImpl<Apple> service;

	@RequestMapping(value = "/apple", method = RequestMethod.GET)
	public @ResponseBody
	Object listApple() {
		List<Apple> apples = service.hfindAll(Apple.class);

		return ControllerResult.success(apples);
	}

	@RequestMapping(value = "/apple/{id}", method = RequestMethod.GET)
	public @ResponseBody
	Object getApple(@PathVariable String id) {
		return ControllerResult.success(service.hfindOne(Apple.class, id));
	}

	@RequestMapping(value = "/apple", method = RequestMethod.POST)
	public @ResponseBody
	Object createApple(@RequestBody Apple apple) {
		ServiceResult sRes = service.hAddOne(apple);
		return ControllerResult.setServiceResult(sRes, service.hfindOne(Apple.class, apple.getId()));
	}

	@RequestMapping(value = "/apple", method = RequestMethod.PUT)
	public @ResponseBody
	Object updateApple(@RequestBody Apple apple) {
		ServiceResult sRes = service.hUpdateOne(apple);
		return ControllerResult.setServiceResult(sRes, service.hfindOne(Apple.class, apple.getId()));
	}

	@RequestMapping(value = "/apple/{id}", method = RequestMethod.DELETE)
	public @ResponseBody
	Object deleteApple(@PathVariable String id) {
		ServiceResult sRes = service.hDeleteOne(new Apple(id));
		return ControllerResult.setServiceResult(sRes, service.hfindAll(Apple.class));
	}

	@RequestMapping(value = "/apple/batch", method = RequestMethod.POST)
	public @ResponseBody
	Object addAppleList(@RequestBody List<Apple> list) {
		ServiceResult sRes = service.hAddList(list);
		return ControllerResult.setServiceResult(sRes, service.hfindAll(Apple.class));
	}

	@RequestMapping(value = "/apple/batch", method = RequestMethod.DELETE)
	public @ResponseBody
	Object deleteAppleList(@RequestBody List<Apple> list) {
		ServiceResult sRes = service.hDeleteList(list);
		return ControllerResult.setServiceResult(sRes, service.hfindAll(Apple.class));
	}
}
