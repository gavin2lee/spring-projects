
ALTER TABLE `bill_backsection_split_dtl` MODIFY COLUMN `backsection_no`  char(25) NULL DEFAULT NULL COMMENT '�ؿ���' AFTER `backsection_dtl_no`;
ALTER TABLE `bill_backsection_split_dtl` ADD COLUMN `system_sales_amount`  decimal(20,4) NULL DEFAULT 0.0000 COMMENT 'ϵͳ����' AFTER `update_time`;
ALTER TABLE `bill_backsection_split_dtl` ADD COLUMN `bank_back_date`  date NULL DEFAULT NULL COMMENT '���лؿ�����' AFTER `system_sales_amount`;
ALTER TABLE `bill_backsection_split_dtl` ADD COLUMN `bank_back_amount`  decimal(20,4) NULL DEFAULT 0.0000 COMMENT '���лؿ���' AFTER `bank_back_date`;
ALTER TABLE `bill_backsection_split_dtl` ADD COLUMN `invoice_apply_date`  date NULL DEFAULT NULL COMMENT '���뿪Ʊ����' AFTER `bank_back_amount`;
ALTER TABLE `bill_backsection_split_dtl` ADD COLUMN `balance_start_date`  date NULL DEFAULT NULL COMMENT '������ʼ����' AFTER `invoice_apply_date`;
ALTER TABLE `bill_backsection_split_dtl` ADD COLUMN `balance_end_date`  date NULL DEFAULT NULL COMMENT '������ֹ����' AFTER `balance_start_date`;

ALTER TABLE `bill_shop_balance_diff` ADD COLUMN `balance_diff_amount`  decimal(12,4) NULL DEFAULT 0.0000 COMMENT '�������' AFTER `billing_code`;
CREATE TABLE `saleproxy_balance_relationshop` (
`id`  char(32) NOT NULL DEFAULT '' COMMENT '����(UUID)' ,
`shop_no`  char(18) NULL DEFAULT NULL COMMENT '���̱���' ,
`short_name`  varchar(100) NULL DEFAULT NULL COMMENT '���̼��' ,
`full_name`  varchar(200) NULL DEFAULT NULL COMMENT '����ȫ��' ,
`brand_unit_no`  char(18) NOT NULL COMMENT 'Ʒ�Ʋ����' ,
`brand_unit_name`  varchar(50) NULL DEFAULT NULL COMMENT 'Ʒ�Ʋ�����' ,
`buyer_no`  char(18) NULL DEFAULT NULL COMMENT '���д�����˾���' ,
`buyer_name`  varchar(200) NULL DEFAULT NULL COMMENT '���д�����˾����' ,
`saler_no`  char(18) NULL DEFAULT NULL COMMENT 'ί�д�����˾���' ,
`saler_name`  varchar(200) NULL DEFAULT NULL COMMENT 'ί�д�����˾����' ,
`effective_date`  date NULL DEFAULT NULL COMMENT '��Ч����' ,
`remark`  varchar(255) NULL DEFAULT '' COMMENT '��ע' ,
`create_user`  varchar(32) NULL DEFAULT NULL COMMENT '������' ,
`create_time`  datetime NULL DEFAULT NULL COMMENT '����ʱ��' ,
`update_user`  varchar(32) NULL DEFAULT NULL COMMENT '�޸���' ,
`sharding_flag`  char(20) NULL DEFAULT NULL COMMENT '�ֿ��ֶ�(�����Ӵ���)' ,
`update_time`  datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '�޸�ʱ��' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
ROW_FORMAT=Compact
COMMENT='���̴�����ϵ'
;
CREATE TABLE `saleproxy_balance_settleprice` (
`id`  char(32) NOT NULL DEFAULT '' COMMENT '����(UUID)' ,
`buyer_no`  char(18) NULL DEFAULT NULL COMMENT '���д�����˾���' ,
`buyer_name`  varchar(200) NULL DEFAULT NULL COMMENT '���д�����˾����' ,
`saler_no`  char(18) NULL DEFAULT NULL COMMENT 'ί�д�����˾���' ,
`saler_name`  varchar(200) NULL DEFAULT NULL COMMENT 'ί�д�����˾����' ,
`shop_no`  char(18) NULL DEFAULT NULL COMMENT '���̱���' ,
`short_name`  varchar(100) NULL DEFAULT NULL COMMENT '���̼��' ,
`full_name`  varchar(200) NULL DEFAULT NULL COMMENT '����ȫ��' ,
`brand_unit_no`  char(18) NOT NULL COMMENT 'Ʒ�Ʋ����' ,
`brand_unit_name`  varchar(50) NULL DEFAULT NULL COMMENT 'Ʒ�Ʋ�����' ,
`pricing_base`  tinyint(3) NULL DEFAULT 3 COMMENT '���ۻ�����1�Ƽ� 2������ 3�ն��������� 4�ն�����-�۷� 5���' ,
`discount`  decimal(8,4) NOT NULL DEFAULT 0.0000 COMMENT '�ۿ�(%)' ,
`add_on_price`  decimal(8,2) NULL DEFAULT 0.00 COMMENT '�Ӽ���' ,
`tariff_amount`  decimal(20,2) NULL DEFAULT NULL COMMENT '���' ,
`effective_date`  date NULL DEFAULT NULL COMMENT '��Ч����' ,
`remark`  varchar(255) NULL DEFAULT '' COMMENT '��ע' ,
`create_user`  varchar(32) NULL DEFAULT NULL COMMENT '������' ,
`create_time`  datetime NULL DEFAULT NULL COMMENT '����ʱ��' ,
`update_user`  varchar(32) NULL DEFAULT NULL COMMENT '�޸���' ,
`sharding_flag`  char(20) NULL DEFAULT NULL COMMENT '�ֿ��ֶ�(�����Ӵ���)' ,
`update_time`  datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '�޸�ʱ��' ,
PRIMARY KEY (`id`)
)
ENGINE=InnoDB
ROW_FORMAT=Compact
COMMENT='���������'
;

ALTER TABLE `customer_order_remain` ADD COLUMN `wholesale_order_type`  tinyint(3) NULL DEFAULT 0 COMMENT '��������(0-����,1-�ֻ�,2-����)' AFTER `unfreeze_order_amount`;


ALTER TABLE `order_main` ADD COLUMN `client_no`  char(18) NULL DEFAULT NULL COMMENT '�ͻ����' AFTER `months`;

ALTER TABLE `order_main` ADD COLUMN `client_name`  varchar(100) NULL DEFAULT NULL COMMENT '�ͻ�����' AFTER `client_no`;

ALTER TABLE `return_exchange_main` ADD COLUMN `client_no`  char(18) NULL DEFAULT NULL COMMENT '�ͻ����' AFTER `months`;

ALTER TABLE `return_exchange_main` ADD COLUMN `client_name`  varchar(100) NULL DEFAULT NULL COMMENT '�ͻ�����' AFTER `client_no`;
