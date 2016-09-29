package cn.wonhigh.retail.fas.service;

import java.util.List;
import java.util.Map;

import cn.wonhigh.retail.fas.common.model.RegisterInvoice;

import com.yougou.logistics.base.service.BaseCrudService;

/**
 * 请写出类的用途 
 * @author chen.mj
 * @date  2014-10-22 13:51:56
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
public interface RegisterInvoiceService extends BaseCrudService {

	List<RegisterInvoice> findByBillNo(Map<String, Object> params);
}