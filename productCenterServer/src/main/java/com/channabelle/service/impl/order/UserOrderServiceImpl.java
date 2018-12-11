package com.channabelle.service.impl.order;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.channabelle.common.ServiceResult;
import com.channabelle.model.order.UserOrder;
import com.channabelle.model.order.UserOrderCreate;
import com.channabelle.model.order.UserSubOrder;
import com.channabelle.model.product.ProInfo;
import com.channabelle.model.user.ShoppingCar;
import com.channabelle.model.user.User;
import com.channabelle.model.user.UserAddress;
import com.channabelle.service.impl.BaseServiceImpl;
import com.channabelle.service.impl.CommonServiceImpl;
import com.channabelle.service.impl.product.ProInfoServiceImpl;
import com.channabelle.service.impl.shoppingCar.ShoppingCarServiceImpl;

@Service
@Transactional
public class UserOrderServiceImpl<T> extends BaseServiceImpl<T> {
	Logger log = Logger.getLogger(UserOrderServiceImpl.class);

	@Autowired
	CommonServiceImpl<UserSubOrder> subOrderService;

	@Autowired
	CommonServiceImpl<UserOrder> orderService;

	@Autowired
	ProInfoServiceImpl<ProInfo> proService;

	@Autowired
	ShoppingCarServiceImpl<ShoppingCar> shoppingCarService;

	public UserOrderServiceImpl() {
		log.info("UserOrderServiceImpl");
	}

	/*
	 * 从购物车创建订单， 如果商品来自不同的商铺， 需要创建不同的订单
	 */
	public ServiceResult createUserOrderFromShoppingCar(final User user, final UserOrderCreate uoc, final UserOrder ou,
			final String uuid_shop) {
		final List<String> shoppingCarUuids = uoc.getShoppingCarUuids();
		ServiceResult res = null;

		if (null != shoppingCarUuids && 0 < shoppingCarUuids.size()) {
			final List<ShoppingCar> shoppingCars = shoppingCarService
					.getByUserUuidAndShoppingCarUuids(user.getUuid_user(), shoppingCarUuids);
			final UserAddress userAddress = uoc.getUserAddress();

			res = this.doTransaction(new TranscationTask() {
				public void run(Session session) throws Exception {
					double order_total_num = 0;
					double order_amount = 0;
					String order_name = null;

					UserOrder userOrder = new UserOrder();
					userOrder.setUuid_user(user.getUuid_user());
					userOrder.setUuid_shop(uuid_shop);

					// 填写订单地址
					userOrder.setAddress_full(userAddress.getAddress_full());
					userOrder.setContact_name(userAddress.getContact_name());
					userOrder.setContact_phone(userAddress.getContact_phone());
					userOrder.setUser_order_remark(uoc.getRemark());
					userOrder.setOrder_name("");// 先占个位

					orderService.hAddOneInTask(userOrder, session);

					for (int m = 0; m < shoppingCars.size(); m++) {
						ShoppingCar sc = shoppingCars.get(m);
						UserSubOrder subOrder = new UserSubOrder();
						ProInfo proInfo = sc.getProInfo();

						// 来自不同商品的订单需要分开创建
						if (false == proInfo.getUuid_shop().equals(uuid_shop)) {
							break;
						}

						subOrder.setUuid_user(sc.getUuid_user());
						subOrder.setUuid_user_order(userOrder.getP_uuid_user_order());
						subOrder.setUuid_shop(uuid_shop);

						subOrder.setUuid_pro_info(sc.getUuid_pro_info());
						subOrder.setUser_order_pro_name(proInfo.getPro_title_short());
						subOrder.setUser_order_price(proInfo.getPro_price());
						subOrder.setUser_order_discount(proInfo.getPro_discount());

						// 产品数量变更
						if (0 == proInfo.getPro_total_quantity_unlimited()) {
							proService.modifyProQuantityInTask(proInfo.getP_uuid_pro_info(), "pro_left_quantity",
									(0 - sc.getAmount()), session);
						}

						subOrder.setUser_order_num(sc.getAmount());
						// 创建子订单
						subOrderService.hAddOneInTask(subOrder, session);

						// 删除购物车记录
						shoppingCarService.hDeleteOneInTask(sc, session);

						order_total_num += subOrder.getUser_order_num();
						order_amount += subOrder.getUser_order_price_with_discount() * order_total_num;

						if (null == order_name) {
							order_name = subOrder.getUser_order_pro_name();
						}
					}

					userOrder.setUuid_shop(uuid_shop);
					userOrder.setOrder_pay_status(1);
					userOrder.setOrder_total_num(order_total_num);
					userOrder.setOrder_amount(order_amount);
					userOrder.setOrder_name(order_name);

					orderService.hUpdateOneInTask(userOrder, session);
					ou.setP_uuid_user_order(userOrder.getP_uuid_user_order());
				}
			});
		}

		return res;
	}

}