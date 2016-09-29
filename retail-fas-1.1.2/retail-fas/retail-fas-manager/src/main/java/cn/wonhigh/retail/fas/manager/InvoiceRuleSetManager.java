package cn.wonhigh.retail.fas.manager;

import java.util.Map;

import cn.wonhigh.retail.fas.common.model.InvoiceRuleSet;

import com.yougou.logistics.base.manager.BaseCrudManager;

/**
 * 请写出类的用途 
 * @author chen.mj
 * @date  2014-09-22 14:37:37
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
public interface InvoiceRuleSetManager extends BaseCrudManager {
	
	/**
	 * 根据店铺编号获取发票
	 * @param params
	 * @return
	 */
	public InvoiceRuleSet selectInvoiceNameByShopNo(Map<String,Object> params);
}