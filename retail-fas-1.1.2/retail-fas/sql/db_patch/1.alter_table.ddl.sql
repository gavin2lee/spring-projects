
ALTER TABLE `bill_backsection_split_dtl` MODIFY COLUMN `backsection_no`  char(25) NULL DEFAULT NULL COMMENT '回款编号' AFTER `backsection_dtl_no`;
ALTER TABLE `bill_backsection_split_dtl` ADD COLUMN `system_sales_amount`  decimal(20,4) NULL DEFAULT 0.0000 COMMENT '系统收入' AFTER `update_time`;
ALTER TABLE `bill_backsection_split_dtl` ADD COLUMN `bank_back_date`  date NULL DEFAULT NULL COMMENT '银行回款日期' AFTER `system_sales_amount`;
ALTER TABLE `bill_backsection_split_dtl` ADD COLUMN `bank_back_amount`  decimal(20,4) NULL DEFAULT 0.0000 COMMENT '银行回款金额' AFTER `bank_back_date`;
ALTER TABLE `bill_backsection_split_dtl` ADD COLUMN `invoice_apply_date`  date NULL DEFAULT NULL COMMENT '申请开票日期' AFTER `bank_back_amount`;
ALTER TABLE `bill_backsection_split_dtl` ADD COLUMN `balance_start_date`  date NULL DEFAULT NULL COMMENT '结算起始日期' AFTER `invoice_apply_date`;
ALTER TABLE `bill_backsection_split_dtl` ADD COLUMN `balance_end_date`  date NULL DEFAULT NULL COMMENT '结算终止日期' AFTER `balance_start_date`;

ALTER TABLE `bill_shop_balance_diff` ADD COLUMN `balance_diff_amount`  decimal(12,4) NULL DEFAULT 0.0000 COMMENT '结算差异' AFTER `billing_code`;
CREATE TABLE `saleproxy_balance_relationshop` (
`id`  char(32) NOT NULL DEFAULT '' COMMENT '主键(UUID)' ,
`shop_no`  char(18) NULL DEFAULT NULL COMMENT '店铺编码' ,
`short_name`  varchar(100) NULL DEFAULT NULL COMMENT '店铺简称' ,
`full_name`  varchar(200) NULL DEFAULT NULL COMMENT '店铺全称' ,
`brand_unit_no`  char(18) NOT NULL COMMENT '品牌部编号' ,
`brand_unit_name`  varchar(50) NULL DEFAULT NULL COMMENT '品牌部名称' ,
`buyer_no`  char(18) NULL DEFAULT NULL COMMENT '受托代销公司编号' ,
`buyer_name`  varchar(200) NULL DEFAULT NULL COMMENT '受托代销公司名称' ,
`saler_no`  char(18) NULL DEFAULT NULL COMMENT '委托代销公司编号' ,
`saler_name`  varchar(200) NULL DEFAULT NULL COMMENT '委托代销公司名称' ,
`effective_date`  date NULL DEFAULT NULL COMMENT '生效日期' ,
`remark`  varchar(255) NULL DEFAULT '' COMMENT '备注' ,
`create_user`  varchar(32) NULL DEFAULT NULL COMMENT '建档人' ,
`create_time`  datetime NULL DEFAULT NULL COMMENT '建档时间' ,
`update_user`  varchar(32) NULL DEFAULT NULL COMMENT '修改人' ,
`sharding_flag`  char(20) NULL DEFAULT NULL COMMENT '分库字段(本部加大区)' ,
`update_time`  datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
ROW_FORMAT=Compact
COMMENT='店铺代销关系'
;
CREATE TABLE `saleproxy_balance_settleprice` (
`id`  char(32) NOT NULL DEFAULT '' COMMENT '主键(UUID)' ,
`buyer_no`  char(18) NULL DEFAULT NULL COMMENT '受托代销公司编号' ,
`buyer_name`  varchar(200) NULL DEFAULT NULL COMMENT '受托代销公司名称' ,
`saler_no`  char(18) NULL DEFAULT NULL COMMENT '委托代销公司编号' ,
`saler_name`  varchar(200) NULL DEFAULT NULL COMMENT '委托代销公司名称' ,
`shop_no`  char(18) NULL DEFAULT NULL COMMENT '店铺编码' ,
`short_name`  varchar(100) NULL DEFAULT NULL COMMENT '店铺简称' ,
`full_name`  varchar(200) NULL DEFAULT NULL COMMENT '店铺全称' ,
`brand_unit_no`  char(18) NOT NULL COMMENT '品牌部编号' ,
`brand_unit_name`  varchar(50) NULL DEFAULT NULL COMMENT '品牌部名称' ,
`pricing_base`  tinyint(3) NULL DEFAULT 3 COMMENT '定价基数（1牌价 2地区价 3终端销售收入 4终端销售-扣费 5租金）' ,
`discount`  decimal(8,4) NOT NULL DEFAULT 0.0000 COMMENT '折扣(%)' ,
`add_on_price`  decimal(8,2) NULL DEFAULT 0.00 COMMENT '加减价' ,
`tariff_amount`  decimal(20,2) NULL DEFAULT NULL COMMENT '租金' ,
`effective_date`  date NULL DEFAULT NULL COMMENT '生效日期' ,
`remark`  varchar(255) NULL DEFAULT '' COMMENT '备注' ,
`create_user`  varchar(32) NULL DEFAULT NULL COMMENT '建档人' ,
`create_time`  datetime NULL DEFAULT NULL COMMENT '建档时间' ,
`update_user`  varchar(32) NULL DEFAULT NULL COMMENT '修改人' ,
`sharding_flag`  char(20) NULL DEFAULT NULL COMMENT '分库字段(本部加大区)' ,
`update_time`  datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
ROW_FORMAT=Compact
COMMENT='代销结算价'
;

ALTER TABLE `customer_order_remain` ADD COLUMN `wholesale_order_type`  tinyint(3) NULL DEFAULT 0 COMMENT '订单类型(0-订货,1-现货,2-补货)' AFTER `unfreeze_order_amount`;


ALTER TABLE `order_main` ADD COLUMN `client_no`  char(18) NULL DEFAULT NULL COMMENT '客户编号' AFTER `months`;

ALTER TABLE `order_main` ADD COLUMN `client_name`  varchar(100) NULL DEFAULT NULL COMMENT '客户名称' AFTER `client_no`;

ALTER TABLE `return_exchange_main` ADD COLUMN `client_no`  char(18) NULL DEFAULT NULL COMMENT '客户编号' AFTER `months`;

ALTER TABLE `return_exchange_main` ADD COLUMN `client_name`  varchar(100) NULL DEFAULT NULL COMMENT '客户名称' AFTER `client_no`;
