package cn.wonhigh.retail.fas.manager;

import java.util.List;

import cn.wonhigh.retail.fas.common.model.SelfShopBankInfo;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.manager.BaseCrudManager;

/**
 * 请写出类的用途 
 * @author zhouxm
 * @date  2014-10-10 11:20:13
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
public interface SelfShopBankInfoManager extends BaseCrudManager {
	
	boolean addListShopBankInfo(List<SelfShopBankInfo> selfShopBankInfoList) throws ManagerException;
	
}