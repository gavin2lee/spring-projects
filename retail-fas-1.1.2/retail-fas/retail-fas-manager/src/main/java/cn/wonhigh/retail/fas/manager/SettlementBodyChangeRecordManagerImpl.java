package cn.wonhigh.retail.fas.manager;

import java.util.Map;

import cn.wonhigh.retail.fas.common.model.SettlementBodyChangeRecord;
import cn.wonhigh.retail.fas.service.SettlementBodyChangeRecordService;

import com.yougou.logistics.base.common.exception.ManagerException;
import com.yougou.logistics.base.common.exception.ServiceException;
import com.yougou.logistics.base.manager.BaseCrudManagerImpl;
import com.yougou.logistics.base.service.BaseCrudService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 请写出类的用途 
 * @author user
 * @date  2015-12-28 11:09:32
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
@Service("settlementBodyChangeRecordManager")
class SettlementBodyChangeRecordManagerImpl extends BaseCrudManagerImpl implements SettlementBodyChangeRecordManager {
    @Resource
    private SettlementBodyChangeRecordService settlementBodyChangeRecordService;

    @Override
    public BaseCrudService init() {
        return settlementBodyChangeRecordService;
    }

	@Override
	public SettlementBodyChangeRecord findByPageSum(Map<String, Object> params) throws ManagerException{
		try {
			return settlementBodyChangeRecordService.findByPageSum(params);
		} catch (ServiceException e) {
			throw new ManagerException(e.getMessage(), e);
		}
	}
}