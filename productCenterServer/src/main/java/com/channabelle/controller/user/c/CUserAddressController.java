package com.channabelle.controller.user.c;

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
import com.channabelle.common.ServiceResult.Status;
import com.channabelle.controller.BaseController;
import com.channabelle.model.user.User;
import com.channabelle.model.user.UserAddress;
import com.channabelle.service.impl.user.UserAddressServiceImpl;
import com.channabelle.service.impl.user.UserServiceImpl;

@Controller
@RequestMapping("/c/user/address")
public class CUserAddressController extends BaseController {
	private Logger log = Logger.getLogger(CUserAddressController.class);

	@Autowired
	UserAddressServiceImpl<UserAddress> service;

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody Object patchUpdateUser(@RequestBody UserAddress ud) {
		User u = UserServiceImpl.getSession_User(this.request);
		UserAddress userAddress = service.hfindOne(UserAddress.class, ud.getP_uuid_user_address());

		if (false == u.getUuid_user().equals(userAddress.getUuid_user())) {
			return ControllerResult.setServiceResult(new ServiceResult(Status.Error, "当前用户与修改用户编号", "不一致"), null);
		}
		ServiceResult sRes = service.hPatchUpdateOne(ud, UserAddress.class, ud.getP_uuid_user_address());
		return ControllerResult.setServiceResult(sRes,
				service.hfindOne(UserAddress.class, ud.getP_uuid_user_address()));
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public @ResponseBody Object create(@RequestBody UserAddress ud) {
		User u = UserServiceImpl.getSession_User(this.request);

		ud.setUuid_user(u.getUuid_user());
		ServiceResult sRes = service.hAddOne(ud);
		return ControllerResult.setServiceResult(sRes, ud);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody Object delete(@RequestBody UserAddress userAddress) {
		User u = UserServiceImpl.getSession_User(this.request);
		UserAddress ud = service.hfindOne(UserAddress.class, userAddress.getP_uuid_user_address());

		if (false == u.getUuid_user().equals(ud.getUuid_user())) {
			return ControllerResult.setServiceResult(new ServiceResult(Status.Error, "当前用户与删除对象所属用户编号", "不一致"), null);
		}

		ServiceResult sRes = service.hDeleteOne(ud);
		return ControllerResult.setServiceResult(sRes, service.listByUser(u.getUuid_user()));
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody Object list() {
		User u = UserServiceImpl.getSession_User(this.request);
		List<UserAddress> ll = service.listByUser(u.getUuid_user());

		return ControllerResult.success(ll);
	}

	@RequestMapping(value = "/info/{uuid}", method = RequestMethod.GET)
	public @ResponseBody Object info(@PathVariable String uuid) {
		User u = UserServiceImpl.getSession_User(this.request);
		UserAddress ud = service.hfindOne(UserAddress.class, uuid);
		if (false == u.getUuid_user().equals(ud.getUuid_user())) {
			return ControllerResult.setServiceResult(new ServiceResult(Status.Error, "当前用户与查看对象所属用户编号", "不一致"), null);
		}

		return ControllerResult.success(ud);
	}
}
