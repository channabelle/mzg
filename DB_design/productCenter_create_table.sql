/*
 Navicat Premium Data Transfer

 Source Server         : 本地数据库
 Source Server Type    : MySQL
 Source Server Version : 50714
 Source Host           : localhost:3306
 Source Schema         : productCenter

 Target Server Type    : MySQL
 Target Server Version : 50714
 File Encoding         : 65001

 Date: 26/07/2018 12:02:03
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for T_Administrator
-- ----------------------------
DROP TABLE IF EXISTS `T_Administrator`;
CREATE TABLE `T_Administrator` (
  `p_uuid_administrator` varchar(36) NOT NULL COMMENT '1）用户编号，主键',
  `account` varchar(128) NOT NULL COMMENT '2）用户名',
  `realname` varchar(32) DEFAULT NULL COMMENT '3）真实姓名',
  `password` varchar(128) DEFAULT NULL COMMENT '6）密码（MD5加密）',
  `phone` varchar(32) DEFAULT NULL COMMENT '7）手机号码',
  `email` varchar(128) DEFAULT NULL COMMENT '8）邮箱',
  `remark` varchar(2048) DEFAULT NULL COMMENT '10）备注',
  `status` int(1) NOT NULL DEFAULT '1' COMMENT '11）账号状态（-1：锁定，1：可用）',
  `cTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '100）注册时间',
  `uTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '101）更新时间',
  PRIMARY KEY (`p_uuid_administrator`),
  UNIQUE KEY `account` (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='超级管理员表';

-- ----------------------------
-- Table structure for T_Apple
-- ----------------------------
DROP TABLE IF EXISTS `T_Apple`;
CREATE TABLE `T_Apple` (
  `id` varchar(36) NOT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for T_Http_Record
-- ----------------------------
DROP TABLE IF EXISTS `T_Http_Record`;
CREATE TABLE `T_Http_Record` (
  `p_uuid` varchar(36) NOT NULL COMMENT '主键',
  `session_id` varchar(64) NOT NULL COMMENT 'http-session id',
  `user_id` varchar(36) DEFAULT NULL COMMENT '访问者用户id',
  `ip` varchar(16) DEFAULT NULL COMMENT '访问者ip',
  `url` varchar(256) NOT NULL COMMENT '请求url',
  `method` varchar(16) DEFAULT NULL COMMENT '请求方式',
  `req_content` varchar(1024) DEFAULT NULL,
  `cTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '请求时间',
  `status` int(11) NOT NULL COMMENT '响应状态',
  `error_serialNo` varchar(32) DEFAULT NULL COMMENT '异常日志编号',
  `response_time` double NOT NULL COMMENT '响应时间',
  PRIMARY KEY (`p_uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for T_Log4j
-- ----------------------------
DROP TABLE IF EXISTS `T_Log4j`;
CREATE TABLE `T_Log4j` (
  `p_uuid` varchar(36) NOT NULL COMMENT '主键',
  `d` varchar(32) NOT NULL DEFAULT 'CURRENT_TIMESTAMP',
  `p` varchar(32) DEFAULT NULL,
  `F` varchar(256) DEFAULT NULL,
  `L` varchar(8) DEFAULT NULL,
  `m` varchar(1024) DEFAULT NULL,
  `n` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`p_uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='Log4j记录表';

-- ----------------------------
-- Table structure for T_Menu_ProInfo
-- ----------------------------
DROP TABLE IF EXISTS `T_Menu_ProInfo`;
CREATE TABLE `T_Menu_ProInfo` (
  `p_uuid_menu_proInfo` varchar(36) NOT NULL COMMENT '1）编号，主键',
  `uuid_pro_menu` varchar(36) NOT NULL COMMENT '2）目录编号',
  `uuid_pro_info` varchar(36) NOT NULL COMMENT '3）产品编号',
  `cTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '100）创建时间',
  `uTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '101）更新时间',
  PRIMARY KEY (`p_uuid_menu_proInfo`),
  UNIQUE KEY `uuid_menu_proInfo` (`uuid_pro_menu`,`uuid_pro_info`) USING BTREE,
  KEY `uuid_pro_info` (`uuid_pro_info`),
  CONSTRAINT `t_menu_proInfo_ibfk_1` FOREIGN KEY (`uuid_pro_menu`) REFERENCES `T_ProMenu` (`p_uuid_pro_menu`),
  CONSTRAINT `t_menu_proInfo_ibfk_2` FOREIGN KEY (`uuid_pro_info`) REFERENCES `T_ProInfo` (`p_uuid_pro_info`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='目录-产品-对应表';

-- ----------------------------
-- Table structure for T_ProDetail
-- ----------------------------
DROP TABLE IF EXISTS `T_ProDetail`;
CREATE TABLE `T_ProDetail` (
  `p_uuid_pro_detail` varchar(36) NOT NULL COMMENT '1）编号，主键',
  `uuid_pro_info` varchar(36) NOT NULL COMMENT '2）产品编号',
  `pro_detail_full_h5` text COMMENT '3）产品详细介绍h5',
  `pro_detail_img_url_list` varchar(2048) DEFAULT NULL,
  `cTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '100）创建时间',
  `uTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '101）更新时间',
  PRIMARY KEY (`p_uuid_pro_detail`),
  UNIQUE KEY `uuid_pro_info_2` (`uuid_pro_info`) USING BTREE,
  KEY `uuid_pro_info` (`uuid_pro_info`),
  CONSTRAINT `t_prodetail_ibfk_1` FOREIGN KEY (`uuid_pro_info`) REFERENCES `T_ProInfo` (`p_uuid_pro_info`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='产品详情表';

-- ----------------------------
-- Table structure for T_ProInfo
-- ----------------------------
DROP TABLE IF EXISTS `T_ProInfo`;
CREATE TABLE `T_ProInfo` (
  `p_uuid_pro_info` varchar(36) NOT NULL COMMENT '1）产品编号，主键',
  `uuid_shop` varchar(36) NOT NULL COMMENT '2）所属商户',
  `uuid_shop_pro` varchar(64) NOT NULL COMMENT '3）所属商铺产品编号',
  `pro_title_full` varchar(128) DEFAULT NULL COMMENT '4）产品标题-全',
  `pro_title_short` varchar(64) NOT NULL COMMENT '5）产品标题-简',
  `pro_cover_img_url` varchar(256) DEFAULT NULL COMMENT '6）产品封面',
  `pro_price` decimal(20,2) NOT NULL COMMENT '7）标价',
  `recommended_rank` decimal(10,0) NOT NULL,
  `pro_discount` decimal(20,2) DEFAULT '0.00' COMMENT '8）折扣',
  `pro_discount_sTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '9）折扣有效期-起始',
  `pro_discount_eTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '10）折扣有效期-终止',
  `valid_sTime_unlimited` int(1) NOT NULL DEFAULT '1',
  `valid_sTime` timestamp NULL DEFAULT '0000-00-00 00:00:00' COMMENT '12）折扣有效期-起始',
  `valid_eTime_unlimited` int(1) NOT NULL DEFAULT '1',
  `valid_eTime` timestamp NULL DEFAULT '0000-00-00 00:00:00' COMMENT '13）折扣有效期-终止',
  `pro_status` int(1) NOT NULL DEFAULT '1' COMMENT '14）产品状态（-1：下架，1：可售）',
  `pro_total_quantity_unlimited` int(1) NOT NULL DEFAULT '1',
  `pro_total_quantity` decimal(20,0) DEFAULT '-1' COMMENT '8）产品总量（-1：无限量）',
  `pro_left_quantity` decimal(20,0) DEFAULT '-1' COMMENT '8）产品余量（-1：无限量）',
  `cTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '100）产品创建时间',
  `uTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '101）产品更新时间',
  PRIMARY KEY (`p_uuid_pro_info`),
  UNIQUE KEY `uuid_shop_2` (`uuid_shop`,`uuid_shop_pro`),
  UNIQUE KEY `uuid_shop_3` (`uuid_shop`,`pro_title_short`) USING BTREE,
  KEY `uuid_shop` (`uuid_shop`),
  CONSTRAINT `t_proinfo_ibfk_1` FOREIGN KEY (`uuid_shop`) REFERENCES `T_Shop` (`p_uuid_shop`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='产品信息表';

-- ----------------------------
-- Table structure for T_ProMenu
-- ----------------------------
DROP TABLE IF EXISTS `T_ProMenu`;
CREATE TABLE `T_ProMenu` (
  `p_uuid_pro_menu` varchar(36) NOT NULL COMMENT '1）目录编号，主键',
  `uuid_shop` varchar(48) NOT NULL COMMENT '2）所属商户',
  `menu_name` varchar(32) NOT NULL COMMENT '3）目录名称',
  `menu_father_uuid` varchar(32) DEFAULT NULL COMMENT '4）上级目录编号',
  `menu_status` int(1) NOT NULL DEFAULT '0' COMMENT '5）目录状态',
  `order_number` int(10) NOT NULL,
  `cTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '100）目录创建时间',
  `uTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '101）目录更新时间',
  PRIMARY KEY (`p_uuid_pro_menu`),
  UNIQUE KEY `uuid_shop_2` (`uuid_shop`,`menu_name`),
  KEY `uuid_shop` (`uuid_shop`),
  CONSTRAINT `t_promenu_ibfk_1` FOREIGN KEY (`uuid_shop`) REFERENCES `T_Shop` (`p_uuid_shop`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='商品目录表';

-- ----------------------------
-- Table structure for T_Shop
-- ----------------------------
DROP TABLE IF EXISTS `T_Shop`;
CREATE TABLE `T_Shop` (
  `p_uuid_shop` varchar(36) NOT NULL COMMENT '1）商铺编号，主键',
  `shop_name_full` varchar(256) DEFAULT NULL COMMENT '2）商铺全称',
  `shop_name_short` varchar(64) NOT NULL COMMENT '3）商铺简称',
  `shop_address` varchar(128) DEFAULT NULL COMMENT '4）商铺地址',
  `shop_phone` varchar(32) DEFAULT NULL COMMENT '5）商铺联系电话',
  `shop_contact` varchar(32) DEFAULT NULL COMMENT '6）商铺联系人',
  `shop_contact_phone` varchar(32) DEFAULT NULL COMMENT '7）商铺联系人电话',
  `shop_business_licence` varchar(64) DEFAULT NULL COMMENT '8）商铺营业执照',
  `shop_status` int(1) NOT NULL DEFAULT '1' COMMENT '9）商铺状态（-1：停业，1：开业）',
  `cTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '100）创建时间',
  `uTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '101）更新时间',
  PRIMARY KEY (`p_uuid_shop`),
  UNIQUE KEY `shop_name_short` (`shop_name_short`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='商铺表';

-- ----------------------------
-- Table structure for T_Shop_Config
-- ----------------------------
DROP TABLE IF EXISTS `T_Shop_Config`;
CREATE TABLE `T_Shop_Config` (
  `p_uuid_shop_config` varchar(36) NOT NULL COMMENT '1）用户配置编号，主键',
  `uuid_shop` varchar(64) NOT NULL COMMENT '2）用户编号',
  `config_name` varchar(128) DEFAULT NULL COMMENT '3）配置名称',
  `config_value` varchar(256) DEFAULT NULL COMMENT '4）配置值',
  `config_remark` varchar(256) DEFAULT NULL COMMENT '5）配置备注',
  `cTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '100）注册时间',
  `uTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '101）更新时间',
  PRIMARY KEY (`p_uuid_shop_config`),
  UNIQUE KEY `uuid_user_AND_config_name` (`uuid_shop`,`config_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户配置表';

-- ----------------------------
-- Table structure for T_Shop_Manager
-- ----------------------------
DROP TABLE IF EXISTS `T_Shop_Manager`;
CREATE TABLE `T_Shop_Manager` (
  `p_uuid_shop_manager` varchar(36) NOT NULL COMMENT '1）用户编号，主键',
  `uuid_shop` varchar(36) NOT NULL,
  `account` varchar(128) NOT NULL COMMENT '2）用户名',
  `realname` varchar(32) DEFAULT NULL COMMENT '3）真实姓名',
  `password` varchar(128) DEFAULT NULL COMMENT '6）密码（MD5加密）',
  `phone` varchar(32) DEFAULT NULL COMMENT '7）手机号码',
  `email` varchar(128) DEFAULT NULL COMMENT '8）邮箱',
  `remark` varchar(2048) DEFAULT NULL COMMENT '10）备注',
  `status` int(1) NOT NULL DEFAULT '1' COMMENT '11）账号状态（-1：锁定，1：可用）',
  `cTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '100）注册时间',
  `uTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '101）更新时间',
  PRIMARY KEY (`p_uuid_shop_manager`),
  UNIQUE KEY `account` (`account`),
  KEY `uuid_shop` (`uuid_shop`),
  KEY `uuid_shop_2` (`uuid_shop`),
  KEY `uuid_shop_3` (`uuid_shop`),
  KEY `uuid_shop_4` (`uuid_shop`),
  CONSTRAINT `t_shop_manager_ibfk_1` FOREIGN KEY (`uuid_shop`) REFERENCES `T_Shop` (`p_uuid_shop`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='商铺管理员表';

-- ----------------------------
-- Table structure for T_ShoppingCar
-- ----------------------------
DROP TABLE IF EXISTS `T_ShoppingCar`;
CREATE TABLE `T_ShoppingCar` (
  `p_uuid_shopping_car` varchar(36) NOT NULL COMMENT '1）编号，主键',
  `uuid_user` varchar(36) NOT NULL COMMENT '2）用户编号',
  `uuid_pro_info` varchar(36) NOT NULL COMMENT '3）产品编号',
  `amount` decimal(20,2) NOT NULL COMMENT '4）数量',
  `checked` varchar(36) NOT NULL DEFAULT 'checked' COMMENT '5）是否选择结算',
  `cTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '100）创建时间',
  `uTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '101）更新时间',
  PRIMARY KEY (`p_uuid_shopping_car`),
  UNIQUE KEY `uuid_user_AND_uuid_pro_info` (`uuid_user`,`uuid_pro_info`),
  KEY `uuid_pro_info` (`uuid_pro_info`),
  CONSTRAINT `t_shoppingcar_ibfk_1` FOREIGN KEY (`uuid_user`) REFERENCES `T_User` (`p_uuid_user`),
  CONSTRAINT `t_shoppingcar_ibfk_2` FOREIGN KEY (`uuid_pro_info`) REFERENCES `T_ProInfo` (`p_uuid_pro_info`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='购物车';

-- ----------------------------
-- Table structure for T_User
-- ----------------------------
DROP TABLE IF EXISTS `T_User`;
CREATE TABLE `T_User` (
  `p_uuid_user` varchar(36) NOT NULL COMMENT '1）用户编号，主键',
  `account` varchar(128) NOT NULL COMMENT '2）用户名',
  `realname` varchar(32) DEFAULT NULL COMMENT '3）真实姓名',
  `id_card` varchar(32) DEFAULT NULL COMMENT '4）身份证号码',
  `sex` int(1) NOT NULL DEFAULT '0' COMMENT '5）性别（0：未定，1：男，2：女）',
  `password` varchar(128) DEFAULT NULL COMMENT '6）密码（MD5加密）',
  `phone` varchar(32) DEFAULT NULL COMMENT '7）手机号码',
  `email` varchar(128) DEFAULT NULL COMMENT '8）邮箱',
  `u_tag_list` varchar(2048) DEFAULT NULL COMMENT '9）标签列表',
  `remark` varchar(2048) DEFAULT NULL COMMENT '10）备注',
  `status` int(1) NOT NULL DEFAULT '1' COMMENT '11）账号状态（-1：锁定，1：可用）',
  `miniProgram_appid` varchar(128) DEFAULT NULL,
  `miniProgram_openid` varchar(128) DEFAULT NULL,
  `cTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '100）注册时间',
  `uTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '101）更新时间',
  PRIMARY KEY (`p_uuid_user`),
  UNIQUE KEY `account` (`account`),
  UNIQUE KEY `miniProgram_appid` (`miniProgram_appid`,`miniProgram_openid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='用户表';

-- ----------------------------
-- Table structure for T_SubOrder
-- ----------------------------
DROP TABLE IF EXISTS `T_SubOrder`;
CREATE TABLE `T_SubOrder` (
  `p_uuid_sub_order` varchar(36) NOT NULL COMMENT '1）子订单编号，主键',
  `uuid_user` varchar(36) NOT NULL COMMENT '2）下单用户编号',
  `uuid_user_order` varchar(36) NOT NULL COMMENT '3）用户订单编号',
  `uuid_pro_info` varchar(36) NOT NULL COMMENT '4）产品编号',
  `user_order_pro_name` varchar(512) NOT NULL COMMENT '5）产品名称',
  `user_order_price` decimal(20,2) NOT NULL COMMENT '6）订购价格',
  `user_order_discount` decimal(20,2) NOT NULL COMMENT '7）订购折扣',
  `user_order_num` decimal(20,2) NOT NULL COMMENT '）订购数量',
  `cTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '100）创建时间',
  `uTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '101）更新时间',
  PRIMARY KEY (`p_uuid_sub_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='子订单表';

-- ----------------------------
-- Table structure for T_UserOrder
-- ----------------------------
DROP TABLE IF EXISTS `T_UserOrder`;
CREATE TABLE `T_UserOrder` (
  `p_uuid_user_order` varchar(36) NOT NULL COMMENT '1）订单编号，主键',
  `uuid_user` varchar(36) NOT NULL COMMENT '2）下单用户编号',
  `order_amount` decimal(20,2) NOT NULL COMMENT '3）订单总额',
  `order_pay_status` int(4) NOT NULL DEFAULT '1' COMMENT '4）订单支付状态（1：待支付，2：支付中，3：已支付，4：已退款）',
  `order_pay_mode` int(4) NOT NULL DEFAULT '1' COMMENT '5）支付方式（1：在线支付，2：货到付款）',
  `order_pay_channel` int(4) NOT NULL DEFAULT '1' COMMENT '6）支付渠道（1：现金，2：POS，3：微信，4：支付宝）',
  `user_order_remark` varchar(512) COMMENT '7）订单备注',
  
  `cTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '100）创建时间',
  `uTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '101）更新时间',
  PRIMARY KEY (`p_uuid_user_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='用户订单表';

-- ----------------------------
-- Table structure for T_UserAddress
-- ----------------------------
DROP TABLE IF EXISTS `T_UserAddress`;
CREATE TABLE `T_UserAddress` (
  `p_uuid_user_address` varchar(36) NOT NULL COMMENT '1）地址编号，主键',
  `uuid_user` varchar(36) NOT NULL COMMENT '2）所属用户编号',
  `address_type` varchar(128) NOT NULL COMMENT '3）地址类型',
  `address_full` varchar(512) NOT NULL COMMENT '4）地址全写',
  `contact_name` varchar(128) NOT NULL COMMENT '5）联系人',
  `contact_phone` varchar(32) NOT NULL COMMENT '6）联系人电话',
  `is_default` int(1) NOT NULL DEFAULT '1' COMMENT '7）是否默认地址（0：否，1：是）',
  
  `cTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '100）创建时间',
  `uTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '101）更新时间',
  PRIMARY KEY (`p_uuid_user_address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='用户地址表';

-- ----------------------------
-- Table structure for T_Wepay_Order
-- ----------------------------
DROP TABLE IF EXISTS `T_Wepay_Order`;
CREATE TABLE `T_Wepay_Order` (
  `p_out_trade_no` varchar(32) NOT NULL COMMENT '1）微信商户订单号，主键',
  `appid` varchar(32) NOT NULL COMMENT '2）公众账号ID',
  `mch_id` varchar(32) NOT NULL COMMENT '3）商户号',
  `body` varchar(128) NOT NULL COMMENT '4）商品简单描述',
  `attach` varchar(128) NOT NULL COMMENT '5）商品附加描述，在查询API和支付通知中原样返回',
  `total_fee` int(10) NOT NULL DEFAULT '0' COMMENT '6）商品单价（单位：分）',
  `trade_state` varchar(32) NOT NULL DEFAULT 'NOTPAY' COMMENT '7）交易状态：SUCCESS—支付成功/REFUND—转入退款/NOTPAY—未支付/CLOSED—已关闭/REVOKED—已撤销（刷卡支付）/USERPAYING--用户支付中/PAYERROR--支付失败(其他原因，如银行返回失败)',
  `trade_state_desc` varchar(256) DEFAULT NULL COMMENT '8）对当前查询订单状态的描述和下一步操作的指引',
  `trade_type` varchar(16) NOT NULL COMMENT '9）交易方式，JSAPI，NATIVE，APP等',
  `openid` varchar(128) DEFAULT NULL COMMENT '10）交易用户标识',
  `transaction_id` varchar(32) DEFAULT NULL COMMENT '11）微信支付订单号，和用户对账',
  `prepay_id` varchar(64) NOT NULL COMMENT '12）微信生成的预支付会话标识，用于后续接口调用中使用，该值有效期为2小时',
  `cTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '100）交易发起时间',
  `uTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '101）交易更新时间',
  PRIMARY KEY (`p_out_trade_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='微信订单';

SET FOREIGN_KEY_CHECKS = 1;
