package cn.wonhigh.retail.fas.dal.database;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;

import cn.wonhigh.retail.fas.common.model.BillPurchaseAdjust;
import cn.wonhigh.retail.fas.common.model.ItemCost;

import com.yougou.logistics.base.dal.database.BaseCrudMapper;

/**
 * 请写出类的用途 
 * @author user
 * @date  2016-04-20 18:31:53
 * @version 1.0.0
 * @copyright (C) 2013 YouGou Information Technology Co.,Ltd 
 * All Rights Reserved. 
 * 
 * The software for the YouGou technology development, without the 
 * company's written consent, and any other individuals and 
 * organizations shall not be used, Copying, Modify or distribute 
 * the software.
 * 
 */
public interface BillPurchaseAdjustMapper extends BaseCrudMapper {
	int verify(BillPurchaseAdjust obj);

	/**
	 * 成本核算获取商品采购入库金额
	 * @param itemCost
	 * @return
	 */
	BigDecimal queryCostAdjustment(@Param("params")ItemCost itemCost);
}